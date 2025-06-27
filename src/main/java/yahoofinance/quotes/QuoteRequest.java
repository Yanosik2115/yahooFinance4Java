package yahoofinance.quotes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.util.RedirectableRequest;
import yahoofinance.util.Utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class QuoteRequest<T> {

	private final String symbol;
	private final ObjectMapper objectMapper;
	private static final int CONNECTION_TIMEOUT = 1000;

	protected QuoteRequest(String symbol) {
		this.symbol = symbol;
		this.objectMapper = new ObjectMapper();
	}

	public abstract String getURL();

	public Map<String, String> getParams() {
		return Collections.emptyMap();
	}

	public abstract T parseJson(JsonNode node);

	public final T execute() throws IOException {
		URL request = new URL(getURL() + "/" + symbol + (getParams().isEmpty() ? "" : "?" + Utils.getURLParameters(getParams())));
		RedirectableRequest redirectableRequest = new RedirectableRequest(request, 5);
		redirectableRequest.setConnectTimeout(CONNECTION_TIMEOUT);
		redirectableRequest.setReadTimeout(CONNECTION_TIMEOUT);
		HttpURLConnection connection = (HttpURLConnection) redirectableRequest.openConnection();
		if (connection.getResponseCode() >= 300) {
			log.error("Error: {}", connection.getResponseMessage());
			InputStreamReader es = new InputStreamReader(connection.getErrorStream());
			JsonNode errorNode = objectMapper.readTree(es);
			log.error(errorNode.toPrettyString());
		}

		InputStreamReader is = new InputStreamReader(connection.getInputStream());
		JsonNode node = objectMapper.readTree(is);
		return parseJson(node);
	}

	protected final String getSymbol() {
		return symbol;
	}

}

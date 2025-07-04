package yahoofinance.quotes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.exception.ConnectionException;
import yahoofinance.exception.YFinanceException;
import yahoofinance.util.Utils;
import yahoofinance.web.RedirectableRequest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

@Slf4j
public abstract class QuoteRequest<T> {

	private final String symbol;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private static final int CONNECTION_TIMEOUT = 10000;
	private static final int READ_TIMEOUT = 15000;

	protected QuoteRequest(String symbol) {
		this.symbol = symbol;
	}

	protected QuoteRequest() {
		this.symbol = null;
	}

	public abstract String getURL();

	public Map<String, String> getParams() {
		return Collections.emptyMap();
	}


	public abstract T parseJson(JsonNode node);


	protected boolean requiresSymbol() {
		return true;
	}

	protected boolean useCookieAndCrumb() {
		return true;
	}

	protected String buildRequestURL() {
		StringBuilder urlBuilder = new StringBuilder(getURL());

		if (requiresSymbol() && symbol != null && !symbol.isEmpty()) {
			urlBuilder.append("/").append(symbol);
		}

		Map<String, String> params = getParams();
		if (!params.isEmpty()) {
			urlBuilder.append("?").append(Utils.getURLParameters(params));
		}

		return urlBuilder.toString();
	}

	protected HttpURLConnection setupConnection(URL url) throws YFinanceException {
		RedirectableRequest redirectableRequest = new RedirectableRequest(url, 5);
		redirectableRequest.setConnectTimeout(CONNECTION_TIMEOUT);
		redirectableRequest.setReadTimeout(READ_TIMEOUT);
		return (HttpURLConnection) redirectableRequest.openConnection(useCookieAndCrumb());
	}

	protected void handleErrorResponse(HttpURLConnection connection) throws IOException {
		int responseCode = connection.getResponseCode();
		String responseMessage = connection.getResponseMessage();

		log.error("HTTP Error {}: {}", responseCode, responseMessage);
		log.error("Request URL: {}", connection.getURL());

		try (InputStreamReader errorStream = new InputStreamReader(
				connection.getErrorStream() != null ? connection.getErrorStream() : connection.getInputStream())) {
			JsonNode errorNode = objectMapper.readTree(errorStream);
			log.error("Error response body: {}", errorNode.toPrettyString());
		} catch (Exception e) {
			log.error("Could not parse error response", e);
		}

		throw new IOException(String.format("HTTP %d: %s", responseCode, responseMessage));
	}

	public final T execute() throws YFinanceException {
		if (requiresSymbol() && (symbol == null || symbol.trim().isEmpty())) {
			throw new IllegalArgumentException("Symbol is required for this request type");
		}

		String requestUrl = buildRequestURL();
		HttpURLConnection connection = null;
//		log.debug("Executing request: {}", requestUrl);

		try {
			URL url = URI.create(requestUrl).toURL();
			connection = setupConnection(url);
			int responseCode = connection.getResponseCode();

			if (responseCode >= 400) {
				handleErrorResponse(connection);
				return null;
			}

			try (InputStreamReader inputStream = new InputStreamReader(connection.getInputStream())) {
				JsonNode node = objectMapper.readTree(inputStream);

				if (log.isTraceEnabled()) {
					log.trace("Response JSON: {}", node.toPrettyString());
				}
//				log.debug(node.toPrettyString());
				return parseJson(node);
			}

		} catch (IOException e) {
			String ticker = requiresSymbol() ? symbol : "market data";
			log.error("Failed to execute request for {}: {}",
					requiresSymbol() ? symbol : "market data", e.getMessage());
			throw new ConnectionException("Failed to execute request for " + ticker + ": " + e.getMessage());
		} finally {
			if(connection != null)
				connection.disconnect();
		}
	}

	protected final String getSymbol() {
		return symbol;
	}

}
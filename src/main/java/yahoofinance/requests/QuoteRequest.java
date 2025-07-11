package yahoofinance.requests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.exception.ConnectionException;
import yahoofinance.exception.YFinanceException;
import yahoofinance.util.Utils;
import yahoofinance.web.RedirectableRequest;

import javax.xml.bind.ValidationException;
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

	protected boolean extractResultNode() {
		return true;
	}

	private JsonNode getResultNode(JsonNode node, String name) throws ValidationException {
		JsonNode mainNode = node.get(name);

		if (mainNode == null)
			throw new ValidationException("No node " + name + " available");

		JsonNode errorNode = mainNode.get("error");
		if (errorNode != null && !errorNode.isNull()) {
			throw new ValidationException(errorNode.asText());
		}

		JsonNode resultNode = mainNode.get("result");
		if (resultNode == null)
			throw new ValidationException("No result node available");

		return resultNode;
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
		log.debug("Executing request: {}", requestUrl);

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
				if (extractResultNode())
					node = getResultNode(node, node.fieldNames().next());

				return parseJson(node);
			} catch (ValidationException e) {
				throw new YFinanceException(e.getMessage());
			}

		} catch (IOException e) {
			String ticker = requiresSymbol() ? symbol : "market data";
			log.error("Failed to execute request for {}: {}",
					requiresSymbol() ? symbol : "market data", e.getMessage());
			throw new ConnectionException("Failed to execute request for " + ticker + ": " + e.getMessage());
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	protected final String getSymbol() {
		return symbol;
	}

}
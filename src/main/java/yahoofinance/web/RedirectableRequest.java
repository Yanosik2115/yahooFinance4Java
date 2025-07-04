package yahoofinance.web;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.exception.*;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class RedirectableRequest {

	private URL request;
	private int protocolRedirectLimit;

	@Setter
	private int connectTimeout = 10000;
	@Setter
	private int readTimeout = 10000;

	public RedirectableRequest(URL request) {
		this(request, 2);
	}

	public RedirectableRequest(URL request, int protocolRedirectLimit) {
		this.request = request;
		this.protocolRedirectLimit = Math.max(0, protocolRedirectLimit);
	}

	public URLConnection openConnection() throws YFinanceException {
		return openConnection(new HashMap<>(), true);
	}

	public URLConnection openConnection(boolean useCookieAndCrumb) throws YFinanceException {
		return openConnection(new HashMap<>(), useCookieAndCrumb);
	}

	public URLConnection openConnection(Map<String, String> requestProperties, boolean useCookieAndCrumb)
			throws YFinanceException {
		Map<String, String> enhancedProperties = new HashMap<>(requestProperties);

		try {
			if (useCookieAndCrumb) {
				URL enhancedUrl = enhanceUrlWithCrumb(this.request);
				enhancedProperties.put("Cookie", CookieManager.getCookie());
				return executeRequestWithRedirects(enhancedUrl, enhancedProperties);
			} else {
				return executeRequestWithRedirects(this.request, requestProperties);
			}
		} catch (CookieException | CrumbException e) {
			throw new AuthenticationException("Failed to authenticate with Yahoo Finance", e);
		}
	}

	private URLConnection executeRequestWithRedirects(URL initialUrl, Map<String, String> requestProperties)
			throws ConnectionException {
		int redirectCount = 0;
		URL currentUrl = initialUrl;

		while (redirectCount <= this.protocolRedirectLimit) {
			try {
				HttpURLConnection connection = createConnection(currentUrl, requestProperties);

				int responseCode = connection.getResponseCode();

				if (isRedirectResponse(responseCode)) {
					String location = connection.getHeaderField("Location");
					if (location == null || location.trim().isEmpty()) {
						throw new RedirectException("Redirect response without Location header",
								redirectCount, this.protocolRedirectLimit, currentUrl.toString());
					}

					currentUrl = new URL(currentUrl, location);
					redirectCount++;

					if (redirectCount > this.protocolRedirectLimit) {
						throw new RedirectException(
								String.format("Protocol redirect limit exceeded for URL: %s", this.request.toExternalForm()),
								redirectCount, this.protocolRedirectLimit, currentUrl.toString());
					}

					connection.disconnect();
					continue;
				}

				if (responseCode >= 400) {
					throw new ConnectionException(
							String.format("HTTP error response: %d", responseCode),
							responseCode, currentUrl.toString());
				}

				return connection;

			} catch (IOException e) {
				throw new ConnectionException("Failed to execute HTTP request", e);
			}
		}

		throw new ConnectionException("Unexpected redirect handling error");
	}

	private HttpURLConnection createConnection(URL url, Map<String, String> requestProperties) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(this.connectTimeout);
		connection.setReadTimeout(this.readTimeout);
		connection.setInstanceFollowRedirects(false);

		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
		connection.setRequestProperty("Accept", "*/*");

		for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
			connection.setRequestProperty(entry.getKey(), entry.getValue());
		}

		return connection;
	}

	private URL enhanceUrlWithCrumb(URL originalUrl) throws CrumbException {
		try {
			String crumb = CrumbManager.getCrumb();
			log.info("Crumb value: {}", crumb);
			String encodedCrumb = URLEncoder.encode(crumb, StandardCharsets.UTF_8);

			String currentQuery = originalUrl.getQuery();
			String newQuery = currentQuery == null ?
					"crumb=" + encodedCrumb :
					currentQuery + "&crumb=" + encodedCrumb;

			URI uri = new URI(
					originalUrl.getProtocol(),
					originalUrl.getUserInfo(),
					originalUrl.getHost(),
					originalUrl.getPort() == -1 ? originalUrl.getDefaultPort() : originalUrl.getPort(),
					originalUrl.getPath(),
					newQuery,
					originalUrl.getRef()
			);

			return uri.toURL();

		} catch (URISyntaxException | CookieException e) {
			throw new CrumbException("Failed to enhance URL with crumb parameter", e);
		} catch (MalformedURLException e) {
			throw new CrumbException("Failed to parse URI to URL");
		}
	}

	private boolean isRedirectResponse(int responseCode) {
		return responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
		       responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
		       responseCode == HttpURLConnection.HTTP_SEE_OTHER ||
		       responseCode == 307 ||
		       responseCode == 308;
	}

	public void setRequest(URL request) {
		if (request == null) {
			throw new IllegalArgumentException("Request URL cannot be null");
		}
		this.request = request;
	}

	public void setProtocolRedirectLimit(int protocolRedirectLimit) {
		this.protocolRedirectLimit = Math.max(0, protocolRedirectLimit);
	}
}
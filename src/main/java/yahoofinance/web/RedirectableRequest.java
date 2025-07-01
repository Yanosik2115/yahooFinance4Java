package yahoofinance.web;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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

	public URLConnection openConnection() throws IOException {
		return openConnection(new HashMap<>(), true);
	}

	public URLConnection openConnection(boolean useCookieAndCrumb) throws IOException {
		return openConnection(new HashMap<>(), useCookieAndCrumb);
	}

	public URLConnection openConnection(Map<String, String> requestProperties, boolean useCookieAndCrumb) throws IOException {
		Map<String, String> enhancedProperties = new HashMap<>(requestProperties);

		if (useCookieAndCrumb) {
			URL enhancedUrl = enhanceUrlWithCrumb(this.request);
			enhancedProperties.put("Cookie", CookieManager.getCookie());
			return executeRequestWithRedirects(enhancedUrl, enhancedProperties);
		} else {
			return executeRequestWithRedirects(this.request, requestProperties);
		}
	}

	private URLConnection executeRequestWithRedirects(URL initialUrl, Map<String, String> requestProperties) throws IOException {
		int redirectCount = 0;
		URL currentUrl = initialUrl;

		log.debug("Executing request url: {}", currentUrl);

		while (redirectCount <= this.protocolRedirectLimit) {
			HttpURLConnection connection = createConnection(currentUrl, requestProperties);

			int responseCode = connection.getResponseCode();

			if (isRedirectResponse(responseCode)) {
				String location = connection.getHeaderField("Location");
				if (location == null || location.trim().isEmpty()) {
					throw new IOException("Redirect response without Location header");
				}

				currentUrl = new URL(currentUrl, location);
				redirectCount++;

				if (redirectCount > this.protocolRedirectLimit) {
					throw new IOException(
							String.format("Protocol redirect limit (%d) exceeded for URL: %s",
									this.protocolRedirectLimit, this.request.toExternalForm()));
				}

				connection.disconnect();
				continue;
			}

			return connection;
		}

		throw new IOException("Unexpected redirect handling error");
	}

	private HttpURLConnection createConnection(URL url, Map<String, String> requestProperties) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(this.connectTimeout);
		connection.setReadTimeout(this.readTimeout);
		connection.setInstanceFollowRedirects(true);

		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
		connection.setRequestProperty("Accept", "*/*");

		for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
			connection.setRequestProperty(entry.getKey(), entry.getValue());
		}

		return connection;
	}

	private URL enhanceUrlWithCrumb(URL originalUrl) throws IOException {
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

			return URL.of(uri, null);

		} catch (URISyntaxException e) {
			throw new IOException("Failed to enhance URL with crumb parameter", e);
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
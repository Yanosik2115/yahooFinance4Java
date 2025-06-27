package yahoofinance.util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public final class CrumbManager {

	private static final Crumb crumb = new Crumb();
	private static volatile String cookie = null;
	private static final ReentrantLock lock = new ReentrantLock();

	private static final int CONNECTION_TIMEOUT = 5000;
	private static final String COOKIE_SCRAPE_URL = "https://fc.yahoo.com";
	private static final String CRUMB_URL = "https://query1.finance.yahoo.com/v1/test/getcrumb";

	private CrumbManager() {
		throw new AssertionError("CrumbManager should not be instantiated");
	}

	/**
	 * Gets the Yahoo Finance crumb token required for API requests
	 *
	 * @return the crumb value as String
	 * @throws IOException if unable to retrieve crumb
	 */
	public static String getCrumb() throws IOException {
		if (crumb.isValid()) {
			return crumb.getValue();
		}

		lock.lock();
		try {
			if (crumb.isValid()) {
				return crumb.getValue();
			}

			initializeCrumb();

			if (!crumb.isValid()) {
				throw new IOException("Failed to initialize valid crumb");
			}

			return crumb.getValue();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Gets the Yahoo Finance cookie required for API requests
	 *
	 * @return the cookie value
	 * @throws IOException if unable to retrieve cookie
	 */
	public static String getCookie() throws IOException {
		if (cookie != null && !cookie.isEmpty()) {
			return cookie;
		}

		lock.lock();
		try {
			if (cookie != null && !cookie.isEmpty()) {
				return cookie;
			}

			initializeCookie();
			return cookie;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Clears cached crumb and cookie values, forcing re-initialization on next access
	 */
	public static void clearCache() {
		lock.lock();
		try {
			crumb.clear();
			cookie = null;
			log.debug("Cleared cached crumb and cookie");
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Initializes both cookie and crumb in the correct order
	 */
	private static void initializeCrumb() throws IOException {
		if (cookie == null || cookie.isEmpty()) {
			initializeCookie();
		}
		fetchCrumb();
	}

	/**
	 * Fetches cookie from Yahoo Finance
	 */
	private static void initializeCookie() throws IOException {
		log.debug("Initializing cookie from Yahoo Finance");

		try {
			URL url = new URL(COOKIE_SCRAPE_URL);
			HttpURLConnection connection = createBasicConnection(url);

			int responseCode = connection.getResponseCode();
			log.debug("Cookie request response code: {}", responseCode);

			String setCookieHeader = connection.getHeaderField("Set-Cookie");
			log.debug("Raw Set-Cookie header: {}", setCookieHeader);

			if (setCookieHeader != null && !setCookieHeader.trim().isEmpty()) {
				cookie = extractA3Cookie(setCookieHeader);
			}

			if (cookie == null || cookie.isEmpty()) {
				throw new IOException("Failed to extract A3 cookie from response");
			}

			log.debug("Successfully initialized cookie: {}", cookie);

		} catch (IOException e) {
			log.error("Failed to initialize cookie", e);
			throw new IOException("Unable to retrieve Yahoo Finance cookie", e);
		}
	}

	/**
	 * Fetches crumb from Yahoo Finance using the existing cookie
	 */
	private static void fetchCrumb() throws IOException {
		log.debug("Fetching crumb from Yahoo Finance");

		try {
			URL url = new URL(CRUMB_URL);
			HttpURLConnection connection = createBasicConnection(url);

			connection.setRequestProperty("Cookie", cookie);

			int responseCode = connection.getResponseCode();
			log.debug("Crumb request response code: {}", responseCode);

			try (InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			     BufferedReader reader = new BufferedReader(isr)) {

				String crumbResult = reader.readLine();

				if (crumbResult != null && !crumbResult.trim().isEmpty()) {
					crumb.setValue(crumbResult.trim(), LocalDateTime.now().plusDays(1));
					log.debug("Successfully fetched crumb: {}", crumb);
				} else {
					throw new IOException("Empty crumb response from Yahoo Finance");
				}
			}

		} catch (IOException e) {
			log.error("Failed to fetch crumb", e);
			throw new IOException("Unable to retrieve Yahoo Finance crumb", e);
		}
	}

	/**
	 * Creates a basic HTTP connection with standard settings
	 */
	private static HttpURLConnection createBasicConnection(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(CONNECTION_TIMEOUT);
		connection.setReadTimeout(CONNECTION_TIMEOUT);
		connection.setInstanceFollowRedirects(true);

		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
		connection.setRequestProperty("Accept", "*/*");

		return connection;
	}

	/**
	 * Extracts A3 cookie value from Set-Cookie header
	 */
	private static String extractA3Cookie(String setCookieHeader) {
		try {
			return HttpCookie.parse(setCookieHeader)
					.stream()
					.filter(httpCookie -> "A3".equals(httpCookie.getName()))
					.map(httpCookie -> httpCookie.getName() + "=" + httpCookie.getValue())
					.findFirst()
					.orElse(null);
		} catch (Exception e) {
			log.warn("Failed to parse cookie header: {}", setCookieHeader, e);
			return null;
		}
	}

	/**
	 * Creates a connection for external use (non-circular)
	 */
	public static URLConnection createExternalConnection(String url, @Nullable Map<String, String> requestProperties) throws IOException {
		URL request = new URL(url);
		HttpURLConnection connection = createBasicConnection(request);

		if (requestProperties != null) {
			for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		return connection;
	}
}
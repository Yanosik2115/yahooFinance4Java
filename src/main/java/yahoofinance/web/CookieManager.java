package yahoofinance.web;

import lombok.extern.slf4j.Slf4j;
import yahoofinance.util.ConnectionUtils;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public final class CookieManager {

	private static volatile String cookie = null;
	private static final ReentrantLock lock = new ReentrantLock();

	private static final int CONNECTION_TIMEOUT = 5000;
	private static final String COOKIE_SCRAPE_URL = "https://fc.yahoo.com";

	private CookieManager() {
		throw new AssertionError("CookieManager should not be instantiated");
	}

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

	public static void clearCache() {
		lock.lock();
		try {
			cookie = null;
			log.debug("Cleared cached cookie");
		} finally {
			lock.unlock();
		}
	}

	private static void initializeCookie() throws IOException {
		log.debug("Initializing cookie from Yahoo Finance");

		try {
			URL url = new URL(COOKIE_SCRAPE_URL);
			HttpURLConnection connection = ConnectionUtils.createBasicConnection(url, CONNECTION_TIMEOUT);

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
}
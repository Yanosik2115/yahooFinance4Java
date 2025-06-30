package yahoofinance.web;

import lombok.extern.slf4j.Slf4j;
import yahoofinance.util.ConnectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public final class CrumbManager {

	private static final Crumb crumb = new Crumb();
	private static final ReentrantLock lock = new ReentrantLock();

	private static final int CONNECTION_TIMEOUT = 5000;
	private static final String CRUMB_URL = "https://query1.finance.yahoo.com/v1/test/getcrumb";

	private CrumbManager() {
		throw new AssertionError("CrumbManager should not be instantiated");
	}

	public static String getCrumb() throws IOException {
		if (crumb.isValid()) {
			return crumb.getValue();
		}

		lock.lock();
		try {
			if (crumb.isValid()) {
				return crumb.getValue();
			}

			fetchCrumb();

			if (!crumb.isValid()) {
				throw new IOException("Failed to initialize valid crumb");
			}

			return crumb.getValue();
		} finally {
			lock.unlock();
		}
	}

	public static void clearCache() {
		lock.lock();
		try {
			crumb.clear();
			log.debug("Cleared cached crumb");
		} finally {
			lock.unlock();
		}
	}

	private static void fetchCrumb() throws IOException {
		log.debug("Fetching crumb from Yahoo Finance");

		try {
			String cookie = CookieManager.getCookie();

			URL url = new URL(CRUMB_URL);
			HttpURLConnection connection = ConnectionUtils.createBasicConnection(url, CONNECTION_TIMEOUT);
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
}
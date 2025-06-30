package yahoofinance.util;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public final class ConnectionUtils {

	private ConnectionUtils() {
		throw new AssertionError("ConnectionUtils should not be instantiated");
	}

	public static HttpURLConnection createBasicConnection(URL url, int timeout) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(timeout);
		connection.setReadTimeout(timeout);
		connection.setInstanceFollowRedirects(true);

		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
		connection.setRequestProperty("Accept", "*/*");

		return connection;
	}

	public static URLConnection createExternalConnection(String url, @Nullable Map<String, String> requestProperties) throws IOException {
		URL request = new URL(url);
		HttpURLConnection connection = createBasicConnection(request, 5000);

		if (requestProperties != null) {
			for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		return connection;
	}
}
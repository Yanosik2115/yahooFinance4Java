package yahoofinance.exception;

import lombok.Getter;

@Getter
public class ConnectionException extends YFinanceException {

	private final int responseCode;
	private final String url;

	public ConnectionException(String message) {
		super(message);
		this.responseCode = -1;
		this.url = null;
	}

	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
		this.responseCode = -1;
		this.url = null;
	}

	public ConnectionException(String message, int responseCode, String url) {
		super(message);
		this.responseCode = responseCode;
		this.url = url;
	}

	public ConnectionException(String message, int responseCode, String url, Throwable cause) {
		super(message, cause);
		this.responseCode = responseCode;
		this.url = url;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder(super.getMessage());
		if (url != null) {
			sb.append(" [URL: ").append(url).append("]");
		}
		if (responseCode != -1) {
			sb.append(" [Response Code: ").append(responseCode).append("]");
		}
		return sb.toString();
	}
}

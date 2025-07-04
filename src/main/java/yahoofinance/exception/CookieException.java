package yahoofinance.exception;

public class CookieException extends YFinanceException {

	public CookieException(String message) {
		super(message);
	}

	public CookieException(String message, Throwable cause) {
		super(message, cause);
	}
}


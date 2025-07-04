package yahoofinance.exception;

public class CrumbException extends YFinanceException {

	public CrumbException(String message) {
		super(message);
	}

	public CrumbException(String message, Throwable cause) {
		super(message, cause);
	}
}

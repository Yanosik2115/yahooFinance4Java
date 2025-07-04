package yahoofinance.exception;

public class YFinanceException extends Exception{
	public YFinanceException(String message) {
		super(message);
	}

	public YFinanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public YFinanceException(Throwable cause) {
		super(cause);
	}
}

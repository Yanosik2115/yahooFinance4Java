package yahoofinance.exception;

public class ResponseParsingException extends YFinanceException {

	private final String responseContent;

	public ResponseParsingException(String message) {
		super(message);
		this.responseContent = null;
	}

	public ResponseParsingException(String message, String responseContent) {
		super(message);
		this.responseContent = responseContent;
	}

	public ResponseParsingException(String message, Throwable cause) {
		super(message, cause);
		this.responseContent = null;
	}

	public ResponseParsingException(String message, String responseContent, Throwable cause) {
		super(message, cause);
		this.responseContent = responseContent;
	}

	public String getResponseContent() {
		return responseContent;
	}
}

package yahoofinance.exception;

import lombok.Getter;

@Getter
public class RedirectException extends ConnectionException {

	private final int redirectCount;
	private final int redirectLimit;

	public RedirectException(String message, int redirectCount, int redirectLimit) {
		super(message);
		this.redirectCount = redirectCount;
		this.redirectLimit = redirectLimit;
	}

	public RedirectException(String message, int redirectCount, int redirectLimit, String url) {
		super(message, -1, url);
		this.redirectCount = redirectCount;
		this.redirectLimit = redirectLimit;
	}

	@Override
	public String getMessage() {
		return super.getMessage() +
		       String.format(" [Redirects: %d/%d]", redirectCount, redirectLimit);
	}
}

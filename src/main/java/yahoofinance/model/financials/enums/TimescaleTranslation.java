package yahoofinance.model.financials.enums;

public enum TimescaleTranslation {
	YEARLY("annual"),
	QUARTERLY("quarterly"),
	TRAILING("trailing");

	private final String value;

	TimescaleTranslation(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
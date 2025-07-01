package yahoofinance.quotes;

import lombok.Getter;

@Getter
public enum Region {
	EUROPE("EUROPE"),
	ASIA("ASIA"),
	US("US"),
	GB("GB"),
	RATES("RATES"),
	COMMODITIES("COMMODITIES"),
	CURRENCIES("CURRENCIES"),
	CRYPTOCURRENCIES("CRYPTOCURRENCIES");

	private final String region;

	Region(String region) {
		this.region = region;
	}
}

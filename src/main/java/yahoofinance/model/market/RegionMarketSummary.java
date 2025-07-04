package yahoofinance.model.market;

import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.FormattedValue;

import java.util.List;

@Getter
@Setter
public class RegionMarketSummary {

	private Region region;
	private String error;
	private List<MarketSummary> marketSummaries;
	private List<MarketStatus> marketStatus;


	@Getter
	@Setter
	public static class MarketStatus {
		private String id;
		private String name;
		private String status;
		private String yfitMarketId;
		private String close;
		private List<MarketTimeDuration> duration;
		private String message;
		private String open;
		private String yfitMarketStatus;
		private String time;
		private List<MarketTimeZone> timezone;
	}

	@Getter
	@Setter
	public static class MarketTimeZone {
		private String dst;
		private String gmtoffset;
		private String shortName;
		private String text;
	}

	@Getter
	@Setter
	public static class MarketTimeDuration {
		private String hrs;
		private String mins;
	}

	@Getter
	@Setter
	public static class MarketSummary {
		// Basic identification
		private String fullExchangeName;
		private String symbol;
		private String shortName;
		private String exchange;
		private String market;
		private String region;

		// Quote type information
		private String quoteType;
		private String typeDisp;
		private String language;
		private String currency;
		private String marketState;
		private String quoteSourceName;

		// Trading flags - Fixed naming
		private Boolean tradeable; // Fixed from "tradable"
		private Boolean cryptoTradeable;
		private Boolean triggerable;
		private Boolean hasPrePostMarketData;

		// Timezone information
		private String exchangeTimezoneName;
		private String exchangeTimezoneShortName;
		private Long gmtOffSetMilliseconds;

		// Timing and data info
		private Long firstTradeDateMilliseconds;
		private Integer exchangeDataDelayedBy; // Fixed from "exchangeDateDelayedBy"
		private Integer sourceInterval;
		private Integer priceHint;
		private String customPriceAlertConfidence;

		// Market values with formatted display
		private FormattedValue regularMarketTime;
		private FormattedValue regularMarketPrice;
		private FormattedValue regularMarketPreviousClose;
		private FormattedValue regularMarketChange;
		private FormattedValue regularMarketChangePercent;
	}

	public boolean hasMarketSummary() {
		return marketSummaries != null && !marketSummaries.isEmpty();
	}

	public boolean hasMarketTimes() {
		return marketStatus != null;
	}

	public int getMarketSummaryCount() {
		return marketSummaries != null ? marketSummaries.size() : 0;
	}
}
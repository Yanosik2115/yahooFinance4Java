package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;

@Getter
@Setter
public class SummaryDetail extends AbstractQuoteSummaryModule<SummaryDetail> {
	private Integer maxAge;
	private String currency;
	private Boolean tradeable;

	// Price Information
	private FormattedValue priceHint;
	private FormattedValue previousClose;
	private FormattedValue open;
	private FormattedValue dayLow;
	private FormattedValue dayHigh;

	// Regular Market Data
	private FormattedValue regularMarketPreviousClose;
	private FormattedValue regularMarketOpen;
	private FormattedValue regularMarketDayLow;
	private FormattedValue regularMarketDayHigh;

	// Dividend Information
	private FormattedValue dividendRate;
	private FormattedValue dividendYield;
	private FormattedValue exDividendDate;
	private FormattedValue payoutRatio;
	private FormattedValue fiveYearAvgDividendYield;
	private FormattedValue trailingAnnualDividendRate;
	private FormattedValue trailingAnnualDividendYield;

	// Valuation Metrics
	private FormattedValue beta;
	private FormattedValue trailingPE;
	private FormattedValue forwardPE;
	private FormattedValue priceToSalesTrailing12Months;
	private FormattedValue marketCap;

	// Volume Information
	private FormattedValue volume;
	private FormattedValue regularMarketVolume;
	private FormattedValue averageVolume;
	private FormattedValue averageVolume10days;
	private FormattedValue averageDailyVolume10Day;

	// Bid/Ask Information
	private FormattedValue bid;
	private FormattedValue ask;
	private FormattedValue bidSize;
	private FormattedValue askSize;

	// Technical Indicators
	private FormattedValue fiftyTwoWeekLow;
	private FormattedValue fiftyTwoWeekHigh;
	private FormattedValue fiftyDayAverage;
	private FormattedValue twoHundredDayAverage;

	@Override
	protected SummaryDetail parseInternal(JsonNode node) {
		SummaryDetail detail = new SummaryDetail();

		detail.setMaxAge(getIntegerValue(node, "maxAge"));
		detail.setCurrency(getStringValue(node, "currency"));
		detail.setTradeable(getBooleanValue(node, "tradeable"));

		detail.setPriceHint(parseFormattedValue(node.get("priceHint")));
		detail.setPreviousClose(parseFormattedValue(node.get("previousClose")));
		detail.setOpen(parseFormattedValue(node.get("open")));
		detail.setDayLow(parseFormattedValue(node.get("dayLow")));
		detail.setDayHigh(parseFormattedValue(node.get("dayHigh")));

		detail.setRegularMarketPreviousClose(parseFormattedValue(node.get("regularMarketPreviousClose")));
		detail.setRegularMarketOpen(parseFormattedValue(node.get("regularMarketOpen")));
		detail.setRegularMarketDayLow(parseFormattedValue(node.get("regularMarketDayLow")));
		detail.setRegularMarketDayHigh(parseFormattedValue(node.get("regularMarketDayHigh")));

		detail.setDividendRate(parseFormattedValue(node.get("dividendRate")));
		detail.setDividendYield(parseFormattedValue(node.get("dividendYield")));
		detail.setExDividendDate(parseFormattedValue(node.get("exDividendDate")));
		detail.setPayoutRatio(parseFormattedValue(node.get("payoutRatio")));
		detail.setFiveYearAvgDividendYield(parseFormattedValue(node.get("fiveYearAvgDividendYield")));
		detail.setTrailingAnnualDividendRate(parseFormattedValue(node.get("trailingAnnualDividendRate")));
		detail.setTrailingAnnualDividendYield(parseFormattedValue(node.get("trailingAnnualDividendYield")));

		detail.setBeta(parseFormattedValue(node.get("beta")));
		detail.setTrailingPE(parseFormattedValue(node.get("trailingPE")));
		detail.setForwardPE(parseFormattedValue(node.get("forwardPE")));
		detail.setPriceToSalesTrailing12Months(parseFormattedValue(node.get("priceToSalesTrailing12Months")));
		detail.setMarketCap(parseFormattedValue(node.get("marketCap")));

		detail.setVolume(parseFormattedValue(node.get("volume")));
		detail.setRegularMarketVolume(parseFormattedValue(node.get("regularMarketVolume")));
		detail.setAverageVolume(parseFormattedValue(node.get("averageVolume")));
		detail.setAverageVolume10days(parseFormattedValue(node.get("averageVolume10days")));
		detail.setAverageDailyVolume10Day(parseFormattedValue(node.get("averageDailyVolume10Day")));

		detail.setBid(parseFormattedValue(node.get("bid")));
		detail.setAsk(parseFormattedValue(node.get("ask")));
		detail.setBidSize(parseFormattedValue(node.get("bidSize")));
		detail.setAskSize(parseFormattedValue(node.get("askSize")));

		detail.setFiftyTwoWeekLow(parseFormattedValue(node.get("fiftyTwoWeekLow")));
		detail.setFiftyTwoWeekHigh(parseFormattedValue(node.get("fiftyTwoWeekHigh")));
		detail.setFiftyDayAverage(parseFormattedValue(node.get("fiftyDayAverage")));
		detail.setTwoHundredDayAverage(parseFormattedValue(node.get("twoHundredDayAverage")));

		return detail;
	}

	public static SummaryDetail fromJson(JsonNode node) {
		return new SummaryDetail().parse(node);
	}
}
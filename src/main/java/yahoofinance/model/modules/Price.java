package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;

@Getter
@Setter
public class Price extends AbstractQuoteSummaryModule<Price> {
	private Integer maxAge;

	// Pre-Market Data
	private FormattedValue preMarketChangePercent;
	private FormattedValue preMarketChange;
	private Long preMarketTime;
	private FormattedValue preMarketPrice;
	private String preMarketSource;

	// Post-Market Data
	private FormattedValue postMarketChangePercent;
	private FormattedValue postMarketChange;
	private Long postMarketTime;
	private FormattedValue postMarketPrice;
	private String postMarketSource;

	// Regular Market Data
	private FormattedValue regularMarketChangePercent;
	private FormattedValue regularMarketChange;
	private Long regularMarketTime;
	private FormattedValue priceHint;
	private FormattedValue regularMarketPrice;
	private FormattedValue regularMarketDayHigh;
	private FormattedValue regularMarketDayLow;
	private FormattedValue regularMarketVolume;
	private FormattedValue regularMarketPreviousClose;
	private String regularMarketSource;
	private FormattedValue regularMarketOpen;

	// Volume Metrics
	private FormattedValue averageDailyVolume10Day;
	private FormattedValue averageDailyVolume3Month;

	// Options Data
	private FormattedValue strikePrice;
	private FormattedValue openInterest;

	// Exchange and Symbol Information
	private String exchange;
	private String exchangeName;
	private Integer exchangeDataDelayedBy;
	private String marketState;
	private String quoteType;
	private String symbol;
	private String underlyingSymbol;
	private String shortName;
	private String longName;
	private String currency;
	private String quoteSourceName;
	private String currencySymbol;
	private String fromCurrency;
	private String toCurrency;
	private String lastMarket;

	// Crypto-Specific Fields
	private FormattedValue volume24Hr;
	private FormattedValue volumeAllCurrencies;
	private FormattedValue circulatingSupply;

	// Market Valuation
	private FormattedValue marketCap;

	@Override
	protected Price parseInternal(JsonNode node) {
		Price price = new Price();

		price.setMaxAge(getIntegerValue(node, "maxAge"));

		price.setPreMarketChangePercent(parseFormattedValue(node.get("preMarketChangePercent")));
		price.setPreMarketChange(parseFormattedValue(node.get("preMarketChange")));
		price.setPreMarketTime(getLongValue(node, "preMarketTime"));
		price.setPreMarketPrice(parseFormattedValue(node.get("preMarketPrice")));
		price.setPreMarketSource(getStringValue(node, "preMarketSource"));

		price.setPostMarketChangePercent(parseFormattedValue(node.get("postMarketChangePercent")));
		price.setPostMarketChange(parseFormattedValue(node.get("postMarketChange")));
		price.setPostMarketTime(getLongValue(node, "postMarketTime"));
		price.setPostMarketPrice(parseFormattedValue(node.get("postMarketPrice")));
		price.setPostMarketSource(getStringValue(node, "postMarketSource"));

		price.setRegularMarketChangePercent(parseFormattedValue(node.get("regularMarketChangePercent")));
		price.setRegularMarketChange(parseFormattedValue(node.get("regularMarketChange")));
		price.setRegularMarketTime(getLongValue(node, "regularMarketTime"));
		price.setPriceHint(parseFormattedValue(node.get("priceHint")));
		price.setRegularMarketPrice(parseFormattedValue(node.get("regularMarketPrice")));
		price.setRegularMarketDayHigh(parseFormattedValue(node.get("regularMarketDayHigh")));
		price.setRegularMarketDayLow(parseFormattedValue(node.get("regularMarketDayLow")));
		price.setRegularMarketVolume(parseFormattedValue(node.get("regularMarketVolume")));
		price.setRegularMarketPreviousClose(parseFormattedValue(node.get("regularMarketPreviousClose")));
		price.setRegularMarketSource(getStringValue(node, "regularMarketSource"));
		price.setRegularMarketOpen(parseFormattedValue(node.get("regularMarketOpen")));

		price.setAverageDailyVolume10Day(parseFormattedValue(node.get("averageDailyVolume10Day")));
		price.setAverageDailyVolume3Month(parseFormattedValue(node.get("averageDailyVolume3Month")));

		price.setStrikePrice(parseFormattedValue(node.get("strikePrice")));
		price.setOpenInterest(parseFormattedValue(node.get("openInterest")));

		price.setExchange(getStringValue(node, "exchange"));
		price.setExchangeName(getStringValue(node, "exchangeName"));
		price.setExchangeDataDelayedBy(getIntegerValue(node, "exchangeDataDelayedBy"));
		price.setMarketState(getStringValue(node, "marketState"));
		price.setQuoteType(getStringValue(node, "quoteType"));
		price.setSymbol(getStringValue(node, "symbol"));
		price.setUnderlyingSymbol(getStringValue(node, "underlyingSymbol"));
		price.setShortName(getStringValue(node, "shortName"));
		price.setLongName(getStringValue(node, "longName"));
		price.setCurrency(getStringValue(node, "currency"));
		price.setQuoteSourceName(getStringValue(node, "quoteSourceName"));
		price.setCurrencySymbol(getStringValue(node, "currencySymbol"));
		price.setFromCurrency(getStringValue(node, "fromCurrency"));
		price.setToCurrency(getStringValue(node, "toCurrency"));
		price.setLastMarket(getStringValue(node, "lastMarket"));

		price.setVolume24Hr(parseFormattedValue(node.get("volume24Hr")));
		price.setVolumeAllCurrencies(parseFormattedValue(node.get("volumeAllCurrencies")));
		price.setCirculatingSupply(parseFormattedValue(node.get("circulatingSupply")));

		price.setMarketCap(parseFormattedValue(node.get("marketCap")));

		return price;
	}

	public static Price fromJson(JsonNode node) {
		return new Price().parse(node);
	}
}
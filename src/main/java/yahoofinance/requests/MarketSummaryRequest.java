package yahoofinance.requests;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.market.Region;
import yahoofinance.model.market.RegionMarketSummary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static yahoofinance.util.Utils.*;

@Slf4j
public class MarketSummaryRequest extends QuoteRequest<List<RegionMarketSummary.MarketSummary>> {

	private static final String MARKET_SUMMARY_URL = "https://query1.finance.yahoo.com/v6/finance/quote/marketSummary";

	private final Region region;

	public MarketSummaryRequest(Region region) {
		super();
		this.region = region;
	}

	public MarketSummaryRequest() {
		this(Region.US);
	}

	@Override
	public String getURL() {
		return MARKET_SUMMARY_URL;
	}

	@Override
	protected boolean requiresSymbol() {
		return false;
	}

	@Override
	protected boolean useCookieAndCrumb() {
		return false;
	}

	@Override
	public Map<String, String> getParams() {
		return Map.of(
				"formatted", "true",
				"lang", "en-US",
				"region", region.getRegion()
		);
	}

	@Override
	public List<RegionMarketSummary.MarketSummary> parseJson(JsonNode node) {
		if (node == null || node.isNull()) {
			return List.of();
		}
		return parseMarketSummaryResults(node);
	}

	private List<RegionMarketSummary.MarketSummary> parseMarketSummaryResults(JsonNode resultNode) {
		List<RegionMarketSummary.MarketSummary> marketSummaryList = new ArrayList<>();

		Iterator<JsonNode> it = resultNode.elements();
		while (it.hasNext()) {
			RegionMarketSummary.MarketSummary marketSummary = new RegionMarketSummary.MarketSummary();
			JsonNode node = it.next();

			marketSummary.setFullExchangeName(getStringValue(node, "fullExchangeName"));
			marketSummary.setSymbol(getStringValue(node, "symbol"));
			marketSummary.setShortName(getStringValue(node, "shortName"));
			marketSummary.setExchange(getStringValue(node, "exchange"));
			marketSummary.setMarket(getStringValue(node, "market"));
			marketSummary.setRegion(getStringValue(node, "region"));

			marketSummary.setQuoteType(getStringValue(node, "quoteType"));
			marketSummary.setTypeDisp(getStringValue(node, "typeDisp"));
			marketSummary.setLanguage(getStringValue(node, "language"));
			marketSummary.setCurrency(getStringValue(node, "currency"));
			marketSummary.setMarketState(getStringValue(node, "marketState"));
			marketSummary.setQuoteSourceName(getStringValue(node, "quoteSourceName"));

			marketSummary.setTradeable(getBooleanValue(node, "tradeable"));
			marketSummary.setCryptoTradeable(getBooleanValue(node, "cryptoTradeable"));
			marketSummary.setTriggerable(getBooleanValue(node, "triggerable"));
			marketSummary.setHasPrePostMarketData(getBooleanValue(node, "hasPrePostMarketData"));

			marketSummary.setExchangeTimezoneName(getStringValue(node, "exchangeTimezoneName"));
			marketSummary.setExchangeTimezoneShortName(getStringValue(node, "exchangeTimezoneShortName"));
			marketSummary.setGmtOffSetMilliseconds(getLongValue(node, "gmtOffSetMilliseconds"));

			marketSummary.setFirstTradeDateMilliseconds(getLongValue(node, "firstTradeDateMilliseconds"));
			marketSummary.setExchangeDataDelayedBy(getIntegerValue(node, "exchangeDataDelayedBy"));
			marketSummary.setSourceInterval(getIntegerValue(node, "sourceInterval"));
			marketSummary.setPriceHint(getIntegerValue(node, "priceHint"));
			marketSummary.setCustomPriceAlertConfidence(getStringValue(node, "customPriceAlertConfidence"));

			marketSummary.setRegularMarketTime(parseFormattedValue(node.get("regularMarketTime")));
			marketSummary.setRegularMarketChangePercent(parseFormattedValue(node.get("regularMarketChangePercent")));
			marketSummary.setRegularMarketPreviousClose(parseFormattedValue(node.get("regularMarketPreviousClose")));
			marketSummary.setRegularMarketChange(parseFormattedValue(node.get("regularMarketChange")));
			marketSummary.setRegularMarketPrice(parseFormattedValue(node.get("regularMarketPrice")));

			marketSummaryList.add(marketSummary);
		}

		return marketSummaryList;
	}
}
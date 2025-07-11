package yahoofinance.requests;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.StockHistory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static yahoofinance.util.Utils.*;

@Slf4j
public class StockHistoryRequest extends QuoteRequest<StockHistory> {
	private static final String STOCK_HISTORY_URL = "https://query2.finance.yahoo.com/v8/finance/chart";
	private final ValidRanges range;
	private final ValidIntervals interval;
	private Long startDate;
	private Long endDate;

	@Getter
	public enum ValidRanges {
		ONE_DAY("1d"),
		FIVE_DAYS("5d"),
		ONE_MONTH("1mo"),
		THREE_MONTHS("3mo"),
		SIX_MONTHS("6mo"),
		ONE_YEAR("1y"),
		TWO_YEARS("2y"),
		FIVE_YEARS("5y"),
		TEN_YEARS("10y"),
		YTD("ytd"),
		MAX("max");

		public long getTimeMilisecond() {
			return switch (this) {
				case ONE_DAY -> (24 * 60 * 60);
				case FIVE_DAYS -> (5 * 24 * 60 * 60);
				case ONE_MONTH -> (30L * 24 * 60 * 60);
				case THREE_MONTHS -> (90L * 24 * 60 * 60);
				case SIX_MONTHS -> (180L * 24 * 60 * 60);
				case ONE_YEAR -> (365L * 24 * 60 * 60);
				case TWO_YEARS -> (2 * 365L * 24 * 60 * 60);
				case FIVE_YEARS -> (5 * 365L * 24 * 60 * 60);
				case TEN_YEARS -> (10 * 365L * 24 * 60 * 60);
				case YTD -> getYearStartMiliseconds();
				case MAX -> 0;
			};
		}

		private long getYearStartMiliseconds() {
			LocalDateTime ytd = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0);
			LocalDateTime now = LocalDateTime.now();

			return now.toEpochSecond(ZoneOffset.UTC) - ytd.toEpochSecond(ZoneOffset.UTC);
		}

		private final String range;

		ValidRanges(String range) {
			this.range = range;
		}
	}

	@Getter
	public enum ValidIntervals {
		ONE_MINUTE("1m"),
		TWO_MINUTES("2m"),
		FIVE_MINUTES("5m"),
		FIFTEEN_MINUTES("15m"),
		THIRTY_MINUTES("30m"),
		SIXTY_MINUTES("60m"),
		NINETY_MINUTES("90m"),
		ONE_HOUR("1h"),
		FOUR_HOURS("4h"),
		ONE_DAY("1d"),
		FIVE_DAYS("5d"),
		ONE_WEEK("1wk"),
		ONE_MONTH("1mo"),
		THREE_MONTHS("3mo");

		private final String interval;

		ValidIntervals(String interval) {
			this.interval = interval;
		}
	}


	public StockHistoryRequest(String symbol, ValidRanges range, ValidIntervals interval) {
		super(symbol);
		this.range = range;
		this.interval = interval;
	}

	public StockHistoryRequest(String symbol, LocalDateTime startDate, LocalDateTime endDate, ValidIntervals interval) {
		super(symbol);
		this.interval = interval;
		this.range = null;
		if (startDate != null)
			this.startDate = startDate.toEpochSecond(ZoneOffset.UTC);
		if (endDate != null)
			this.endDate = endDate.toEpochSecond(ZoneOffset.UTC);
	}

	public StockHistoryRequest(String symbol, LocalDateTime startDate, ValidRanges range, ValidIntervals interval) {
		super(symbol);
		this.interval = interval;
		this.range = range;
		if (startDate != null) {
			this.startDate = startDate.toEpochSecond(ZoneOffset.UTC);
			this.endDate = startDate.toEpochSecond(ZoneOffset.UTC) + range.getTimeMilisecond();
		}

	}

	public StockHistoryRequest(String symbol, ValidRanges range, LocalDateTime endDate, ValidIntervals interval) {
		super(symbol);
		this.interval = interval;
		this.range = range;
		if (endDate != null) {
			this.startDate = endDate.toEpochSecond(ZoneOffset.UTC) - range.getTimeMilisecond();
			this.endDate = endDate.toEpochSecond(ZoneOffset.UTC);
		}

	}

	public StockHistoryRequest(String symbol) {
		this(symbol, ValidRanges.ONE_MONTH, ValidIntervals.ONE_DAY);
	}


	@Override
	public Map<String, String> getParams() {
		Map<String, String> params = new HashMap<>();

		if (range != null && startDate == null && endDate == null) {
			params.put("range", range.getRange());
		}

		if (interval != null) {
			params.put("interval", interval.getInterval());
		} else {
			params.put("interval", ValidIntervals.ONE_DAY.getInterval());
		}

		if (startDate != null) {
			params.put("period1", String.valueOf(startDate));
		}

		if (endDate != null) {
			params.put("period2", String.valueOf(endDate));
		}

		return params;
	}

	@Override
	public String getURL() {
		return STOCK_HISTORY_URL;
	}

	@Override
	public StockHistory parseJson(JsonNode resultNode) {
		if (resultNode == null || resultNode.isNull()) {
			return null;
		}
		StockHistory stockHistory = new StockHistory();
		resultNode = resultNode.get(0);

		try {
			JsonNode metaNode = resultNode.get("meta");
			if (metaNode != null && !metaNode.isNull()) {
				stockHistory.setMeta(parseMeta(metaNode));
			}

			JsonNode timestampNode = resultNode.get("timestamp");
			if (timestampNode != null && timestampNode.isArray()) {
				List<Long> timestamps = new ArrayList<>();
				for (JsonNode tsNode : timestampNode) {
					if (!tsNode.isNull()) {
						timestamps.add(tsNode.asLong());
					}
				}
				stockHistory.setTimestamp(timestamps);
			}

			JsonNode indicatorsNode = resultNode.get("indicators");
			if (indicatorsNode != null && !indicatorsNode.isNull()) {
				stockHistory.setIndicators(parseIndicators(indicatorsNode));
			}

		} catch (Exception e) {
			log.error("Error parsing StockHistory JSON: {}", e.getMessage());
			return null;
		}

		return stockHistory;
	}

	private StockHistory.Meta parseMeta(JsonNode metaNode) {
		StockHistory.Meta meta = new StockHistory.Meta();

		meta.setCurrency(getStringValue(metaNode, "currency"));
		meta.setSymbol(getStringValue(metaNode, "symbol"));
		meta.setExchangeName(getStringValue(metaNode, "exchangeName"));
		meta.setFullExchangeName(getStringValue(metaNode, "fullExchangeName"));
		meta.setInstrumentType(getStringValue(metaNode, "instrumentType"));
		meta.setTimezone(getStringValue(metaNode, "timezone"));
		meta.setExchangeTimezoneName(getStringValue(metaNode, "exchangeTimezoneName"));
		meta.setLongName(getStringValue(metaNode, "longName"));
		meta.setShortName(getStringValue(metaNode, "shortName"));
		meta.setDataGranularity(getStringValue(metaNode, "dataGranularity"));
		meta.setRange(getStringValue(metaNode, "range"));

		meta.setFirstTradeDate(getLongValue(metaNode, "firstTradeDate"));
		meta.setRegularMarketTime(getLongValue(metaNode, "regularMarketTime"));
		meta.setGmtoffset(getLongValue(metaNode, "gmtoffset"));
		meta.setRegularMarketVolume(getLongValue(metaNode, "regularMarketVolume"));

		meta.setRegularMarketPrice(getDoubleValue(metaNode, "regularMarketPrice"));
		meta.setFiftyTwoWeekHigh(getDoubleValue(metaNode, "fiftyTwoWeekHigh"));
		meta.setFiftyTwoWeekLow(getDoubleValue(metaNode, "fiftyTwoWeekLow"));
		meta.setRegularMarketDayHigh(getDoubleValue(metaNode, "regularMarketDayHigh"));
		meta.setRegularMarketDayLow(getDoubleValue(metaNode, "regularMarketDayLow"));
		meta.setChartPreviousClose(getDoubleValue(metaNode, "chartPreviousClose"));
		meta.setPreviousClose(getDoubleValue(metaNode, "previousClose"));

		meta.setScale(getIntegerValue(metaNode, "scale"));
		meta.setPriceHint(getIntegerValue(metaNode, "priceHint"));

		meta.setHasPrePostMarketData(getBooleanValue(metaNode, "hasPrePostMarketData"));

		JsonNode currentTradingPeriodNode = metaNode.get("currentTradingPeriod");
		if (currentTradingPeriodNode != null && !currentTradingPeriodNode.isNull()) {
			meta.setCurrentTradingPeriod(parseCurrentTradingPeriod(currentTradingPeriodNode));
		}

		JsonNode tradingPeriodsNode = metaNode.get("tradingPeriods");
		if (tradingPeriodsNode != null && tradingPeriodsNode.isArray()) {
			List<List<StockHistory.TradingPeriodValue>> tradingPeriods = new ArrayList<>();
			for (JsonNode outerArray : tradingPeriodsNode) {
				if (outerArray.isArray()) {
					List<StockHistory.TradingPeriodValue> innerList = new ArrayList<>();
					for (JsonNode periodNode : outerArray) {
						innerList.add(parseTradingPeriodValue(periodNode));
					}
					tradingPeriods.add(innerList);
				}
			}
			meta.setTradingPeriods(tradingPeriods);
		}

		JsonNode validRangesNode = metaNode.get("validRanges");
		if (validRangesNode != null && validRangesNode.isArray()) {
			List<String> validRanges = new ArrayList<>();
			for (JsonNode rangeNode : validRangesNode) {
				if (!rangeNode.isNull()) {
					validRanges.add(rangeNode.asText());
				}
			}
			meta.setValidRanges(validRanges);
		}

		return meta;
	}

	private StockHistory.CurrentTradingPeriod parseCurrentTradingPeriod(JsonNode node) {
		StockHistory.CurrentTradingPeriod ctp = new StockHistory.CurrentTradingPeriod();

		JsonNode preNode = node.get("pre");
		if (preNode != null && !preNode.isNull()) {
			ctp.setPre(parseTradingPeriodValue(preNode));
		}

		JsonNode regularNode = node.get("regular");
		if (regularNode != null && !regularNode.isNull()) {
			ctp.setRegular(parseTradingPeriodValue(regularNode));
		}

		JsonNode postNode = node.get("post");
		if (postNode != null && !postNode.isNull()) {
			ctp.setPost(parseTradingPeriodValue(postNode));
		}

		return ctp;
	}

	private StockHistory.TradingPeriodValue parseTradingPeriodValue(JsonNode node) {
		StockHistory.TradingPeriodValue tpv = new StockHistory.TradingPeriodValue();

		tpv.setTimezone(getStringValue(node, "timezone"));
		tpv.setStart(getLongValue(node, "start"));
		tpv.setEnd(getLongValue(node, "end"));
		tpv.setGmtoffset(getLongValue(node, "gmtoffset"));

		return tpv;
	}

	private StockHistory.Indicators parseIndicators(JsonNode indicatorsNode) {
		StockHistory.Indicators indicators = new StockHistory.Indicators();

		JsonNode quoteNode = indicatorsNode.get("quote");
		if (quoteNode != null && quoteNode.isArray()) {
			List<StockHistory.Quote> quotes = new ArrayList<>();
			for (JsonNode qNode : quoteNode) {
				quotes.add(parseQuote(qNode));
			}
			indicators.setQuote(quotes);
		}

		return indicators;
	}

	private StockHistory.Quote parseQuote(JsonNode quoteNode) {
		StockHistory.Quote quote = new StockHistory.Quote();

		JsonNode highNode = quoteNode.get("high");
		if (highNode != null && highNode.isArray()) {
			quote.setHigh(parseDoubleArray(highNode));
		}

		JsonNode lowNode = quoteNode.get("low");
		if (lowNode != null && lowNode.isArray()) {
			quote.setLow(parseDoubleArray(lowNode));
		}

		JsonNode openNode = quoteNode.get("open");
		if (openNode != null && openNode.isArray()) {
			quote.setOpen(parseDoubleArray(openNode));
		}

		JsonNode closeNode = quoteNode.get("close");
		if (closeNode != null && closeNode.isArray()) {
			quote.setClose(parseDoubleArray(closeNode));
		}

		JsonNode volumeNode = quoteNode.get("volume");
		if (volumeNode != null && volumeNode.isArray()) {
			List<Long> volumes = new ArrayList<>();
			for (JsonNode vNode : volumeNode) {
				if (vNode.isNull()) {
					volumes.add(null);
				} else {
					volumes.add(vNode.asLong());
				}
			}
			quote.setVolume(volumes);
		}

		return quote;
	}
}

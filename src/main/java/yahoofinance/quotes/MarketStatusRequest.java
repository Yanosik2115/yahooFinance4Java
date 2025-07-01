package yahoofinance.quotes;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.RegionMarketSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static yahoofinance.util.Utils.*;

@Slf4j
public class MarketStatusRequest extends QuoteRequest<List<RegionMarketSummary.MarketStatus>> {

	private static final String MARKET_STATUS_URL = "https://query1.finance.yahoo.com/v6/finance/markettime";

	private final Region region;

	public MarketStatusRequest(Region region) {
		super();
		this.region = region;
	}

	@Override
	public String getURL() {
		return MARKET_STATUS_URL;
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
	public List<RegionMarketSummary.MarketStatus> parseJson(JsonNode node) {
		if (node == null || node.isNull()) {
			return null;
		}

		try {
			JsonNode financeNode = node.get("finance");
			if (financeNode != null && !financeNode.isNull()) {
				return parseMarketStatusResponse(financeNode);
			}
		} catch (Exception e) {
			log.error("Error parsing MarketStatus JSON: {}", e.getMessage(), e);
			return null;
		}

		return null;
	}

	private List<RegionMarketSummary.MarketStatus> parseMarketStatusResponse(JsonNode financeNode) {
		List<RegionMarketSummary.MarketStatus> marketStatusList = new ArrayList<>();
		JsonNode marketStatusArrayNode = financeNode.get("marketTimes");
		if (marketStatusArrayNode != null && marketStatusArrayNode.isArray()) {
			for (JsonNode marketStatus : marketStatusArrayNode) {
				JsonNode marketStatusNode = marketStatus.get("marketTime");
				if (marketStatusNode != null && marketStatusNode.isArray()) {
					for (JsonNode marketTime : marketStatusNode) {
						RegionMarketSummary.MarketStatus marketStatus1 = parseMarketStatus(marketTime);
						marketStatusList.add(marketStatus1);
					}
				}
			}
		}

		return marketStatusList;
	}

	private RegionMarketSummary.MarketStatus parseMarketStatus(JsonNode timeNode) {
		RegionMarketSummary.MarketStatus marketStatus = new RegionMarketSummary.MarketStatus();

		marketStatus.setId(getStringValue(timeNode, "id"));
		marketStatus.setName(getStringValue(timeNode, "name"));
		marketStatus.setStatus(getStringValue(timeNode, "status"));
		marketStatus.setYfitMarketId(getStringValue(timeNode, "yfit_market_id"));
		marketStatus.setClose(getStringValue(timeNode, "close"));
		marketStatus.setMessage(getStringValue(timeNode, "message"));
		marketStatus.setOpen(getStringValue(timeNode, "open"));
		marketStatus.setYfitMarketStatus(getStringValue(timeNode, "yfit_market_status"));
		marketStatus.setTime(getStringValue(timeNode, "time"));

		JsonNode durationNode = timeNode.get("duration");
		if (durationNode != null && durationNode.isArray()) {
			List<RegionMarketSummary.MarketTimeDuration> durations = new ArrayList<>();
			for (JsonNode durNode : durationNode) {
				RegionMarketSummary.MarketTimeDuration duration = new RegionMarketSummary.MarketTimeDuration();
				duration.setHrs(getStringValue(durNode, "hrs"));
				duration.setMins(getStringValue(durNode, "mins"));
				durations.add(duration);
			}
			marketStatus.setDuration(durations);
		}

		JsonNode timezoneNode = timeNode.get("timezone");
		if (timezoneNode != null && timezoneNode.isArray()) {
			List<RegionMarketSummary.MarketTimeZone> timezones = new ArrayList<>();
			for (JsonNode tzNode : timezoneNode) {
				RegionMarketSummary.MarketTimeZone timezone = new RegionMarketSummary.MarketTimeZone();
				timezone.setDst(getStringValue(tzNode, "dst"));
				timezone.setGmtoffset(getStringValue(tzNode, "gmtoffset"));
				timezone.setShortName(getStringValue(tzNode, "short"));
				timezone.setText(getStringValue(tzNode, "$text"));
				timezones.add(timezone);
			}
			marketStatus.setTimezone(timezones);
		}

		return marketStatus;
	}


}
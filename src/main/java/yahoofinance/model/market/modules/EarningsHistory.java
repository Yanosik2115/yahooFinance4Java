package yahoofinance.model.market.modules;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;
import java.util.List;
import static yahoofinance.util.Utils.*;

@Getter
@Setter
public class EarningsHistory extends AbstractQuoteSummaryModule<EarningsHistory> {
	private List<EarningsHistoryItem> history;
	private Integer maxAge;

	@Override
	protected EarningsHistory parseInternal(JsonNode node) {
		EarningsHistory earningsHistory = new EarningsHistory();

		earningsHistory.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("history")) {
			earningsHistory.setHistory(parseModuleArray(node.get("history"), EarningsHistoryItem.class));
		}

		return earningsHistory;
	}


	public static EarningsHistory fromJson(JsonNode node) {
		return new EarningsHistory().parse(node);
	}

	@Getter
	@Setter
	public static class EarningsHistoryItem extends AbstractQuoteSummaryModule<EarningsHistoryItem> {
		private Integer maxAge;

		private FormattedValue epsActual;
		private FormattedValue epsEstimate;
		private FormattedValue epsDifference;
		private FormattedValue surprisePercent;

		private FormattedValue quarter;
		private String period;
		private String currency;

		@Override
		protected EarningsHistoryItem parseInternal(JsonNode node) {
			EarningsHistoryItem item = new EarningsHistoryItem();

			item.setMaxAge(getIntegerValue(node, "maxAge"));

			// Parse EPS metrics
			item.setEpsActual(parseFormattedValue(node.get("epsActual")));
			item.setEpsEstimate(parseFormattedValue(node.get("epsEstimate")));
			item.setEpsDifference(parseFormattedValue(node.get("epsDifference")));
			item.setSurprisePercent(parseFormattedValue(node.get("surprisePercent")));

			// Parse period information
			item.setQuarter(parseFormattedValue(node.get("quarter")));
			item.setPeriod(getStringValue(node, "period"));
			item.setCurrency(getStringValue(node, "currency"));

			return item;
		}

		public static EarningsHistoryItem fromJson(JsonNode node) {
			return new EarningsHistoryItem().parse(node);
		}
	}
}
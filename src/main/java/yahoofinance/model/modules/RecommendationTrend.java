package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.model.common.AbstractQuoteSummaryModule;

import java.util.List;

@Getter
@Setter
public class RecommendationTrend extends AbstractQuoteSummaryModule<RecommendationTrend> {
	private List<RecommendationTrendItem> trend;
	private Integer maxAge;

	@Override
	protected RecommendationTrend parseInternal(JsonNode node) {
		RecommendationTrend recommendationTrend = new RecommendationTrend();

		recommendationTrend.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("trend")) {
			recommendationTrend.setTrend(parseModuleArray(node.get("trend"), RecommendationTrendItem.class));
		}

		return recommendationTrend;
	}


	public static RecommendationTrend fromJson(JsonNode node) {
		return new RecommendationTrend().parse(node);
	}

	@Getter
	@Setter
	public static class RecommendationTrendItem extends AbstractQuoteSummaryModule<RecommendationTrendItem> {
		private String period;

		// Analyst Recommendation Counts
		private Integer strongBuy;
		private Integer buy;
		private Integer hold;
		private Integer sell;
		private Integer strongSell;

		@Override
		protected RecommendationTrendItem parseInternal(JsonNode node) {
			RecommendationTrendItem item = new RecommendationTrendItem();

			item.setPeriod(getStringValue(node, "period"));

			item.setStrongBuy(getIntegerValue(node, "strongBuy"));
			item.setBuy(getIntegerValue(node, "buy"));
			item.setHold(getIntegerValue(node, "hold"));
			item.setSell(getIntegerValue(node, "sell"));
			item.setStrongSell(getIntegerValue(node, "strongSell"));

			return item;
		}

		public static RecommendationTrendItem fromJson(JsonNode node) {
			return new RecommendationTrendItem().parse(node);
		}

		/**
		 * Calculate total number of analyst recommendations
		 */
		public int getTotalRecommendations() {
			int total = 0;
			if (strongBuy != null) total += strongBuy;
			if (buy != null) total += buy;
			if (hold != null) total += hold;
			if (sell != null) total += sell;
			if (strongSell != null) total += strongSell;
			return total;
		}

		/**
		 * Calculate weighted average recommendation score
		 * Scale: 1.0 (Strong Buy) to 5.0 (Strong Sell)
		 */
		public double getWeightedAverageScore() {
			int total = getTotalRecommendations();
			if (total == 0) return 0.0;

			int weightedSum = 0;
			if (strongBuy != null) weightedSum += strongBuy * 1;
			if (buy != null) weightedSum += buy * 2;
			if (hold != null) weightedSum += hold * 3;
			if (sell != null) weightedSum += sell * 4;
			if (strongSell != null) weightedSum += strongSell * 5;

			return (double) weightedSum / total;
		}

		/**
		 * Get percentage of bullish recommendations (Strong Buy + Buy)
		 */
		public double getBullishPercentage() {
			int total = getTotalRecommendations();
			if (total == 0) return 0.0;

			int bullish = 0;
			if (strongBuy != null) bullish += strongBuy;
			if (buy != null) bullish += buy;

			return (double) bullish / total * 100.0;
		}
	}
}
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
public class EarningsTrend extends AbstractQuoteSummaryModule<EarningsTrend> {
	private List<EarningsTrendItem> trend;
	private Integer maxAge;

	@Override
	protected EarningsTrend parseInternal(JsonNode node) {
		EarningsTrend earningsTrend = new EarningsTrend();

		earningsTrend.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("trend")) {
			earningsTrend.setTrend(parseModuleArray(node.get("trend"), EarningsTrendItem.class));
		}

		return earningsTrend;
	}

	public static EarningsTrend fromJson(JsonNode node) {
		return new EarningsTrend().parse(node);
	}

	@Getter
	@Setter
	public static class EarningsTrendItem extends AbstractQuoteSummaryModule<EarningsTrendItem> {
		private Integer maxAge;
		private String period;
		private String endDate;
		private FormattedValue growth;
		private EarningsEstimate earningsEstimate;
		private RevenueEstimate revenueEstimate;
		private EpsTrend epsTrend;
		private EpsRevisions epsRevisions;

		@Override
		protected EarningsTrendItem parseInternal(JsonNode node) {
			EarningsTrendItem item = new EarningsTrendItem();

			item.setMaxAge(getIntegerValue(node, "maxAge"));
			item.setPeriod(getStringValue(node, "period"));
			item.setEndDate(getStringValue(node, "endDate"));
			item.setGrowth(parseFormattedValue(node.get("growth")));

			if (node.has("earningsEstimate")) {
				item.setEarningsEstimate(EarningsEstimate.fromJson(node.get("earningsEstimate")));
			}

			if (node.has("revenueEstimate")) {
				item.setRevenueEstimate(RevenueEstimate.fromJson(node.get("revenueEstimate")));
			}

			if (node.has("epsTrend")) {
				item.setEpsTrend(EpsTrend.fromJson(node.get("epsTrend")));
			}

			if (node.has("epsRevisions")) {
				item.setEpsRevisions(EpsRevisions.fromJson(node.get("epsRevisions")));
			}

			return item;
		}

		public static EarningsTrendItem fromJson(JsonNode node) {
			return new EarningsTrendItem().parse(node);
		}
	}

	@Getter
	@Setter
	public static class EarningsEstimate extends AbstractQuoteSummaryModule<EarningsEstimate> {
		private FormattedValue avg;
		private FormattedValue low;
		private FormattedValue high;
		private FormattedValue yearAgoEps;
		private FormattedValue numberOfAnalysts;
		private FormattedValue growth;
		private String earningsCurrency;

		@Override
		protected EarningsEstimate parseInternal(JsonNode node) {
			EarningsEstimate estimate = new EarningsEstimate();

			estimate.setAvg(parseFormattedValue(node.get("avg")));
			estimate.setLow(parseFormattedValue(node.get("low")));
			estimate.setHigh(parseFormattedValue(node.get("high")));
			estimate.setYearAgoEps(parseFormattedValue(node.get("yearAgoEps")));
			estimate.setNumberOfAnalysts(parseFormattedValue(node.get("numberOfAnalysts")));
			estimate.setGrowth(parseFormattedValue(node.get("growth")));
			estimate.setEarningsCurrency(getStringValue(node, "earningsCurrency"));

			return estimate;
		}

		public static EarningsEstimate fromJson(JsonNode node) {
			return new EarningsEstimate().parse(node);
		}
	}

	@Getter
	@Setter
	public static class RevenueEstimate extends AbstractQuoteSummaryModule<RevenueEstimate> {
		private FormattedValue avg;
		private FormattedValue low;
		private FormattedValue high;
		private FormattedValue numberOfAnalysts;
		private FormattedValue yearAgoRevenue;
		private FormattedValue growth;
		private String revenueCurrency;

		@Override
		protected RevenueEstimate parseInternal(JsonNode node) {
			RevenueEstimate estimate = new RevenueEstimate();

			estimate.setAvg(parseFormattedValue(node.get("avg")));
			estimate.setLow(parseFormattedValue(node.get("low")));
			estimate.setHigh(parseFormattedValue(node.get("high")));
			estimate.setNumberOfAnalysts(parseFormattedValue(node.get("numberOfAnalysts")));
			estimate.setYearAgoRevenue(parseFormattedValue(node.get("yearAgoRevenue")));
			estimate.setGrowth(parseFormattedValue(node.get("growth")));
			estimate.setRevenueCurrency(getStringValue(node, "revenueCurrency"));

			return estimate;
		}

		public static RevenueEstimate fromJson(JsonNode node) {
			return new RevenueEstimate().parse(node);
		}
	}

	@Getter
	@Setter
	public static class EpsTrend extends AbstractQuoteSummaryModule<EpsTrend> {
		private FormattedValue current;
		private FormattedValue sevenDaysAgo;
		private FormattedValue thirtyDaysAgo;
		private FormattedValue sixtyDaysAgo;
		private FormattedValue ninetyDaysAgo;
		private String epsTrendCurrency;

		@Override
		protected EpsTrend parseInternal(JsonNode node) {
			EpsTrend trend = new EpsTrend();

			trend.setCurrent(parseFormattedValue(node.get("current")));
			trend.setSevenDaysAgo(parseFormattedValue(node.get("sevenDaysAgo")));
			trend.setThirtyDaysAgo(parseFormattedValue(node.get("thirtyDaysAgo")));
			trend.setSixtyDaysAgo(parseFormattedValue(node.get("sixtyDaysAgo")));
			trend.setNinetyDaysAgo(parseFormattedValue(node.get("ninetyDaysAgo")));
			trend.setEpsTrendCurrency(getStringValue(node, "epsTrendCurrency"));

			return trend;
		}

		public static EpsTrend fromJson(JsonNode node) {
			return new EpsTrend().parse(node);
		}
	}

	@Getter
	@Setter
	public static class EpsRevisions extends AbstractQuoteSummaryModule<EpsRevisions> {
		private FormattedValue upLast7days;
		private FormattedValue upLast30days;
		private FormattedValue downLast30days;
		private FormattedValue downLast7Days;
		private FormattedValue downLast90days;
		private String epsRevisionsCurrency;

		@Override
		protected EpsRevisions parseInternal(JsonNode node) {
			EpsRevisions revisions = new EpsRevisions();

			revisions.setUpLast7days(parseFormattedValue(node.get("upLast7days")));
			revisions.setUpLast30days(parseFormattedValue(node.get("upLast30days")));
			revisions.setDownLast30days(parseFormattedValue(node.get("downLast30days")));
			revisions.setDownLast7Days(parseFormattedValue(node.get("downLast7Days")));
			revisions.setDownLast90days(parseFormattedValue(node.get("downLast90days")));
			revisions.setEpsRevisionsCurrency(getStringValue(node, "epsRevisionsCurrency"));

			return revisions;
		}

		public static EpsRevisions fromJson(JsonNode node) {
			return new EpsRevisions().parse(node);
		}
	}
}
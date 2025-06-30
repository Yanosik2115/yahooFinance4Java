package yahoofinance.model.modules;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Earnings extends AbstractQuoteSummaryModule<Earnings> {
	private Integer maxAge;
	private EarningsChart earningsChart;
	private FinancialsChart financialsChart;
	private String financialCurrency;

	@Override
	protected Earnings parseInternal(JsonNode node) {
		Earnings earnings = new Earnings();

		earnings.setMaxAge(getIntegerValue(node, "maxAge"));
		earnings.setFinancialCurrency(getStringValue(node, "financialCurrency"));

		if (node.has("earningsChart")) {
			earnings.setEarningsChart(EarningsChart.fromJson(node.get("earningsChart")));
		}

		if (node.has("financialsChart")) {
			earnings.setFinancialsChart(FinancialsChart.fromJson(node.get("financialsChart")));
		}

		return earnings;
	}

	public static Earnings fromJson(JsonNode node) {
		return new Earnings().parse(node);
	}

	@Getter
	@Setter
	public static class EarningsChart extends AbstractQuoteSummaryModule<EarningsChart> {
		private List<QuarterlyEarnings> quarterly;
		private FormattedValue currentQuarterEstimate;
		private String currentQuarterEstimateDate;
		private Integer currentQuarterEstimateYear;
		private List<FormattedValue> earningsDate;
		private Boolean isEarningsDateEstimate;

		@Override
		protected EarningsChart parseInternal(JsonNode node) {
			EarningsChart chart = new EarningsChart();

			chart.setCurrentQuarterEstimate(parseFormattedValue(node.get("currentQuarterEstimate")));
			chart.setCurrentQuarterEstimateDate(getStringValue(node, "currentQuarterEstimateDate"));
			chart.setCurrentQuarterEstimateYear(getIntegerValue(node, "currentQuarterEstimateYear"));
			chart.setIsEarningsDateEstimate(getBooleanValue(node, "isEarningsDateEstimate"));

			if (node.has("quarterly")) {
				chart.setQuarterly(parseModuleArray(node.get("quarterly"), QuarterlyEarnings.class));
			}

			chart.setEarningsDate(parseFormattedValueArray(node.get("earningsDate")));

			return chart;
		}


		@Override
		protected List<FormattedValue> parseFormattedValueArray(JsonNode arrayNode) {
			List<FormattedValue> values = new ArrayList<>();

			if (arrayNode != null && arrayNode.isArray()) {
				for (JsonNode valueNode : arrayNode) {
					FormattedValue value = parseFormattedValue(valueNode);
					if (value != null) {
						values.add(value);
					}
				}
			}

			return values;
		}
		public static EarningsChart fromJson(JsonNode node) {
			return new EarningsChart().parse(node);
		}
	}

	@Getter
	@Setter
	public static class QuarterlyEarnings extends AbstractQuoteSummaryModule<QuarterlyEarnings> {
		private String date;
		private FormattedValue actual;
		private FormattedValue estimate;

		@Override
		protected QuarterlyEarnings parseInternal(JsonNode node) {
			QuarterlyEarnings quarterly = new QuarterlyEarnings();

			quarterly.setDate(getStringValue(node, "date"));
			quarterly.setActual(parseFormattedValue(node.get("actual")));
			quarterly.setEstimate(parseFormattedValue(node.get("estimate")));

			return quarterly;
		}

		public static QuarterlyEarnings fromJson(JsonNode node) {
			return new QuarterlyEarnings().parse(node);
		}
	}

	@Getter
	@Setter
	public static class FinancialsChart extends AbstractQuoteSummaryModule<FinancialsChart> {
		private List<YearlyFinancials> yearly;
		private List<QuarterlyFinancials> quarterly;

		@Override
		protected FinancialsChart parseInternal(JsonNode node) {
			FinancialsChart chart = new FinancialsChart();

			if (node.has("yearly")) {
				chart.setYearly(parseModuleArray(node.get("yearly"), YearlyFinancials.class));
			}

			if (node.has("quarterly")) {
				chart.setQuarterly(parseModuleArray(node.get("quarterly"), QuarterlyFinancials.class));
			}

			return chart;
		}

		public static FinancialsChart fromJson(JsonNode node) {
			return new FinancialsChart().parse(node);
		}
	}

	@Getter
	@Setter
	public static class YearlyFinancials extends AbstractQuoteSummaryModule<YearlyFinancials> {
		private Integer date;
		private FormattedValue revenue;
		private FormattedValue earnings;

		@Override
		protected YearlyFinancials parseInternal(JsonNode node) {
			YearlyFinancials yearly = new YearlyFinancials();

			yearly.setDate(getIntegerValue(node, "date"));
			yearly.setRevenue(parseFormattedValue(node.get("revenue")));
			yearly.setEarnings(parseFormattedValue(node.get("earnings")));

			return yearly;
		}

		public static YearlyFinancials fromJson(JsonNode node) {
			return new YearlyFinancials().parse(node);
		}
	}

	@Getter
	@Setter
	public static class QuarterlyFinancials extends AbstractQuoteSummaryModule<QuarterlyFinancials> {
		private String date;
		private FormattedValue revenue;
		private FormattedValue earnings;

		@Override
		protected QuarterlyFinancials parseInternal(JsonNode node) {
			QuarterlyFinancials quarterly = new QuarterlyFinancials();

			quarterly.setDate(getStringValue(node, "date"));
			quarterly.setRevenue(parseFormattedValue(node.get("revenue")));
			quarterly.setEarnings(parseFormattedValue(node.get("earnings")));

			return quarterly;
		}

		public static QuarterlyFinancials fromJson(JsonNode node) {
			return new QuarterlyFinancials().parse(node);
		}
	}
}
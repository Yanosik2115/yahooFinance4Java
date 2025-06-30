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
public class CalendarEvents extends AbstractQuoteSummaryModule<CalendarEvents> {
	private Integer maxAge;
	private CalendarEarnings earnings;
	private FormattedValue exDividendDate;
	private FormattedValue dividendDate;

	@Override
	protected CalendarEvents parseInternal(JsonNode node) {
		CalendarEvents events = new CalendarEvents();

		events.setMaxAge(getIntegerValue(node, "maxAge"));
		events.setExDividendDate(parseFormattedValue(node.get("exDividendDate")));
		events.setDividendDate(parseFormattedValue(node.get("dividendDate")));

		// Parse earnings calendar
		if (node.has("earnings")) {
			events.setEarnings(CalendarEarnings.fromJson(node.get("earnings")));
		}

		return events;
	}

	public static CalendarEvents fromJson(JsonNode node) {
		return new CalendarEvents().parse(node);
	}

	@Getter
	@Setter
	public static class CalendarEarnings extends AbstractQuoteSummaryModule<CalendarEarnings> {
		private List<FormattedValue> earningsDate;
		private List<FormattedValue> earningsCallDate;
		private Boolean isEarningsDateEstimate;
		private FormattedValue earningsAverage;
		private FormattedValue earningsLow;
		private FormattedValue earningsHigh;
		private FormattedValue revenueAverage;
		private FormattedValue revenueLow;
		private FormattedValue revenueHigh;

		@Override
		protected CalendarEarnings parseInternal(JsonNode node) {
			CalendarEarnings earnings = new CalendarEarnings();

			earnings.setEarningsDate(parseFormattedValueArray(node.get("earningsDate")));
			earnings.setEarningsCallDate(parseFormattedValueArray(node.get("earningsCallDate")));

			earnings.setIsEarningsDateEstimate(getBooleanValue(node, "isEarningsDateEstimate"));

			earnings.setEarningsAverage(parseFormattedValue(node.get("earningsAverage")));
			earnings.setEarningsLow(parseFormattedValue(node.get("earningsLow")));
			earnings.setEarningsHigh(parseFormattedValue(node.get("earningsHigh")));

			earnings.setRevenueAverage(parseFormattedValue(node.get("revenueAverage")));
			earnings.setRevenueLow(parseFormattedValue(node.get("revenueLow")));
			earnings.setRevenueHigh(parseFormattedValue(node.get("revenueHigh")));

			return earnings;
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

		public static CalendarEarnings fromJson(JsonNode node) {
			return new CalendarEarnings().parse(node);
		}
	}
}
package yahoofinance.model.financials;

import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.FormattedValue;

import java.util.List;

@Getter
@Setter
public abstract class FinancialStatementSummary<T extends FinancialStatementSummary.TimeSeriesResult> {

	private String error;
	private List<Long> timestamp;
	private T result;

	@Getter
	@Setter
	public abstract static class TimeSeriesResult {
	}

	@Getter
	@Setter
	public static class FinancialDataPoint {
		private Long dataId;
		private String asOfDate;
		private String periodType;
		private String currencyCode;
		private FormattedValue reportedValue;
		private List<SegmentData> businessSegmentData;
		private List<SegmentData> geographicSegmentData;

		public String prettyPrint() {
			return prettyPrint(0);
		}

		public String prettyPrint(int indentLevel) {
			String indent = "  ".repeat(indentLevel);
			StringBuilder sb = new StringBuilder();

			sb.append(indent).append("FinancialDataPoint {\n");
			sb.append(indent).append("  dataId: ").append(dataId).append("\n");
			sb.append(indent).append("  asOfDate: ").append(asOfDate).append("\n");
			sb.append(indent).append("  periodType: ").append(periodType).append("\n");
			sb.append(indent).append("  currencyCode: ").append(currencyCode).append("\n");

			if (reportedValue != null) {
				sb.append(indent).append("  reportedValue: {\n");
				sb.append(indent).append("    raw: ").append(reportedValue.getRaw()).append("\n");
				sb.append(indent).append("    fmt: \"").append(reportedValue.getFmt()).append("\"\n");
				sb.append(indent).append("  }\n");
			} else {
				sb.append(indent).append("  reportedValue: null\n");
			}

			if (businessSegmentData != null && !businessSegmentData.isEmpty()) {
				sb.append(indent).append("  businessSegmentData: [\n");
				for (int i = 0; i < businessSegmentData.size(); i++) {
					SegmentData segment = businessSegmentData.get(i);
					sb.append(segment.prettyPrint(indentLevel + 2));
					if (i < businessSegmentData.size() - 1) {
						sb.append(",");
					}
					sb.append("\n");
				}
				sb.append(indent).append("  ]\n");
			}

			if (geographicSegmentData != null && !geographicSegmentData.isEmpty()) {
				sb.append(indent).append("  geographicSegmentData: [\n");
				for (int i = 0; i < geographicSegmentData.size(); i++) {
					SegmentData segment = geographicSegmentData.get(i);
					sb.append(segment.prettyPrint(indentLevel + 2));
					if (i < geographicSegmentData.size() - 1) {
						sb.append(",");
					}
					sb.append("\n");
				}
				sb.append(indent).append("  ]\n");
			}

			sb.append(indent).append("}");
			return sb.toString();
		}

		public String prettyPrintCompact() {
			StringBuilder sb = new StringBuilder();
			sb.append("[").append(asOfDate).append("] ");
			sb.append(currencyCode).append(" ");

			if (reportedValue != null) {
				sb.append(reportedValue.getFmt());
				if (reportedValue.getRaw() != null) {
					sb.append(" (").append(reportedValue.getRaw()).append(")");
				}
			} else {
				sb.append("N/A");
			}

			if (geographicSegmentData != null && !geographicSegmentData.isEmpty()) {
				sb.append(" | Geo Segments: ").append(geographicSegmentData.size());
			}

			if (businessSegmentData != null && !businessSegmentData.isEmpty()) {
				sb.append(" | Biz Segments: ").append(businessSegmentData.size());
			}

			return sb.toString();
		}

		@Override
		public String toString() {
			return prettyPrintCompact();
		}
	}

	@Getter
	@Setter
	public static class SegmentData {
		private Double dataValue;
		private String segmentType;
		private String segmentName;
		private Integer isPrimarySegment;

		public String prettyPrint() {
			return prettyPrint(0);
		}

		public String prettyPrint(int indentLevel) {
			String indent = "  ".repeat(indentLevel);

			return indent + "SegmentData {\n" +
			       indent + "  segmentName: \"" + segmentName + "\"\n" +
			       indent + "  segmentType: \"" + segmentType + "\"\n" +
			       indent + "  dataValue: " + formatDataValue() + "\n" +
			       indent + "  isPrimarySegment: " + isPrimarySegment + "\n" +
			       indent + "}";
		}

		public String prettyPrintCompact() {
			StringBuilder sb = new StringBuilder();
			sb.append(segmentName != null ? segmentName : "Unknown");
			sb.append(" (").append(segmentType != null ? segmentType : "N/A").append(")");
			sb.append(": ").append(formatDataValue());

			if (isPrimarySegment != null && isPrimarySegment == 1) {
				sb.append(" [PRIMARY]");
			}

			return sb.toString();
		}

		private String formatDataValue() {
			if (dataValue == null) {
				return "null";
			}

			double value = Math.abs(dataValue);
			String sign = dataValue < 0 ? "-" : "";

			if (value >= 1_000_000_000_000L) {
				return String.format("%s%.2fT", sign, dataValue / 1_000_000_000_000.0);
			} else if (value >= 1_000_000_000L) {
				return String.format("%s%.2fB", sign, dataValue / 1_000_000_000.0);
			} else if (value >= 1_000_000L) {
				return String.format("%s%.2fM", sign, dataValue / 1_000_000.0);
			} else if (value >= 1_000L) {
				return String.format("%s%.2fK", sign, dataValue / 1_000.0);
			} else {
				return String.format("%s%.2f", sign, dataValue);
			}
		}

		@Override
		public String toString() {
			return prettyPrintCompact();
		}
	}

}
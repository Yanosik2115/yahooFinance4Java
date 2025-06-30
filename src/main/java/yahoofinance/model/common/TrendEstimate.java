package yahoofinance.model.common;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TrendEstimate extends AbstractQuoteSummaryModule<TrendEstimate> {
	private String period;
	private FormattedValue growth;

	@Override
	protected TrendEstimate parseInternal(JsonNode node) {
		TrendEstimate estimate = new TrendEstimate();

		estimate.setPeriod(getStringValue(node, "period"));
		estimate.setGrowth(parseFormattedValue(node.get("growth")));

		return estimate;
	}

	public static TrendEstimate fromJson(JsonNode node) {
		return new TrendEstimate().parse(node);
	}
}
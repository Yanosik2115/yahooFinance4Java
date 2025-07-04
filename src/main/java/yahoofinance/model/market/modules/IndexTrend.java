package yahoofinance.model.market.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;
import yahoofinance.model.common.TrendEstimate;

import java.util.List;

import static yahoofinance.util.Utils.*;

@Getter
@Setter
public class IndexTrend extends AbstractQuoteSummaryModule<IndexTrend> {
	private Integer maxAge;
	private String symbol;
	private FormattedValue peRatio;
	private FormattedValue pegRatio;
	private List<TrendEstimate> estimates;

	@Override
	protected IndexTrend parseInternal(JsonNode node) {
		IndexTrend indexTrend = new IndexTrend();

		indexTrend.setMaxAge(getIntegerValue(node, "maxAge"));
		indexTrend.setSymbol(getStringValue(node, "symbol"));
		indexTrend.setPeRatio(parseFormattedValue(node.get("peRatio")));
		indexTrend.setPegRatio(parseFormattedValue(node.get("pegRatio")));

		if (node.has("estimates")) {
			indexTrend.setEstimates(parseModuleArray(node.get("estimates"), TrendEstimate.class));
		}

		return indexTrend;
	}

	public static IndexTrend fromJson(JsonNode node) {
		return new IndexTrend().parse(node);
	}
}

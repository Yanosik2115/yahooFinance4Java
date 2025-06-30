package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.IncomeStatement;

import java.util.List;

@Getter
@Setter
public class IncomeStatementHistoryQuarterly extends AbstractQuoteSummaryModule<IncomeStatementHistoryQuarterly> {
	private List<IncomeStatement> incomeStatementHistory;
	private Integer maxAge;

	@Override
	protected IncomeStatementHistoryQuarterly parseInternal(JsonNode node) {
		IncomeStatementHistoryQuarterly history = new IncomeStatementHistoryQuarterly();

		history.setMaxAge(getIntegerValue(node, "maxAge"));

		// Parse quarterly income statements array
		if (node.has("incomeStatementHistory")) {
			history.setIncomeStatementHistory(parseModuleArray(node.get("incomeStatementHistory"), IncomeStatement.class));
		}

		return history;
	}

	/**
	 * Static factory method for cleaner usage
	 */
	public static IncomeStatementHistoryQuarterly fromJson(JsonNode node) {
		return new IncomeStatementHistoryQuarterly().parse(node);
	}
}
package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.IncomeStatement;

import java.util.List;

@Getter
@Setter
public class IncomeStatementHistory extends AbstractQuoteSummaryModule<IncomeStatementHistory> {
	private List<IncomeStatement> incomeStatementHistory;
	private Integer maxAge;

	@Override
	protected IncomeStatementHistory parseInternal(JsonNode node) {
		IncomeStatementHistory history = new IncomeStatementHistory();

		history.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("incomeStatementHistory")) {
			history.setIncomeStatementHistory(parseModuleArray(node.get("incomeStatementHistory"), IncomeStatement.class));
		}

		return history;
	}

	public static IncomeStatementHistory fromJson(JsonNode node) {
		return new IncomeStatementHistory().parse(node);
	}
}
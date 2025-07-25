package yahoofinance.model.market.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.IncomeStatement;

import java.util.List;
import static yahoofinance.util.Utils.*;

@Getter
@Setter
public class IncomeStatementHistory extends AbstractQuoteSummaryModule<IncomeStatementHistory> {
	private List<IncomeStatement> incomeStatementList;
	private Integer maxAge;

	@Override
	protected IncomeStatementHistory parseInternal(JsonNode node) {
		IncomeStatementHistory history = new IncomeStatementHistory();

		history.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("incomeStatementHistory")) {
			history.setIncomeStatementList(parseModuleArray(node.get("incomeStatementHistory"), IncomeStatement.class));
		}

		return history;
	}

	public static IncomeStatementHistory fromJson(JsonNode node) {
		return new IncomeStatementHistory().parse(node);
	}
}
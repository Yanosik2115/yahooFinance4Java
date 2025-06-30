package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.CashflowStatement;

import java.util.List;

@Getter
@Setter
public class CashflowStatementHistoryQuarterly extends AbstractQuoteSummaryModule<CashflowStatementHistoryQuarterly> {
	private List<CashflowStatement> cashflowStatements;
	private Integer maxAge;

	@Override
	protected CashflowStatementHistoryQuarterly parseInternal(JsonNode node) {
		CashflowStatementHistoryQuarterly history = new CashflowStatementHistoryQuarterly();

		history.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("cashflowStatements")) {
			history.setCashflowStatements(parseModuleArray(node.get("cashflowStatements"), CashflowStatement.class));
		}

		return history;
	}

	public static CashflowStatementHistoryQuarterly fromJson(JsonNode node) {
		return new CashflowStatementHistoryQuarterly().parse(node);
	}
}
package yahoofinance.model.modules;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.CashflowStatement;

import java.util.List;

@Getter
@Setter
public class CashflowStatementHistory extends AbstractQuoteSummaryModule<CashflowStatementHistory> {
	private List<CashflowStatement> cashflowStatements;
	private Integer maxAge;

	@Override
	protected CashflowStatementHistory parseInternal(JsonNode node) {
		CashflowStatementHistory history = new CashflowStatementHistory();

		history.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("cashflowStatements")) {
			history.setCashflowStatements(parseModuleArray(node.get("cashflowStatements"), CashflowStatement.class));
		}

		return history;
	}

	public static CashflowStatementHistory fromJson(JsonNode node) {
		return new CashflowStatementHistory().parse(node);
	}
}
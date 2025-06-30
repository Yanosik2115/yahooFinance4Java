package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.BalanceSheetStatement;

import java.util.List;

@Getter
@Setter
public class BalanceSheetHistory extends AbstractQuoteSummaryModule<BalanceSheetHistory> {
	private List<BalanceSheetStatement> balanceSheetStatements;
	private Integer maxAge;

	@Override
	protected BalanceSheetHistory parseInternal(JsonNode node) {
		BalanceSheetHistory history = new BalanceSheetHistory();

		history.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("balanceSheetStatements")) {
			history.setBalanceSheetStatements(parseModuleArray(node.get("balanceSheetStatements"), BalanceSheetStatement.class));
		}

		return history;
	}

	public static BalanceSheetHistory fromJson(JsonNode node) {
		return new BalanceSheetHistory().parse(node);
	}
}

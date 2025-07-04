package yahoofinance.model.market.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.BalanceSheetStatement;

import java.util.List;
import static yahoofinance.util.Utils.*;

@Getter
@Setter
public class BalanceSheetHistoryQuarterly extends AbstractQuoteSummaryModule<BalanceSheetHistoryQuarterly> {
	private List<BalanceSheetStatement> balanceSheetStatements;
	private Integer maxAge;

	@Override
	protected BalanceSheetHistoryQuarterly parseInternal(JsonNode node) {
		BalanceSheetHistoryQuarterly history = new BalanceSheetHistoryQuarterly();

		history.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("balanceSheetStatements")) {
			history.setBalanceSheetStatements(parseModuleArray(node.get("balanceSheetStatements"), BalanceSheetStatement.class));
		}

		return history;
	}

	public static BalanceSheetHistoryQuarterly fromJson(JsonNode node) {
		return new BalanceSheetHistoryQuarterly().parse(node);
	}
}

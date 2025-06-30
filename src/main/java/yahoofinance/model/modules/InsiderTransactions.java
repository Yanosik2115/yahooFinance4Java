package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;

import java.util.List;

@Getter
@Setter
public class InsiderTransactions extends AbstractQuoteSummaryModule<InsiderTransactions> {
	private List<InsiderTransaction> transactions;
	private Integer maxAge;

	@Override
	protected InsiderTransactions parseInternal(JsonNode node) {
		InsiderTransactions insiderTransactions = new InsiderTransactions();

		insiderTransactions.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("transactions")) {
			insiderTransactions.setTransactions(parseModuleArray(node.get("transactions"), InsiderTransaction.class));
		}

		return insiderTransactions;
	}

	public static InsiderTransactions fromJson(JsonNode node) {
		return new InsiderTransactions().parse(node);
	}

	@Getter
	@Setter
	public static class InsiderTransaction extends AbstractQuoteSummaryModule<InsiderTransaction> {
		private Integer maxAge;

		private FormattedValue shares;
		private FormattedValue value;
		private String moneyText;
		private FormattedValue startDate;
		private String ownership;

		private String filerName;
		private String filerRelation;
		private String filerUrl;
		private String transactionText;

		@Override
		protected InsiderTransaction parseInternal(JsonNode node) {
			InsiderTransaction transaction = new InsiderTransaction();

			transaction.setMaxAge(getIntegerValue(node, "maxAge"));

			transaction.setShares(parseFormattedValue(node.get("shares")));
			transaction.setValue(parseFormattedValue(node.get("value")));
			transaction.setMoneyText(getStringValue(node, "moneyText"));
			transaction.setStartDate(parseFormattedValue(node.get("startDate")));
			transaction.setOwnership(getStringValue(node, "ownership"));

			transaction.setFilerName(getStringValue(node, "filerName"));
			transaction.setFilerRelation(getStringValue(node, "filerRelation"));
			transaction.setFilerUrl(getStringValue(node, "filerUrl"));
			transaction.setTransactionText(getStringValue(node, "transactionText"));

			return transaction;
		}

		public static InsiderTransaction fromJson(JsonNode node) {
			return new InsiderTransaction().parse(node);
		}
	}
}
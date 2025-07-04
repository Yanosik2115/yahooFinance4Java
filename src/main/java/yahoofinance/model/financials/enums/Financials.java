package yahoofinance.model.financials.enums;

import lombok.Getter;

public enum Financials {
	CASH_FLOW(CashFlowKey.class),
	BALANCE_SHEET(BalanceSheetKey.class),
	INCOME(FinancialsKey.class);

	@Getter
	private Class<?> financialClass;

	Financials(Class<?> financialClass) {
		this.financialClass = financialClass;
	}
}

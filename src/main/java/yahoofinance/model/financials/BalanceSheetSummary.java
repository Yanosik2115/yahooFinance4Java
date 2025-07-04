package yahoofinance.model.financials;

import lombok.*;
import java.util.List;

@Getter
@Setter
public class BalanceSheetSummary extends FinancialStatementSummary<BalanceSheetSummary.BalanceSheetTimeSeriesResult> {

	@Getter
	@Setter
	public static class BalanceSheetTimeSeriesResult extends TimeSeriesResult {
		// Assets
		private List<FinancialDataPoint> TotalAssets;
		private List<FinancialDataPoint> CurrentAssets;
		private List<FinancialDataPoint> TotalNonCurrentAssets;

		// Cash and Cash Equivalents
		private List<FinancialDataPoint> CashAndCashEquivalents;
		private List<FinancialDataPoint> CashFinancial;
		private List<FinancialDataPoint> CashEquivalents;
		private List<FinancialDataPoint> CashCashEquivalentsAndShortTermInvestments;

		// Receivables
		private List<FinancialDataPoint> AccountsReceivable;
		private List<FinancialDataPoint> Receivables;
		private List<FinancialDataPoint> OtherReceivables;

		// Investments
		private List<FinancialDataPoint> AvailableForSaleSecurities;
		private List<FinancialDataPoint> InvestmentsAndAdvances;
		private List<FinancialDataPoint> InvestmentinFinancialAssets;
		private List<FinancialDataPoint> OtherShortTermInvestments;
		private List<FinancialDataPoint> OtherInvestments;

		// Property, Plant & Equipment
		private List<FinancialDataPoint> NetPPE;
		private List<FinancialDataPoint> GrossPPE;
		private List<FinancialDataPoint> AccumulatedDepreciation;
		private List<FinancialDataPoint> MachineryFurnitureEquipment;
		private List<FinancialDataPoint> LandAndImprovements;
		private List<FinancialDataPoint> Properties;
		private List<FinancialDataPoint> OtherProperties;
		private List<FinancialDataPoint> Leases;

		// Other Assets
		private List<FinancialDataPoint> Inventory;
		private List<FinancialDataPoint> OtherCurrentAssets;
		private List<FinancialDataPoint> OtherNonCurrentAssets;
		private List<FinancialDataPoint> NonCurrentDeferredTaxesAssets;
		private List<FinancialDataPoint> NonCurrentDeferredAssets;

		// Liabilities
		private List<FinancialDataPoint> TotalLiabilitiesNetMinorityInterest;
		private List<FinancialDataPoint> CurrentLiabilities;
		private List<FinancialDataPoint> TotalNonCurrentLiabilitiesNetMinorityInterest;

		// Current Liabilities
		private List<FinancialDataPoint> AccountsPayable;
		private List<FinancialDataPoint> Payables;
		private List<FinancialDataPoint> PayablesAndAccruedExpenses;
		private List<FinancialDataPoint> OtherCurrentLiabilities;
		private List<FinancialDataPoint> CurrentDeferredRevenue;
		private List<FinancialDataPoint> CurrentDeferredLiabilities;

		// Debt
		private List<FinancialDataPoint> TotalDebt;
		private List<FinancialDataPoint> CurrentDebt;
		private List<FinancialDataPoint> CurrentDebtAndCapitalLeaseObligation;
		private List<FinancialDataPoint> LongTermDebt;
		private List<FinancialDataPoint> LongTermDebtAndCapitalLeaseObligation;
		private List<FinancialDataPoint> NetDebt;
		private List<FinancialDataPoint> CommercialPaper;
		private List<FinancialDataPoint> OtherCurrentBorrowings;

		// Capital Lease Obligations
		private List<FinancialDataPoint> CapitalLeaseObligations;
		private List<FinancialDataPoint> CurrentCapitalLeaseObligation;
		private List<FinancialDataPoint> LongTermCapitalLeaseObligation;

		// Tax Related
		private List<FinancialDataPoint> IncomeTaxPayable;
		private List<FinancialDataPoint> TotalTaxPayable;

		// Other Liabilities
		private List<FinancialDataPoint> OtherNonCurrentLiabilities;
		private List<FinancialDataPoint> TradeandOtherPayablesNonCurrent;

		// Equity
		private List<FinancialDataPoint> StockholdersEquity;
		private List<FinancialDataPoint> TotalEquityGrossMinorityInterest;
		private List<FinancialDataPoint> CommonStockEquity;
		private List<FinancialDataPoint> TangibleBookValue;
		private List<FinancialDataPoint> NetTangibleAssets;

		// Stock Related
		private List<FinancialDataPoint> CapitalStock;
		private List<FinancialDataPoint> CommonStock;
		private List<FinancialDataPoint> RetainedEarnings;
		private List<FinancialDataPoint> OtherEquityAdjustments;
		private List<FinancialDataPoint> GainsLossesNotAffectingRetainedEarnings;

		// Share Information
		private List<FinancialDataPoint> ShareIssued;
		private List<FinancialDataPoint> OrdinarySharesNumber;
		private List<FinancialDataPoint> TreasurySharesNumber;

		// Financial Metrics
		private List<FinancialDataPoint> WorkingCapital;
		private List<FinancialDataPoint> TotalCapitalization;
		private List<FinancialDataPoint> InvestedCapital;

		// Items with no data (empty meta only)
		private List<FinancialDataPoint> NonCurrentDeferredLiabilities;
		private List<FinancialDataPoint> AllowanceForDoubtfulAccountsReceivable;
		private List<FinancialDataPoint> GoodwillAndOtherIntangibleAssets;
		private List<FinancialDataPoint> NonCurrentDeferredTaxesLiabilities;
		private List<FinancialDataPoint> CurrentAccruedExpenses;
		private List<FinancialDataPoint> OtherIntangibleAssets;
		private List<FinancialDataPoint> Goodwill;
	}
}
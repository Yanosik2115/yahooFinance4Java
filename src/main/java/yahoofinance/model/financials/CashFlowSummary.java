package yahoofinance.model.financials;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class CashFlowSummary extends FinancialStatementSummary<CashFlowSummary.CashFlowTimeSeriesResult> {

	@Getter
	@Setter
	public static class CashFlowTimeSeriesResult extends TimeSeriesResult {

		private List<FinancialDataPoint> OperatingCashFlow;
		private List<FinancialDataPoint> CashFlowFromContinuingOperatingActivities;
		private List<FinancialDataPoint> NetIncomeFromContinuingOperations;

		private List<FinancialDataPoint> DepreciationAndAmortization;
		private List<FinancialDataPoint> DepreciationAmortizationDepletion;
		private List<FinancialDataPoint> StockBasedCompensation;
		private List<FinancialDataPoint> DeferredTax;
		private List<FinancialDataPoint> DeferredIncomeTax;
		private List<FinancialDataPoint> OtherNonCashItems;

		private List<FinancialDataPoint> ChangeInWorkingCapital;
		private List<FinancialDataPoint> ChangeInInventory;
		private List<FinancialDataPoint> ChangesInAccountReceivables;
		private List<FinancialDataPoint> ChangeInReceivables;
		private List<FinancialDataPoint> ChangeInAccountPayable;
		private List<FinancialDataPoint> ChangeInPayable;
		private List<FinancialDataPoint> ChangeInPayablesAndAccruedExpense;
		private List<FinancialDataPoint> ChangeInOtherCurrentAssets;
		private List<FinancialDataPoint> ChangeInOtherCurrentLiabilities;
		private List<FinancialDataPoint> ChangeInOtherWorkingCapital;

		private List<FinancialDataPoint> InvestingCashFlow;
		private List<FinancialDataPoint> CashFlowFromContinuingInvestingActivities;
		private List<FinancialDataPoint> CapitalExpenditure;
		private List<FinancialDataPoint> PurchaseOfPPE;
		private List<FinancialDataPoint> NetPPEPurchaseAndSale;

		private List<FinancialDataPoint> PurchaseOfInvestment;
		private List<FinancialDataPoint> SaleOfInvestment;
		private List<FinancialDataPoint> NetInvestmentPurchaseAndSale;

		private List<FinancialDataPoint> PurchaseOfBusiness;
		private List<FinancialDataPoint> NetBusinessPurchaseAndSale;
		private List<FinancialDataPoint> NetOtherInvestingChanges;

		private List<FinancialDataPoint> FinancingCashFlow;
		private List<FinancialDataPoint> CashFlowFromContinuingFinancingActivities;

		private List<FinancialDataPoint> IssuanceOfDebt;
		private List<FinancialDataPoint> RepaymentOfDebt;
		private List<FinancialDataPoint> NetIssuancePaymentsOfDebt;
		private List<FinancialDataPoint> LongTermDebtIssuance;
		private List<FinancialDataPoint> LongTermDebtPayments;
		private List<FinancialDataPoint> NetLongTermDebtIssuance;
		private List<FinancialDataPoint> NetShortTermDebtIssuance;

		private List<FinancialDataPoint> RepurchaseOfCapitalStock;
		private List<FinancialDataPoint> CommonStockPayments;
		private List<FinancialDataPoint> NetCommonStockIssuance;
		private List<FinancialDataPoint> IssuanceOfCapitalStock;
		private List<FinancialDataPoint> CommonStockIssuance;

		private List<FinancialDataPoint> CashDividendsPaid;
		private List<FinancialDataPoint> CommonStockDividendPaid;

		private List<FinancialDataPoint> NetOtherFinancingCharges;

		private List<FinancialDataPoint> BeginningCashPosition;
		private List<FinancialDataPoint> EndCashPosition;
		private List<FinancialDataPoint> ChangesInCash;

		private List<FinancialDataPoint> FreeCashFlow;

		private List<FinancialDataPoint> InterestPaidSupplementalData;
		private List<FinancialDataPoint> IncomeTaxPaidSupplementalData;

		private List<FinancialDataPoint> NetIntangiblesPurchaseAndSale;
		private List<FinancialDataPoint> PurchaseOfIntangibles;
		private List<FinancialDataPoint> ShortTermDebtPayments;
		private List<FinancialDataPoint> ChangeInPrepaidAssets;
		private List<FinancialDataPoint> NetInvestmentPropertiesPurchaseAndSale;
		private List<FinancialDataPoint> ChangeInIncomeTaxPayable;
		private List<FinancialDataPoint> OperatingGainsLosses;
		private List<FinancialDataPoint> CapitalExpenditureReported;
		private List<FinancialDataPoint> DividendReceivedCFO;
		private List<FinancialDataPoint> InterestPaidDirect;
		private List<FinancialDataPoint> CashFlowFromDiscontinuedOperation;
		private List<FinancialDataPoint> EffectOfExchangeRateChanges;
		private List<FinancialDataPoint> DividendsReceivedDirect;
		private List<FinancialDataPoint> SaleOfPPE;
		private List<FinancialDataPoint> CashFlowsfromusedinOperatingActivitiesDirect;
		private List<FinancialDataPoint> ChangeInTaxPayable;
		private List<FinancialDataPoint> EarningsLossesFromEquityInvestments;
		private List<FinancialDataPoint> ClassesofCashReceiptsfromOperatingActivities;
		private List<FinancialDataPoint> ProvisionandWriteOffofAssets;
		private List<FinancialDataPoint> PreferredStockIssuance;
		private List<FinancialDataPoint> ClassesofCashPayments;
		private List<FinancialDataPoint> ShortTermDebtIssuance;
		private List<FinancialDataPoint> InterestReceivedCFI;
		private List<FinancialDataPoint> TaxesRefundPaidDirect;
		private List<FinancialDataPoint> ReceiptsfromCustomers;
		private List<FinancialDataPoint> CashFromDiscontinuedInvestingActivities;
		private List<FinancialDataPoint> UnrealizedGainLossOnInvestmentSecurities;
		private List<FinancialDataPoint> PreferredStockDividendPaid;
		private List<FinancialDataPoint> SaleOfInvestmentProperties;
		private List<FinancialDataPoint> ReceiptsfromGovernmentGrants;
		private List<FinancialDataPoint> AmortizationCashFlow;
		private List<FinancialDataPoint> OtherCashAdjustmentInsideChangeinCash;
		private List<FinancialDataPoint> ChangeInDividendPayable;
		private List<FinancialDataPoint> OtherCashAdjustmentOutsideChangeinCash;
		private List<FinancialDataPoint> AmortizationOfSecurities;
		private List<FinancialDataPoint> DividendsReceivedCFI;
		private List<FinancialDataPoint> SaleOfIntangibles;
		private List<FinancialDataPoint> PaymentstoSuppliersforGoodsandServices;
		private List<FinancialDataPoint> Depletion;
		private List<FinancialDataPoint> CashFromDiscontinuedFinancingActivities;
		private List<FinancialDataPoint> PurchaseOfInvestmentProperties;
		private List<FinancialDataPoint> Depreciation;
		private List<FinancialDataPoint> GainLossOnInvestmentSecurities;
		private List<FinancialDataPoint> AssetImpairmentCharge;
		private List<FinancialDataPoint> ChangeInAccruedExpense;
		private List<FinancialDataPoint> InterestReceivedDirect;
		private List<FinancialDataPoint> DividendPaidCFO;
		private List<FinancialDataPoint> GainLossOnSaleOfPPE;
		private List<FinancialDataPoint> DomesticSales;
		private List<FinancialDataPoint> AdjustedGeographySegmentData;
		private List<FinancialDataPoint> NetPreferredStockIssuance;
		private List<FinancialDataPoint> SaleOfBusiness;
		private List<FinancialDataPoint> PreferredStockPayments;
		private List<FinancialDataPoint> GainLossOnSaleOfBusiness;
		private List<FinancialDataPoint> InterestReceivedCFO;
		private List<FinancialDataPoint> PaymentsonBehalfofEmployees;
		private List<FinancialDataPoint> ForeignSales;
		private List<FinancialDataPoint> TaxesRefundPaid;
		private List<FinancialDataPoint> ChangeInInterestPayable;
		private List<FinancialDataPoint> CashFromDiscontinuedOperatingActivities;
		private List<FinancialDataPoint> OtherCashPaymentsfromOperatingActivities;
		private List<FinancialDataPoint> NetForeignCurrencyExchangeGainLoss;
		private List<FinancialDataPoint> PensionAndEmployeeBenefitExpense;
		private List<FinancialDataPoint> ProceedsFromStockOptionExercised;
		private List<FinancialDataPoint> DividendsPaidDirect;
		private List<FinancialDataPoint> InterestPaidCFF;
		private List<FinancialDataPoint> InterestPaidCFO;
		private List<FinancialDataPoint> ExcessTaxBenefitFromStockBasedCompensation;
		private List<FinancialDataPoint> AmortizationOfIntangibles;
		private List<FinancialDataPoint> OtherCashReceiptsfromOperatingActivities;
	}
}
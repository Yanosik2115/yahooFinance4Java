package yahoofinance.model.financials;

import lombok.*;
import java.util.List;

@Getter
@Setter
public class IncomeSummary extends FinancialStatementSummary<IncomeSummary.IncomeTimeSeriesResult> {

	@Getter
	@Setter
	public static class IncomeTimeSeriesResult extends TimeSeriesResult {
		private List<FinancialDataPoint> NetIncomeContinuousOperations;
		private List<FinancialDataPoint> TaxEffectOfUnusualItems;
		private List<FinancialDataPoint> NetIncomeFromContinuingOperationNetMinorityInterest;
		private List<FinancialDataPoint> TotalOperatingIncomeAsReported;
		private List<FinancialDataPoint> BasicAverageShares;
		private List<FinancialDataPoint> ReconciledDepreciation;
		private List<FinancialDataPoint> OperatingExpense;
		private List<FinancialDataPoint> TotalRevenue;
		private List<FinancialDataPoint> InterestExpense;
		private List<FinancialDataPoint> DilutedNIAvailtoComStockholders;
		private List<FinancialDataPoint> NetIncomeIncludingNoncontrollingInterests;
		private List<FinancialDataPoint> NormalizedEBITDA;
		private List<FinancialDataPoint> BasicEPS;
		private List<FinancialDataPoint> GrossProfit;
		private List<FinancialDataPoint> OtherNonOperatingIncomeExpenses;
		private List<FinancialDataPoint> OperatingRevenue;
		private List<FinancialDataPoint> EBITDA;
		private List<FinancialDataPoint> InterestIncome;
		private List<FinancialDataPoint> NetNonOperatingInterestIncomeExpense;
		private List<FinancialDataPoint> ReconciledCostOfRevenue;
		private List<FinancialDataPoint> TaxRateForCalcs;
		private List<FinancialDataPoint> NetIncomeCommonStockholders;
		private List<FinancialDataPoint> DilutedAverageShares;
		private List<FinancialDataPoint> NetIncome;
		private List<FinancialDataPoint> EBIT;
		private List<FinancialDataPoint> NormalizedIncome;
		private List<FinancialDataPoint> CostOfRevenue;
		private List<FinancialDataPoint> PretaxIncome;
		private List<FinancialDataPoint> TaxProvision;
		private List<FinancialDataPoint> InterestIncomeNonOperating;
		private List<FinancialDataPoint> SellingGeneralAndAdministration;
		private List<FinancialDataPoint> OtherIncomeExpense;
		private List<FinancialDataPoint> OperatingIncome;
		private List<FinancialDataPoint> NetIncomeFromContinuingAndDiscontinuedOperation;
		private List<FinancialDataPoint> NetInterestIncome;
		private List<FinancialDataPoint> TotalExpenses;
		private List<FinancialDataPoint> InterestExpenseNonOperating;
		private List<FinancialDataPoint> DilutedEPS;
		private List<FinancialDataPoint> NormalizedDilutedEPS;
		private List<FinancialDataPoint> NormalizedBasicEPS;
		private List<FinancialDataPoint> TotalUnusualItems;
		private List<FinancialDataPoint> TotalUnusualItemsExcludingGoodwill;
		private List<FinancialDataPoint> ContinuingAndDiscontinuedDilutedEPS;
		private List<FinancialDataPoint> ContinuingAndDiscontinuedBasicEPS;
		private List<FinancialDataPoint> RentExpenseSupplemental;
		private List<FinancialDataPoint> ReportedNormalizedDilutedEPS;
		private List<FinancialDataPoint> ReportedNormalizedBasicEPS;
		private List<FinancialDataPoint> DividendPerShare;
		private List<FinancialDataPoint> DilutedEPSOtherGainsLosses;
		private List<FinancialDataPoint> TaxLossCarryforwardDilutedEPS;
		private List<FinancialDataPoint> DilutedAccountingChange;
		private List<FinancialDataPoint> DilutedExtraordinary;
		private List<FinancialDataPoint> DilutedDiscontinuousOperations;
		private List<FinancialDataPoint> DilutedContinuousOperations;
		private List<FinancialDataPoint> BasicEPSOtherGainsLosses;
		private List<FinancialDataPoint> TaxLossCarryforwardBasicEPS;
		private List<FinancialDataPoint> BasicAccountingChange;
		private List<FinancialDataPoint> BasicExtraordinary;
		private List<FinancialDataPoint> BasicDiscontinuousOperations;
		private List<FinancialDataPoint> BasicContinuousOperations;
		private List<FinancialDataPoint> AverageDilutionEarnings;
		private List<FinancialDataPoint> OtherunderPreferredStockDividend;
		private List<FinancialDataPoint> PreferredStockDividends;
		private List<FinancialDataPoint> MinorityInterests;
		private List<FinancialDataPoint> NetIncomeFromTaxLossCarryforward;
		private List<FinancialDataPoint> NetIncomeExtraordinary;
		private List<FinancialDataPoint> NetIncomeDiscontinuousOperations;
		private List<FinancialDataPoint> EarningsFromEquityInterestNetOfTax;
		private List<FinancialDataPoint> SpecialIncomeCharges;
		private List<FinancialDataPoint> GainOnSaleOfPPE;
		private List<FinancialDataPoint> GainOnSaleOfBusiness;
		private List<FinancialDataPoint> OtherSpecialCharges;
		private List<FinancialDataPoint> WriteOff;
		private List<FinancialDataPoint> ImpairmentOfCapitalAssets;
		private List<FinancialDataPoint> RestructuringAndMergernAcquisition;
		private List<FinancialDataPoint> SecuritiesAmortization;
		private List<FinancialDataPoint> EarningsFromEquityInterest;
		private List<FinancialDataPoint> GainOnSaleOfSecurity;
		private List<FinancialDataPoint> TotalOtherFinanceCost;
		private List<FinancialDataPoint> OtherOperatingExpenses;
		private List<FinancialDataPoint> OtherTaxes;
		private List<FinancialDataPoint> ProvisionForDoubtfulAccounts;
		private List<FinancialDataPoint> DepreciationAmortizationDepletionIncomeStatement;
		private List<FinancialDataPoint> DepletionIncomeStatement;
		private List<FinancialDataPoint> DepreciationAndAmortizationInIncomeStatement;
		private List<FinancialDataPoint> Amortization;
		private List<FinancialDataPoint> AmortizationOfIntangiblesIncomeStatement;
		private List<FinancialDataPoint> DepreciationIncomeStatement;
		private List<FinancialDataPoint> ResearchAndDevelopment;
		private List<FinancialDataPoint> SellingAndMarketingExpense;
		private List<FinancialDataPoint> GeneralAndAdministrativeExpense;
		private List<FinancialDataPoint> OtherGandA;
		private List<FinancialDataPoint> InsuranceAndClaims;
		private List<FinancialDataPoint> RentAndLandingFees;
		private List<FinancialDataPoint> SalariesAndWages;
		private List<FinancialDataPoint> ExciseTaxes;
		private List<FinancialDataPoint> LossAdjustmentExpense;
		private List<FinancialDataPoint> NetPolicyholderBenefitsAndClaims;
		private List<FinancialDataPoint> PolicyholderBenefitsGross;
		private List<FinancialDataPoint> PolicyholderBenefitsCeded;
		private List<FinancialDataPoint> OccupancyAndEquipment;
		private List<FinancialDataPoint> ProfessionalExpenseAndContractServicesExpense;
		private List<FinancialDataPoint> OtherNonInterestExpense;
	}
}
package yahoofinance.model.common;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;
import static yahoofinance.util.Utils.*;

@Getter
@Setter
public class IncomeStatement extends AbstractQuoteSummaryModule<IncomeStatement> {
	private Integer maxAge;
	private FormattedValue endDate;

	// Revenue and Cost of Goods Sold
	private FormattedValue totalRevenue;
	private FormattedValue costOfRevenue;
	private FormattedValue grossProfit;

	// Operating Expenses
	private FormattedValue researchDevelopment;
	private FormattedValue sellingGeneralAdministrative;
	private FormattedValue nonRecurring;
	private FormattedValue otherOperatingExpenses;
	private FormattedValue totalOperatingExpenses;

	// Operating Results
	private FormattedValue operatingIncome;
	private FormattedValue totalOtherIncomeExpenseNet;
	private FormattedValue ebit;

	// Pre-Tax Results
	private FormattedValue interestExpense;
	private FormattedValue incomeBeforeTax;
	private FormattedValue incomeTaxExpense;

	// Net Income Components
	private FormattedValue minorityInterest;
	private FormattedValue netIncomeFromContinuingOps;
	private FormattedValue discontinuedOperations;
	private FormattedValue extraordinaryItems;
	private FormattedValue effectOfAccountingCharges;
	private FormattedValue otherItems;
	private FormattedValue netIncome;
	private FormattedValue netIncomeApplicableToCommonShares;

	@Override
	protected IncomeStatement parseInternal(JsonNode node) {
		IncomeStatement statement = new IncomeStatement();

		statement.setMaxAge(getIntegerValue(node, "maxAge"));
		statement.setEndDate(parseFormattedValue(node.get("endDate")));

		// Parse revenue and cost of goods sold
		statement.setTotalRevenue(parseFormattedValue(node.get("totalRevenue")));
		statement.setCostOfRevenue(parseFormattedValue(node.get("costOfRevenue")));
		statement.setGrossProfit(parseFormattedValue(node.get("grossProfit")));

		// Parse operating expenses
		statement.setResearchDevelopment(parseFormattedValue(node.get("researchDevelopment")));
		statement.setSellingGeneralAdministrative(parseFormattedValue(node.get("sellingGeneralAdministrative")));
		statement.setNonRecurring(parseFormattedValue(node.get("nonRecurring")));
		statement.setOtherOperatingExpenses(parseFormattedValue(node.get("otherOperatingExpenses")));
		statement.setTotalOperatingExpenses(parseFormattedValue(node.get("totalOperatingExpenses")));

		// Parse operating results
		statement.setOperatingIncome(parseFormattedValue(node.get("operatingIncome")));
		statement.setTotalOtherIncomeExpenseNet(parseFormattedValue(node.get("totalOtherIncomeExpenseNet")));
		statement.setEbit(parseFormattedValue(node.get("ebit")));

		// Parse pre-tax results
		statement.setInterestExpense(parseFormattedValue(node.get("interestExpense")));
		statement.setIncomeBeforeTax(parseFormattedValue(node.get("incomeBeforeTax")));
		statement.setIncomeTaxExpense(parseFormattedValue(node.get("incomeTaxExpense")));

		// Parse net income components
		statement.setMinorityInterest(parseFormattedValue(node.get("minorityInterest")));
		statement.setNetIncomeFromContinuingOps(parseFormattedValue(node.get("netIncomeFromContinuingOps")));
		statement.setDiscontinuedOperations(parseFormattedValue(node.get("discontinuedOperations")));
		statement.setExtraordinaryItems(parseFormattedValue(node.get("extraordinaryItems")));
		statement.setEffectOfAccountingCharges(parseFormattedValue(node.get("effectOfAccountingCharges")));
		statement.setOtherItems(parseFormattedValue(node.get("otherItems")));
		statement.setNetIncome(parseFormattedValue(node.get("netIncome")));
		statement.setNetIncomeApplicableToCommonShares(parseFormattedValue(node.get("netIncomeApplicableToCommonShares")));

		return statement;
	}

	/**
	 * Static factory method for cleaner usage
	 */
	public static IncomeStatement fromJson(JsonNode node) {
		return new IncomeStatement().parse(node);
	}
}
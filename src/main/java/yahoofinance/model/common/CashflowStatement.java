package yahoofinance.model.common;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;
import static yahoofinance.util.Utils.*;

@Getter
@Setter
public class CashflowStatement extends AbstractQuoteSummaryModule<CashflowStatement> {
	private Integer maxAge;
	private FormattedValue endDate;

	// Operating Activities
	private FormattedValue netIncome;
	private FormattedValue depreciation;
	private FormattedValue changeToNetincome;
	private FormattedValue changeToAccountReceivables;
	private FormattedValue changeToLiabilities;
	private FormattedValue changeToInventory;
	private FormattedValue changeToOperatingActivities;
	private FormattedValue totalCashFromOperatingActivities;

	// Investing Activities
	private FormattedValue capitalExpenditures;
	private FormattedValue investments;
	private FormattedValue otherCashflowsFromInvestingActivities;
	private FormattedValue totalCashflowsFromInvestingActivities;

	// Financing Activities
	private FormattedValue dividendsPaid;
	private FormattedValue salePurchaseOfStock;
	private FormattedValue netBorrowings;
	private FormattedValue otherCashflowsFromFinancingActivities;
	private FormattedValue totalCashFromFinancingActivities;

	// Other Cash Flow Items
	private FormattedValue effectOfExchangeRate;
	private FormattedValue changeInCash;
	private FormattedValue repurchaseOfStock;
	private FormattedValue issuanceOfStock;

	@Override
	protected CashflowStatement parseInternal(JsonNode node) {
		CashflowStatement statement = new CashflowStatement();

		statement.setMaxAge(getIntegerValue(node, "maxAge"));
		statement.setEndDate(parseFormattedValue(node.get("endDate")));

		// Parse operating activities
		statement.setNetIncome(parseFormattedValue(node.get("netIncome")));
		statement.setDepreciation(parseFormattedValue(node.get("depreciation")));
		statement.setChangeToNetincome(parseFormattedValue(node.get("changeToNetincome")));
		statement.setChangeToAccountReceivables(parseFormattedValue(node.get("changeToAccountReceivables")));
		statement.setChangeToLiabilities(parseFormattedValue(node.get("changeToLiabilities")));
		statement.setChangeToInventory(parseFormattedValue(node.get("changeToInventory")));
		statement.setChangeToOperatingActivities(parseFormattedValue(node.get("changeToOperatingActivities")));
		statement.setTotalCashFromOperatingActivities(parseFormattedValue(node.get("totalCashFromOperatingActivities")));

		// Parse investing activities
		statement.setCapitalExpenditures(parseFormattedValue(node.get("capitalExpenditures")));
		statement.setInvestments(parseFormattedValue(node.get("investments")));
		statement.setOtherCashflowsFromInvestingActivities(parseFormattedValue(node.get("otherCashflowsFromInvestingActivities")));
		statement.setTotalCashflowsFromInvestingActivities(parseFormattedValue(node.get("totalCashflowsFromInvestingActivities")));

		// Parse financing activities
		statement.setDividendsPaid(parseFormattedValue(node.get("dividendsPaid")));
		statement.setSalePurchaseOfStock(parseFormattedValue(node.get("salePurchaseOfStock")));
		statement.setNetBorrowings(parseFormattedValue(node.get("netBorrowings")));
		statement.setOtherCashflowsFromFinancingActivities(parseFormattedValue(node.get("otherCashflowsFromFinancingActivities")));
		statement.setTotalCashFromFinancingActivities(parseFormattedValue(node.get("totalCashFromFinancingActivities")));

		// Parse other cash flow items
		statement.setEffectOfExchangeRate(parseFormattedValue(node.get("effectOfExchangeRate")));
		statement.setChangeInCash(parseFormattedValue(node.get("changeInCash")));
		statement.setRepurchaseOfStock(parseFormattedValue(node.get("repurchaseOfStock")));
		statement.setIssuanceOfStock(parseFormattedValue(node.get("issuanceOfStock")));

		return statement;
	}

	/**
	 * Static factory method for cleaner usage
	 */
	public static CashflowStatement fromJson(JsonNode node) {
		return new CashflowStatement().parse(node);
	}
}
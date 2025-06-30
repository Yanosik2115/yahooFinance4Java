package yahoofinance.model.common;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceSheetStatement extends AbstractQuoteSummaryModule<BalanceSheetStatement> {
	private Integer maxAge;
	private FormattedValue endDate;

	// Current Assets
	private FormattedValue cash;
	private FormattedValue shortTermInvestments;
	private FormattedValue netReceivables;
	private FormattedValue inventory;
	private FormattedValue otherCurrentAssets;
	private FormattedValue totalCurrentAssets;

	// Long-term Assets
	private FormattedValue longTermInvestments;
	private FormattedValue propertyPlantEquipment;
	private FormattedValue goodWill;
	private FormattedValue intangibleAssets;
	private FormattedValue accumulatedAmortization;
	private FormattedValue otherAssets;
	private FormattedValue deferredLongTermAssetCharges;
	private FormattedValue totalAssets;

	// Current Liabilities
	private FormattedValue accountsPayable;
	private FormattedValue shortLongTermDebt;
	private FormattedValue otherCurrentLiab;
	private FormattedValue totalCurrentLiabilities;

	// Long-term Liabilities
	private FormattedValue longTermDebt;
	private FormattedValue otherLiab;
	private FormattedValue deferredLongTermLiab;
	private FormattedValue minorityInterest;
	private FormattedValue negativeGoodwill;
	private FormattedValue totalLiab;

	// Stockholder Equity
	private FormattedValue miscStocksOptionsWarrants;
	private FormattedValue redeemablePreferredStock;
	private FormattedValue preferredStock;
	private FormattedValue commonStock;
	private FormattedValue retainedEarnings;
	private FormattedValue treasuryStock;
	private FormattedValue capitalSurplus;
	private FormattedValue otherStockholderEquity;
	private FormattedValue totalStockholderEquity;
	private FormattedValue netTangibleAssets;

	@Override
	protected BalanceSheetStatement parseInternal(JsonNode node) {
		BalanceSheetStatement statement = new BalanceSheetStatement();

		statement.setMaxAge(getIntegerValue(node, "maxAge"));
		statement.setEndDate(parseFormattedValue(node.get("endDate")));

		// Parse current assets
		statement.setCash(parseFormattedValue(node.get("cash")));
		statement.setShortTermInvestments(parseFormattedValue(node.get("shortTermInvestments")));
		statement.setNetReceivables(parseFormattedValue(node.get("netReceivables")));
		statement.setInventory(parseFormattedValue(node.get("inventory")));
		statement.setOtherCurrentAssets(parseFormattedValue(node.get("otherCurrentAssets")));
		statement.setTotalCurrentAssets(parseFormattedValue(node.get("totalCurrentAssets")));

		// Parse long-term assets
		statement.setLongTermInvestments(parseFormattedValue(node.get("longTermInvestments")));
		statement.setPropertyPlantEquipment(parseFormattedValue(node.get("propertyPlantEquipment")));
		statement.setGoodWill(parseFormattedValue(node.get("goodWill")));
		statement.setIntangibleAssets(parseFormattedValue(node.get("intangibleAssets")));
		statement.setAccumulatedAmortization(parseFormattedValue(node.get("accumulatedAmortization")));
		statement.setOtherAssets(parseFormattedValue(node.get("otherAssets")));
		statement.setDeferredLongTermAssetCharges(parseFormattedValue(node.get("deferredLongTermAssetCharges")));
		statement.setTotalAssets(parseFormattedValue(node.get("totalAssets")));

		// Parse current liabilities
		statement.setAccountsPayable(parseFormattedValue(node.get("accountsPayable")));
		statement.setShortLongTermDebt(parseFormattedValue(node.get("shortLongTermDebt")));
		statement.setOtherCurrentLiab(parseFormattedValue(node.get("otherCurrentLiab")));
		statement.setTotalCurrentLiabilities(parseFormattedValue(node.get("totalCurrentLiabilities")));

		// Parse long-term liabilities
		statement.setLongTermDebt(parseFormattedValue(node.get("longTermDebt")));
		statement.setOtherLiab(parseFormattedValue(node.get("otherLiab")));
		statement.setDeferredLongTermLiab(parseFormattedValue(node.get("deferredLongTermLiab")));
		statement.setMinorityInterest(parseFormattedValue(node.get("minorityInterest")));
		statement.setNegativeGoodwill(parseFormattedValue(node.get("negativeGoodwill")));
		statement.setTotalLiab(parseFormattedValue(node.get("totalLiab")));

		// Parse stockholder equity
		statement.setMiscStocksOptionsWarrants(parseFormattedValue(node.get("miscStocksOptionsWarrants")));
		statement.setRedeemablePreferredStock(parseFormattedValue(node.get("redeemablePreferredStock")));
		statement.setPreferredStock(parseFormattedValue(node.get("preferredStock")));
		statement.setCommonStock(parseFormattedValue(node.get("commonStock")));
		statement.setRetainedEarnings(parseFormattedValue(node.get("retainedEarnings")));
		statement.setTreasuryStock(parseFormattedValue(node.get("treasuryStock")));
		statement.setCapitalSurplus(parseFormattedValue(node.get("capitalSurplus")));
		statement.setOtherStockholderEquity(parseFormattedValue(node.get("otherStockholderEquity")));
		statement.setTotalStockholderEquity(parseFormattedValue(node.get("totalStockholderEquity")));
		statement.setNetTangibleAssets(parseFormattedValue(node.get("netTangibleAssets")));

		return statement;
	}

	/**
	 * Static factory method for cleaner usage
	 */
	public static BalanceSheetStatement fromJson(JsonNode node) {
		return new BalanceSheetStatement().parse(node);
	}
}
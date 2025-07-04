package yahoofinance.model.market.modules;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;
import static yahoofinance.util.Utils.*;

@Getter
@Setter
public class FinancialData extends AbstractQuoteSummaryModule<FinancialData> {
	private Integer maxAge;
	private String financialCurrency;

	// Price and Target Information
	private FormattedValue currentPrice;
	private FormattedValue targetHighPrice;
	private FormattedValue targetLowPrice;
	private FormattedValue targetMeanPrice;
	private FormattedValue targetMedianPrice;

	// Analyst Recommendations
	private FormattedValue recommendationMean;
	private String recommendationKey;
	private FormattedValue numberOfAnalystOpinions;

	// Balance Sheet Metrics
	private FormattedValue totalCash;
	private FormattedValue totalCashPerShare;
	private FormattedValue totalDebt;
	private FormattedValue debtToEquity;

	// Liquidity Ratios
	private FormattedValue quickRatio;
	private FormattedValue currentRatio;

	// Revenue and Profitability
	private FormattedValue totalRevenue;
	private FormattedValue revenuePerShare;
	private FormattedValue grossProfits;
	private FormattedValue ebitda;

	// Cash Flow Metrics
	private FormattedValue freeCashflow;
	private FormattedValue operatingCashflow;

	// Growth Metrics
	private FormattedValue earningsGrowth;
	private FormattedValue revenueGrowth;

	// Profitability Margins
	private FormattedValue grossMargins;
	private FormattedValue ebitdaMargins;
	private FormattedValue operatingMargins;
	private FormattedValue profitMargins;

	// Return Metrics
	private FormattedValue returnOnAssets;
	private FormattedValue returnOnEquity;

	@Override
	protected FinancialData parseInternal(JsonNode node) {
		FinancialData data = new FinancialData();

		data.setMaxAge(getIntegerValue(node, "maxAge"));
		data.setFinancialCurrency(getStringValue(node, "financialCurrency"));

		data.setCurrentPrice(parseFormattedValue(node.get("currentPrice")));
		data.setTargetHighPrice(parseFormattedValue(node.get("targetHighPrice")));
		data.setTargetLowPrice(parseFormattedValue(node.get("targetLowPrice")));
		data.setTargetMeanPrice(parseFormattedValue(node.get("targetMeanPrice")));
		data.setTargetMedianPrice(parseFormattedValue(node.get("targetMedianPrice")));

		data.setRecommendationMean(parseFormattedValue(node.get("recommendationMean")));
		data.setRecommendationKey(getStringValue(node, "recommendationKey"));
		data.setNumberOfAnalystOpinions(parseFormattedValue(node.get("numberOfAnalystOpinions")));

		data.setTotalCash(parseFormattedValue(node.get("totalCash")));
		data.setTotalCashPerShare(parseFormattedValue(node.get("totalCashPerShare")));
		data.setTotalDebt(parseFormattedValue(node.get("totalDebt")));
		data.setDebtToEquity(parseFormattedValue(node.get("debtToEquity")));

		data.setQuickRatio(parseFormattedValue(node.get("quickRatio")));
		data.setCurrentRatio(parseFormattedValue(node.get("currentRatio")));

		data.setTotalRevenue(parseFormattedValue(node.get("totalRevenue")));
		data.setRevenuePerShare(parseFormattedValue(node.get("revenuePerShare")));
		data.setGrossProfits(parseFormattedValue(node.get("grossProfits")));
		data.setEbitda(parseFormattedValue(node.get("ebitda")));

		data.setFreeCashflow(parseFormattedValue(node.get("freeCashflow")));
		data.setOperatingCashflow(parseFormattedValue(node.get("operatingCashflow")));

		data.setEarningsGrowth(parseFormattedValue(node.get("earningsGrowth")));
		data.setRevenueGrowth(parseFormattedValue(node.get("revenueGrowth")));

		data.setGrossMargins(parseFormattedValue(node.get("grossMargins")));
		data.setEbitdaMargins(parseFormattedValue(node.get("ebitdaMargins")));
		data.setOperatingMargins(parseFormattedValue(node.get("operatingMargins")));
		data.setProfitMargins(parseFormattedValue(node.get("profitMargins")));

		data.setReturnOnAssets(parseFormattedValue(node.get("returnOnAssets")));
		data.setReturnOnEquity(parseFormattedValue(node.get("returnOnEquity")));

		return data;
	}

	public static FinancialData fromJson(JsonNode node) {
		return new FinancialData().parse(node);
	}
}
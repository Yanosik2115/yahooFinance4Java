package yahoofinance.model.market.modules;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;
import static yahoofinance.util.Utils.*;

@Getter
@Setter
public class DefaultKeyStatistics extends AbstractQuoteSummaryModule<DefaultKeyStatistics> {
	private Integer maxAge;

	// Valuation Metrics
	private FormattedValue enterpriseValue;
	private FormattedValue forwardPE;
	private FormattedValue profitMargins;
	private FormattedValue priceToBook;
	private FormattedValue enterpriseToRevenue;
	private FormattedValue enterpriseToEbitda;

	// Share Information
	private FormattedValue floatShares;
	private FormattedValue sharesOutstanding;
	private FormattedValue sharesShort;
	private FormattedValue sharesShortPriorMonth;

	// Financial Metrics
	private FormattedValue beta;
	private FormattedValue bookValue;
	private FormattedValue trailingEps;
	private FormattedValue forwardEps;

	// Stock Split Information
	private String lastSplitFactor;
	private FormattedValue lastSplitDate;

	// Performance Metrics
	private FormattedValue fiftyTwoWeekChange;
	private FormattedValue sandP52WeekChange;

	// Dividend Information
	private FormattedValue lastDividendValue;
	private FormattedValue lastDividendDate;

	@Override
	protected DefaultKeyStatistics parseInternal(JsonNode node) {
		DefaultKeyStatistics statistics = new DefaultKeyStatistics();

		statistics.setMaxAge(getIntegerValue(node, "maxAge"));

		statistics.setEnterpriseValue(parseFormattedValue(node.get("enterpriseValue")));
		statistics.setForwardPE(parseFormattedValue(node.get("forwardPE")));
		statistics.setProfitMargins(parseFormattedValue(node.get("profitMargins")));
		statistics.setPriceToBook(parseFormattedValue(node.get("priceToBook")));
		statistics.setEnterpriseToRevenue(parseFormattedValue(node.get("enterpriseToRevenue")));
		statistics.setEnterpriseToEbitda(parseFormattedValue(node.get("enterpriseToEbitda")));
		statistics.setFloatShares(parseFormattedValue(node.get("floatShares")));
		statistics.setSharesOutstanding(parseFormattedValue(node.get("sharesOutstanding")));
		statistics.setSharesShort(parseFormattedValue(node.get("sharesShort")));
		statistics.setSharesShortPriorMonth(parseFormattedValue(node.get("sharesShortPriorMonth")));
		statistics.setBeta(parseFormattedValue(node.get("beta")));
		statistics.setBookValue(parseFormattedValue(node.get("bookValue")));
		statistics.setTrailingEps(parseFormattedValue(node.get("trailingEps")));
		statistics.setForwardEps(parseFormattedValue(node.get("forwardEps")));
		statistics.setLastSplitFactor(getStringValue(node, "lastSplitFactor"));
		statistics.setLastSplitDate(parseFormattedValue(node.get("lastSplitDate")));
		statistics.setFiftyTwoWeekChange(parseFormattedValue(node.get("fiftyTwoWeekChange")));
		statistics.setSandP52WeekChange(parseFormattedValue(node.get("sandP52WeekChange")));
		statistics.setLastDividendValue(parseFormattedValue(node.get("lastDividendValue")));
		statistics.setLastDividendDate(parseFormattedValue(node.get("lastDividendDate")));

		return statistics;
	}
	public static DefaultKeyStatistics fromJson(JsonNode node) {
		return new DefaultKeyStatistics().parse(node);
	}
}
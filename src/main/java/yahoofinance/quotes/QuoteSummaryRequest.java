package yahoofinance.quotes;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static yahoofinance.util.Utils.*;

@Slf4j
public class QuoteSummaryRequest extends QuoteRequest<StockQuoteSummary> {

	/*
	"summaryProfile",  # contains general information about the company
    "summaryDetail",  # prices + volume + market cap + etc
    "assetProfile",  # summaryProfile + company officers
    "fundProfile",
    "price",  # current prices
    "quoteType",  # quoteType
    "esgScores",  # Environmental, social, and governance (ESG) scores, sustainability and ethical performance of companies
    "incomeStatementHistory",
    "incomeStatementHistoryQuarterly",
    "balanceSheetHistory",
    "balanceSheetHistoryQuarterly",
    "cashFlowStatementHistory",
    "cashFlowStatementHistoryQuarterly",
    "defaultKeyStatistics",  # KPIs (PE, enterprise value, EPS, EBITA, and more)
    "financialData",  # Financial KPIs (revenue, gross margins, operating cash flow, free cash flow, and more)
    "calendarEvents",  # future earnings date
    "secFilings",  # SEC filings, such as 10K and 10Q reports
    "upgradeDowngradeHistory",  # upgrades and downgrades that analysts have given a company's stock
    "institutionOwnership",  # institutional ownership, holders and shares outstanding
    "fundOwnership",  # mutual fund ownership, holders and shares outstanding
    "majorDirectHolders",
    "majorHoldersBreakdown",
    "insiderTransactions",  # insider transactions, such as the number of shares bought and sold by company executives
    "insiderHolders",  # insider holders, such as the number of shares held by company executives
    "netSharePurchaseActivity",  # net share purchase activity, such as the number of shares bought and sold by company executives
    "earnings",  # earnings history
    "earningsHistory",
    "earningsTrend",  # earnings trend
    "industryTrend",
    "indexTrend",
    "sectorTrend",
    "recommendationTrend",
    "futuresChain",
	*/
	private static final String QUOTE_SUMMARY_URL = "https://query2.finance.yahoo.com/v10/finance/quoteSummary";


	public QuoteSummaryRequest(String symbol) {
		super(symbol);
	}

	@Override
	public String getURL() {
		return QUOTE_SUMMARY_URL;
	}

	@Override
	public java.util.Map<String, String> getParams() {
		return java.util.Map.of("modules", "financialData,quoteType,defaultKeyStatistics,assetProfile,summaryDetail");
	}

	@Override
	public StockQuoteSummary parseJson(JsonNode node) {
		log.debug("Parsing quote summary response for symbol: {}", getSymbol());

		try {
			StockQuoteSummary stockQuoteSummary = new StockQuoteSummary();
			JsonNode quoteSummaryNode = node.get("quoteSummary");
			if (quoteSummaryNode != null) {
				stockQuoteSummary = parseQuoteSummary(quoteSummaryNode);
			}

			log.debug("Successfully parsed quote summary for symbol: {}", getSymbol());
			return stockQuoteSummary;

		} catch (Exception e) {
			log.error("Failed to parse JSON response for symbol: {}", getSymbol(), e);
			throw new RuntimeException("Failed to parse quote summary JSON", e);
		}
	}

	private StockQuoteSummary parseQuoteSummary(JsonNode node) {
		StockQuoteSummary quoteSummary = new StockQuoteSummary();

		// Parse error field
		JsonNode errorNode = node.get("error");
		if (errorNode != null && !errorNode.isNull()) {
			quoteSummary.setError(errorNode.asText());
		}

		// Parse result array
		JsonNode resultNode = node.get("result");
		if (resultNode != null && resultNode.isArray()) {
			List<StockQuoteSummary.Result> results = new ArrayList<>();
			for (JsonNode resultItem : resultNode) {
				StockQuoteSummary.Result result = parseResult(resultItem);
				results.add(result);
			}
			quoteSummary.setResult(results);
		}

		return quoteSummary;
	}

	private StockQuoteSummary.Result parseResult(JsonNode node) {
		StockQuoteSummary.Result result = new StockQuoteSummary.Result();

		JsonNode assetProfileNode = node.get("assetProfile");
		if (assetProfileNode != null) {
			result.setAssetProfile(parseAssetProfile(assetProfileNode));
		}

		JsonNode summaryDetailNode = node.get("summaryDetail");
		if (summaryDetailNode != null) {
			result.setSummaryDetail(parseSummaryDetail(summaryDetailNode));
		}

		JsonNode defaultKeyStatisticsNode = node.get("defaultKeyStatistics");
		if (defaultKeyStatisticsNode != null) {
			result.setDefaultKeyStatistics(parseDefaultKeyStatistics(defaultKeyStatisticsNode));
		}

		JsonNode quoteTypeNode = node.get("quoteType");
		if (quoteTypeNode != null) {
			result.setQuoteType(parseQuoteType(quoteTypeNode));
		}

		JsonNode financialDataNode = node.get("financialData");
		if (financialDataNode != null) {
			result.setFinancialData(parseFinancialData(financialDataNode));
		}

		return result;
	}

	private StockQuoteSummary.AssetProfile parseAssetProfile(JsonNode node) {
		StockQuoteSummary.AssetProfile assetProfile = new StockQuoteSummary.AssetProfile();

		assetProfile.setAddress1(getStringValue(node, "address1"));
		assetProfile.setCity(getStringValue(node, "city"));
		assetProfile.setState(getStringValue(node, "state"));
		assetProfile.setZip(getStringValue(node, "zip"));
		assetProfile.setCountry(getStringValue(node, "country"));
		assetProfile.setPhone(getStringValue(node, "phone"));
		assetProfile.setWebsite(getStringValue(node, "website"));
		assetProfile.setIndustry(getStringValue(node, "industry"));
		assetProfile.setIndustryKey(getStringValue(node, "industryKey"));
		assetProfile.setIndustryDisp(getStringValue(node, "industryDisp"));
		assetProfile.setSector(getStringValue(node, "sector"));
		assetProfile.setSectorKey(getStringValue(node, "sectorKey"));
		assetProfile.setSectorDisp(getStringValue(node, "sectorDisp"));
		assetProfile.setLongBusinessSummary(getStringValue(node, "longBusinessSummary"));
		assetProfile.setFullTimeEmployees(getLongValue(node, "fullTimeEmployees"));
		assetProfile.setAuditRisk(getIntegerValue(node, "auditRisk"));
		assetProfile.setBoardRisk(getIntegerValue(node, "boardRisk"));
		assetProfile.setCompensationRisk(getIntegerValue(node, "compensationRisk"));
		assetProfile.setShareHolderRightsRisk(getIntegerValue(node, "shareHolderRightsRisk"));
		assetProfile.setOverallRisk(getIntegerValue(node, "overallRisk"));
		assetProfile.setGovernanceEpochDate(getLongValue(node, "governanceEpochDate"));
		assetProfile.setCompensationAsOfEpochDate(getLongValue(node, "compensationAsOfEpochDate"));
		assetProfile.setIrWebsite(getStringValue(node, "irWebsite"));
		assetProfile.setMaxAge(getIntegerValue(node, "maxAge"));

		JsonNode officersNode = node.get("companyOfficers");
		if (officersNode != null && officersNode.isArray()) {
			List<StockQuoteSummary.CompanyOfficer> officers = new ArrayList<>();
			for (JsonNode officerNode : officersNode) {
				officers.add(parseCompanyOfficer(officerNode));
			}
			assetProfile.setCompanyOfficers(officers);
		}

		return assetProfile;
	}

	private StockQuoteSummary.CompanyOfficer parseCompanyOfficer(JsonNode node) {
		StockQuoteSummary.CompanyOfficer officer = new StockQuoteSummary.CompanyOfficer();

		officer.setMaxAge(getIntegerValue(node, "maxAge"));
		officer.setName(getStringValue(node, "name"));
		officer.setAge(getIntegerValue(node, "age"));
		officer.setTitle(getStringValue(node, "title"));
		officer.setYearBorn(getIntegerValue(node, "yearBorn"));
		officer.setFiscalYear(getIntegerValue(node, "fiscalYear"));
		officer.setTotalPay(parseFormattedValue(node.get("totalPay")));
		officer.setExercisedValue(parseFormattedValue(node.get("exercisedValue")));
		officer.setUnexercisedValue(parseFormattedValue(node.get("unexercisedValue")));

		return officer;
	}

	private StockQuoteSummary.SummaryDetail parseSummaryDetail(JsonNode node) {
		StockQuoteSummary.SummaryDetail summaryDetail = new StockQuoteSummary.SummaryDetail();

		summaryDetail.setMaxAge(getIntegerValue(node, "maxAge"));
		summaryDetail.setPriceHint(parseFormattedValue(node.get("priceHint")));
		summaryDetail.setPreviousClose(parseFormattedValue(node.get("previousClose")));
		summaryDetail.setOpen(parseFormattedValue(node.get("open")));
		summaryDetail.setDayLow(parseFormattedValue(node.get("dayLow")));
		summaryDetail.setDayHigh(parseFormattedValue(node.get("dayHigh")));
		summaryDetail.setRegularMarketPreviousClose(parseFormattedValue(node.get("regularMarketPreviousClose")));
		summaryDetail.setRegularMarketOpen(parseFormattedValue(node.get("regularMarketOpen")));
		summaryDetail.setRegularMarketDayLow(parseFormattedValue(node.get("regularMarketDayLow")));
		summaryDetail.setRegularMarketDayHigh(parseFormattedValue(node.get("regularMarketDayHigh")));
		summaryDetail.setDividendRate(parseFormattedValue(node.get("dividendRate")));
		summaryDetail.setDividendYield(parseFormattedValue(node.get("dividendYield")));
		summaryDetail.setExDividendDate(parseFormattedValue(node.get("exDividendDate")));
		summaryDetail.setPayoutRatio(parseFormattedValue(node.get("payoutRatio")));
		summaryDetail.setFiveYearAvgDividendYield(parseFormattedValue(node.get("fiveYearAvgDividendYield")));
		summaryDetail.setBeta(parseFormattedValue(node.get("beta")));
		summaryDetail.setTrailingPE(parseFormattedValue(node.get("trailingPE")));
		summaryDetail.setForwardPE(parseFormattedValue(node.get("forwardPE")));
		summaryDetail.setVolume(parseFormattedValue(node.get("volume")));
		summaryDetail.setRegularMarketVolume(parseFormattedValue(node.get("regularMarketVolume")));
		summaryDetail.setAverageVolume(parseFormattedValue(node.get("averageVolume")));
		summaryDetail.setAverageVolume10days(parseFormattedValue(node.get("averageVolume10days")));
		summaryDetail.setAverageDailyVolume10Day(parseFormattedValue(node.get("averageDailyVolume10Day")));
		summaryDetail.setBid(parseFormattedValue(node.get("bid")));
		summaryDetail.setAsk(parseFormattedValue(node.get("ask")));
		summaryDetail.setBidSize(parseFormattedValue(node.get("bidSize")));
		summaryDetail.setAskSize(parseFormattedValue(node.get("askSize")));
		summaryDetail.setMarketCap(parseFormattedValue(node.get("marketCap")));
		summaryDetail.setFiftyTwoWeekLow(parseFormattedValue(node.get("fiftyTwoWeekLow")));
		summaryDetail.setFiftyTwoWeekHigh(parseFormattedValue(node.get("fiftyTwoWeekHigh")));
		summaryDetail.setPriceToSalesTrailing12Months(parseFormattedValue(node.get("priceToSalesTrailing12Months")));
		summaryDetail.setFiftyDayAverage(parseFormattedValue(node.get("fiftyDayAverage")));
		summaryDetail.setTwoHundredDayAverage(parseFormattedValue(node.get("twoHundredDayAverage")));
		summaryDetail.setTrailingAnnualDividendRate(parseFormattedValue(node.get("trailingAnnualDividendRate")));
		summaryDetail.setTrailingAnnualDividendYield(parseFormattedValue(node.get("trailingAnnualDividendYield")));
		summaryDetail.setCurrency(getStringValue(node, "currency"));
		summaryDetail.setTradeable(getBooleanValue(node, "tradeable"));

		return summaryDetail;
	}

	private StockQuoteSummary.DefaultKeyStatistics parseDefaultKeyStatistics(JsonNode node) {
		StockQuoteSummary.DefaultKeyStatistics stats = new StockQuoteSummary.DefaultKeyStatistics();

		stats.setMaxAge(getIntegerValue(node, "maxAge"));
		stats.setEnterpriseValue(parseFormattedValue(node.get("enterpriseValue")));
		stats.setForwardPE(parseFormattedValue(node.get("forwardPE")));
		stats.setProfitMargins(parseFormattedValue(node.get("profitMargins")));
		stats.setFloatShares(parseFormattedValue(node.get("floatShares")));
		stats.setSharesOutstanding(parseFormattedValue(node.get("sharesOutstanding")));
		stats.setSharesShort(parseFormattedValue(node.get("sharesShort")));
		stats.setSharesShortPriorMonth(parseFormattedValue(node.get("sharesShortPriorMonth")));
		stats.setBeta(parseFormattedValue(node.get("beta")));
		stats.setBookValue(parseFormattedValue(node.get("bookValue")));
		stats.setPriceToBook(parseFormattedValue(node.get("priceToBook")));
		stats.setTrailingEps(parseFormattedValue(node.get("trailingEps")));
		stats.setForwardEps(parseFormattedValue(node.get("forwardEps")));
		stats.setLastSplitFactor(getStringValue(node, "lastSplitFactor"));
		stats.setLastSplitDate(parseFormattedValue(node.get("lastSplitDate")));
		stats.setEnterpriseToRevenue(parseFormattedValue(node.get("enterpriseToRevenue")));
		stats.setEnterpriseToEbitda(parseFormattedValue(node.get("enterpriseToEbitda")));
		stats.setFiftyTwoWeekChange(parseFormattedValue(node.get("52WeekChange")));
		stats.setSandP52WeekChange(parseFormattedValue(node.get("SandP52WeekChange")));
		stats.setLastDividendValue(parseFormattedValue(node.get("lastDividendValue")));
		stats.setLastDividendDate(parseFormattedValue(node.get("lastDividendDate")));

		return stats;
	}

	private StockQuoteSummary.QuoteType parseQuoteType(JsonNode node) {
		StockQuoteSummary.QuoteType quoteType = new StockQuoteSummary.QuoteType();

		quoteType.setExchange(getStringValue(node, "exchange"));
		quoteType.setQuoteType(getStringValue(node, "quoteType"));
		quoteType.setSymbol(getStringValue(node, "symbol"));
		quoteType.setUnderlyingSymbol(getStringValue(node, "underlyingSymbol"));
		quoteType.setShortName(getStringValue(node, "shortName"));
		quoteType.setLongName(getStringValue(node, "longName"));
		quoteType.setFirstTradeDateEpochUtc(getLongValue(node, "firstTradeDateEpochUtc"));
		quoteType.setTimeZoneFullName(getStringValue(node, "timeZoneFullName"));
		quoteType.setTimeZoneShortName(getStringValue(node, "timeZoneShortName"));
		quoteType.setUuid(getStringValue(node, "uuid"));
		quoteType.setMessageBoardId(getStringValue(node, "messageBoardId"));
		quoteType.setGmtOffSetMilliseconds(getLongValue(node, "gmtOffSetMilliseconds"));
		quoteType.setMaxAge(getIntegerValue(node, "maxAge"));

		return quoteType;
	}

	private StockQuoteSummary.FinancialData parseFinancialData(JsonNode node) {
		StockQuoteSummary.FinancialData financialData = new StockQuoteSummary.FinancialData();

		financialData.setMaxAge(getIntegerValue(node, "maxAge"));
		financialData.setCurrentPrice(parseFormattedValue(node.get("currentPrice")));
		financialData.setTargetHighPrice(parseFormattedValue(node.get("targetHighPrice")));
		financialData.setTargetLowPrice(parseFormattedValue(node.get("targetLowPrice")));
		financialData.setTargetMeanPrice(parseFormattedValue(node.get("targetMeanPrice")));
		financialData.setTargetMedianPrice(parseFormattedValue(node.get("targetMedianPrice")));
		financialData.setRecommendationMean(parseFormattedValue(node.get("recommendationMean")));
		financialData.setRecommendationKey(getStringValue(node, "recommendationKey"));
		financialData.setNumberOfAnalystOpinions(parseFormattedValue(node.get("numberOfAnalystOpinions")));
		financialData.setTotalCash(parseFormattedValue(node.get("totalCash")));
		financialData.setTotalCashPerShare(parseFormattedValue(node.get("totalCashPerShare")));
		financialData.setEbitda(parseFormattedValue(node.get("ebitda")));
		financialData.setTotalDebt(parseFormattedValue(node.get("totalDebt")));
		financialData.setQuickRatio(parseFormattedValue(node.get("quickRatio")));
		financialData.setCurrentRatio(parseFormattedValue(node.get("currentRatio")));
		financialData.setTotalRevenue(parseFormattedValue(node.get("totalRevenue")));
		financialData.setDebtToEquity(parseFormattedValue(node.get("debtToEquity")));
		financialData.setRevenuePerShare(parseFormattedValue(node.get("revenuePerShare")));
		financialData.setReturnOnAssets(parseFormattedValue(node.get("returnOnAssets")));
		financialData.setReturnOnEquity(parseFormattedValue(node.get("returnOnEquity")));
		financialData.setGrossProfits(parseFormattedValue(node.get("grossProfits")));
		financialData.setFreeCashflow(parseFormattedValue(node.get("freeCashflow")));
		financialData.setOperatingCashflow(parseFormattedValue(node.get("operatingCashflow")));
		financialData.setEarningsGrowth(parseFormattedValue(node.get("earningsGrowth")));
		financialData.setRevenueGrowth(parseFormattedValue(node.get("revenueGrowth")));
		financialData.setGrossMargins(parseFormattedValue(node.get("grossMargins")));
		financialData.setEbitdaMargins(parseFormattedValue(node.get("ebitdaMargins")));
		financialData.setOperatingMargins(parseFormattedValue(node.get("operatingMargins")));
		financialData.setProfitMargins(parseFormattedValue(node.get("profitMargins")));
		financialData.setFinancialCurrency(getStringValue(node, "financialCurrency"));

		return financialData;
	}

	private StockQuoteSummary.FormattedValue parseFormattedValue(JsonNode node) {
		if (node == null || node.isNull()) {
			return null;
		}

		StockQuoteSummary.FormattedValue formattedValue = new StockQuoteSummary.FormattedValue();

		JsonNode rawNode = node.get("raw");
		if (rawNode != null && !rawNode.isNull()) {
			if (rawNode.isNumber()) {
				formattedValue.setRaw(new BigDecimal(rawNode.asText()));
			}
		}

		JsonNode fmtNode = node.get("fmt");
		if (fmtNode != null && !fmtNode.isNull()) {
			formattedValue.setFmt(fmtNode.asText());
		}

		JsonNode longFmtNode = node.get("longFmt");
		if (longFmtNode != null && !longFmtNode.isNull()) {
			formattedValue.setLongFmt(longFmtNode.asText());
		}

		return formattedValue;
	}
}
package yahoofinance.quotes;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.model.common.QuoteSummaryModule;
import yahoofinance.model.market.modules.*;
import java.util.*;

@Slf4j
public class QuoteSummaryRequest extends QuoteRequest<StockQuoteSummary> {


	@Getter
	public enum Module {
		SUMMARY_PROFILE("summaryProfile"),           // General company information
		SUMMARY_DETAIL("summaryDetail"),             // Prices + volume + market cap + etc
		ASSET_PROFILE("assetProfile"),               // Company profile + officers
		FUND_PROFILE("fundProfile"),                 // Fund-specific information TODO check how to use "No fundamentals data found for symbol"
		PRICE("price"),                              // Current prices
		QUOTE_TYPE("quoteType"),                     // Quote type information
		ESG_SCORES("esgScores"),                     // Environmental, social, governance scores
		INCOME_STATEMENT_HISTORY("incomeStatementHistory"),
		INCOME_STATEMENT_HISTORY_QUARTERLY("incomeStatementHistoryQuarterly"),
		BALANCE_SHEET_HISTORY("balanceSheetHistory"),
		BALANCE_SHEET_HISTORY_QUARTERLY("balanceSheetHistoryQuarterly"),
		CASH_FLOW_STATEMENT_HISTORY("cashFlowStatementHistory"),
		CASH_FLOW_STATEMENT_HISTORY_QUARTERLY("cashFlowStatementHistoryQuarterly"),
		DEFAULT_KEY_STATISTICS("defaultKeyStatistics"), // KPIs (PE, enterprise value, EPS, EBITDA, etc)
		FINANCIAL_DATA("financialData"),             // Financial KPIs (revenue, margins, cash flow, etc)
		CALENDAR_EVENTS("calendarEvents"),           // Future earnings dates
		SEC_FILINGS("secFilings"),                   // SEC filings (10K, 10Q reports)
		UPGRADE_DOWNGRADE_HISTORY("upgradeDowngradeHistory"), // Analyst upgrades/downgrades
		INSTITUTION_OWNERSHIP("institutionOwnership"), // Institutional ownership data
		FUND_OWNERSHIP("fundOwnership"),             // Mutual fund ownership data
		MAJOR_DIRECT_HOLDERS("majorDirectHolders"),  // Major direct holders
		MAJOR_HOLDERS_BREAKDOWN("majorHoldersBreakdown"), // Major holders breakdown
		INSIDER_TRANSACTIONS("insiderTransactions"), // Insider transactions
		INSIDER_HOLDERS("insiderHolders"),           // Insider holders
		NET_SHARE_PURCHASE_ACTIVITY("netSharePurchaseActivity"), // Net share purchase activity
		EARNINGS("earnings"),                        // Earnings history
		EARNINGS_HISTORY("earningsHistory"),         // Historical earnings
		EARNINGS_TREND("earningsTrend"),             // Earnings trend analysis
		INDUSTRY_TREND("industryTrend"),             // Industry trend analysis
		INDEX_TREND("indexTrend"),                   // Index trend analysis
		SECTOR_TREND("sectorTrend"),                 // Sector trend analysis
		RECOMMENDATION_TREND("recommendationTrend"), // Analyst recommendation trends
		FUTURES_CHAIN("futuresChain");               // Futures chain data //TODO check how to use "No fundamentals data found for symbol"

		private final String moduleName;

		Module(String moduleName) {
			this.moduleName = moduleName;
		}

	}

	private static final String QUOTE_SUMMARY_URL = "https://query2.finance.yahoo.com/v10/finance/quoteSummary";

	private static final Set<Module> DEFAULT_MODULES = Set.of(
			Module.FINANCIAL_DATA,
			Module.QUOTE_TYPE,
			Module.DEFAULT_KEY_STATISTICS,
			Module.ASSET_PROFILE,
			Module.SUMMARY_DETAIL
	);

	private final Set<Module> requestedModules;


	public QuoteSummaryRequest(String symbol) {
		this(symbol, DEFAULT_MODULES);
	}

	public QuoteSummaryRequest(String symbol, Set<Module> modules) {
		super(symbol);
		this.requestedModules = modules != null && !modules.isEmpty() ?
				new HashSet<>(modules) : new HashSet<>(DEFAULT_MODULES);
	}

	public QuoteSummaryRequest(String symbol, Module... modules) {
		this(symbol, modules.length > 0 ? Set.of(modules) : DEFAULT_MODULES);
	}

	@Override
	public String getURL() {
		return QUOTE_SUMMARY_URL;
	}

	@Override
	public Map<String, String> getParams() {
		String modulesList = requestedModules.stream()
				.map(Module::getModuleName)
				.reduce((a, b) -> a + "," + b)
				.orElse("");

		return Map.of("modules", modulesList);
	}

	public Set<Module> getRequestedModules() {
		return Collections.unmodifiableSet(requestedModules);
	}


	public QuoteSummaryRequest addModules(Module... modules) {
		this.requestedModules.addAll(Arrays.asList(modules));
		return this;
	}

	public QuoteSummaryRequest removeModules(Module... modules) {
		Arrays.asList(modules).forEach(this.requestedModules::remove);
		return this;
	}

	public QuoteSummaryRequest setModules(Set<Module> modules) {
		this.requestedModules.clear();
		if (modules != null && !modules.isEmpty()) {
			this.requestedModules.addAll(modules);
		} else {
			this.requestedModules.addAll(DEFAULT_MODULES);
		}
		return this;
	}

	@Override
	public StockQuoteSummary parseJson(JsonNode node) {
		log.debug("Parsing quote summary response for symbol: {} with modules: {}",
				getSymbol(), requestedModules);

		try {
			StockQuoteSummary stockQuoteSummary = parseQuoteSummary(node.get("quoteSummary"));
			log.debug("Successfully parsed quote summary for symbol: {}", getSymbol());
			return stockQuoteSummary;

		} catch (Exception e) {
			log.error("Failed to parse JSON response for symbol: {}", getSymbol(), e);
			throw new RuntimeException("Failed to parse quote summary JSON", e);
		}
	}

	private StockQuoteSummary parseQuoteSummary(JsonNode quoteSummaryNode) {
		StockQuoteSummary quoteSummary = new StockQuoteSummary();

		if (quoteSummaryNode == null) {
			return quoteSummary;
		}

		// Handle error case
		JsonNode errorNode = quoteSummaryNode.get("error");
		if (errorNode != null && !errorNode.isNull()) {
			quoteSummary.setError(errorNode.asText());
			return quoteSummary;
		}

		// Parse results
		JsonNode resultNode = quoteSummaryNode.get("result");
		if (resultNode != null && resultNode.isArray() && !resultNode.isEmpty()) {
			JsonNode firstResult = resultNode.get(0);
			List<QuoteSummaryModule<?>> modules = parseModules(firstResult);
			quoteSummary.setModules(modules);
		}

		return quoteSummary;
	}

	private List<QuoteSummaryModule<?>> parseModules(JsonNode resultNode) {
		List<QuoteSummaryModule<?>> modules = new ArrayList<>();

		parseAndAddModule(modules, Module.ASSET_PROFILE, resultNode, "assetProfile", AssetProfile::fromJson);
		parseAndAddModule(modules, Module.SUMMARY_DETAIL, resultNode, "summaryDetail", SummaryDetail::fromJson);
		parseAndAddModule(modules, Module.DEFAULT_KEY_STATISTICS, resultNode, "defaultKeyStatistics", DefaultKeyStatistics::fromJson);
		parseAndAddModule(modules, Module.QUOTE_TYPE, resultNode, "quoteType", QuoteType::fromJson);
		parseAndAddModule(modules, Module.FINANCIAL_DATA, resultNode, "financialData", FinancialData::fromJson);
		parseAndAddModule(modules, Module.CALENDAR_EVENTS, resultNode, "calendarEvents", CalendarEvents::fromJson);
		parseAndAddModule(modules, Module.SUMMARY_PROFILE, resultNode, "summaryProfile", SummaryProfile::fromJson);
		parseAndAddModule(modules, Module.ESG_SCORES, resultNode, "esgScores", EsgScores::fromJson);
		parseAndAddModule(modules, Module.PRICE, resultNode, "price", Price::fromJson);
		parseAndAddModule(modules, Module.EARNINGS, resultNode, "earnings", Earnings::fromJson);
		parseAndAddModule(modules, Module.EARNINGS_HISTORY, resultNode, "earningsHistory", EarningsHistory::fromJson);
		parseAndAddModule(modules, Module.EARNINGS_TREND, resultNode, "earningsTrend", EarningsTrend::fromJson);
		parseAndAddModule(modules, Module.RECOMMENDATION_TREND, resultNode, "recommendationTrend", RecommendationTrend::fromJson);
		parseAndAddModule(modules, Module.INDEX_TREND, resultNode, "indexTrend", IndexTrend::fromJson);
		parseAndAddModule(modules, Module.INDUSTRY_TREND, resultNode, "industryTrend", IndustryTrend::fromJson);

		// Financial statements
		parseAndAddModule(modules, Module.INCOME_STATEMENT_HISTORY, resultNode, "incomeStatementHistory", IncomeStatementHistory::fromJson);
		parseAndAddModule(modules, Module.INCOME_STATEMENT_HISTORY_QUARTERLY, resultNode, "incomeStatementHistoryQuarterly", IncomeStatementHistoryQuarterly::fromJson);
		parseAndAddModule(modules, Module.BALANCE_SHEET_HISTORY, resultNode, "balanceSheetHistory", BalanceSheetHistory::fromJson);
		parseAndAddModule(modules, Module.BALANCE_SHEET_HISTORY_QUARTERLY, resultNode, "balanceSheetHistoryQuarterly", BalanceSheetHistoryQuarterly::fromJson);
		parseAndAddModule(modules, Module.CASH_FLOW_STATEMENT_HISTORY, resultNode, "cashFlowStatementHistory", CashflowStatementHistory::fromJson);
		parseAndAddModule(modules, Module.CASH_FLOW_STATEMENT_HISTORY_QUARTERLY, resultNode, "cashFlowStatementHistoryQuarterly", CashflowStatementHistoryQuarterly::fromJson);

		// Ownership and transactions
		parseAndAddModule(modules, Module.INSTITUTION_OWNERSHIP, resultNode, "institutionOwnership", InstitutionOwnership::fromJson);
		parseAndAddModule(modules, Module.FUND_OWNERSHIP, resultNode, "fundOwnership", FundOwnership::fromJson);
		parseAndAddModule(modules, Module.MAJOR_DIRECT_HOLDERS, resultNode, "majorDirectHolders", MajorDirectHolders::fromJson);
		parseAndAddModule(modules, Module.MAJOR_HOLDERS_BREAKDOWN, resultNode, "majorHoldersBreakdown", MajorHoldersBreakdown::fromJson);
		parseAndAddModule(modules, Module.INSIDER_HOLDERS, resultNode, "insiderHolders", InsiderHolders::fromJson);
		parseAndAddModule(modules, Module.INSIDER_TRANSACTIONS, resultNode, "insiderTransactions", InsiderTransactions::fromJson);
		parseAndAddModule(modules, Module.NET_SHARE_PURCHASE_ACTIVITY, resultNode, "netSharePurchaseActivity", NetSharePurchaseActivity::fromJson);

		// Additional modules
		parseAndAddModule(modules, Module.SEC_FILINGS, resultNode, "secFilings", SecFilings::fromJson);
		parseAndAddModule(modules, Module.UPGRADE_DOWNGRADE_HISTORY, resultNode, "upgradeDowngradeHistory", UpgradeDowngradeHistory::fromJson);

		return modules;
	}

	/**
	 * Helper method to parse and add a module if it's requested and exists in the JSON
	 */
	private <T extends QuoteSummaryModule<T>> void parseAndAddModule(
			List<QuoteSummaryModule<?>> modules,
			Module moduleType,
			JsonNode resultNode,
			String jsonFieldName,
			QuoteSummaryModule<T> parser) {

		if (requestedModules.contains(moduleType)) {
			JsonNode moduleNode = resultNode.get(jsonFieldName);
			if (moduleNode != null && !moduleNode.isNull()) {
				try {
					T parsedModule = parser.parse(moduleNode);
					if (parsedModule != null) {
						modules.add(parsedModule);
						log.debug("Successfully parsed module: {}", moduleType.name());
					}
				} catch (Exception e) {
					log.warn("Failed to parse module: {} for symbol: {}", moduleType.name(), getSymbol(), e);
				}
			}
		}
	}
}
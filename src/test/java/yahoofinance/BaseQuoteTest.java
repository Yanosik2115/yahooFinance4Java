package yahoofinance;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.model.common.QuoteSummaryModule;
import yahoofinance.model.market.modules.*;
import yahoofinance.requests.QuoteSummaryRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Base test class with common setup and utilities (simplified)
 */
@Slf4j
//@ExtendWith(MockitoExtension.class)
public abstract class BaseQuoteTest {

	protected static final String[] TEST_SYMBOLS = {"AAPL", "MSFT", "GOOGL", "TSLA", "SPY"};
	protected static final String INVALID_SYMBOL = "INVALID_SYMBOL_XYZ";
	protected static final int DEFAULT_TIMEOUT_SECONDS = 30;

	@BeforeAll
	static void globalSetup() {
		log.info("Starting test setup - logging configured via logback-test.xml");
		assumeTrue(isConnected(), "Internet connection required for integration tests");
	}

	protected static boolean isConnected() {
		try {
			java.net.InetAddress.getByName("finance.yahoo.com").isReachable(5000);
			return true;
		} catch (Exception e) {
			log.warn("Internet connectivity check failed: {}", e.getMessage());
			return false;
		}
	}

	protected void assertValidQuoteSummary(StockQuoteSummary summary, QuoteSummaryRequest.Module[] modules) {
		log.debug("Validating quote summary with {} modules", modules.length);

		assertNotNull(summary, "Quote summary should not be null");
		assertNull(summary.getError(), "Should not have error: " + summary.getError());
		assertNotNull(summary.getModules(), "Modules should not be null");
		assertFalse(summary.getModules().isEmpty(), "Should have at least one module");

		for (QuoteSummaryRequest.Module module : modules) {
			assertValidQuoteModules(summary, module);
		}

		log.debug("Quote summary validation completed successfully");
	}

	protected void assertValidQuoteModules(StockQuoteSummary summary, QuoteSummaryRequest.Module module) {
		log.debug("Checking presence of module: {}", module.name());

		switch (module) {
			case ASSET_PROFILE -> assertModulePresent(summary, AssetProfile.class);
			case SUMMARY_DETAIL -> assertModulePresent(summary, SummaryDetail.class);
			case DEFAULT_KEY_STATISTICS -> assertModulePresent(summary, DefaultKeyStatistics.class);
			case QUOTE_TYPE -> assertModulePresent(summary, QuoteType.class);
			case FINANCIAL_DATA -> assertModulePresent(summary, FinancialData.class);
			case CALENDAR_EVENTS -> assertModulePresent(summary, CalendarEvents.class);
			case SUMMARY_PROFILE -> assertModulePresent(summary, SummaryProfile.class);
			case ESG_SCORES -> assertModulePresent(summary, EsgScores.class);
			case PRICE -> assertModulePresent(summary, Price.class);
			case EARNINGS -> assertModulePresent(summary, Earnings.class);
			case EARNINGS_HISTORY -> assertModulePresent(summary, EarningsHistory.class);
			case EARNINGS_TREND -> assertModulePresent(summary, EarningsTrend.class);
			case RECOMMENDATION_TREND -> assertModulePresent(summary, RecommendationTrend.class);
			case INDEX_TREND -> assertModulePresent(summary, IndexTrend.class);
			case INDUSTRY_TREND -> assertModulePresent(summary, IndustryTrend.class);
			case INCOME_STATEMENT_HISTORY -> assertModulePresent(summary, IncomeStatementHistory.class);
			case INCOME_STATEMENT_HISTORY_QUARTERLY -> assertModulePresent(summary, IncomeStatementHistoryQuarterly.class);
			case BALANCE_SHEET_HISTORY -> assertModulePresent(summary, BalanceSheetHistory.class);
			case BALANCE_SHEET_HISTORY_QUARTERLY -> assertModulePresent(summary, BalanceSheetHistoryQuarterly.class);
			case CASH_FLOW_STATEMENT_HISTORY -> assertModulePresent(summary, CashflowStatementHistory.class);
			case CASH_FLOW_STATEMENT_HISTORY_QUARTERLY -> assertModulePresent(summary, CashflowStatementHistoryQuarterly.class);
			case INSTITUTION_OWNERSHIP -> assertModulePresent(summary, InstitutionOwnership.class);
			case FUND_OWNERSHIP -> assertModulePresent(summary, FundOwnership.class);
			case MAJOR_DIRECT_HOLDERS -> assertModulePresent(summary, MajorDirectHolders.class);
			case MAJOR_HOLDERS_BREAKDOWN -> assertModulePresent(summary, MajorHoldersBreakdown.class);
			case INSIDER_HOLDERS -> assertModulePresent(summary, InsiderHolders.class);
			case INSIDER_TRANSACTIONS -> assertModulePresent(summary, InsiderTransactions.class);
			case NET_SHARE_PURCHASE_ACTIVITY -> assertModulePresent(summary, NetSharePurchaseActivity.class);
			case SEC_FILINGS -> assertModulePresent(summary, SecFilings.class);
			case UPGRADE_DOWNGRADE_HISTORY -> assertModulePresent(summary, UpgradeDowngradeHistory.class);
			case SECTOR_TREND, FUTURES_CHAIN, FUND_PROFILE -> {
				log.debug("Skipping optional module validation for: {}", module.name());
			}
			default -> log.warn("Unknown module type: {}", module.name());
		}
	}

	protected <T extends QuoteSummaryModule<T>> void assertModulePresent(
			StockQuoteSummary summary, Class<T> moduleClass) {

		boolean hasModule = summary.hasModule(moduleClass);
		boolean isPresent = summary.getModule(moduleClass).isPresent();
		assertTrue(hasModule, "Should contain " + moduleClass.getSimpleName() + " module");
		assertTrue(isPresent, "Module should be present: " + moduleClass.getSimpleName());
	}
}
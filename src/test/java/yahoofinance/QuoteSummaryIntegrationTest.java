package yahoofinance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import yahoofinance.exception.YFinanceException;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.model.market.modules.*;
import yahoofinance.quotes.QuoteSummaryRequest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static yahoofinance.YFinance.getStockQuoteSummary;

@DisplayName("Quote Summary Integration Tests")
class QuoteSummaryIntegrationTest extends BaseQuoteTest {

	private static final QuoteSummaryRequest.Module[] DEFAULT_MODULES_ARRAY = QuoteSummaryRequest.DEFAULT_MODULES.toArray(new QuoteSummaryRequest.Module[0]);

	@Test
	@DisplayName("Should fetch basic quote summary for valid symbol")
	void testBasicQuoteSummaryFetch() throws YFinanceException {
		StockQuoteSummary summary = getStockQuoteSummary("AAPL");

		assertValidQuoteSummary(summary, DEFAULT_MODULES_ARRAY);

		assertModulePresent(summary, FinancialData.class);
		assertModulePresent(summary, QuoteType.class);
		assertModulePresent(summary, DefaultKeyStatistics.class);
		assertModulePresent(summary, AssetProfile.class);
		assertModulePresent(summary, SummaryDetail.class);
	}

	@Test
	@DisplayName("Should fetch quote summary with specific modules")
	void testSpecificModulesFetch() throws YFinanceException {
		QuoteSummaryRequest.Module[] modules = new QuoteSummaryRequest.Module[]{QuoteSummaryRequest.Module.SUMMARY_PROFILE, QuoteSummaryRequest.Module.INDEX_TREND, QuoteSummaryRequest.Module.INCOME_STATEMENT_HISTORY};
		StockQuoteSummary summary = getStockQuoteSummary("MSFT", modules);

		assertValidQuoteSummary(summary, modules);
		assertTrue(summary.getModules().size() <= 5,
				"Should not have significantly more modules than requested");
	}

	@ParameterizedTest
	@ValueSource(strings = {"AAPL", "MSFT", "GOOGL", "AMZN", "TSLA"})
	@DisplayName("Should handle multiple popular stocks")
	void testMultipleValidSymbols(String symbol) throws YFinanceException {
		StockQuoteSummary summary = getStockQuoteSummary(symbol);
		assertValidQuoteSummary(summary, DEFAULT_MODULES_ARRAY);
	}

	@Test
	@DisplayName("Should handle invalid symbol gracefully")
	void testInvalidSymbol() {
		assertThrows(YFinanceException.class, () -> {
			getStockQuoteSummary(INVALID_SYMBOL);
		});
	}

	@Test
	@DisplayName("Should fetch all available modules")
	void testAllModulesFetch() throws YFinanceException {
		StockQuoteSummary summary = getStockQuoteSummary("AAPL", QuoteSummaryRequest.Module.values());
		assertValidQuoteSummary(summary, QuoteSummaryRequest.Module.values());
		assertTrue(summary.getModules().size() >= 10,
				"Should have many modules when requesting all");
	}

	@RepeatedTest(3)
	@DisplayName("Should be consistent across multiple calls")
	void testConsistency() throws YFinanceException {
		StockQuoteSummary summary1 = getStockQuoteSummary("AAPL");
		StockQuoteSummary summary2 = getStockQuoteSummary("AAPL");

		assertValidQuoteSummary(summary1, DEFAULT_MODULES_ARRAY);
		assertValidQuoteSummary(summary2, DEFAULT_MODULES_ARRAY);

		assertEquals(summary1.getModules().size(), summary2.getModules().size());
	}

	@Test
	@Timeout(value = DEFAULT_TIMEOUT_SECONDS, unit = TimeUnit.SECONDS)
	@DisplayName("Should complete within timeout")
	void testTimeout() throws YFinanceException {
		StockQuoteSummary summary = getStockQuoteSummary("AAPL");
		assertValidQuoteSummary(summary, DEFAULT_MODULES_ARRAY);
	}
}
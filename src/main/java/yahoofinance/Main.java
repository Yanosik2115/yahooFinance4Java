package yahoofinance;

import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.Pricing;
import yahoofinance.model.financials.BalanceSheetSummary;
import yahoofinance.model.financials.CashFlowSummary;
import yahoofinance.model.financials.FinancialStatementSummary;
import yahoofinance.model.financials.IncomeSummary;
import yahoofinance.model.market.RegionMarketSummary;
import yahoofinance.model.StockHistory;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.model.financials.enums.Financials;
import yahoofinance.model.market.Region;
import yahoofinance.model.financials.enums.TimescaleTranslation;
import yahoofinance.model.market.modules.CalendarEvents;
import yahoofinance.model.market.modules.FinancialData;
import yahoofinance.service.StockWebSocket;
import yahoofinance.model.market.modules.SummaryProfile;
import yahoofinance.quotes.QuoteSummaryRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Main {

	public static void main(String[] args) throws IOException {
		testNonDefaultModules();
//		testWss();
//		testStockHistory();
//		testRegionMargetSummary();
		testFinancialTimeSeries();
	}

	private static void testFinancialTimeSeries() throws IOException {
		IncomeSummary incomeSummary = YFinance.getStockIncomeSummary("AAPL", TimescaleTranslation.QUARTERLY);
		for (FinancialStatementSummary.FinancialDataPoint financialDataPoint : incomeSummary.getResult().getNetIncome()) {
			log.info(financialDataPoint.prettyPrint());
		}

		CashFlowSummary cashFlowSummary = YFinance.getStockCashFlowSummary("PLTR", TimescaleTranslation.TRAILING);
		for (FinancialStatementSummary.FinancialDataPoint financialDataPoint : cashFlowSummary.getResult().getCapitalExpenditure()) {
			log.info(financialDataPoint.prettyPrint());
		}

		BalanceSheetSummary balanceSheetSummary = YFinance.getStockBalanceSheetSummary("MSFT", TimescaleTranslation.YEARLY);
		for (FinancialStatementSummary.FinancialDataPoint financialDataPoint : balanceSheetSummary.getResult().getCashFinancial()) {
			log.info(financialDataPoint.prettyPrint());
		}

	}

	private static void testStockHistory() throws IOException {
		StockHistory stockHistory = YFinance.getStockHistory("ODFL");
		log.info(stockHistory.prettyPrintChart());
	}

	private static void testRegionMargetSummary() throws IOException {
		RegionMarketSummary regionMarketSummary = YFinance.getRegionMarketSummary(Region.EUROPE);
		log.info(regionMarketSummary.getMarketStatus().getFirst().getMessage());
		log.info(regionMarketSummary.getMarketSummaries().getFirst().getExchangeTimezoneName());
	}

	private static void testNonDefaultModules() throws IOException {
		StockQuoteSummary stockQuoteSummary = YFinance.getStockQuoteSummary("MSFT", QuoteSummaryRequest.Module.SUMMARY_PROFILE, QuoteSummaryRequest.Module.CALENDAR_EVENTS);
		Optional<SummaryProfile> summaryProfileOptional = stockQuoteSummary.getModule(SummaryProfile.class);
		summaryProfileOptional.ifPresent(summaryProfile -> log.info(summaryProfile.getAddress1()));
		Optional<CalendarEvents> calendarEventsOptional = stockQuoteSummary.getModule(CalendarEvents.class);
		calendarEventsOptional.ifPresent(calendarEvents -> log.info(calendarEvents.getEarnings().toString()));
	}

	private static void testWss() {
		StockWebSocket webSocket = YFinance.getStockWebSocket();
		CountDownLatch latch = new CountDownLatch(1);

		try {
			webSocket.listen(pricingData -> {
				log.info("Received pricing data: Symbol={}, Price={}, Volume={}",
						pricingData.getId(),
						pricingData.getPrice(),
						pricingData.getDayVolume());

				processPricingData(pricingData);
			});

			log.info("Connecting to Yahoo Finance WebSocket...");
			webSocket.connect();

			Thread.sleep(2000);

			if (webSocket.isConnected()) {
				webSocket.subscribe(Arrays.asList("AAPL", "GOOGL", "MSFT", "TSLA"));
				log.info("Subscribed to tickers");
			} else {
				log.error("Failed to connect to WebSocket");
				return;
			}

			log.info("Listening for price updates...");

			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				log.info("Shutting down...");
				webSocket.close();
				latch.countDown();
			}));

			latch.await();

		} catch (Exception e) {
			log.error("Error in WebSocket example", e);
		} finally {
			webSocket.close();
		}
	}

	private static void processPricingData(Pricing.PricingData pricingData) {
		try {
			String symbol = pricingData.getId();
			double price = pricingData.getPrice();
			long volume = pricingData.getDayVolume();
			double changePercent = pricingData.getChangePercent();

			log.info("Stock Update - {}: {} {} Vol: {}",
					symbol, price, changePercent, volume);

		} catch (Exception e) {
			log.error("Error processing pricing data", e);
		}
	}
}

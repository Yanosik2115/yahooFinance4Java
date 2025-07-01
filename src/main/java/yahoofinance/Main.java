package yahoofinance;

import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.Pricing;
import yahoofinance.model.Stock;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.model.StockWebSocket;
import yahoofinance.model.modules.SummaryProfile;
import yahoofinance.quotes.QuoteSummaryRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Main {

	public static void main(String[] args) throws IOException {
//		testNonDefaultModules();
		testWss();
	}

	private static void testNonDefaultModules() throws IOException {
		Stock stock = YFinance.get("MSFT", QuoteSummaryRequest.Module.SUMMARY_PROFILE, QuoteSummaryRequest.Module.CALENDAR_EVENTS);
		StockQuoteSummary stockQuoteSummary = stock.getStockQuoteSummary();
		Optional<SummaryProfile> summaryProfileOptional = stockQuoteSummary.getModule(SummaryProfile.class);
		summaryProfileOptional.ifPresent(summaryProfile -> log.info(summaryProfile.getAddress1()));
	}

	private static void testWss() {
		StockWebSocket webSocket = new StockWebSocket();
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
		// Example processing - customize based on your needs
		try {
			String symbol = pricingData.getId();
			double price = pricingData.getPrice();
			long volume = pricingData.getDayVolume();
			double changePercent = pricingData.getChangePercent();

			log.info("Stock Update - {}: {} {} Vol: {}",
					symbol, price, changePercent, volume);

			// Add your custom logic here:
			// - Store in database
			// - Trigger alerts
			// - Update UI
			// - Send notifications
			// etc.

		} catch (Exception e) {
			log.error("Error processing pricing data", e);
		}
	}
}

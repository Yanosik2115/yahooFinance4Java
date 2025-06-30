package yahoofinance;

import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.Stock;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.model.modules.SummaryProfile;
import yahoofinance.quotes.QuoteSummaryRequest;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class Main {

	public static void main(String[] args) throws IOException {
//		testNonDefaultModules();
		testWss();
	}

	private static void testNonDefaultModules() throws IOException {
		Stock stock = YFinance.get("MSFT", QuoteSummaryRequest.Module.SUMMARY_PROFILE, QuoteSummaryRequest.Module.CALENDAR_EVENTS);
		StockQuoteSummary result = stock.getStockQuoteSummary();
		Optional<SummaryProfile> summaryProfileOptional = result.getModule(SummaryProfile.class);
		summaryProfileOptional.ifPresent(summaryProfile -> log.info(summaryProfile.getAddress1()));
	}

	private static void testWss() throws IOException {
		YFinance.get("AAPL").getWebSocketConnection(quote -> {
			log.info("Real-time update: {}", quote);
			log.info("MSFT: {} {}",
					quote.getPrice(), quote.getChangePercent());
		});
	}
}

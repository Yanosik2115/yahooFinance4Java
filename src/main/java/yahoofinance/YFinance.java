package yahoofinance;


import org.jetbrains.annotations.Nullable;
import yahoofinance.model.StockHistory;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.quotes.StockHistoryRequest;
import yahoofinance.service.StockWebSocket;
import yahoofinance.quotes.QuoteRequest;
import yahoofinance.quotes.QuoteSummaryRequest;

import java.io.IOException;

public class YFinance {


	public static StockQuoteSummary getStockQuoteSummary(String symbol) throws IOException {
		QuoteRequest<StockQuoteSummary> request = new QuoteSummaryRequest(symbol);
		return request.execute();
	}

	public static StockQuoteSummary getStockQuoteSummary(String symbol, QuoteSummaryRequest.Module... modules) throws IOException {
		QuoteRequest<StockQuoteSummary> request = new QuoteSummaryRequest(symbol, modules);
		return request.execute();
	}

	public static StockWebSocket getStockWebSocket() {
		return new StockWebSocket();
	}

	public static StockHistory getStockHistory(String symbol, StockHistoryRequest.ValidRanges range, StockHistoryRequest.ValidIntervals interval) throws IOException {
		QuoteRequest<StockHistory> request = new StockHistoryRequest(symbol, range, interval);
		return request.execute();
	}

	public static StockHistory getStockHistory(String symbol) throws IOException {
		QuoteRequest<StockHistory> request = new StockHistoryRequest(symbol);
		return request.execute();
	}
}

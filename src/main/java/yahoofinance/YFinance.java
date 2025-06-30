package yahoofinance;


import yahoofinance.model.Stock;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.quotes.QuoteRequest;
import yahoofinance.quotes.QuoteSummaryRequest;

import java.io.IOException;

public class YFinance {


	public static Stock get(String symbol) throws IOException {
		QuoteRequest<StockQuoteSummary> request = new QuoteSummaryRequest(symbol);
		Stock stock = new Stock(symbol);
		stock.setStockQuoteSummary(request.execute());
		return stock;
	}
}

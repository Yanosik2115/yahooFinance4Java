package yahoofinance;


import yahoofinance.model.Stock;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.quotes.QuoteRequest;
import yahoofinance.quotes.QuoteSummaryRequest;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class YFinance {


	public static Stock get(String symbol) throws IOException {
		Stock stock = new Stock();
		QuoteRequest<StockQuoteSummary> request = new QuoteSummaryRequest(symbol);
		stock.setStockQuoteSummary(request.execute());
		return stock;
	}


}

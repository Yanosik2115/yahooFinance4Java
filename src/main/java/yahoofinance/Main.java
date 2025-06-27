package yahoofinance;

import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.Stock;

import java.io.IOException;

@Slf4j
public class Main {

	public static void main(String[] args) throws IOException {
		Stock stock = YFinance.get("MSFT");
		log.info(stock.getStockQuoteSummary().getResult().getFirst().getSummaryDetail().getAverageVolume().toString());
		Stock sto1 = YFinance.get("AAPL");
		log.info(sto1.getStockQuoteSummary().getResult().getFirst().getFinancialData().getRecommendationKey());
	}
}

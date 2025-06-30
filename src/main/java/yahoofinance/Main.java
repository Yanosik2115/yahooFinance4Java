package yahoofinance;

import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.Stock;

import javax.websocket.DeploymentException;
import java.io.IOException;

@Slf4j
public class Main {

	public static void main(String[] args) throws IOException, DeploymentException {
		Stock stock = YFinance.get("BTC-USD");
		log.info(stock.getStockQuoteSummary().getResult().getFirst().getSummaryDetail().getAverageVolume().toString());
		Stock sto1 = YFinance.get("AAPL");
		log.info(sto1.getStockQuoteSummary().getResult().getFirst().getFinancialData().getRecommendationKey());

		sto1.getWebSocketConnection(quote -> {
			log.info("Real-time update: {}", quote);
			log.info("MSFT: {} {}",
					quote.getPrice(), quote.getChangePercent());
		});


	}
}

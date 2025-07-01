package yahoofinance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class Stock {
	@Getter
	private final String ticker;
	@Getter
	@Setter
	private StockQuoteSummary stockQuoteSummary;
}

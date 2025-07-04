package yahoofinance.quotes;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.financials.*;
import yahoofinance.model.financials.enums.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static yahoofinance.model.financials.parser.FinancialStatementParser.*;

@Slf4j
public class FinancialsTimeSeriesRequest extends QuoteRequest<FinancialStatementSummary<? extends FinancialStatementSummary.TimeSeriesResult>> {

	private final TimescaleTranslation timescaleTranslation;
	private final Financials financials;
	private static final String FINANCIALS_ULR = "https://query2.finance.yahoo.com/ws/fundamentals-timeseries/v1/finance/timeseries";
	private static final Instant startTimestamp = LocalDateTime.of(2016, 12, 31, 0, 0).toInstant(ZoneOffset.UTC);
	private final Instant endTimestamp = Instant.now();

	public FinancialsTimeSeriesRequest(String symbol, TimescaleTranslation timescaleTranslation, Financials financials) {
		super(symbol);
		this.financials = financials;
		this.timescaleTranslation = timescaleTranslation;
		if (timescaleTranslation == TimescaleTranslation.TRAILING && financials == Financials.BALANCE_SHEET) {
			throw new IllegalArgumentException("Illegal argument: frequency 'trailing' only available for cash-flow or income data.");
		}
	}

	@Override
	protected boolean useCookieAndCrumb() {
		return false;
	}

	@Override
	public String getURL() {
		return FINANCIALS_ULR;
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", getSymbol());
		params.put("period1", String.valueOf(startTimestamp.getEpochSecond()));
		params.put("period2", String.valueOf(endTimestamp.getEpochSecond()));
		params.put("type", getTypeParam());
		return params;
	}

	@Override
	public FinancialStatementSummary<? extends FinancialStatementSummary.TimeSeriesResult> parseJson(JsonNode node) {
		return switch (financials) {
			case INCOME -> parseIncomeSummary(node, timescaleTranslation);
			case BALANCE_SHEET -> parseBalanceSheetSummary(node, timescaleTranslation);
			case CASH_FLOW -> parseCashFlowSummary(node, timescaleTranslation);
		};
	}

	private String getTypeParam() {
		Class<?> declaringClass = this.financials.getFinancialClass();
		String timescale = timescaleTranslation.getValue();

		if (declaringClass == CashFlowKey.class) {
			return Arrays.stream(CashFlowKey.values())
					.map(value -> timescale + value.getValue())
					.collect(Collectors.joining(","));
		} else if (declaringClass == BalanceSheetKey.class) {
			return Arrays.stream(BalanceSheetKey.values())
					.map(value -> timescale + value.getValue())
					.collect(Collectors.joining(","));
		} else if (declaringClass == FinancialsKey.class) {
			return Arrays.stream(FinancialsKey.values())
					.map(value -> timescale + value.getValue())
					.collect(Collectors.joining(","));
		} else {
			throw new IllegalArgumentException("Unknown financial type: " + declaringClass);
		}
	}

}

package yahoofinance;


import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.*;
import yahoofinance.model.financials.BalanceSheetSummary;
import yahoofinance.model.financials.CashFlowSummary;
import yahoofinance.model.financials.IncomeSummary;
import yahoofinance.model.financials.enums.Financials;
import yahoofinance.model.market.Region;
import yahoofinance.model.financials.enums.TimescaleTranslation;
import yahoofinance.model.market.RegionMarketSummary;
import yahoofinance.quotes.*;
import yahoofinance.service.StockWebSocket;

import java.io.IOException;
import java.util.List;

@Slf4j
public class YFinance {

	private YFinance() {
	}

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

	public static RegionMarketSummary getRegionMarketSummary(Region region) throws IOException {
		RegionMarketSummary regionMarketSummary = new RegionMarketSummary();
		QuoteRequest<List<RegionMarketSummary.MarketSummary>> summaryRequest = new MarketSummaryRequest(region);
		List<RegionMarketSummary.MarketSummary> summaries = summaryRequest.execute();

		QuoteRequest<List<RegionMarketSummary.MarketStatus>> statusRequest = new MarketStatusRequest(region);
		List<RegionMarketSummary.MarketStatus> regionMarketStatus = statusRequest.execute();

		regionMarketSummary.setRegion(region);
		regionMarketSummary.setMarketSummaries(summaries);
		regionMarketSummary.setMarketStatus(regionMarketStatus);

		return regionMarketSummary;
	}

	public static IncomeSummary getStockIncomeSummary(String symbol, TimescaleTranslation timescaleTranslation) throws IOException {
		FinancialsTimeSeriesRequest request = new FinancialsTimeSeriesRequest(symbol, timescaleTranslation, Financials.INCOME);
		return (IncomeSummary) request.execute();
	}

	public static CashFlowSummary getStockCashFlowSummary(String symbol, TimescaleTranslation timescaleTranslation) throws IOException {
		FinancialsTimeSeriesRequest request = new FinancialsTimeSeriesRequest(symbol, timescaleTranslation, Financials.CASH_FLOW);
		return (CashFlowSummary) request.execute();
	}

	public static BalanceSheetSummary getStockBalanceSheetSummary(String symbol, TimescaleTranslation timescaleTranslation) throws IOException {
		FinancialsTimeSeriesRequest request = new FinancialsTimeSeriesRequest(symbol, timescaleTranslation, Financials.BALANCE_SHEET);
		return (BalanceSheetSummary) request.execute();
	}

}

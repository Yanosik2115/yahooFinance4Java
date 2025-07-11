package yahoofinance;


import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import yahoofinance.exception.YFinanceException;
import yahoofinance.model.*;
import yahoofinance.model.financials.BalanceSheetSummary;
import yahoofinance.model.financials.CashFlowSummary;
import yahoofinance.model.financials.IncomeSummary;
import yahoofinance.model.financials.enums.Financials;
import yahoofinance.model.lookup.LookupResult;
import yahoofinance.model.market.Region;
import yahoofinance.model.financials.enums.TimescaleTranslation;
import yahoofinance.model.market.RegionMarketSummary;
import yahoofinance.requests.*;
import yahoofinance.service.StockWebSocket;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class YFinance {

    private YFinance() {
    }

    /**
     * This method return StockQuoteSummary object which contains default modules:
     * <ul>
     *      <li>Module.FINANCIAL_DATA</li>
     *      <li>Module.QUOTE_TYPE</li>
     *      <li>Module.DEFAULT_KEY_STATISTICS</li>
     *      <li>Module.ASSET_PROFILE</li>
     *      <li>Module.SUMMARY_DETAIL</li>
     * </ul>
     *
     * @param ticker valid ticker for a stock, ex. "AAPL"
     * @return StockQuoteSummary object with selected or default modules
     * @throws YFinanceException standard exception
     */
    public static StockQuoteSummary getStockQuoteSummary(String ticker) throws YFinanceException {
        QuoteRequest<StockQuoteSummary> request = new QuoteSummaryRequest(ticker);
        return request.execute();
    }

    /**
     * This method return StockQuoteSummary object which contains user specified modules
     *
     * @param ticker  Valid ticker for a stock, ex. "AAPL"
     * @param modules Array of modules which should be included in the response object
     * @return StockQuoteSummary object with selected or default modules
     * @throws YFinanceException standard exception
     */
    public static StockQuoteSummary getStockQuoteSummary(String ticker, QuoteSummaryRequest.Module... modules) throws YFinanceException {
        QuoteRequest<StockQuoteSummary> request = new QuoteSummaryRequest(ticker, modules);
        return request.execute();
    }

    /**
     * @return New instance of StockWebSocket object
     */
    @Contract(" -> new")
    public static @NotNull StockWebSocket getStockWebSocket() {
        return new StockWebSocket();
    }


    /**
     * <p>This method returns history stock quotes, meta and timestamps for a specified range and in specified interval
     * * Start date in this case is range value subtracted from today date. </p>
     * For example for range <code>StockHistoryRequest.ValidRanges.ONE_MONTH</code> and interval <code>StockHistoryRequest.ValidIntervals.ONE_DAY</code>
     * this method will return series of data from one month back until today for each day
     *
     * @param ticker   Valid ticker for a stock, ex. "AAPL"
     * @param range    StockHistoryRequest.ValidRanges
     * @param interval StockHistoryRequest.ValidIntervals
     * @return StockHistory object
     * @throws YFinanceException standard exception
     */
    public static StockHistory getStockHistory(String ticker, StockHistoryRequest.ValidRanges range, StockHistoryRequest.ValidIntervals interval) throws YFinanceException {
        QuoteRequest<StockHistory> request = new StockHistoryRequest(ticker, range, interval);
        return request.execute();
    }

    public static StockHistory getStockHistory(String ticker, LocalDateTime startDate, LocalDateTime endDate, StockHistoryRequest.ValidIntervals interval) throws YFinanceException {
        QuoteRequest<StockHistory> request = new StockHistoryRequest(ticker, startDate, endDate, interval);
        return request.execute();
    }

    public static StockHistory getStockHistory(String ticker, LocalDateTime startDate, StockHistoryRequest.ValidRanges ranges, StockHistoryRequest.ValidIntervals interval) throws YFinanceException {
        QuoteRequest<StockHistory> request = new StockHistoryRequest(ticker, startDate, ranges, interval);
        return request.execute();
    }

    public static StockHistory getStockHistory(String ticker, StockHistoryRequest.ValidRanges ranges, LocalDateTime endDate, StockHistoryRequest.ValidIntervals interval) throws YFinanceException {
        QuoteRequest<StockHistory> request = new StockHistoryRequest(ticker, ranges, endDate, interval);
        return request.execute();
    }

    /**
     * <p>This method returns history stock quotes, meta and timestamps for a default range ONE_MONTH and in default interval ONE_DAY
     * Start date in this case is range value subtracted from today date. </p>
     * This method will return series of data from one month back until today for each day
     *
     * @param ticker Valid ticker for a stock, ex. "AAPL"
     * @return StockHistory object
     * @throws YFinanceException standard exception
     */
    public static StockHistory getStockHistory(String ticker) throws YFinanceException {
        QuoteRequest<StockHistory> request = new StockHistoryRequest(ticker);
        return request.execute();
    }

    /**
     * Returns region market summary including market summaries and status.
     *
     * @param region Region to get market data for
     * @return RegionMarketSummary object with market summaries and status
     * @throws YFinanceException standard exception
     */
    public static RegionMarketSummary getRegionMarketSummary(Region region) throws YFinanceException {
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

    /**
     * Returns income statement summary for specified symbol and timescale.
     *
     * @param symbol               Valid stock symbol, ex. "AAPL"
     * @param timescaleTranslation Timescale for the data (annual/quarterly)
     * @return IncomeSummary object with income statement data
     * @throws YFinanceException standard exception
     */
    public static IncomeSummary getStockIncomeSummary(String symbol, TimescaleTranslation timescaleTranslation) throws YFinanceException {
        FinancialsTimeSeriesRequest request = new FinancialsTimeSeriesRequest(symbol, timescaleTranslation, Financials.INCOME);
        return (IncomeSummary) request.execute();
    }

    /**
     * Returns cash flow summary for specified symbol and timescale.
     *
     * @param symbol               Valid stock symbol, ex. "AAPL"
     * @param timescaleTranslation Timescale for the data (annual/quarterly)
     * @return CashFlowSummary object with cash flow data
     * @throws YFinanceException standard exception
     */
    public static CashFlowSummary getStockCashFlowSummary(String symbol, TimescaleTranslation timescaleTranslation) throws YFinanceException {
        FinancialsTimeSeriesRequest request = new FinancialsTimeSeriesRequest(symbol, timescaleTranslation, Financials.CASH_FLOW);
        return (CashFlowSummary) request.execute();
    }

    /**
     * Returns balance sheet summary for specified symbol and timescale.
     *
     * @param symbol               Valid stock symbol, ex. "AAPL"
     * @param timescaleTranslation Timescale for the data (annual/quarterly)
     * @return BalanceSheetSummary object with balance sheet data
     * @throws YFinanceException standard exception
     */
    public static BalanceSheetSummary getStockBalanceSheetSummary(String symbol, TimescaleTranslation timescaleTranslation) throws YFinanceException {
        FinancialsTimeSeriesRequest request = new FinancialsTimeSeriesRequest(symbol, timescaleTranslation, Financials.BALANCE_SHEET);
        return (BalanceSheetSummary) request.execute();
    }

    public static LookupResult lookupTicker(String ticker) throws YFinanceException {
        TickerLookupRequest request = new TickerLookupRequest(ticker, TickerLookupRequest.LOOKUP_TYPES.ALL);
        return request.execute();
    }

    public static LookupResult lookupTicker(String ticker, TickerLookupRequest.LOOKUP_TYPES lookupType) throws YFinanceException {
        TickerLookupRequest request = new TickerLookupRequest(ticker, lookupType);
        return request.execute();
    }

}

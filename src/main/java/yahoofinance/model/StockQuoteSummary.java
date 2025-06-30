package yahoofinance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Setter
@Getter
public class StockQuoteSummary {

	private List<Result> result;
	private String error;

	@Getter
	@Setter
	public static class Result {
		private AssetProfile assetProfile;
		private SummaryDetail summaryDetail;
		private DefaultKeyStatistics defaultKeyStatistics;
		private QuoteType quoteType;
		private FinancialData financialData;
	}

	@Getter
	@Setter
	public static class AssetProfile {
		private String address1;
		private String city;
		private String state;
		private String zip;
		private String country;
		private String phone;
		private String website;
		private String industry;
		private String industryKey;
		private String industryDisp;
		private String sector;
		private String sectorKey;
		private String sectorDisp;
		private String longBusinessSummary;
		private Long fullTimeEmployees;
		private List<CompanyOfficer> companyOfficers;
		private Integer auditRisk;
		private Integer boardRisk;
		private Integer compensationRisk;
		private Integer shareHolderRightsRisk;
		private Integer overallRisk;
		private Long governanceEpochDate;
		private Long compensationAsOfEpochDate;
		private String irWebsite;
		private Integer maxAge;


	}

	@Getter
	@Setter
	public static class CompanyOfficer {
		private Integer maxAge;
		private String name;
		private Integer age;
		private String title;
		private Integer yearBorn;
		private Integer fiscalYear;
		private FormattedValue totalPay;
		private FormattedValue exercisedValue;
		private FormattedValue unexercisedValue;

	}

	@Getter
	@Setter
	public static class SummaryDetail {
		private Integer maxAge;
		private FormattedValue priceHint;
		private FormattedValue previousClose;
		private FormattedValue open;
		private FormattedValue dayLow;
		private FormattedValue dayHigh;
		private FormattedValue regularMarketPreviousClose;
		private FormattedValue regularMarketOpen;
		private FormattedValue regularMarketDayLow;
		private FormattedValue regularMarketDayHigh;
		private FormattedValue dividendRate;
		private FormattedValue dividendYield;
		private FormattedValue exDividendDate;
		private FormattedValue payoutRatio;
		private FormattedValue fiveYearAvgDividendYield;
		private FormattedValue beta;
		private FormattedValue trailingPE;
		private FormattedValue forwardPE;
		private FormattedValue volume;
		private FormattedValue regularMarketVolume;
		private FormattedValue averageVolume;
		private FormattedValue averageVolume10days;
		private FormattedValue averageDailyVolume10Day;
		private FormattedValue bid;
		private FormattedValue ask;
		private FormattedValue bidSize;
		private FormattedValue askSize;
		private FormattedValue marketCap;
		private FormattedValue fiftyTwoWeekLow;
		private FormattedValue fiftyTwoWeekHigh;
		private FormattedValue priceToSalesTrailing12Months;
		private FormattedValue fiftyDayAverage;
		private FormattedValue twoHundredDayAverage;
		private FormattedValue trailingAnnualDividendRate;
		private FormattedValue trailingAnnualDividendYield;
		private String currency;
		private Boolean tradeable;
	}


	@Getter
	@Setter
	public static class DefaultKeyStatistics {
		private Integer maxAge;
		private FormattedValue enterpriseValue;
		private FormattedValue forwardPE;
		private FormattedValue profitMargins;
		private FormattedValue floatShares;
		private FormattedValue sharesOutstanding;
		private FormattedValue sharesShort;
		private FormattedValue sharesShortPriorMonth;
		private FormattedValue beta;
		private FormattedValue bookValue;
		private FormattedValue priceToBook;
		private FormattedValue trailingEps;
		private FormattedValue forwardEps;
		private String lastSplitFactor;
		private FormattedValue lastSplitDate;
		private FormattedValue enterpriseToRevenue;
		private FormattedValue enterpriseToEbitda;
		private FormattedValue fiftyTwoWeekChange;
		private FormattedValue sandP52WeekChange;
		private FormattedValue lastDividendValue;
		private FormattedValue lastDividendDate;
	}

	@Getter
	@Setter
	public static class QuoteType {
		private String exchange;
		private String quoteType;
		private String symbol;
		private String underlyingSymbol;
		private String shortName;
		private String longName;
		private Long firstTradeDateEpochUtc;
		private String timeZoneFullName;
		private String timeZoneShortName;
		private String uuid;
		private String messageBoardId;
		private Long gmtOffSetMilliseconds;
		private Integer maxAge;
	}

	@Getter
	@Setter
	public static class FinancialData {
		private Integer maxAge;
		private FormattedValue currentPrice;
		private FormattedValue targetHighPrice;
		private FormattedValue targetLowPrice;
		private FormattedValue targetMeanPrice;
		private FormattedValue targetMedianPrice;
		private FormattedValue recommendationMean;
		private String recommendationKey;
		private FormattedValue numberOfAnalystOpinions;
		private FormattedValue totalCash;
		private FormattedValue totalCashPerShare;
		private FormattedValue ebitda;
		private FormattedValue totalDebt;
		private FormattedValue quickRatio;
		private FormattedValue currentRatio;
		private FormattedValue totalRevenue;
		private FormattedValue debtToEquity;
		private FormattedValue revenuePerShare;
		private FormattedValue returnOnAssets;
		private FormattedValue returnOnEquity;
		private FormattedValue grossProfits;
		private FormattedValue freeCashflow;
		private FormattedValue operatingCashflow;
		private FormattedValue earningsGrowth;
		private FormattedValue revenueGrowth;
		private FormattedValue grossMargins;
		private FormattedValue ebitdaMargins;
		private FormattedValue operatingMargins;
		private FormattedValue profitMargins;
		private String financialCurrency;
	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class FormattedValue {
		private BigDecimal raw;
		private String fmt;
		private String longFmt;

		@Override
		public String toString() {
			return fmt != null ? fmt : (raw != null ? raw.toString() : "null");
		}
	}
}
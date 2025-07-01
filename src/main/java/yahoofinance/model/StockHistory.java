package yahoofinance.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class StockHistory {

	private Meta meta;
	private List<Long> timestamp;
	private Indicators indicators;
	private String error;


	@Getter
	@Setter
	public static class Indicators {
		private List<Quote> quote;
	}

	@Getter
	@Setter
	public static class Quote {
		private List<Double> high;
		private List<Long> volume;
		private List<Double> open;
		private List<Double> close;
		private List<Double> low;
	}

	@Getter
	@Setter
	public static class Meta {
		private String currency;
		private String symbol;
		private String exchangeName;
		private String fullExchangeName;
		private String instrumentType;
		private Long firstTradeDate;
		private Long regularMarketTime;
		private Boolean hasPrePostMarketData;
		private Long gmtoffset;
		private String timezone;
		private String exchangeTimezoneName;
		private Double regularMarketPrice;
		private Double fiftyTwoWeekHigh;
		private Double fiftyTwoWeekLow;
		private Double regularMarketDayHigh;
		private Double regularMarketDayLow;
		private Long regularMarketVolume;
		private String longName;
		private String shortName;
		private Double chartPreviousClose;
		private Double previousClose;
		private Integer scale;
		private Integer priceHint;
		private CurrentTradingPeriod currentTradingPeriod;
		private List<List<TradingPeriodValue>> tradingPeriods;
		private String dataGranularity;
		private String range;
		private List<String> validRanges;
	}

	@Getter
	@Setter
	public static class CurrentTradingPeriod {
		private TradingPeriodValue pre;
		private TradingPeriodValue regular;
		private TradingPeriodValue post;
	}

	@Getter
	@Setter
	public static class TradingPeriodValue {
		private String timezone;
		private Long start;
		private Long end;
		private Long gmtoffset;
	}

	public String prettyPrintChart() {
		if (this.meta == null || this.timestamp == null || this.indicators == null ||
		    this.indicators.getQuote() == null || this.indicators.getQuote().isEmpty()) {
			return "No stock data available";
		}

		StringBuilder sb = new StringBuilder();
		Quote quote = this.indicators.getQuote().get(0); // Get first quote data

		// Header with stock info
		sb.append("═".repeat(100)).append("\n");
		sb.append(String.format("📈 %s (%s) - %s\n",
				this.meta.getLongName() != null ? this.meta.getLongName() : "N/A",
				this.meta.getSymbol() != null ? this.meta.getSymbol() : "N/A",
				this.meta.getExchangeName() != null ? this.meta.getExchangeName() : "N/A"));
		sb.append("═".repeat(100)).append("\n");

		// Current market info
		if (this.meta.getRegularMarketPrice() != null) {
			sb.append(String.format("Current Price: $%.2f %s | ",
					this.meta.getRegularMarketPrice(),
					this.meta.getCurrency() != null ? this.meta.getCurrency() : ""));
		}
		if (this.meta.getRegularMarketDayHigh() != null && this.meta.getRegularMarketDayLow() != null) {
			sb.append(String.format("Day Range: $%.2f - $%.2f | ",
					this.meta.getRegularMarketDayLow(), this.meta.getRegularMarketDayHigh()));
		}
		if (this.meta.getFiftyTwoWeekHigh() != null && this.meta.getFiftyTwoWeekLow() != null) {
			sb.append(String.format("52W Range: $%.2f - $%.2f",
					this.meta.getFiftyTwoWeekLow(), this.meta.getFiftyTwoWeekHigh()));
		}
		sb.append("\n");
		sb.append("─".repeat(100)).append("\n");

		// Table header
		sb.append(String.format("%-20s %-12s %-12s %-12s %-12s %-15s\n",
				"Time", "Open", "High", "Low", "Close", "Volume"));
		sb.append("─".repeat(100)).append("\n");

		int dataSize = Math.min(this.timestamp.size(),
				Math.min(quote.getOpen() != null ? quote.getOpen().size() : 0,
						Math.min(quote.getHigh() != null ? quote.getHigh().size() : 0,
								Math.min(quote.getLow() != null ? quote.getLow().size() : 0,
										Math.min(quote.getClose() != null ? quote.getClose().size() : 0,
												quote.getVolume() != null ? quote.getVolume().size() : 0)))));

		int startIndex = Math.max(0, dataSize - 20);

		for (int i = startIndex; i < dataSize; i++) {
			String timeStr = formatTimestamp(this.timestamp.get(i));

			String openStr = quote.getOpen() != null && i < quote.getOpen().size() && quote.getOpen().get(i) != null
					? String.format("$%.2f", quote.getOpen().get(i)) : "N/A";
			String highStr = quote.getHigh() != null && i < quote.getHigh().size() && quote.getHigh().get(i) != null
					? String.format("$%.2f", quote.getHigh().get(i)) : "N/A";
			String lowStr = quote.getLow() != null && i < quote.getLow().size() && quote.getLow().get(i) != null
					? String.format("$%.2f", quote.getLow().get(i)) : "N/A";
			String closeStr = quote.getClose() != null && i < quote.getClose().size() && quote.getClose().get(i) != null
					? String.format("$%.2f", quote.getClose().get(i)) : "N/A";
			String volumeStr = quote.getVolume() != null && i < quote.getVolume().size() && quote.getVolume().get(i) != null
					? formatVolume(quote.getVolume().get(i)) : "N/A";

			sb.append(String.format("%-20s %-12s %-12s %-12s %-12s %-15s\n",
					timeStr, openStr, highStr, lowStr, closeStr, volumeStr));
		}

		sb.append("─".repeat(100)).append("\n");
		sb.append(String.format("Showing %d of %d data points | Range: %s | Granularity: %s\n",
				Math.min(20, dataSize), dataSize,
				this.meta.getRange() != null ? this.meta.getRange() : "N/A",
				this.meta.getDataGranularity() != null ? this.meta.getDataGranularity() : "N/A"));
		sb.append("═".repeat(100));

		return sb.toString();
	}

	public String prettyPrintSummary() {
		if (this.meta == null) {
			return "No stock data available";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("┌─ ").append(this.meta.getSymbol() != null ? this.meta.getSymbol() : "N/A").append(" Summary ─┐\n");
		sb.append("│ Company: ").append(this.meta.getLongName() != null ? this.meta.getLongName() : "N/A").append("\n");

		if (this.meta.getRegularMarketPrice() != null) {
			sb.append("│ Price: $").append(String.format("%.2f", this.meta.getRegularMarketPrice()))
					.append(" ").append(this.meta.getCurrency() != null ? this.meta.getCurrency() : "").append("\n");
		}

		if (this.meta.getPreviousClose() != null && this.meta.getRegularMarketPrice() != null) {
			double change = this.meta.getRegularMarketPrice() - this.meta.getPreviousClose();
			double changePercent = (change / this.meta.getPreviousClose()) * 100;
			String arrow = change >= 0 ? "📈" : "📉";
			sb.append("│ Change: ").append(arrow).append(" $").append(String.format("%.2f", change))
					.append(" (").append(String.format("%.2f", changePercent)).append("%)\n");
		}

		if (this.meta.getRegularMarketVolume() != null) {
			sb.append("│ Volume: ").append(formatVolume(this.meta.getRegularMarketVolume())).append("\n");
		}

		if (this.meta.getRegularMarketDayHigh() != null && this.meta.getRegularMarketDayLow() != null) {
			sb.append("│ Day Range: $").append(String.format("%.2f", this.meta.getRegularMarketDayLow()))
					.append(" - $").append(String.format("%.2f", this.meta.getRegularMarketDayHigh())).append("\n");
		}

		if (this.timestamp != null && !this.timestamp.isEmpty()) {
			sb.append("│ Data Points: ").append(this.timestamp.size()).append("\n");
		}

		sb.append("└").append("─".repeat(30)).append("┘");

		return sb.toString();
	}

	/**
	 * Formats a Unix timestamp to a readable date-time string
	 */
	private String formatTimestamp(Long timestamp) {
		if (timestamp == null) return "N/A";

		try {
			LocalDateTime dateTime = LocalDateTime.ofInstant(
					Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
			return dateTime.format(DateTimeFormatter.ofPattern("MMM dd HH:mm"));
		} catch (Exception e) {
			return "Invalid Date";
		}
	}

	/**
	 * Formats volume numbers in a human-readable format (K, M, B)
	 */
	private String formatVolume(Long volume) {
		if (volume == null) return "N/A";

		if (volume >= 1_000_000_000) {
			return String.format("%.1fB", volume / 1_000_000_000.0);
		} else if (volume >= 1_000_000) {
			return String.format("%.1fM", volume / 1_000_000.0);
		} else if (volume >= 1_000) {
			return String.format("%.1fK", volume / 1_000.0);
		} else {
			return volume.toString();
		}
	}
}
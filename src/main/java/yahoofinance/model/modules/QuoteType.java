package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;

@Getter
@Setter
public class QuoteType extends AbstractQuoteSummaryModule<QuoteType> {
	private Integer maxAge;

	// Exchange Information
	private String exchange;
	private String quoteType;

	// Symbol Information
	private String symbol;
	private String underlyingSymbol;
	private String shortName;
	private String longName;

	// Trading Information
	private Long firstTradeDateEpochUtc;

	// Time Zone Information
	private String timeZoneFullName;
	private String timeZoneShortName;
	private Long gmtOffSetMilliseconds;

	// Identifiers
	private String uuid;
	private String messageBoardId;

	@Override
	protected QuoteType parseInternal(JsonNode node) {
		QuoteType quoteType = new QuoteType();

		quoteType.setMaxAge(getIntegerValue(node, "maxAge"));

		quoteType.setExchange(getStringValue(node, "exchange"));
		quoteType.setQuoteType(getStringValue(node, "quoteType"));

		quoteType.setSymbol(getStringValue(node, "symbol"));
		quoteType.setUnderlyingSymbol(getStringValue(node, "underlyingSymbol"));
		quoteType.setShortName(getStringValue(node, "shortName"));
		quoteType.setLongName(getStringValue(node, "longName"));

		quoteType.setFirstTradeDateEpochUtc(getLongValue(node, "firstTradeDateEpochUtc"));

		quoteType.setTimeZoneFullName(getStringValue(node, "timeZoneFullName"));
		quoteType.setTimeZoneShortName(getStringValue(node, "timeZoneShortName"));
		quoteType.setGmtOffSetMilliseconds(getLongValue(node, "gmtOffSetMilliseconds"));

		quoteType.setUuid(getStringValue(node, "uuid"));
		quoteType.setMessageBoardId(getStringValue(node, "messageBoardId"));

		return quoteType;
	}

	public static QuoteType fromJson(JsonNode node) {
		return new QuoteType().parse(node);
	}
}
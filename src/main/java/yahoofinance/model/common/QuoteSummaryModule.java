package yahoofinance.model.common;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface QuoteSummaryModule<T extends QuoteSummaryModule<T>> {
	T parse(JsonNode node);
}
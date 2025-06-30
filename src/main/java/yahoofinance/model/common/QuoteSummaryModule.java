package yahoofinance.model.common;

import com.fasterxml.jackson.databind.JsonNode;

public interface QuoteSummaryModule<T extends QuoteSummaryModule<T>> {

	T parse(JsonNode node);

	static <T extends QuoteSummaryModule<T>> T parseStatic(JsonNode node, Class<T> clazz) {
		try {
			T instance = clazz.getDeclaredConstructor().newInstance();
			return instance.parse(node);
		} catch (Exception e) {
			return null;
		}
	}
}
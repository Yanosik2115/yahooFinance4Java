package yahoofinance.model.common;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractQuoteSummaryModule<T extends QuoteSummaryModule<T>> implements QuoteSummaryModule<T>{

	@Override
	public T parse(JsonNode node) {
		if (node == null || node.isEmpty()) {
			return null;
		}
		return parseInternal(node);
	}


	protected abstract T parseInternal(JsonNode node);

	protected <M extends QuoteSummaryModule<M>> List<M> parseModuleArray(JsonNode arrayNode, Class<M> moduleClass) {
		List<M> results = new ArrayList<>();

		if (arrayNode != null && arrayNode.isArray()) {
			for (JsonNode itemNode : arrayNode) {
				try {
					M instance = moduleClass.getDeclaredConstructor().newInstance();
					M parsed = instance.parse(itemNode);
					if (parsed != null) {
						results.add(parsed);
					}
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}

		return results;
	}
}

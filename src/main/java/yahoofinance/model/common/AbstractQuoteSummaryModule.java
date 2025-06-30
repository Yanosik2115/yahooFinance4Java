package yahoofinance.model.common;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractQuoteSummaryModule<T extends QuoteSummaryModule<T>> implements QuoteSummaryModule<T>{

	@Override
	public T parse(JsonNode node) {
		if (node == null || node.isEmpty()) {
			return null;
		}
		return parseInternal(node);
	}


	protected abstract T parseInternal(JsonNode node);

	protected String getStringValue(JsonNode node, String fieldName) {
		JsonNode fieldNode = node.get(fieldName);
		if (fieldNode != null && !fieldNode.isNull()) {
			return fieldNode.asText();
		}
		return null;
	}

	protected Integer getIntegerValue(JsonNode node, String fieldName) {
		JsonNode fieldNode = node.get(fieldName);
		if (fieldNode != null && !fieldNode.isNull() && fieldNode.isNumber()) {
			return fieldNode.asInt();
		}
		return null;
	}

	protected Long getLongValue(JsonNode node, String fieldName) {
		JsonNode fieldNode = node.get(fieldName);
		if (fieldNode != null && !fieldNode.isNull() && fieldNode.isNumber()) {
			return fieldNode.asLong();
		}
		return null;
	}

	protected Boolean getBooleanValue(JsonNode node, String fieldName) {
		JsonNode fieldNode = node.get(fieldName);
		if (fieldNode != null && !fieldNode.isNull() && fieldNode.isBoolean()) {
			return fieldNode.asBoolean();
		}
		return null;
	}

	protected Double getDoubleValue(JsonNode node, String fieldName) {
		if (node != null && node.has(fieldName) && !node.get(fieldName).isNull()) {
			return node.get(fieldName).asDouble();
		}
		return null;
	}

	protected FormattedValue parseFormattedValue(JsonNode node) {
		if (node == null || node.isNull()) {
			return null;
		}

		FormattedValue formattedValue = new FormattedValue();

		JsonNode rawNode = node.get("raw");
		if (rawNode != null && !rawNode.isNull() && rawNode.isNumber()) {
			formattedValue.setRaw(new BigDecimal(rawNode.asText()));
		}


		JsonNode fmtNode = node.get("fmt");
		if (fmtNode != null && !fmtNode.isNull()) {
			formattedValue.setFmt(fmtNode.asText());
		}

		JsonNode longFmtNode = node.get("longFmt");
		if (longFmtNode != null && !longFmtNode.isNull()) {
			formattedValue.setLongFmt(longFmtNode.asText());
		}

		return formattedValue;
	}

	protected List<FormattedValue> parseFormattedValueArray(JsonNode arrayNode) {
		if (arrayNode == null || !arrayNode.isArray()) {
			return new ArrayList<>();
		}

		List<FormattedValue> formattedValues = new ArrayList<>();
		for (JsonNode itemNode : arrayNode) {
			FormattedValue formattedValue = parseFormattedValue(itemNode);
			if (formattedValue != null) {
				formattedValues.add(formattedValue);
			}
		}
		return formattedValues;
	}

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
					// Log error or handle as needed
				}
			}
		}

		return results;
	}
}

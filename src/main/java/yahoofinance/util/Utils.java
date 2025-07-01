package yahoofinance.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.common.FormattedValue;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class Utils {

	private Utils() {
	}

	@Getter
	private static final Gson gson = new Gson();

	@Getter
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String getURLParameters(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (!sb.isEmpty()) {
				sb.append("&");
			}
			String key = entry.getKey();
			String value = entry.getValue();

			if (value.contains(",")) {
				String[] parts = value.split(",");
				StringBuilder sbValue = new StringBuilder();
				for (String part : parts) {
					if (!sbValue.isEmpty()) sbValue.append(",");
					part = URLEncoder.encode(part, StandardCharsets.UTF_8);
					sbValue.append(part);
				}
				value = sbValue.toString();
			} else {
				key = URLEncoder.encode(key, StandardCharsets.UTF_8);
				value = URLEncoder.encode(value, StandardCharsets.UTF_8);
			}

			sb.append(String.format("%s=%s", key, value));
		}
		return sb.toString();
	}

	public static String getStringValue(JsonNode node, String fieldName) {
		JsonNode fieldNode = node.get(fieldName);
		if (fieldNode != null && !fieldNode.isNull()) {
			return fieldNode.asText();
		}
		return null;
	}

	public static Integer getIntegerValue(JsonNode node, String fieldName) {
		JsonNode fieldNode = node.get(fieldName);
		if (fieldNode != null && !fieldNode.isNull() && fieldNode.isNumber()) {
			return fieldNode.asInt();
		}
		return null;
	}

	public static Long getLongValue(JsonNode node, String fieldName) {
		JsonNode fieldNode = node.get(fieldName);
		if (fieldNode != null && !fieldNode.isNull() && fieldNode.isNumber()) {
			return fieldNode.asLong();
		}
		return null;
	}

	public static Boolean getBooleanValue(JsonNode node, String fieldName) {
		JsonNode fieldNode = node.get(fieldName);
		if (fieldNode != null && !fieldNode.isNull() && fieldNode.isBoolean()) {
			return fieldNode.asBoolean();
		}
		return null;
	}

	public static Double getDoubleValue(JsonNode node, String fieldName) {
		if (node != null && node.has(fieldName) && !node.get(fieldName).isNull()) {
			return node.get(fieldName).asDouble();
		}
		return null;
	}

	public static FormattedValue parseFormattedValue(JsonNode node) {
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

	public static List<FormattedValue> parseFormattedValueArray(JsonNode arrayNode) {
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

	public static List<Double> parseDoubleArray(JsonNode arrayNode) {
		List<Double> values = new ArrayList<>();
		for (JsonNode valueNode : arrayNode) {
			if (valueNode.isNull()) {
				values.add(null);
			} else {
				values.add(valueNode.asDouble());
			}
		}
		return values;
	}
}

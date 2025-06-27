package yahoofinance.util;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class Utils {

	private Utils() {
	}

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
}

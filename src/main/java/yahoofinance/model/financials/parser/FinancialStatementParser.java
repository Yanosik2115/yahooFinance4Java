package yahoofinance.model.financials.parser;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.common.FormattedValue;
import yahoofinance.model.financials.BalanceSheetSummary;
import yahoofinance.model.financials.CashFlowSummary;
import yahoofinance.model.financials.FinancialStatementSummary;
import yahoofinance.model.financials.IncomeSummary;
import yahoofinance.model.financials.enums.BalanceSheetKey;
import yahoofinance.model.financials.enums.CashFlowKey;
import yahoofinance.model.financials.enums.FinancialsKey;
import yahoofinance.model.financials.enums.TimescaleTranslation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static yahoofinance.util.Utils.parseFormattedValue;

@Slf4j
public class FinancialStatementParser {

	private FinancialStatementParser() {
	}

	private static <T extends FinancialStatementSummary<R>, R extends FinancialStatementSummary.TimeSeriesResult>
	T parseFinancialStatement(JsonNode node, Supplier<T> summarySupplier,
	                          Supplier<R> resultSupplier, Enum<?>[] enumValues, TimescaleTranslation timescaleTranslation) {

		T summary = summarySupplier.get();
		R consolidatedResult = resultSupplier.get();

		List<Long> timestamps = getTimestamps(node);
		parseAndConsolidateData(node, consolidatedResult, enumValues, timescaleTranslation);

		summary.setResult(consolidatedResult);
		summary.setTimestamp(timestamps);

		return summary;
	}

	private static List<Long> getTimestamps(JsonNode resultNode){
		Iterator<JsonNode> it = resultNode.iterator();
		List<Long> timestamps = new ArrayList<>();
		while (it.hasNext()){
			JsonNode node = it.next();
			if (node.has("timestamp")) {
				for (JsonNode timestampNode : node.get("timestamp")) {
					timestamps.add(timestampNode.asLong());
				}
				break;
			}
		}
		return timestamps;
	}

	private static <R extends FinancialStatementSummary.TimeSeriesResult> void parseAndConsolidateData(
			JsonNode resultNode, R result, Enum<?>[] enumValues, TimescaleTranslation timescaleTranslation) {

		String timescale = timescaleTranslation.getValue();

		for (Enum<?> enumValue : enumValues) {
			try {
				String enumStringValue = getEnumValue(enumValue);
				String jsonFieldName = timescale + enumStringValue;

				for (Iterator<JsonNode> it = resultNode.elements(); it.hasNext(); ) {
					JsonNode node = it.next();
					if (node.has(jsonFieldName)) {
						JsonNode fieldNode = node.get(jsonFieldName);

						if (fieldNode.isArray()) {
							List<FinancialStatementSummary.FinancialDataPoint> dataPoints =
									parseFinancialDataPointArray(fieldNode);

							setFieldValue(result, enumStringValue, dataPoints);
							break;
						}
					}
				}
			} catch (Exception e) {
				log.error("Error processing field: {} - {}", enumValue, e.getMessage());
			}
		}
	}

	private static String getEnumValue(Enum<?> enumValue) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method getValueMethod = enumValue.getClass().getMethod("getValue");
		return (String) getValueMethod.invoke(enumValue);
	}

	private static final Map<String, Map<String, Field>> FIELD_CACHE = new ConcurrentHashMap<>();

	private static <T> void setFieldValue(T object, String fieldName, Object value) {
		try {
			String className = object.getClass().getName();
			Map<String, Field> classFields = FIELD_CACHE.computeIfAbsent(className, k -> new ConcurrentHashMap<>());

			Field field = classFields.computeIfAbsent(fieldName, fn -> {
				try {
					Field f = object.getClass().getDeclaredField(fn);
					f.setAccessible(true);
					return f;
				} catch (NoSuchFieldException e) {
					return null;
				}
			});

			if (field != null) {
				field.set(object, value);
			}
		} catch (IllegalAccessException e) {
			log.error("Could not set field: {} - {}", fieldName, e.getMessage());
		}
	}

	private static List<FinancialStatementSummary.FinancialDataPoint> parseFinancialDataPointArray(JsonNode arrayNode) {
		List<FinancialStatementSummary.FinancialDataPoint> dataPoints = new ArrayList<>();

		for (JsonNode dataPointNode : arrayNode) {
			FinancialStatementSummary.FinancialDataPoint dataPoint = parseFinancialDataPoint(dataPointNode);
			dataPoints.add(dataPoint);
		}

		return dataPoints;
	}

	private static FinancialStatementSummary.FinancialDataPoint parseFinancialDataPoint(JsonNode dataPointNode) {
		FinancialStatementSummary.FinancialDataPoint dataPoint = new FinancialStatementSummary.FinancialDataPoint();

		if (dataPointNode.has("dataId")) {
			dataPoint.setDataId(dataPointNode.get("dataId").asLong());
		}

		if (dataPointNode.has("asOfDate")) {
			dataPoint.setAsOfDate(dataPointNode.get("asOfDate").asText());
		}

		if (dataPointNode.has("periodType")) {
			dataPoint.setPeriodType(dataPointNode.get("periodType").asText());
		}

		if (dataPointNode.has("currencyCode")) {
			dataPoint.setCurrencyCode(dataPointNode.get("currencyCode").asText());
		}

		if (dataPointNode.has("reportedValue")) {
			JsonNode reportedValueNode = dataPointNode.get("reportedValue");
			FormattedValue formattedValue = parseFormattedValue(reportedValueNode);
			dataPoint.setReportedValue(formattedValue);
		}

		if (dataPointNode.has("geographicSegmentData")) {
			List<FinancialStatementSummary.SegmentData> geoSegmentData = parseSegmentDataArray(dataPointNode.get("geographicSegmentData"));
			dataPoint.setGeographicSegmentData(geoSegmentData);
		}

		if (dataPointNode.has("businessSegmentData")) {
			List<FinancialStatementSummary.SegmentData> bizSegmentData = parseSegmentDataArray(dataPointNode.get("businessSegmentData"));
			dataPoint.setBusinessSegmentData(bizSegmentData);
		}

		return dataPoint;
	}

	private static List<FinancialStatementSummary.SegmentData> parseSegmentDataArray(JsonNode segmentArrayNode) {
		List<FinancialStatementSummary.SegmentData> segmentDataList = new ArrayList<>();

		for (JsonNode segmentNode : segmentArrayNode) {
			FinancialStatementSummary.SegmentData segmentData = new FinancialStatementSummary.SegmentData();

			if (segmentNode.has("dataValue")) {
				segmentData.setDataValue(segmentNode.get("dataValue").asDouble());
			}

			if (segmentNode.has("segmentType")) {
				segmentData.setSegmentType(segmentNode.get("segmentType").asText());
			}

			if (segmentNode.has("segmentName")) {
				segmentData.setSegmentName(segmentNode.get("segmentName").asText());
			}

			if (segmentNode.has("isPrimarySegment")) {
				segmentData.setIsPrimarySegment(segmentNode.get("isPrimarySegment").asInt());
			}

			segmentDataList.add(segmentData);
		}

		return segmentDataList;
	}

	public static CashFlowSummary parseCashFlowSummary(JsonNode node, TimescaleTranslation timescaleTranslation) {
		return parseFinancialStatement(
				node,
				CashFlowSummary::new,
				CashFlowSummary.CashFlowTimeSeriesResult::new,
				CashFlowKey.values(),
				timescaleTranslation
		);
	}

	public static IncomeSummary parseIncomeSummary(JsonNode node, TimescaleTranslation timescaleTranslation) {
		return parseFinancialStatement(
				node,
				IncomeSummary::new,
				IncomeSummary.IncomeTimeSeriesResult::new,
				FinancialsKey.values(),
				timescaleTranslation
		);
	}

	public static BalanceSheetSummary parseBalanceSheetSummary(JsonNode node, TimescaleTranslation timescaleTranslation) {
		return parseFinancialStatement(
				node,
				BalanceSheetSummary::new,
				BalanceSheetSummary.BalanceSheetTimeSeriesResult::new,
				BalanceSheetKey.values(),
				timescaleTranslation
		);
	}
}

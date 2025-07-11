package yahoofinance.quotes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import yahoofinance.model.StockHistory;
import yahoofinance.requests.StockHistoryRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StockHistoryRequestTest {

	private static final String TEST_SYMBOL = "AAPL";
	private static final LocalDateTime TEST_START_DATE = LocalDateTime.of(2024, 1, 1, 0, 0);
	private static final LocalDateTime TEST_END_DATE = LocalDateTime.of(2024, 2, 1, 0, 0);

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Nested
	@DisplayName("Constructor Tests")
	class ConstructorTests {

		@Test
		@DisplayName("Should create request with range and interval")
		void shouldCreateRequestWithRangeAndInterval() {
			StockHistoryRequest request = new StockHistoryRequest(
					TEST_SYMBOL,
					StockHistoryRequest.ValidRanges.ONE_MONTH,
					StockHistoryRequest.ValidIntervals.ONE_DAY
			);

			assertNotNull(request);
		}

		@Test
		@DisplayName("Should create request with start and end date")
		void shouldCreateRequestWithStartAndEndDate() {
			StockHistoryRequest request = new StockHistoryRequest(
					TEST_SYMBOL,
					TEST_START_DATE,
					TEST_END_DATE,
					StockHistoryRequest.ValidIntervals.ONE_DAY
			);

			assertNotNull(request);
		}

		@Test
		@DisplayName("Should create request with start date and range")
		void shouldCreateRequestWithStartDateAndRange() {
			StockHistoryRequest request = new StockHistoryRequest(
					TEST_SYMBOL,
					TEST_START_DATE,
					StockHistoryRequest.ValidRanges.ONE_MONTH,
					StockHistoryRequest.ValidIntervals.ONE_DAY
			);

			assertNotNull(request);
		}

		@Test
		@DisplayName("Should create request with range and end date")
		void shouldCreateRequestWithRangeAndEndDate() {
			StockHistoryRequest request = new StockHistoryRequest(
					TEST_SYMBOL,
					StockHistoryRequest.ValidRanges.ONE_MONTH,
					TEST_END_DATE,
					StockHistoryRequest.ValidIntervals.ONE_DAY
			);

			assertNotNull(request);
		}

		@Test
		@DisplayName("Should create request with default values")
		void shouldCreateRequestWithDefaultValues() {
			StockHistoryRequest request = new StockHistoryRequest(TEST_SYMBOL);

			assertNotNull(request);
		}
	}

	@Nested
	@DisplayName("ValidRanges Enum Tests")
	class ValidRangesTests {

		@ParameterizedTest
		@EnumSource(StockHistoryRequest.ValidRanges.class)
		@DisplayName("Should return valid epoch time for all ranges")
		void shouldReturnValidEpochTimeForAllRanges(StockHistoryRequest.ValidRanges range) {
			long epochTime = range.getTimeMilisecond();

			if (range == StockHistoryRequest.ValidRanges.MAX) {
				assertEquals(0, epochTime);
			} else {
				assertTrue(epochTime > 0, "Epoch time should be positive for " + range);
				assertTrue(epochTime <= System.currentTimeMillis() / 1000,
						"Epoch time should not be in the future for " + range);
			}
		}

		@Test
		@DisplayName("Should return correct range string")
		void shouldReturnCorrectRangeString() {
			assertEquals("1d", StockHistoryRequest.ValidRanges.ONE_DAY.getRange());
			assertEquals("5d", StockHistoryRequest.ValidRanges.FIVE_DAYS.getRange());
			assertEquals("1mo", StockHistoryRequest.ValidRanges.ONE_MONTH.getRange());
			assertEquals("ytd", StockHistoryRequest.ValidRanges.YTD.getRange());
			assertEquals("max", StockHistoryRequest.ValidRanges.MAX.getRange());
		}
	}

	@Nested
	@DisplayName("ValidIntervals Enum Tests")
	class ValidIntervalsTests {

		@Test
		@DisplayName("Should return correct interval string")
		void shouldReturnCorrectIntervalString() {
			assertEquals("1m", StockHistoryRequest.ValidIntervals.ONE_MINUTE.getInterval());
			assertEquals("90m", StockHistoryRequest.ValidIntervals.NINETY_MINUTES.getInterval());
			assertEquals("1d", StockHistoryRequest.ValidIntervals.ONE_DAY.getInterval());
			assertEquals("1wk", StockHistoryRequest.ValidIntervals.ONE_WEEK.getInterval());
		}
	}

	@Nested
	@DisplayName("Parameter Generation Tests")
	class ParameterTests {

		@Test
		@DisplayName("Should generate correct parameters for range-based request")
		void shouldGenerateCorrectParametersForRangeBasedRequest() {
			StockHistoryRequest request = new StockHistoryRequest(
					TEST_SYMBOL,
					StockHistoryRequest.ValidRanges.ONE_MONTH,
					StockHistoryRequest.ValidIntervals.ONE_DAY
			);

			Map<String, String> params = request.getParams();

			assertEquals("1mo", params.get("range"));
			assertEquals("1d", params.get("interval"));
			assertNull(params.get("period1"));
			assertNull(params.get("period2"));
		}

		@Test
		@DisplayName("Should generate correct parameters for date range request")
		void shouldGenerateCorrectParametersForDateRangeRequest() {
			StockHistoryRequest request = new StockHistoryRequest(
					TEST_SYMBOL,
					TEST_START_DATE,
					TEST_END_DATE,
					StockHistoryRequest.ValidIntervals.ONE_DAY
			);

			Map<String, String> params = request.getParams();

			assertNull(params.get("range"));
			assertEquals("1d", params.get("interval"));
			assertEquals(String.valueOf(TEST_START_DATE.toEpochSecond(ZoneOffset.UTC)), params.get("period1"));
			assertEquals(String.valueOf(TEST_END_DATE.toEpochSecond(ZoneOffset.UTC)), params.get("period2"));
		}

		@Test
		@DisplayName("Should use default interval when interval is null")
		void shouldUseDefaultIntervalWhenIntervalIsNull() {
			// Create a request and manually set interval to null using reflection or subclass
			StockHistoryRequest request = new StockHistoryRequest(
					TEST_SYMBOL,
					StockHistoryRequest.ValidRanges.ONE_MONTH,
					null
			) {
				// Override to test null interval handling
			};

			Map<String, String> params = request.getParams();
			assertEquals("1d", params.get("interval")); // Should default to ONE_DAY
		}
	}

	@Nested
	@DisplayName("URL Tests")
	class URLTests {

		@Test
		@DisplayName("Should return correct URL")
		void shouldReturnCorrectURL() {
			StockHistoryRequest request = new StockHistoryRequest(TEST_SYMBOL);
			assertEquals("https://query2.finance.yahoo.com/v8/finance/chart", request.getURL());
		}
	}

	@Nested
	@DisplayName("JSON Parsing Tests")
	class JsonParsingTests {

		@Test
		@DisplayName("Should return null for null input")
		void shouldReturnNullForNullInput() {
			StockHistoryRequest request = new StockHistoryRequest(TEST_SYMBOL);
			assertNull(request.parseJson(null));
		}

		@Test
		@DisplayName("Should return null for empty JSON")
		void shouldReturnNullForEmptyJson() throws Exception {
			StockHistoryRequest request = new StockHistoryRequest(TEST_SYMBOL);
			JsonNode emptyNode = objectMapper.readTree("null");
			assertNull(request.parseJson(emptyNode));
		}

		@Test
		@DisplayName("Should parse valid JSON successfully")
		void shouldParseValidJsonSuccessfully() throws Exception {
			String validJson = """
                [{
                    "meta": {
                        "currency": "USD",
                        "symbol": "AAPL",
                        "exchangeName": "NMS",
                        "regularMarketPrice": 150.0
                    },
                    "timestamp": [1640995200, 1641081600],
                    "indicators": {
                        "quote": [{
                            "high": [155.0, 152.0],
                            "low": [148.0, 149.0],
                            "open": [150.0, 151.0],
                            "close": [154.0, 150.5],
                            "volume": [1000000, 1200000]
                        }]
                    }
                }]
                """;

			JsonNode jsonNode = objectMapper.readTree(validJson);
			StockHistoryRequest request = new StockHistoryRequest(TEST_SYMBOL);

			StockHistory result = request.parseJson(jsonNode);

			assertNotNull(result);
			assertNotNull(result.getMeta());
			assertEquals("USD", result.getMeta().getCurrency());
			assertEquals("AAPL", result.getMeta().getSymbol());
			assertEquals(2, result.getTimestamp().size());
			assertNotNull(result.getIndicators());
			assertEquals(1, result.getIndicators().getQuote().size());
		}

		@Test
		@DisplayName("Should handle malformed JSON gracefully")
		void shouldHandleMalformedJsonGracefully() throws Exception {
			String malformedJson = """
                [{
                    "meta": "invalid_structure",
                    "timestamp": "not_an_array"
                }]
                """;

			JsonNode jsonNode = objectMapper.readTree(malformedJson);
			StockHistoryRequest request = new StockHistoryRequest(TEST_SYMBOL);

			// Should not throw exception, might return null or partially parsed data
			assertDoesNotThrow(() -> request.parseJson(jsonNode));
		}
	}

	@Nested
	@DisplayName("YFinance Static Method Tests")
	class YFinanceStaticMethodTests {

		@Test
		@DisplayName("Should create correct request for range-based method")
		void shouldCreateCorrectRequestForRangeBasedMethod() {
			assertDoesNotThrow(() -> {
				StockHistoryRequest request = new StockHistoryRequest(
						TEST_SYMBOL,
						StockHistoryRequest.ValidRanges.ONE_MONTH,
						StockHistoryRequest.ValidIntervals.ONE_DAY
				);
				assertNotNull(request);
			});
		}

		@Test
		@DisplayName("Should create correct request for date range method")
		void shouldCreateCorrectRequestForDateRangeMethod() {
			assertDoesNotThrow(() -> {
				StockHistoryRequest request = new StockHistoryRequest(
						TEST_SYMBOL,
						TEST_START_DATE,
						TEST_END_DATE,
						StockHistoryRequest.ValidIntervals.ONE_DAY
				);
				assertNotNull(request);
			});
		}

		@Test
		@DisplayName("Should create correct request for start date with range method")
		void shouldCreateCorrectRequestForStartDateWithRangeMethod() {
			assertDoesNotThrow(() -> {
				StockHistoryRequest request = new StockHistoryRequest(
						TEST_SYMBOL,
						TEST_START_DATE,
						StockHistoryRequest.ValidRanges.ONE_MONTH,
						StockHistoryRequest.ValidIntervals.ONE_DAY
				);
				assertNotNull(request);
			});
		}

		@Test
		@DisplayName("Should create correct request for range with end date method")
		void shouldCreateCorrectRequestForRangeWithEndDateMethod() {
			assertDoesNotThrow(() -> {
				StockHistoryRequest request = new StockHistoryRequest(
						TEST_SYMBOL,
						StockHistoryRequest.ValidRanges.ONE_MONTH,
						TEST_END_DATE,
						StockHistoryRequest.ValidIntervals.ONE_DAY
				);
				assertNotNull(request);
			});
		}
	}

	@Nested
	@DisplayName("Edge Cases and Error Handling")
	class EdgeCasesTests {

		@Test
		@DisplayName("Should handle null symbol gracefully")
		void shouldHandleNullSymbolGracefully() {
			assertDoesNotThrow(() -> {
				new StockHistoryRequest(null,
						StockHistoryRequest.ValidRanges.ONE_MONTH,
						StockHistoryRequest.ValidIntervals.ONE_DAY);
			});
		}

		@Test
		@DisplayName("Should handle empty symbol gracefully")
		void shouldHandleEmptySymbolGracefully() {
			assertDoesNotThrow(() -> {
				new StockHistoryRequest("",
						StockHistoryRequest.ValidRanges.ONE_MONTH,
						StockHistoryRequest.ValidIntervals.ONE_DAY);
			});
		}

		@Test
		@DisplayName("Should handle null dates gracefully")
		void shouldHandleNullDatesGracefully() {
			assertDoesNotThrow(() -> {
				new StockHistoryRequest(TEST_SYMBOL,
						(LocalDateTime) null,
						(LocalDateTime) null,
						StockHistoryRequest.ValidIntervals.ONE_DAY);
			});
		}

		@Test
		@DisplayName("Should handle start date after end date")
		void shouldHandleStartDateAfterEndDate() {
			LocalDateTime startDate = LocalDateTime.of(2024, 2, 1, 0, 0);
			LocalDateTime endDate = LocalDateTime.of(2024, 1, 1, 0, 0);

			assertDoesNotThrow(() -> {
				new StockHistoryRequest(TEST_SYMBOL, startDate, endDate,
						StockHistoryRequest.ValidIntervals.ONE_DAY);
			});
		}
	}

	@Nested
	@DisplayName("Integration Tests")
	class IntegrationTests {

		@Test
		@DisplayName("Should maintain consistency between constructors")
		void shouldMaintainConsistencyBetweenConstructors() {

			StockHistoryRequest request1 = new StockHistoryRequest(
					TEST_SYMBOL,
					StockHistoryRequest.ValidRanges.ONE_MONTH,
					StockHistoryRequest.ValidIntervals.ONE_DAY
			);

			StockHistoryRequest request2 = new StockHistoryRequest(TEST_SYMBOL);

			Map<String, String> params1 = request1.getParams();
			Map<String, String> params2 = request2.getParams();

			assertEquals(params1.get("range"), params2.get("range"));
			assertEquals(params1.get("interval"), params2.get("interval"));
		}

		@Test
		@DisplayName("Should create equivalent requests using different constructors with same time period")
		void shouldCreateEquivalentRequestsUsingDifferentConstructors() {
			// Define a specific time period for testing
			LocalDateTime fixedStartDate = LocalDateTime.of(2024, 1, 1, 0, 0);
			LocalDateTime fixedEndDate = LocalDateTime.of(2024, 2, 1, 0, 0); // Exactly 31 days later
			StockHistoryRequest.ValidRanges testRange = StockHistoryRequest.ValidRanges.ONE_MONTH;
			StockHistoryRequest.ValidIntervals testInterval = StockHistoryRequest.ValidIntervals.ONE_HOUR;

			// Constructor 1: Using explicit start and end dates
			StockHistoryRequest request1 = new StockHistoryRequest(
					TEST_SYMBOL,
					fixedStartDate,
					fixedEndDate,
					testInterval
			);

			// Constructor 2: Using start date + range
			StockHistoryRequest request2 = new StockHistoryRequest(
					TEST_SYMBOL,
					fixedStartDate,
					testRange,
					testInterval
			);

			// Constructor 3: Using range + end date
			StockHistoryRequest request3 = new StockHistoryRequest(
					TEST_SYMBOL,
					testRange,
					fixedEndDate,
					testInterval
			);

			Map<String, String> params1 = request1.getParams();
			Map<String, String> params2 = request2.getParams();
			Map<String, String> params3 = request3.getParams();

			assertEquals(testInterval.getInterval(), params1.get("interval"));
			assertEquals(testInterval.getInterval(), params2.get("interval"));
			assertEquals(testInterval.getInterval(), params3.get("interval"));

			assertNull(params1.get("range"));
			assertNull(params2.get("range"));
			assertNull(params3.get("range"));

			assertNotNull(params1.get("period1"));
			assertNotNull(params1.get("period2"));
			assertNotNull(params2.get("period1"));
			assertNotNull(params2.get("period2"));
			assertNotNull(params3.get("period1"));
			assertNotNull(params3.get("period2"));

			assertEquals(String.valueOf(fixedStartDate.toEpochSecond(ZoneOffset.UTC)), params1.get("period1"));
			assertEquals(String.valueOf(fixedEndDate.toEpochSecond(ZoneOffset.UTC)), params1.get("period2"));

			assertEquals(String.valueOf(fixedStartDate.toEpochSecond(ZoneOffset.UTC)), params2.get("period1"));

			long expectedEndEpoch = fixedStartDate.toEpochSecond(ZoneOffset.UTC) + testRange.getTimeMilisecond();
			long actualEndEpoch = Long.parseLong(params2.get("period2"));
			assertTrue(Math.abs(expectedEndEpoch - actualEndEpoch) < 86400, // Within 1 day tolerance
					"Request 2 end date should be approximately start date + range duration");

			assertEquals(String.valueOf(fixedEndDate.toEpochSecond(ZoneOffset.UTC)), params3.get("period2"));
			long expectedStartEpoch = fixedEndDate.toEpochSecond(ZoneOffset.UTC) - testRange.getTimeMilisecond();
			long actualStartEpoch = Long.parseLong(params3.get("period1"));
			assertTrue(Math.abs(expectedStartEpoch - actualStartEpoch) < 86400, // Within 1 day tolerance
					"Request 3 start date should be approximately end date - range duration");
		}

		@Test
		@DisplayName("Should create equivalent requests with different constructors for specific duration")
		void shouldCreateEquivalentRequestsWithSpecificDuration() {
			LocalDateTime baseDate = LocalDateTime.of(2024, 6, 15, 12, 0);
			StockHistoryRequest.ValidIntervals interval = StockHistoryRequest.ValidIntervals.FIFTEEN_MINUTES;

			StockHistoryRequest.ValidRanges oneDayRange = StockHistoryRequest.ValidRanges.ONE_DAY;
			long oneDaySeconds = 24 * 60 * 60; // 86400 seconds

			LocalDateTime endDate = baseDate.plusDays(1);

			StockHistoryRequest request1 = new StockHistoryRequest(
					TEST_SYMBOL,
					baseDate,
					endDate,
					interval
			);

			// Constructor 2: Start date + range
			StockHistoryRequest request2 = new StockHistoryRequest(
					TEST_SYMBOL,
					baseDate,
					oneDayRange,
					interval
			);

			// Constructor 3: Range + end date
			StockHistoryRequest request3 = new StockHistoryRequest(
					TEST_SYMBOL,
					oneDayRange,
					endDate,
					interval
			);

			Map<String, String> params1 = request1.getParams();
			Map<String, String> params2 = request2.getParams();
			Map<String, String> params3 = request3.getParams();

			String expectedInterval = interval.getInterval();
			assertEquals(expectedInterval, params1.get("interval"));
			assertEquals(expectedInterval, params2.get("interval"));
			assertEquals(expectedInterval, params3.get("interval"));

			long start1 = Long.parseLong(params1.get("period1"));
			long end1 = Long.parseLong(params1.get("period2"));
			long start2 = Long.parseLong(params2.get("period1"));
			long end2 = Long.parseLong(params2.get("period2"));
			long start3 = Long.parseLong(params3.get("period1"));
			long end3 = Long.parseLong(params3.get("period2"));

			// Request 1: Should have exact start and end times
			assertEquals(baseDate.toEpochSecond(ZoneOffset.UTC), start1);
			assertEquals(endDate.toEpochSecond(ZoneOffset.UTC), end1);
			assertEquals(oneDaySeconds, end1 - start1);

			// Request 2: Should have same start, end should be start + duration
			assertEquals(baseDate.toEpochSecond(ZoneOffset.UTC), start2);
			// The duration should be approximately 1 day
			long duration2 = end2 - start2;
			assertTrue(Math.abs(duration2 - oneDaySeconds) < 3600, // Within 1 hour tolerance
					"Duration should be approximately 1 day");

			// Request 3: Should have same end, start should be end - duration
			assertEquals(endDate.toEpochSecond(ZoneOffset.UTC), end3);
			long duration3 = end3 - start3;
			assertTrue(Math.abs(duration3 - oneDaySeconds) < 3600, // Within 1 hour tolerance
					"Duration should be approximately 1 day");
		}

		@Test
		@DisplayName("Should generate same URLs regardless of constructor used")
		void shouldGenerateSameUrlsRegardlessOfConstructor() {
			LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
			LocalDateTime endDate = LocalDateTime.of(2024, 1, 8, 0, 0); // 1 week later
			StockHistoryRequest.ValidRanges range = StockHistoryRequest.ValidRanges.ONE_MONTH;
			StockHistoryRequest.ValidIntervals interval = StockHistoryRequest.ValidIntervals.ONE_DAY;

			StockHistoryRequest request1 = new StockHistoryRequest(TEST_SYMBOL, startDate, endDate, interval);
			StockHistoryRequest request2 = new StockHistoryRequest(TEST_SYMBOL, startDate, range, interval);
			StockHistoryRequest request3 = new StockHistoryRequest(TEST_SYMBOL, range, endDate, interval);
			StockHistoryRequest request4 = new StockHistoryRequest(TEST_SYMBOL, range, interval);

			String baseUrl = "https://query2.finance.yahoo.com/v8/finance/chart";
			assertEquals(baseUrl, request1.getURL());
			assertEquals(baseUrl, request2.getURL());
			assertEquals(baseUrl, request3.getURL());
			assertEquals(baseUrl, request4.getURL());
		}

		@Test
		@DisplayName("Should generate consistent URLs")
		void shouldGenerateConsistentUrls() {
			StockHistoryRequest request1 = new StockHistoryRequest(
					TEST_SYMBOL,
					StockHistoryRequest.ValidRanges.ONE_MONTH,
					StockHistoryRequest.ValidIntervals.ONE_DAY
			);

			StockHistoryRequest request2 = new StockHistoryRequest(
					"GOOGL",
					StockHistoryRequest.ValidRanges.ONE_YEAR,
					StockHistoryRequest.ValidIntervals.ONE_WEEK
			);

			assertEquals(request1.getURL(), request2.getURL());
		}

		@Test
		@DisplayName("Should produce equivalent parameter sets for same logical time period")
		void shouldProduceEquivalentParameterSetsForSameLogicalTimePeriod() {
			LocalDateTime fixedStart = LocalDateTime.of(2024, 3, 1, 9, 30);
			LocalDateTime fixedEnd = LocalDateTime.of(2024, 3, 6, 9, 30); // Exactly 5 days
			StockHistoryRequest.ValidIntervals commonInterval = StockHistoryRequest.ValidIntervals.THIRTY_MINUTES;

			StockHistoryRequest dateRangeRequest = new StockHistoryRequest(
					TEST_SYMBOL, fixedStart, fixedEnd, commonInterval
			);

			StockHistoryRequest startPlusRangeRequest = new StockHistoryRequest(
					TEST_SYMBOL, fixedStart, StockHistoryRequest.ValidRanges.FIVE_DAYS, commonInterval
			);

			StockHistoryRequest endMinusRangeRequest = new StockHistoryRequest(
					TEST_SYMBOL, StockHistoryRequest.ValidRanges.FIVE_DAYS, fixedEnd, commonInterval
			);

			Map<String, String> dateRangeParams = dateRangeRequest.getParams();
			Map<String, String> startPlusRangeParams = startPlusRangeRequest.getParams();
			Map<String, String> endMinusRangeParams = endMinusRangeRequest.getParams();

			assertEquals(commonInterval.getInterval(), dateRangeParams.get("interval"));
			assertEquals(commonInterval.getInterval(), startPlusRangeParams.get("interval"));
			assertEquals(commonInterval.getInterval(), endMinusRangeParams.get("interval"));

			long directStart = Long.parseLong(dateRangeParams.get("period1"));
			long directEnd = Long.parseLong(dateRangeParams.get("period2"));
			long directDuration = directEnd - directStart;

			long startPlusStart = Long.parseLong(startPlusRangeParams.get("period1"));
			long startPlusEnd = Long.parseLong(startPlusRangeParams.get("period2"));
			long startPlusDuration = startPlusEnd - startPlusStart;

			long endMinusStart = Long.parseLong(endMinusRangeParams.get("period1"));
			long endMinusEnd = Long.parseLong(endMinusRangeParams.get("period2"));
			long endMinusDuration = endMinusEnd - endMinusStart;

			long expectedDuration = 5 * 24 * 60 * 60;
			assertEquals(expectedDuration, directDuration);
			assertEquals(fixedStart.toEpochSecond(ZoneOffset.UTC), directStart);
			assertEquals(fixedEnd.toEpochSecond(ZoneOffset.UTC), directEnd);


			assertEquals(fixedStart.toEpochSecond(ZoneOffset.UTC), startPlusStart);
			assertTrue(Math.abs(startPlusDuration - expectedDuration) < 86400,
					"Start + range duration should be approximately 5 days, got: " + startPlusDuration + " seconds, " + (startPlusDuration / 60 / 60 / 24) + " days");

			assertEquals(fixedEnd.toEpochSecond(ZoneOffset.UTC), endMinusEnd);
			assertTrue(Math.abs(endMinusDuration - expectedDuration) < 86400,
					"End - range duration should be approximately 5 days, got: " + endMinusDuration + " seconds");

			long overlapStart = Math.max(Math.max(directStart, startPlusStart), endMinusStart);
			long overlapEnd = Math.min(Math.min(directEnd, startPlusEnd), endMinusEnd);
			long overlapDuration = Math.max(0, overlapEnd - overlapStart);

			assertTrue(overlapDuration > expectedDuration * 0.8,
					"Time periods should have substantial overlap (>80%), overlap duration: " + overlapDuration + " seconds");
		}
	}
}


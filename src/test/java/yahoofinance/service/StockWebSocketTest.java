package yahoofinance.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import yahoofinance.YFinance;
import yahoofinance.model.Pricing;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("StockWebSocket Tests")
class StockWebSocketTest {

	private StockWebSocket stockWebSocket;

	@BeforeEach
	void setUp() {
		stockWebSocket = YFinance.getStockWebSocket();
	}

	@AfterEach
	void tearDown() {
		if (stockWebSocket != null) {
			stockWebSocket.close();
		}
	}

	@Nested
	@DisplayName("Basic Functionality Tests")
	class BasicFunctionalityTests {

		@Test
		@DisplayName("Should create StockWebSocket with proper defaults")
		void testCreation() {
			assertThat(stockWebSocket).isNotNull();
			assertThat(stockWebSocket.getContainer()).isNotNull();
			assertThat(stockWebSocket.getWebSocketSession()).isNull();
			assertThat(stockWebSocket.getEndpoint()).isNull();
			assertThat(stockWebSocket.getMessageConsumer()).isNull();
			assertThat(stockWebSocket.isConnected()).isFalse();
		}

		@Test
		@DisplayName("Should set up listener correctly")
		void testListenerSetup() {
			AtomicReference<Pricing.PricingData> receivedData = new AtomicReference<>();
			Consumer<Pricing.PricingData> consumer = receivedData::set;

			stockWebSocket.listen(consumer);

			assertThat(stockWebSocket.getMessageConsumer()).isEqualTo(consumer);
			assertThat(stockWebSocket.getEndpoint()).isNotNull();
		}

		@Test
		@DisplayName("Should handle null consumer")
		void testNullConsumer() {
			assertDoesNotThrow(() -> stockWebSocket.listen(null));
			assertThat(stockWebSocket.getMessageConsumer()).isNull();
			assertThat(stockWebSocket.getEndpoint()).isNotNull();
		}

		@Test
		@DisplayName("Should replace consumer on multiple listen calls")
		void testMultipleListenCalls() {
			Consumer<Pricing.PricingData> consumer1 = data -> log.info("Consumer 1");
			Consumer<Pricing.PricingData> consumer2 = data -> log.info("Consumer 2");

			stockWebSocket.listen(consumer1);
			assertThat(stockWebSocket.getMessageConsumer()).isEqualTo(consumer1);

			stockWebSocket.listen(consumer2);
			assertThat(stockWebSocket.getMessageConsumer()).isEqualTo(consumer2);
		}
	}

	@Nested
	@DisplayName("Connection State Tests")
	class ConnectionStateTests {

		@Test
		@DisplayName("Should throw exception when connecting without listener")
		void testConnectWithoutListener() {
			IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
				stockWebSocket.connect();
			});

			assertThat(exception.getMessage()).contains("Must call listen() before connect()");
		}

		@Test
		@DisplayName("Should not be connected initially")
		void testInitialConnectionState() {
			assertThat(stockWebSocket.isConnected()).isFalse();
		}

		@Test
		@DisplayName("Should handle close when not connected")
		void testCloseWhenNotConnected() {
			assertDoesNotThrow(() -> stockWebSocket.close());
			assertThat(stockWebSocket.getWebSocketSession()).isNull();
		}
	}

	@Nested
	@DisplayName("Subscription Tests")
	class SubscriptionTests {

		@Test
		@DisplayName("Should throw exception when subscribing without connection")
		void testSubscribeWithoutConnection() {
			IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
				stockWebSocket.subscribe("AAPL");
			});

			assertThat(exception.getMessage()).contains("WebSocket is not connected");
		}

		@Test
		@DisplayName("Should throw exception when subscribing to multiple tickers without connection")
		void testSubscribeMultipleWithoutConnection() {
			List<String> tickers = List.of("AAPL", "MSFT", "GOOGL");

			IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
				stockWebSocket.subscribe(tickers);
			});

			assertThat(exception.getMessage()).contains("WebSocket is not connected");
		}

		@Test
		@DisplayName("Should handle unsubscribe gracefully when not connected")
		void testUnsubscribeWithoutConnection() {
			assertDoesNotThrow(() -> stockWebSocket.unsubscribe("AAPL"));
			assertDoesNotThrow(() -> stockWebSocket.unsubscribe(List.of("AAPL", "MSFT")));
		}
	}

	@Nested
	@DisplayName("Data Model Tests")
	class DataModelTests {

		@Test
		@DisplayName("Should create and encode pricing data correctly")
		void testPricingDataCreation() {
			Pricing.PricingData testData = Pricing.PricingData.newBuilder()
					.setId("AAPL")
					.setPrice(150.25f)
					.setTime(System.currentTimeMillis())
					.setDayVolume(1000000)
					.setChange(2.5f)
					.setChangePercent(1.67f)
					.build();

			assertThat(testData.getId()).isEqualTo("AAPL");
			assertThat(testData.getPrice()).isEqualTo(150.25f);
			assertThat(testData.getTime()).isGreaterThan(0);
			assertThat(testData.getDayVolume()).isEqualTo(1000000);
			assertThat(testData.getChange()).isEqualTo(2.5f);
			assertThat(testData.getChangePercent()).isEqualTo(1.67f);
		}

		@Test
		@DisplayName("Should encode and decode pricing data correctly")
		void testPricingDataSerialization() {
			Pricing.PricingData originalData = Pricing.PricingData.newBuilder()
					.setId("MSFT")
					.setPrice(305.75f)
					.setTime(System.currentTimeMillis())
					.build();

			byte[] encoded = originalData.toByteArray();
			String base64 = Base64.getEncoder().encodeToString(encoded);

			assertThat(base64).isNotEmpty();

			byte[] decoded = Base64.getDecoder().decode(base64);

			assertDoesNotThrow(() -> {
				Pricing.PricingData decodedData = Pricing.PricingData.parseFrom(decoded);
				assertThat(decodedData.getId()).isEqualTo("MSFT");
				assertThat(decodedData.getPrice()).isEqualTo(305.75f);
				assertThat(decodedData.getTime()).isEqualTo(originalData.getTime());
			});
		}

		@Test
		@DisplayName("Should handle invalid base64 data")
		void testInvalidBase64() {
			String invalidBase64 = "not-valid-base64!@#$%";

			assertThrows(IllegalArgumentException.class, () -> {
				Base64.getDecoder().decode(invalidBase64);
			});
		}

		@Test
		@DisplayName("Should handle malformed protobuf data")
		void testMalformedProtobuf() {
			byte[] invalidProtobuf = "not protobuf data".getBytes();

			assertThrows(Exception.class, () -> {
				Pricing.PricingData.parseFrom(invalidProtobuf);
			});
		}
	}

	@Nested
	@DisplayName("Consumer Behavior Tests")
	class ConsumerBehaviorTests {

		@Test
		@DisplayName("Should handle consumer that throws exception")
		void testConsumerException() {
			Consumer<Pricing.PricingData> throwingConsumer = data -> {
				throw new RuntimeException("Consumer error");
			};

			assertDoesNotThrow(() -> stockWebSocket.listen(throwingConsumer));
			assertThat(stockWebSocket.getMessageConsumer()).isEqualTo(throwingConsumer);
		}

		@Test
		@DisplayName("Should handle consumer that processes data correctly")
		void testConsumerProcessing() {
			AtomicReference<Pricing.PricingData> receivedData = new AtomicReference<>();
			CountDownLatch latch = new CountDownLatch(1);

			Consumer<Pricing.PricingData> testConsumer = data -> {
				receivedData.set(data);
				latch.countDown();
			};

			stockWebSocket.listen(testConsumer);

			Pricing.PricingData testData = Pricing.PricingData.newBuilder()
					.setId("TEST")
					.setPrice(100.0f)
					.build();

			testConsumer.accept(testData);

			assertThat(receivedData.get()).isNotNull();
			assertThat(receivedData.get().getId()).isEqualTo("TEST");
			assertThat(receivedData.get().getPrice()).isEqualTo(100.0f);
		}
	}

	@Nested
	@DisplayName("State Management Tests")
	class StateManagementTests {

		@Test
		@DisplayName("Should maintain proper state transitions")
		void testStateTransitions() {
			assertThat(stockWebSocket.isConnected()).isFalse();
			assertThat(stockWebSocket.getWebSocketSession()).isNull();
			assertThat(stockWebSocket.getEndpoint()).isNull();

			stockWebSocket.listen(data -> {
			});
			assertThat(stockWebSocket.isConnected()).isFalse();
			assertThat(stockWebSocket.getWebSocketSession()).isNull();
			assertThat(stockWebSocket.getEndpoint()).isNotNull();

			stockWebSocket.close();
			assertThat(stockWebSocket.isConnected()).isFalse();
			assertThat(stockWebSocket.getWebSocketSession()).isNull();
		}

		@Test
		@DisplayName("Should validate getter methods")
		void testGetters() {
			assertThat(stockWebSocket.getContainer()).isNotNull();
			assertThat(stockWebSocket.getWebSocketSession()).isNull();
			assertThat(stockWebSocket.getHeartbeatExecutor()).isNotNull();
			assertThat(stockWebSocket.getMessageConsumer()).isNull();
			assertThat(stockWebSocket.getEndpoint()).isNull();

			Consumer<Pricing.PricingData> consumer = data -> {
			};
			stockWebSocket.listen(consumer);

			assertThat(stockWebSocket.getMessageConsumer()).isEqualTo(consumer);
			assertThat(stockWebSocket.getEndpoint()).isNotNull();
		}

		@Test
		@DisplayName("Should handle setter methods correctly")
		void testSetters() {
			Consumer<Pricing.PricingData> consumer = data -> log.info("Test consumer");

			stockWebSocket.setMessageConsumer(consumer);
			assertThat(stockWebSocket.getMessageConsumer()).isEqualTo(consumer);

			stockWebSocket.setMessageConsumer(null);
			assertThat(stockWebSocket.getMessageConsumer()).isNull();
		}
	}

	@Nested
	@DisplayName("Integration Workflow Tests")
	class IntegrationWorkflowTests {

		@Test
		@DisplayName("Should handle complete workflow without actual connection")
		void testCompleteWorkflow() {
			AtomicReference<Pricing.PricingData> receivedData = new AtomicReference<>();
			Consumer<Pricing.PricingData> consumer = receivedData::set;

			// Step 1: Set up listener
			assertDoesNotThrow(() -> stockWebSocket.listen(consumer));
			assertThat(stockWebSocket.getEndpoint()).isNotNull();

			// Step 2: Verify cannot subscribe without connection
			assertThrows(IllegalStateException.class, () -> stockWebSocket.subscribe("AAPL"));

			// Step 3: Verify unsubscribe handles gracefully
			assertDoesNotThrow(() -> stockWebSocket.unsubscribe("AAPL"));

			// Step 4: Verify close works
			assertDoesNotThrow(() -> stockWebSocket.close());
		}

		@Test
		@DisplayName("Should validate ticker list operations")
		void testTickerListOperations() {
			stockWebSocket.listen(data -> {
			});

			List<String> tickers = List.of("AAPL", "MSFT", "GOOGL", "AMZN", "TSLA");

			// Should fail without connection
			assertThrows(IllegalStateException.class, () -> stockWebSocket.subscribe(tickers));

			// Unsubscribe should not fail
			assertDoesNotThrow(() -> stockWebSocket.unsubscribe(tickers));
		}

		@Test
		@DisplayName("Should handle empty ticker lists")
		void testEmptyTickerLists() {
			stockWebSocket.listen(data -> {
			});

			List<String> emptyList = List.of();

			assertThrows(IllegalStateException.class, () -> stockWebSocket.subscribe(emptyList));
			assertDoesNotThrow(() -> stockWebSocket.unsubscribe(emptyList));
		}
	}

	@Nested
	@DisplayName("Error Handling Tests")
	class ErrorHandlingTests {

		@Test
		@DisplayName("Should handle multiple close calls")
		void testMultipleCloseCalls() {
			stockWebSocket.listen(data -> {
			});

			assertDoesNotThrow(() -> stockWebSocket.close());
			assertDoesNotThrow(() -> stockWebSocket.close());
			assertDoesNotThrow(() -> stockWebSocket.close());
		}

		@Test
		@DisplayName("Should handle operations after close")
		void testOperationsAfterClose() {
			stockWebSocket.listen(data -> {
			});
			stockWebSocket.close();

			// These should all handle gracefully
			assertThrows(IllegalStateException.class, () -> stockWebSocket.subscribe("AAPL"));
			assertDoesNotThrow(() -> stockWebSocket.unsubscribe("AAPL"));
			assertThat(stockWebSocket.isConnected()).isFalse();
		}
	}
}


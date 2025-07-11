package yahoofinance;

import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import yahoofinance.model.Pricing;
import yahoofinance.service.StockWebSocket;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class StockWebSocketTest {

    private StockWebSocket stockWebSocket;
    private CountDownLatch messageReceivedLatch;
    private List<Pricing.PricingData> receivedMessages;
    private volatile boolean keepRunning;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockWebSocket = new StockWebSocket();
        receivedMessages = new ArrayList<>();
        keepRunning = true;
    }

    @AfterEach
    void tearDown() {
        keepRunning = false;
        if (stockWebSocket != null && stockWebSocket.isConnected()) {
            stockWebSocket.close();
        }
    }

    // Test 1: Basic Connection and Subscription
    @Test
    @Timeout(30)
    void testBasicConnectionAndSubscription() throws InterruptedException {
        messageReceivedLatch = new CountDownLatch(1);

        stockWebSocket.listen(pricingData -> {
            receivedMessages.add(pricingData);
            messageReceivedLatch.countDown();
        });

        stockWebSocket.connect();
        assertTrue(stockWebSocket.isConnected(), "WebSocket should be connected");

        stockWebSocket.subscribe("AAPL");

        assertTrue(messageReceivedLatch.await(15, TimeUnit.SECONDS),
                "Should receive at least one message within 15 seconds");
        assertFalse(receivedMessages.isEmpty(), "Should have received messages");
    }

    // Test 2: Multiple Subscriptions
    @Test
    @Timeout(45)
    void testMultipleSubscriptions() throws InterruptedException {
        messageReceivedLatch = new CountDownLatch(10);

        stockWebSocket.listen(pricingData -> {
            receivedMessages.add(pricingData);
            messageReceivedLatch.countDown();
        });

        stockWebSocket.connect();

        stockWebSocket.subscribe(List.of("AAPL", "GOOGL", "MSFT"));

        assertTrue(messageReceivedLatch.await(30, TimeUnit.SECONDS),
                "Should receive multiple messages from different tickers");

        assertTrue(receivedMessages.size() >= 3, "Should receive messages from multiple tickers");
    }

    // Test 3: Subscription and Unsubscription
    @Test
    @Timeout(60)
    void testSubscriptionAndUnsubscription() throws InterruptedException {
        AtomicReference<String> lastTicker = new AtomicReference<>();
        messageReceivedLatch = new CountDownLatch(5);

        stockWebSocket.listen(pricingData -> {
            lastTicker.set(pricingData.getId());
            messageReceivedLatch.countDown();
        });

        stockWebSocket.connect();
        stockWebSocket.subscribe("TSLA");

        Thread.sleep(5000);

        stockWebSocket.subscribe("NVDA");
        Thread.sleep(5000);

        stockWebSocket.unsubscribe("TSLA");
        Thread.sleep(5000);

        assertTrue(messageReceivedLatch.await(20, TimeUnit.SECONDS));
    }

    // Test 4: Connection Recovery
    @Test
    @Timeout(90)
    void testConnectionRecovery() throws InterruptedException {
        messageReceivedLatch = new CountDownLatch(2);

        stockWebSocket.listen(pricingData -> {
            receivedMessages.add(pricingData);
            messageReceivedLatch.countDown();
        });

        stockWebSocket.connect();
        stockWebSocket.subscribe("AAPL");

        Thread.sleep(5000);

        stockWebSocket.close();
        assertFalse(stockWebSocket.isConnected(), "WebSocket should be disconnected");

        stockWebSocket.connect();
        stockWebSocket.subscribe("AAPL");

        assertTrue(messageReceivedLatch.await(30, TimeUnit.SECONDS),
                "Should receive messages after reconnection");
    }

    // Test 5: Concurrent Operations
    @Test
    @Timeout(60)
    void testConcurrentOperations() throws InterruptedException {
        messageReceivedLatch = new CountDownLatch(20);

        stockWebSocket.listen(pricingData -> {
            receivedMessages.add(pricingData);
            messageReceivedLatch.countDown();
        });

        stockWebSocket.connect();

        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {

            executor.submit(() -> {
                try {
                    stockWebSocket.subscribe("AAPL");
                    Thread.sleep(2000);
                    stockWebSocket.unsubscribe("AAPL");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            executor.submit(() -> {
                try {
                    Thread.sleep(1000);
                    stockWebSocket.subscribe("GOOGL");
                    Thread.sleep(3000);
                    stockWebSocket.unsubscribe("GOOGL");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            executor.submit(() -> {
                try {
                    Thread.sleep(1500);
                    stockWebSocket.subscribe("MSFT");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            assertTrue(messageReceivedLatch.await(30, TimeUnit.SECONDS));

            executor.shutdown();
            assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        }
    }

    // Test 6: Error Handling
    @Test
    void testErrorHandling() {
        assertThrows(IllegalStateException.class, () -> {
            stockWebSocket.subscribe("AAPL");
        });

        assertThrows(IllegalStateException.class, () -> {
            stockWebSocket.connect();
        });
    }

    // Test 7: Resource Cleanup
    @Test
    @Timeout(30)
    void testResourceCleanup() throws InterruptedException {
        stockWebSocket.listen(pricingData -> {
        });

        stockWebSocket.connect();
        assertTrue(stockWebSocket.isConnected());

        stockWebSocket.close();
        assertFalse(stockWebSocket.isConnected());

        Thread.sleep(2000);
    }
}
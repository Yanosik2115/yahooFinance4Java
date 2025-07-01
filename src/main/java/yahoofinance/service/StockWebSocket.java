package yahoofinance.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.model.Pricing;
import yahoofinance.util.Utils;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
@Setter
@Slf4j
public class StockWebSocket {

	private Session webSocketSession;
	private ScheduledExecutorService heartbeatExecutor;
	private final WebSocketContainer container;
	private static final String YAHOO_WEBSOCKET_URL = "wss://streamer.finance.yahoo.com/?version=2";
	private Consumer<Pricing.PricingData> messageConsumer;
	private Endpoint endpoint;

	public StockWebSocket() {
		this.container = ContainerProvider.getWebSocketContainer();
		this.container.setDefaultMaxSessionIdleTimeout(0);
		this.container.setDefaultMaxTextMessageBufferSize(65536);
		this.container.setDefaultMaxBinaryMessageBufferSize(65536);
		this.heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
	}

	public void listen(Consumer<Pricing.PricingData> consumer) {
		this.messageConsumer = consumer;

		this.endpoint = new Endpoint() {
			@Override
			public void onClose(Session session, CloseReason closeReason) {
				log.warn("WebSocket session closed: {} - {}", closeReason.getCloseCode(), closeReason.getReasonPhrase());
				stopHeartbeat();
			}

			@Override
			public void onError(Session session, Throwable thr) {
				log.error("WebSocket error occurred", thr);
			}

			@Override
			public void onOpen(Session session, EndpointConfig endpointConfig) {
				webSocketSession = session;

				session.addMessageHandler(String.class, message -> {
					log.debug("Received WebSocket message: {}", message);
					try {
						JsonNode jsonNode = Utils.getObjectMapper().readTree(message);

						if (jsonNode.has("type") && jsonNode.has("message")) {
							String type = jsonNode.get("type").asText();

							if ("pricing".equals(type)) {
								String base64Message = jsonNode.get("message").asText();
								Pricing.PricingData pricingData = decodeYahooFinanceData(base64Message);
								if (messageConsumer != null) {
									messageConsumer.accept(pricingData);
								}
							} else {
								log.info("Received non-data message type: {}", type);
							}
						} else {
							log.debug("Received message without expected structure: {}", message);
						}
					} catch (Exception e) {
						log.error("Error parsing WebSocket message: {}", message, e);
					}
				});

				log.info("WebSocket connection established successfully");
				startHeartbeat();
			}
		};
	}

	public void connect() {
		if (endpoint == null) {
			throw new IllegalStateException("Must call listen() before connect()");
		}

		try {
			URI serverEndpointUri = URI.create(YAHOO_WEBSOCKET_URL);
			webSocketSession = container.connectToServer(endpoint, serverEndpointUri);

		} catch (DeploymentException | IOException e) {
			log.error("Failed to connect to WebSocket", e);
			throw new RuntimeException("Failed to connect to Yahoo Finance WebSocket", e);
		}
	}

	public void subscribe(String ticker) {
		subscribe(List.of(ticker));
	}

	public void subscribe(List<String> tickers) {
		if (webSocketSession == null || !webSocketSession.isOpen()) {
			throw new IllegalStateException("WebSocket is not connected. Call connect() first.");
		}

		try {
			Map<String, Object> subscribeMessage = Map.of("subscribe", tickers);
			String subscribeMessageJson = Utils.getGson().toJson(subscribeMessage);

			log.info("Subscribing to tickers: {}", tickers);
			webSocketSession.getBasicRemote().sendText(subscribeMessageJson);

		} catch (IOException e) {
			log.error("Failed to send subscribe message", e);
			throw new RuntimeException("Failed to subscribe to tickers", e);
		}
	}

	public void unsubscribe(String ticker) {
		unsubscribe(List.of(ticker));
	}

	public void unsubscribe(List<String> tickers) {
		if (webSocketSession == null || !webSocketSession.isOpen()) {
			log.warn("WebSocket is not connected. Cannot unsubscribe from tickers: {}", tickers);
			return;
		}

		try {
			Map<String, Object> unsubscribeMessage = Map.of("unsubscribe", tickers);
			String unsubscribeMessageJson = Utils.getGson().toJson(unsubscribeMessage);

			log.info("Unsubscribing from tickers: {}", tickers);
			webSocketSession.getBasicRemote().sendText(unsubscribeMessageJson);

		} catch (IOException e) {
			log.error("Failed to send unsubscribe message", e);
			throw new RuntimeException("Failed to unsubscribe from tickers", e);
		}
	}

	public void close() {
		try {
			stopHeartbeat();

			if (webSocketSession != null && webSocketSession.isOpen()) {
				webSocketSession.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "Client closing"));
				log.info("WebSocket connection closed");
			}
		} catch (IOException e) {
			log.error("Error closing WebSocket connection", e);
		} finally {
			webSocketSession = null;
		}
	}

	public boolean isConnected() {
		return webSocketSession != null && webSocketSession.isOpen();
	}

	private void startHeartbeat() {
		heartbeatExecutor.scheduleAtFixedRate(() -> {
			try {
				if (webSocketSession != null && webSocketSession.isOpen()) {
					webSocketSession.getBasicRemote().sendPing(java.nio.ByteBuffer.allocate(0));
					log.debug("Sent heartbeat ping");
				}
			} catch (IOException e) {
				log.warn("Failed to send heartbeat ping", e);
			}
		}, 30, 30, TimeUnit.SECONDS);
	}

	private void stopHeartbeat() {
		if (heartbeatExecutor != null && !heartbeatExecutor.isShutdown()) {
			heartbeatExecutor.shutdown();
			try {
				if (!heartbeatExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
					heartbeatExecutor.shutdownNow();
				}
			} catch (InterruptedException e) {
				heartbeatExecutor.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}

	private static Pricing.PricingData decodeYahooFinanceData(String base64Data) {
		try {
			byte[] data = Base64.getDecoder().decode(base64Data);
			return Pricing.PricingData.parseFrom(data);

		} catch (Exception e) {
			log.error("Failed to decode Yahoo Finance data: {}", base64Data, e);
			throw new RuntimeException("Failed to decode Yahoo Finance data", e);
		}
	}
}
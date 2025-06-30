package yahoofinance.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import yahoofinance.util.Utils;

import javax.websocket.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class Stock {
	@Getter
	private final String ticker;
	@Getter
	@Setter
	private StockQuoteSummary stockQuoteSummary;
	private Session webSocketSession;
	private ScheduledExecutorService heartbeatExecutor;
	private static final int HEARTBEAT_INTERVAL = 30; // seconds


	private static final String YAHOO_WEBSOCKET_URL = "wss://streamer.finance.yahoo.com/?version=2";

	public void getWebSocketConnection(Consumer<Pricing.PricingData> consumer) throws IOException {

		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.setDefaultMaxSessionIdleTimeout(0);
		container.setDefaultMaxTextMessageBufferSize(65536);
		container.setDefaultMaxBinaryMessageBufferSize(65536);

		URI serverEndpoint = URI.create(YAHOO_WEBSOCKET_URL);

		Endpoint endpoint = new Endpoint() {
			@Override
			public void onClose(Session session, CloseReason closeReason) {
				log.warn("Session closed: {}", closeReason);
			}

			@Override
			public void onError(Session session, Throwable thr) {
				log.error(thr.getMessage());
			}

			@Override
			public void onOpen(Session session, EndpointConfig endpointConfig) {
				session.addMessageHandler(String.class, message -> {
					log.info("Received WebSocket message: {}", message);
					try {
						JsonNode jsonNode = Utils.getObjectMapper().readTree(message);

						if (jsonNode.has("type") && jsonNode.has("message")) {
							String base64Message = jsonNode.get("message").asText();
							Pricing.PricingData pricingData = decodeYahooFinanceData(base64Message);
							consumer.accept(pricingData);
						}
					} catch (Exception e) {
						log.error("Error parsing WebSocket message: {}", message, e);
					}
				});

				log.info("WebSocket connection established");
			}
		};

		try {
			webSocketSession = container.connectToServer(endpoint, serverEndpoint);
			log.info(String.valueOf(webSocketSession.isOpen()));

			startHeartbeat();

			Map<String, Object> subscribeMessage = Map.of("subscribe", List.of(ticker));
			String subscribeMessageJson = Utils.getGson().toJson(subscribeMessage);

			webSocketSession.getBasicRemote().sendObject(subscribeMessageJson);

		} catch (DeploymentException | EncodeException e) {
			throw new RuntimeException(e);
		}
	}

	private void startHeartbeat() {
		heartbeatExecutor = Executors.newSingleThreadScheduledExecutor(
				r -> new Thread(r, "WebSocket-Heartbeat-" + ticker)
		);

		heartbeatExecutor.scheduleAtFixedRate(() -> {
			log.info("Heartbeat {}", webSocketSession.isOpen());
			if (webSocketSession != null && webSocketSession.isOpen()) {
				try {
					webSocketSession.getBasicRemote().sendPing(null);
					log.debug("Sent heartbeat for ticker: {}", ticker);
				} catch (IOException e) {
					log.warn("Failed to send heartbeat for ticker: {}", ticker, e);
				}
			}
		}, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
	}

	public static Pricing.PricingData decodeYahooFinanceData(String base64Data) {
		try {
			byte[] data = Base64.getDecoder().decode(base64Data);
			return Pricing.PricingData.parseFrom(data);

		} catch (Exception e) {
			throw new RuntimeException("Failed to decode Yahoo Finance data", e);
		}
	}


}

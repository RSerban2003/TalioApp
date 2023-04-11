package client.utils;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class WebSocketUtils {

    private static String WSSERVER = "ws://localhost:8080/";
    private static StompSession SESSION;
    private static String hostName = "localhost";

    private StompSession session = connect("ws://localhost:8080/websocket");

    public void establishConnection(){
        this.SESSION = connect(WSSERVER +"websocket");
    }
    private Map<String, StompSession.Subscription> subscriptions = new HashMap<>();
    private StompSession connect(String url){
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } catch (ExecutionException e){
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer){
        StompSession.Subscription subscription = SESSION.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            @SuppressWarnings("unchecked")
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
        subscriptions.put(dest, subscription);
    }

    public void unregisterForMessages(String dest) {
        StompSession.Subscription subscription = subscriptions.get(dest);
        if (subscription != null) {
            subscription.unsubscribe();
            subscriptions.remove(dest);
        }
    }

    public void send(String dest, Object o){
        SESSION.send(dest, o);
    }

}

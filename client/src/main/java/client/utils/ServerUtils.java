/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import commons.Board;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientProperties;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.swing.*;

public class ServerUtils {
    private static String SERVER = "http://localhost:8080/";
    private static String WSSERVER = "ws://localhost:8080/";

    public static void setHost(String hostname) {
        SERVER = "http://" + hostname + ":8080/";
        WSSERVER = "ws://" + hostname + ":8080/";
    }
    public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

    public boolean ping() {
        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        config.property(ClientProperties.READ_TIMEOUT, 1000);
        try {
            String pong = ClientBuilder.newClient(config)
                    .target(SERVER).path("api/ping")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(new GenericType<String>() {});
            return pong.equals("pong");
        } catch (ProcessingException pe) {
            return false;
        }
    }

    /**
     * Checks if a board with a given id is in the database
     * @param boardId
     * @return a boolean if the status of the get request is 200
     */
    public boolean boardExists(String boardId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        Response response = client.target(SERVER).path("api/boards/" + boardId + "/get").request().get();
        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    /**
     * getter for the server URL
     * @return server url
     */
    public String getServerUrl() {
        return SERVER;
    }
    private StompSession session = connect("ws://localhost:8080/websocket");
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
        StompSession.Subscription subscription = session.subscribe(dest, new StompFrameHandler() {
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
        session.send(dest, o);
    }

    public boolean deleteTaskList(Long boardId, Long taskListId){
        Client client = ClientBuilder.newClient(new ClientConfig());
        Response response = client.target(SERVER).path("api/boards/" + boardId + "/"+taskListId).request().delete();
        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public boolean deleteTask(Long boardId, Long taskListId, Long taskId){
        Client client = ClientBuilder.newClient(new ClientConfig());
        Response response = client.target(SERVER).path("api/boards/" + boardId + "/"+taskListId+"/"+taskId).request().delete();
        int status = response.getStatus();
        response.close();
        return status == 200;
    }
}
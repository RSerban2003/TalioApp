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
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import commons.*;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;


@Service
public class ServerUtils {
    private static String SERVER = "http://localhost:8080/";
    private static String WSSERVER = "ws://localhost:8080/";
    private static StompSession SESSION;
    private static String hostName = "localhost";

    public static void setHost(String hostname) {
        SERVER = "http://" + hostname + ":8080/";
        WSSERVER = "ws://" + hostname + ":8080/";
        hostName = hostname;
    }
    private static ExecutorService EXEC;

    public void registerForUpdates(Consumer<Board> consumer) {
        if (EXEC == null || EXEC.isShutdown()) {
            EXEC = Executors.newSingleThreadExecutor();
        }
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig())
                        .target(SERVER).path("api/boards/polling/updates")
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);
                if (res.getStatus() == HTTP_OK) {
                    var board = res.readEntity(Board.class);
                    consumer.accept(board);
                }
            }
        });
    }

    public void stop() {
        EXEC.shutdownNow();
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
    public TaskList moveTask(long boardId, long taskListIdFrom, long taskListIdTo, long taskId, int index) {
        return ClientBuilder.newClient(new ClientConfig())
            .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
            .target(SERVER).path(String.format("/api/boards/%s/%s/%s/move", boardId, taskListIdFrom, taskId)) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(Map.of("index", index, "listTo", taskListIdTo), APPLICATION_JSON), TaskList.class);
    }

    public Task moveNestedTask(long boardId, long taskListId, long taskId, long nestedId, int index){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(String.format("/api/boards/%s/%s/%s/%s/move", boardId, taskListId, taskId, nestedId)) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(Map.of("index", index), APPLICATION_JSON), Task.class);
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

    public void changeBoardName(Map<String,String> name, long boardId) {
        Response response = null;
        try {
            response = ClientBuilder.newClient()
                    .target(SERVER).path("api/boards/" + boardId + "/patch")
                    .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                    .put(Entity.entity(name, MediaType.APPLICATION_JSON_TYPE));
        } catch (Exception e) {
            System.out.println(response);
        }

    }

    public void astablishConnection(){
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

    public boolean deleteBoard(Long boardId) {
        Client client = ClientBuilder.newClient(new ClientConfig());
        Response response = client.target(SERVER).path("api/boards/" + boardId).request().delete();
        int status = response.getStatus();
        response.close();
        return status == 200;
    }
    public boolean addTask(long boardID, long taskListID, Map<String, String> body) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER).path("api/boards/"+ boardID + "/"+ taskListID + "/card")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(body, APPLICATION_JSON));

        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public boolean editTask(long boardID, long taskListID, long taskID, String title, String description) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(SERVER).path("api/boards/"+ boardID + "/"+ taskListID + "/" + taskID + "/edit-card")
                .queryParam("name", title.trim())
                .queryParam("description", description.trim())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.text(""));

        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public boolean renameTaskList(Long boardId, Long taskListId, String newLabel){
        Client client = ClientBuilder.newClient(new ClientConfig());
        Map<String, String> body = new HashMap<>();
        body.put("name", newLabel);
        Response response = client.target(SERVER).path("api/boards/" + boardId + "/" + taskListId + "/edit").request().method("PUT", Entity.json(body));
        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public boolean editNestedTask(Long boardId, Long taskListId, Long taskId, Long nestedId, String name, Boolean complete){
        Client client = ClientBuilder.newClient();
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("isCompleted", complete.toString());
        Response response = client.target(SERVER).path("api/boards/"+ boardId + "/"+ taskListId + "/"+taskId+"/"+nestedId+"/edit-nested")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(body, APPLICATION_JSON));

        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public boolean deleteNestedTask(Long boardId, Long taskListId, Long taskId, Long nestedId){
        Client client = ClientBuilder.newClient(new ClientConfig());
        Response response = client.target(SERVER).path("api/boards/"+ boardId + "/"+ taskListId + "/"+taskId+"/"+nestedId).request().delete();
        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public boolean addNestedTask(Long boardId, Long taskListId, Long taskId) {
        Client client = ClientBuilder.newClient();
        Map<String, String> body = new HashMap<>();
        body.put("name", "New Subtask");
        Response response = client.target(SERVER).path("api/boards/" + boardId + "/" + taskListId + "/" + taskId + "/nestedTask")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(body, APPLICATION_JSON));

        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public boolean createTag(Long boardId, String name) {
        Client client = ClientBuilder.newClient();
        Map<String, String> body = new HashMap<>();
        if(name.trim().isEmpty() || name == null) {
            name = "New Tag";
        }
        body.put("name", name);

        Response response = client.target(SERVER).path("api/boards/" + boardId + "/add-tag")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(body, APPLICATION_JSON));

        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public boolean editTag(Long boardId, Long tagId, String name) {
        Client client = ClientBuilder.newClient();
        Map<String, String> body = new HashMap<>();
        body.put("name", name);

        Response response = client.target(SERVER).path("api/boards/" + boardId + "/" + tagId + "/editTag")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(body, APPLICATION_JSON));

        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public boolean deleteTag(Long boardId, Long tagId) {
        Client client = ClientBuilder.newClient();

        Response response = client.target(SERVER).path("api/boards/"+ boardId + "/"+ tagId + "/delete-tag").request().delete();

        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public boolean addTag(Long boardId, Long listId, Long taskId, Long tagId) {
        Client client = ClientBuilder.newClient();

        Response response = client.target(SERVER)
                .path("api/boards/" + boardId + "/" + listId + "/" + taskId + "/" + tagId)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(new HashMap<>(), APPLICATION_JSON));

        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public boolean removeTag(Long boardId, Long listId, Long taskId, Long tagId) {
        Client client = ClientBuilder.newClient();

        Response response = client.target(SERVER)
                .path("api/boards/" + boardId + "/" + listId + "/" + taskId + "/" + tagId + "/remove")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(new HashMap<>(), APPLICATION_JSON));

        int status = response.getStatus();
        response.close();
        return status == 200;
    }

    public Board getBoard(long boardID) {
        Response response;
        try {
            // If there is an input make a get request with the board id to retrieve it
            Client client = ClientBuilder.newClient();
            response = client.target(SERVER).path("api/boards/" + boardID + "/get/").request().get();
            // if there is no board with such id, put up a warning and wait for another input
            if (response.getStatus() == 404) {
                return null;
            } else if (response.getStatus() != 200) {
                throw new RuntimeException("Failed to retrieve board: HTTP error code " + response.getStatus());
            }
            Board board = ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                    .path("api/boards/" + boardID + "/get").request(APPLICATION_JSON).accept(APPLICATION_JSON).get(new GenericType<Board>() {
                    });
            return board;
        } catch (ProcessingException e) {
            return null;
        }
    }
    public static String getHost() {
        return hostName;
    }
}
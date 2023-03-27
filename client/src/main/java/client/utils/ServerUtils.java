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
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import commons.Board;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientProperties;

public class ServerUtils {
    private static String SERVER = "http://localhost:8080/";

    public static void setHost(String hostname) {
        SERVER = "http://" + hostname + ":8080/";
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
        Response response = client.target(SERVER).path("api/boards/" + boardId).request().get();
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

    public void changeBoardName(String name, long boardId) {
        System.out.println("client2");
        Response response = null;
        try {
            response = ClientBuilder.newClient()
                    .target(SERVER).path("api/boards/" + boardId + "/patch")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    //.method("PATCH", Entity.entity(name, MediaType.APPLICATION_JSON_TYPE));
                    .put(Entity.entity(name, MediaType.APPLICATION_JSON_TYPE)); // fix this and make it send the new name
        } catch (Exception e) {
            System.out.println(response);
        }
    }
}
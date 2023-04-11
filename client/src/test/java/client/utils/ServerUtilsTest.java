package client.utils;

import commons.Board;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServerUtilsTest {

    @Mock
    // creates a mock object for ServerUtils
    private ServerUtils sut;
    @Mock
    // creates a mock object for WebTarget
    private WebTarget mockTarget;
    @Mock
    // creates a mock object for Client
    private Client mockClient;
    @Mock
    // creates a mock object for MockedStatic<ClientBuilder>
    private MockedStatic<ClientBuilder> MockedStaticClientBuilder;
    @Mock
    // creates a mock object for Invocation.Builder
    private Invocation.Builder mockInvocation;

    @BeforeEach
        // sets up the objects and mocks needed for the tests
    void setUp() {
        // creates a new ServerUtils object
        this.sut = new ServerUtils();
        // creates a mock WebTarget object
        this.mockTarget = mock(WebTarget.class);
        // creates a mock ClientBuilder object
        this.MockedStaticClientBuilder = mockStatic(ClientBuilder.class);
        // creates a mock Client object
        this.mockClient = mock(Client.class);
        // creates a mock Invocation.Builder object
        this.mockInvocation = mock(Invocation.Builder.class);
        // sets up the ClientBuilder mock object
        MockedStaticClientBuilder.when(ClientBuilder::newClient).thenReturn(mockClient);
        MockedStaticClientBuilder.when(() -> ClientBuilder.newClient(any())).thenReturn(mockClient);

        // sets up the Client mock object
        when(mockClient.target(anyString())).thenReturn(mockTarget);
        when(mockTarget.path(anyString())).thenReturn(mockTarget);
        when(mockTarget.request(anyString())).thenReturn(mockInvocation);
        when(mockTarget.request()).thenReturn(mockInvocation);
        when(mockInvocation.accept(anyString())).thenReturn(mockInvocation);
    }

    @AfterEach
    void tearDown() {
        // closes the mock object after each test
        MockedStaticClientBuilder.close();
    }

    @Test
        // tests the setHost() method of ServerUtils
    void testSetHost() {
        // sets the host to "localhost"
        sut.setHost("localhost");
        // checks if the host was set correctly
        assertEquals("localhost", sut.getHost());
        // checks if the SERVER URL was generated correctly
        assertEquals("http://localhost:8080/", sut.getSERVER());
    }

    @Test
        // tests the getServerUrl() method of ServerUtils
    void testGetServerUrl(){
        // sets the host to "localhost"
        sut.setHost("localhost");
        // checks if the SERVER URL was generated correctly
        assertEquals("http://localhost:8080/", sut.getServerUrl());
    }

    @Test
        // tests the ping() method of ServerUtils when successful
    void testPing_SUCCESFUL() {
        // sets up the mock object for the ping() method to return "pong"
        when(mockInvocation.get(String.class)).thenReturn("pong");
        // checks if the ping() method returns true
        assertEquals(true, sut.ping());
    }

    @Test
        // tests the ping() method of ServerUtils when unsuccessful
    void testPing_UNSUCCESFUL() {
        // sets up the mock object for the ping() method to throw an exception
        when(mockInvocation.get(String.class)).thenThrow(new ProcessingException("Pong not found"));
        // checks if the ping() method returns false
        assertEquals(false, sut.ping());
    }

    @Test
        // This test case checks if the method boardExists() in the ServerUtils class correctly returns true if a Board object is successfully retrieved from the server.
    void testBoardExists(){
        // Create a new Board object with id 1 and name "test"
        Board board = new Board(1L, "test");
        // Mock the GET request to return a response with a Board object with status 200 OK
        when(mockInvocation.get()).thenReturn(Response.ok(board).build());
        // Assert that the boardExists() method returns true when given the id "1"
        assertEquals(true, sut.boardExists("1"));
    }

    @Test
        // This test case checks if the method getHost() in the ServerUtils class correctly returns the value of the host variable.
    void testGetHost() {
        // Assert that the getHost() method returns "localhost"
        assertEquals("localhost", sut.getHost());
    }

    @Test
        // This test case checks if the method getSERVER() in the ServerUtils class correctly returns the value of the SERVER variable.
    void testGetServer() {
        // Assert that the getSERVER() method returns "http://localhost:8080/"
        assertEquals("http://localhost:8080/", sut.getSERVER());
    }

    @Test
        // This test case checks if the method getWSSERVER() in the ServerUtils class correctly returns the value of the WSSERVER variable.
    void testGetWSServer() {
        // Assert that the getWSSERVER() method returns "ws://localhost:8080/"
        assertEquals("ws://localhost:8080/", sut.getWSSERVER());
    }

}
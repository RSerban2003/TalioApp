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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServerUtilsTest {

    @Mock
    private ServerUtils sut;
    @Mock
    private WebTarget mockTarget;
    @Mock
    private Client mockClient;
    @Mock
    private MockedStatic<ClientBuilder> MockedStaticClientBuilder;
    @Mock
    private Invocation.Builder mockInvocation;
    @Mock
    private Response mockResponse;

    @BeforeEach
    void setUp() {
        this.sut = new ServerUtils();

        this.mockTarget = mock(WebTarget.class);
        this.MockedStaticClientBuilder = mockStatic(ClientBuilder.class);
        this.mockClient = mock(Client.class);
        this.mockInvocation = mock(Invocation.Builder.class);
        this.mockResponse = mock(Response.class);

        MockedStaticClientBuilder.when(ClientBuilder::newClient).thenReturn(mockClient);
        MockedStaticClientBuilder.when(() -> ClientBuilder.newClient(any())).thenReturn(mockClient);

        when(mockClient.target(anyString())).thenReturn(mockTarget);
        when(mockTarget.path(anyString())).thenReturn(mockTarget);
        when(mockTarget.request(anyString())).thenReturn(mockInvocation);
        when(mockTarget.request()).thenReturn(mockInvocation);
        when(mockInvocation.accept(anyString())).thenReturn(mockInvocation);
    }

    @AfterEach
    void tearDown() {
        MockedStaticClientBuilder.close();
    }

    @Test
    void testSetHost() {
        sut.setHost("localhost");
        assertEquals("localhost", sut.getHost());
        assertEquals("http://localhost:8080/", sut.getSERVER());
    }

    @Test
    void testGetServerUrl(){
        sut.setHost("localhost");
        assertEquals("http://localhost:8080/", sut.getServerUrl());
    }

    @Test
    void testPing_SUCCESFUL() {
        when(mockInvocation.get(String.class)).thenReturn("pong");

        assertEquals(true, sut.ping());
    }

    @Test
    void testPing_UNSUCCESFUL() {
        when(mockInvocation.get(String.class)).thenThrow(new ProcessingException("Pong not found"));
        assertEquals(false, sut.ping());
    }

    @Test
    void testBoardExists(){
        Board board = new Board(1L, "test");
        when(mockInvocation.get()).thenReturn(Response.ok(board).build());
        assertEquals(true, sut.boardExists("1"));
    }

    @Test
    void testDeleteTaskList() {

        when(mockInvocation.delete()).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(200);

        // parameters of deleteTaskList are interchangeable with any long value
        assertTrue(sut.deleteTaskList(1L, 1L));
    }

    @Test
    void testDeleteTask() {
        when(mockInvocation.delete()).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(200);

        // parameters of deleteTask are interchangeable with any long value
        assertTrue(sut.deleteTask(1L, 1L, 1L));
    }

    @Test
    void testDeleteBoard() {
        when(mockInvocation.delete()).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(200);

        // parameters of deleteBoard are interchangeable with any long value
        assertTrue(sut.deleteBoard(1L));
    }

    @Test
    void addTask() {
        when(mockInvocation.post(any(Entity.class))).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(200);
        Map<String, String> map = mock(Map.class);
        assertTrue(sut.addTask(1L, 1L, map));
    }

    @Test
    void testEditTask() {
        when(mockTarget.queryParam(anyString(),anyString())).thenReturn(mockTarget);
        when(mockTarget.request(anyString())).thenReturn(mockInvocation);
        when(mockInvocation.post(any(Entity.class))).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(200);

        long id = 1L;
        String title = "title";
        String description = "description";
        assertTrue(sut.editTask(id, id, id, title, description));
    }

    @Test
    void testRenameTaskList() {
        when(mockInvocation.method(anyString(), any(Entity.class))).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(200);
        assertTrue(sut.renameTaskList(1L, 1L, "test"));
    }

    @Test
    void testEditNestedTask() {
        when(mockInvocation.post(any(Entity.class))).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(200);
        assertTrue(sut.editNestedTask(1L, 1L, 1L, 1L, "test", true));
    }

    @Test
    void testDeleteNestedTask() {
        when(mockInvocation.delete()).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(200);
        assertTrue(sut.deleteNestedTask(1L, 1L, 1L, 1L));
    }

    @Test
    void testAddNestedTask() {
        when(mockInvocation.post(any(Entity.class))).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(200);
        assertTrue(sut.addNestedTask(1L, 1L, 1L));
    }

    @Test
    void testGetBoard_UNSUCCESSFUL() {
        Board board = new Board(1L, "test");
        when(mockInvocation.get()).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(404);
        assertEquals(null, sut.getBoard(1L));
    }

    @Test
    void testGetBoard_UNSUCCESFUL1() {
        when(mockInvocation.get()).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenThrow(new ProcessingException("Unable to get board"));
        assertEquals(null, sut.getBoard(1L));
    }

    @Test
    void testGetHost() {
        assertEquals("localhost", sut.getHost());
    }
    @Test
    void testGetServer() {
        assertEquals("http://localhost:8080/", sut.getSERVER());
    }

    @Test
    void testGetWSServer() {
        assertEquals("ws://localhost:8080/", sut.getWSSERVER());
    }

}
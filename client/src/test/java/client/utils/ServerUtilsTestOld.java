package client.utils;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.messaging.simp.stomp.StompSession;
//import org.mockito.*;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import static org.junit.jupiter.api.Assertions.*;

//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;

class ServerUtilsTestOld {

    private ServerUtils sut;
    private static String SERVER = "http://localhost:8080";
//    private ClientBuilder mockBuilder;
    private WebTarget mockTarget;
    private ClientConfig mockConfig;
    private Client mockClient;
    private Invocation.Builder mockInvocation;

    private MockedStatic<ClientBuilder> clientBuilderMockedStatic;

    private StompSession mockSession;



    private Response mockResponse;
//    @InjectMocks
//    private ClientConfig mockConfig;

    @BeforeEach
    void setUp() {
        this.sut = Mockito.mock(ServerUtils.class);
        this.mockSession = Mockito.mock(StompSession.class);

//        this.mockBuilder = Mockito.mock(ClientBuilder.class);
        this.mockTarget = Mockito.mock(WebTarget.class);
//        this.mockConfig = Mockito.mock(ClientConfig.class);
        this.mockClient = Mockito.mock(Client.class);
        this.mockInvocation = Mockito.mock(Invocation.Builder.class);
        this.mockResponse = Mockito.mock(Response.class);
        this.clientBuilderMockedStatic = Mockito.mockStatic(ClientBuilder.class);

        clientBuilderMockedStatic.when(ClientBuilder::newClient).thenReturn(mockClient);
        clientBuilderMockedStatic.when(() -> ClientBuilder.newClient(Mockito.any())).thenReturn(mockClient);

        //when(ClientBuilder.newClient(mockConfig)).thenReturn(mockClient);
        Mockito.when(mockClient.target(Mockito.anyString())).thenReturn(mockTarget);

        Mockito.when(mockTarget.path(Mockito.anyString())).thenReturn(mockTarget);
        Mockito.when(mockTarget.request(APPLICATION_JSON)).thenReturn(mockInvocation); //?

        Mockito.when(mockInvocation.accept(Mockito.anyString())).thenReturn(mockInvocation);
//        Mockito.when(mockSession).subscribe(Mockito.anyString(), Mockito.any());

    }

    @AfterEach
    public void afterEach() {
        clientBuilderMockedStatic.close();
    }

    @Test
    void testSetHost(){
        sut.setHost("localhost");
        assertEquals("localhost", sut.getHost());
        assertEquals("http://localhost:8080/", sut.getSERVER());
        assertEquals("ws://localhost:8080/", sut.getWSSERVER());
    }

    @Test
    void testPing_SUCCESFUL(){
        String mockPong = "pong";
        sut.setHost("xyz");

        Mockito.when(mockInvocation.accept(Mockito.anyString())).thenReturn(mockInvocation);
        Mockito.when(mockInvocation.get(String.class)).thenReturn(mockPong);
        boolean result = sut.ping();
        assertTrue(result);
    }
    @Test
    void testPing_FAILURE(){
        String mockPong = "";
//        when(mockTarget.request()).thenReturn(mockInvocation);
//        when(mockInvocation.get()).thenReturn(mockResponse);
//        when(mockResponse.getStatus()).thenReturn(200);
//        when(mockResponse.readEntity(String.class)).thenReturn(mockPong);
//        when(mockResponse.readEntity()).thenReturn(mockPong);

        Mockito.when(mockInvocation.accept(Mockito.anyString())).thenReturn(mockInvocation);
        Mockito.when(mockInvocation.get(new GenericType<String>() {})).thenReturn(mockPong);
        boolean result = sut.ping();
        assertFalse(result);
    }

//    @Test
//    void testPing_Success(){
//
//        when(mockBuilder.newClient(any(ClientConfig.class))).thenReturn(mockBuilder);
//        when(mockBuilder.target(anyString())).thenReturn(mockBuilder);
//        when(mockBuilder.request()).thenReturn(mockBuilder);
//        when(mockBuilder.get()).thenReturn(mockBuilder);
//        when(mockBuilder.readEntity(String.class)).thenReturn("pong");
//        when(mockBuilder.readEntity()).thenReturn("pong");
//        mockServer.set
//
//        when(mockServer.ping()).thenReturn(true);
//        verify(mockServer).ping();
//
//        this.mockServer.expect(mockServer.ping("localhost")).andReturn(true);
//        assertTrue(ServerUtils.ping("localhost"));
//    }

    @Test
    void testBoardExists_Success(){
        Mockito.when(mockInvocation.get()).thenReturn(mockResponse);

        Mockito.when(mockResponse.getStatus()).thenReturn(200);

        assertEquals(true, sut.boardExists("1"));

//
//        when(mockResponse.readEntity(String.class)).thenReturn("pong");
//        when(mockResponse.readEntity()).thenReturn("pong");
//
//        when(mockInvocation.accept(anyString())).thenReturn(mockInvocation);
//        when(mockInvocation.get(new GenericType<String>() {})).thenReturn("pong");

//        when(mockResponse.getStatus()).thenReturn(200);
//
//        Mockito.when(sut.boardExists("1")).thenReturn(true);
//        Mockito.verify(sut).boardExists("1");
    }

    @Test
    void testBoardExists_Failure(){
        Mockito.when(sut.boardExists("1")).thenReturn(false);
        assertFalse(sut.boardExists("1"));
        Mockito.verify(sut).boardExists("1");
    }



}
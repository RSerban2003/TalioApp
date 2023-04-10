package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ConnectCtrlTest {
    AutoCloseable openMocks;
    @Mock
    private ServerUtils mockServer;
    @Mock
    private MainCtrl mockMainCtrl;
    @InjectMocks
    private ConnectCtrl sut;
    private TextField hostName;
    private Button connectButton;

//    @BeforeEach
//    void setUp() {
//
//        openMocks =  MockitoAnnotations.openMocks(this);
//
//        when(mockServer.ping()).thenReturn(true);
//        doNothing().when(mockMainCtrl).showBoard();
//        doNothing().when(mockMainCtrl).refreshBoardList();
//        doNothing().when(mockServer).establishConnection();
//        doNothing().when(mockServer).registerForMessages(eq("/topic/boardView"), eq(Board.class), any());
//
//        hostName = sut.getHostname();
//        connectButton = sut.getConnect();
//
////        MainCtrl mockMainCtrl = mock(MainCtrl.class);
////        ServerUtils mockServer = mock(ServerUtils.class);
////
////        sut = new ConnectCtrl(mockServer, mockMainCtrl);
//    }

    @Test
    void connectServerWithValidHostname() {
        hostName.setText("localhost");
        connectButton.fire();

       // verify(mockServer,times(1)).setHostname("localhost");
    }

    @Test
    void keyPressed() {
    }
}
package client.scenes;

import client.utils.ServerUtils;
import client.utils.WorkspaceUtils;
import commons.Board;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.ENTER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateBoardCtrlTest {

    @Mock
    private ServerUtils mockServer;

    @Mock
    private MainCtrl mockMainCtrl;

    @Mock
    private BoardCtrl boardCtrl;

    @Mock
    private WorkspaceUtils workspaceUtils;

    @Mock
    private TextField boardNameTextField;

    @Mock
    private Alert alert;

    @Mock
    private Response response;

    @InjectMocks
    private CreateBoardCtrl sut;

    @Test
    public void testCreateBoard() {
        // Set up test data
        String boardName = "Test Board";
        Map<String, String> body = new HashMap<>();
        body.put("name", boardName.trim());

        // Set up mocks
        when(boardNameTextField.getText()).thenReturn(boardName);
        when(mockServer.getServerUrl()).thenReturn("http://localhost:8080/");
        when(ServerUtils.getHost()).thenReturn("localhost");
        when(alert.showAndWait()).thenReturn(null);
        when(response.getStatus()).thenReturn(200);
        when(response.readEntity(Board.class)).thenReturn(new Board());

        // Call method under test
        sut.createBoard();

        // Verify that the correct methods were called
        verify(boardNameTextField).getText();
        verify(mockServer).getServerUrl();
        verify(mockServer).getHost();
        verify(mockMainCtrl).showBoard();
        verify(mockMainCtrl).getPrimaryStage();
        verify(workspaceUtils).addBoardId(any(FileWriter.class), any(Long.class));
        verify(mockMainCtrl).updateBoard(any(Board.class));
        verify(alert, never()).showAndWait();
        verify(response).close();
    }

    @Test
    void testCancel() {
        sut.cancel();

        Mockito.verify(mockMainCtrl).showConnect();
        Mockito.verify(sut).clearFields();
    }

}
/*
class CreateBoardCtrlTest {

    @Mock
    private CreateBoardCtrl sut;
    @Mock
    private ServerUtils mockServer;
    @Mock
    private MainCtrl mockMainCtrl;
    @Mock
    private BoardCtrl mockBoardCtrl;
    @Mock
    private WorkspaceUtils mockWorkspaceUtils;
    @FXML
    private TextField boardNameTextField;

    @BeforeEach
    public void setup() {
        ServerUtils mockServer = Mockito.mock(ServerUtils.class);
        MainCtrl mockMainCtrl = Mockito.mock(MainCtrl.class);
        BoardCtrl mockBoardCtrl = Mockito.mock(BoardCtrl.class);
        WorkspaceUtils mockWorkspaceUtils = Mockito.mock(WorkspaceUtils.class);

        sut = new CreateBoardCtrl(mockServer, mockMainCtrl, mockBoardCtrl, mockWorkspaceUtils);
    }

    @Test
    void createBoardConstructorTest() {
        assertEquals(mockServer, sut.getServer());
        assertEquals(mockMainCtrl, sut.getMainCtrl());
        assertEquals(mockBoardCtrl, sut.getBoardCtrl());
        assertEquals(mockWorkspaceUtils, sut.getWorkspaceUtils());
    }



    @Test
    void testClearFields(){
        TextField mockTextField = Mockito.mock(TextField.class);
        sut.setBoardNameTextField(mockTextField);
        sut.clearFields();
        Mockito.verify(mockTextField).clear();
    }

    /*
    @Test
    public void testKeyPressedEnter() {
        KeyEvent mockKeyEvent = Mockito.mock(KeyEvent.class);
        Mockito.when(mockKeyEvent.getCode()).thenReturn(KeyCode.ENTER);
        sut.keyPressed(mockKeyEvent);
        assertNull(ENTER);
    }

    @Test
    public void testKeyPressedEscape() {
        // Given
        KeyEvent mockKeyEvent = Mockito.mock(KeyEvent.class);
        Mockito.when(mockKeyEvent.getCode()).thenReturn(KeyCode.ESCAPE);
        YourClass yourClass = Mockito.spy(new YourClass());
        Mockito.doNothing().when(yourClass).cancel();

        // When
        yourClass.keyPressed(mockKeyEvent);

        // Then
        // Add assertions here to verify that the correct code path was taken.
        Mockito.verify(yourClass).cancel();
    }

    @Test
    public void testKeyPressedDefault() {
        // Given
        KeyEvent mockKeyEvent = Mockito.mock(KeyEvent.class);
        Mockito.when(mockKeyEvent.getCode()).thenReturn(KeyCode.F1);
        // When
        sut.keyPressed(mockKeyEvent);

        // Then
        // Add assertions here to verify that the correct code path was taken.
    }


    @Test
    void getBoardNameTextField() {
        CreateBoardCtrl mockCreateBoardCtrl = mock(CreateBoardCtrl.class);
        TextField mockTextField = new TextField("School");
        when(mockCreateBoardCtrl.getBoardNameTextField()).thenReturn(mockTextField);
        assertEquals(mockTextField, mockCreateBoardCtrl.getBoardNameTextField());
    }

    @Test
    void testGetServer() {
        when(sut.getServer()).thenReturn(mockServer);
        assertEquals(mockServer, sut.getServer());
    }

    @Test
    void testGetMainCtrl() {
        when(sut.getMainCtrl()).thenReturn(mockMainCtrl);
        assertEquals(mockMainCtrl, sut.getMainCtrl());
    }

    @Test
    void testGetBoardCtrl() {
        when(sut.getBoardCtrl()).thenReturn(mockBoardCtrl);
        assertEquals(mockBoardCtrl, sut.getBoardCtrl());
    }

    @Test
    void testGetWorkspaceUtils() {
        when(sut.getWorkspaceUtils()).thenReturn(mockWorkspaceUtils);
        assertEquals(mockWorkspaceUtils, sut.getWorkspaceUtils());
    }

    @Test
    void testSetBoardNameTextField() {
        TextField mockTextField = Mockito.mock(TextField.class);
        sut.setBoardNameTextField(mockTextField);
        assertEquals(mockTextField, sut.getBoardNameTextField());
    }
}
*/
package client.scenes;

import client.components.NestedTaskComponent;
import client.utils.ServerUtils;
import commons.Task;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TaskOverviewCtrlTest {
    @InjectMocks
    private TaskOverviewCtrl sut;
    @Mock
    private ServerUtils mockServer;
    @Mock
    private MainCtrl mockMainCtrl;
    @Mock
    private NestedTaskComponent mockNestedTaskComponent;
    @Mock
    private SimpleObjectProperty<Task> observableTask;
    @InjectMocks
    private TextArea titleTextArea;

    @BeforeEach
    void setUp() {
        MainCtrl mockMainCtrl = mock(MainCtrl.class);
        ServerUtils mockServer = mock(ServerUtils.class);

//        this.mockNestedTaskComponent = mock(NestedTaskComponent.class);

        this.observableTask = mock(SimpleObjectProperty.class);

        sut = new TaskOverviewCtrl(mockServer, mockMainCtrl);
    }

    @Test
    void testSetIDs() {
        sut.setIDs(1, 2, 3);

        sut.setObservableTask(observableTask);

        assertEquals(1, sut.getBoardID());
        assertEquals(2, sut.getTaskListID());
        assertEquals(3, sut.getTaskID());
        assertEquals(1, sut.getNestedTaskComponent().getBoardId());
        assertEquals(2, sut.getNestedTaskComponent().getTaskListId());
    }

    @Test
    void getBoardID() {
        sut.setIDs(1, 2, 3);
        assertEquals(1, sut.getBoardID());
    }

    @Test
    void getTaskListID() {
        sut.setIDs(1, 2, 3);
        assertEquals(2, sut.getTaskListID());
    }

    @Test
    void getTaskID() {
        sut.setIDs(1, 2, 3);
        assertEquals(3, sut.getTaskID());
    }


//    @Test
//    void testUpdateScene() {
//        Task mockTask = new Task(1L, "name", "d");
//        sut.updateScene(mockTask);
//
//        TextArea mockTitleTextArea = mock(TextArea.class);
//        TextArea descriptionTextArea = mock(TextArea.class);
//        AnchorPane mockNestedAnchorPane = mock(AnchorPane.class);
//
//
//        verify(mockTitleTextArea).setText("name1");
//        verify(descriptionTextArea).setText("d1");
//        verify(observableTask).set(mockTask);
//        verify(mockNestedAnchorPane).getChildren().clear();
//        verify(mockNestedAnchorPane).getChildren().add(mockNestedTaskComponent);
//
////        TextArea mockTitleTextArea = mock(TextArea.class);
////        TextArea mockDescriptionTextArea = mock(TextArea.class);
////        Task mockObservableTask = mock(Task.class);
////        AnchorPane mockNestedTaskAnchorPane = mock(AnchorPane.class);
//
//    }
//
//    @Test
//    void keyPressed() {
//    }
}
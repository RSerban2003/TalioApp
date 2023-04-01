package client.components;

import client.utils.ServerUtils;
import client.scenes.MainCtrl;
import commons.Board;
import commons.TaskList;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;


import javafx.scene.control.Alert;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.Dragboard;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class BoardComponent extends AnchorPane {
    private SimpleObjectProperty<Board> board;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    private final int TASKLISTOFFSET = 250;

    private final int TASKHEIGHT = 100;

    public BoardComponent(SimpleObjectProperty<Board> board, ServerUtils server, MainCtrl mainCtrl) {
        super();
        this.board = board;
        this.server = server;
        this.mainCtrl = mainCtrl;
        board.addListener((observable, oldValue, newValue) -> update(newValue));
    }
    public void update(Board board) {
        Platform.runLater(
            () -> {
                TaskListComponent[] taskLists = board.getListOfTaskList().stream().map((TaskList taskList) -> new TaskListComponent(taskList, board, server, mainCtrl)).toArray(TaskListComponent[]::new);
                HBox taskListContainer = new HBox(taskLists);
                taskListContainer.setSpacing(45.0);
                AnchorPane.setTopAnchor(taskListContainer, 150.0);
                AnchorPane.setLeftAnchor(taskListContainer, 150.0);
                getChildren().clear();
                getChildren().add(taskListContainer);
                for (TaskListComponent taskListComponent : taskLists) {
                    taskListComponent.setOnDragOver(event -> {
                        event.acceptTransferModes(TransferMode.ANY);
                        event.consume();
                    });
                    taskListComponent.setOnDragDropped(event -> {
                        Dragboard db = event.getDragboard();
                        event.setDropCompleted(db.hasString());
                        AnnotationConfigApplicationContext context
                            = new AnnotationConfigApplicationContext();
                        context.scan("client");
                        context.refresh();
                        ServerUtils server = context.getBean(ServerUtils.class);
                        Map<String, Long> params = (Map<String, Long>) db.getContent(TaskListComponent.mapFormat);
                        int index = (int) ((event.getSceneY() - TASKLISTOFFSET) / TASKHEIGHT);
                        server.moveTask(board.getId(), params.get("taskListId"), taskListComponent.getTaskList().getId(), params.get("taskId"), index);
                        event.consume();
                    });
                }
                if (board.getId() == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Board Has been deleted, please select another board to view.");
                    alert.showAndWait();
                    mainCtrl.showBoardinput();
                }
            }
        );
    }
}

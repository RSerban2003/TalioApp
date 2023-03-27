package client.components;

import client.utils.ServerUtils;
import commons.Board;
import javafx.event.EventHandler;
import commons.TaskList;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javafx.scene.Node;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.input.Dragboard;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class BoardComponent extends AnchorPane {
    private SimpleObjectProperty<Board> board;
    private ServerUtils server;
    public BoardComponent(SimpleObjectProperty<Board> board, ServerUtils server) {
        super();
        this.board = board;
        this.server = server;
        board.addListener((observable, oldValue, newValue) -> update(newValue, server));
    }
    public void update(Board board, ServerUtils server) {
        Platform.runLater(
            () -> {
                TaskListComponent[] taskLists = board.getListOfTaskList().stream().map((TaskList taskList) -> new TaskListComponent(taskList, board, server)).toArray(TaskListComponent[]::new);
                HBox taskListContainer = new HBox(taskLists);
                taskListContainer.setSpacing(45.0);
                AnchorPane.setTopAnchor(taskListContainer, 150.0);
                AnchorPane.setLeftAnchor(taskListContainer, 150.0);
                getChildren().clear();
                getChildren().add(taskListContainer);
                for (TaskListComponent taskListComponent : taskLists) {
                    taskListComponent.setOnDragOver(new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                            event.acceptTransferModes(TransferMode.ANY);
                            event.consume();
                        }
                    });
                    taskListComponent.setOnDragDropped(new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                            Dragboard db = event.getDragboard();
                            event.setDropCompleted(db.hasString());
                            AnnotationConfigApplicationContext context
                                = new AnnotationConfigApplicationContext();
                            context.scan("client");
                            context.refresh();
                            ServerUtils server = context.getBean(ServerUtils.class);
                            Map params = (Map) db.getContent(TaskListComponent.mapFormat);
                            server.moveTask(board.getId(), (Long) params.get("taskListId"), taskListComponent.getTaskList().getId(), (Long) params.get("taskId"), 0);
                            event.consume();
                        }
                    });
                }
            }
        );
    }
}

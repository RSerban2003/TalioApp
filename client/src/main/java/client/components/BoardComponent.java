package client.components;

import commons.Board;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.input.Dragboard;

public class BoardComponent extends AnchorPane {
    public BoardComponent(Board board) {
        super();
        TaskListComponent[] taskLists = board.getListOfTaskList().stream().map(TaskListComponent::new).toArray(TaskListComponent[]::new);
        for (TaskListComponent taskListComponent : taskLists) {
            taskListComponent.setOnDragDropped(new EventHandler<DragEvent>() {
                public void handle(DragEvent event) {
                    Dragboard db = event.getDragboard();
                    event.setDropCompleted(db.hasString());
                    event.consume();
                }
            });
        }
        HBox taskListContainer = new HBox(taskLists);
        taskListContainer.setSpacing(45.0);
        AnchorPane.setTopAnchor(taskListContainer, 150.0);
        AnchorPane.setLeftAnchor(taskListContainer, 150.0);
        getChildren().add(taskListContainer);
    }
}

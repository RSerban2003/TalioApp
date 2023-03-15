package client.components;

import commons.Board;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;


public class BoardComponent extends AnchorPane {
    public BoardComponent(Board board) {
        super();
        Node[] taskLists = board.getTasklist().stream().map(TaskListComponent::new).toArray(Node[]::new);
        HBox taskListContainer = new HBox(taskLists);
        taskListContainer.setSpacing(45.0);
        AnchorPane.setTopAnchor(taskListContainer, 150.0);
        AnchorPane.setLeftAnchor(taskListContainer, 150.0);
        getChildren().add(taskListContainer);
    }
}

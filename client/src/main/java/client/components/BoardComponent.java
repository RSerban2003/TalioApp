package client.components;

import commons.Board;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;


public class BoardComponent extends AnchorPane {
    public BoardComponent(Board board) {
        super();
        Node[] taskLists = board.getTasklist().stream().map(TaskListComponent::new).toArray(Node[]::new);
        TilePane taskListContainer = new TilePane(taskLists);
        AnchorPane.setTopAnchor(taskListContainer, 150.0);
        AnchorPane.setLeftAnchor(taskListContainer, 150.0);
        this.getChildren().add(taskListContainer);
    }
}

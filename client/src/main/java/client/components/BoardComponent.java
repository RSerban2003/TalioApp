package client.components;

import commons.Board;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;


public class BoardComponent extends AnchorPane {
    private SimpleObjectProperty<Board> board;
    public BoardComponent(SimpleObjectProperty<Board> board) {
        super();
        this.board = board;
        board.addListener((observable, oldValue, newValue) -> update(newValue));
    }
    public void update(Board board) {
        Platform.runLater(
            () -> {
                Node[] taskLists = board.getListOfTaskList().stream().map(TaskListComponent::new).toArray(Node[]::new);
                HBox taskListContainer = new HBox(taskLists);
                taskListContainer.setSpacing(45.0);
                AnchorPane.setTopAnchor(taskListContainer, 150.0);
                AnchorPane.setLeftAnchor(taskListContainer, 150.0);
                getChildren().clear();
                getChildren().add(taskListContainer);
            }
        );

    }
}

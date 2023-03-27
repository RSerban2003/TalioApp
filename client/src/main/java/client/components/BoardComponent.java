package client.components;

import client.utils.ServerUtils;
import client.scenes.MainCtrl;
import commons.Board;
import commons.TaskList;
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
    private ServerUtils server;
    private MainCtrl mainCtrl;
    public BoardComponent(SimpleObjectProperty<Board> board, ServerUtils server, MainCtrl mainCtrl) {
        super();
        this.board = board;
        this.server = server;
        board.addListener((observable, oldValue, newValue) -> update(newValue, server));
    }
    public void update(Board board, ServerUtils server) {
        Platform.runLater(
            () -> {
                Node[] taskLists = board.getListOfTaskList().stream().map((TaskList taskList) -> new TaskListComponent(taskList, board, server)).toArray(Node[]::new);
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

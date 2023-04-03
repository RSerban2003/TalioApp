package client.components;

import client.utils.ServerUtils;
import client.scenes.MainCtrl;
import commons.Board;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.Map;

import java.util.List;

public class ListBoardComponent extends AnchorPane {
    private SimpleObjectProperty<List<Board>> listBoard;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    public ListBoardComponent(SimpleObjectProperty<List<Board>> listBoard, ServerUtils server, MainCtrl mainCtrl) {
        super();
        this.listBoard = listBoard;
        this.server = server;
        this.mainCtrl = mainCtrl;
        listBoard.addListener((observable, oldValue, newValue) -> update(newValue));
    }

    public void update(List<Board> boardList){
        Platform.runLater(() -> {
            SubListBoardComponent[] taskLists = boardList.stream()
                    .map((board) -> new SubListBoardComponent(board, mainCtrl, server))
                    .toArray(SubListBoardComponent[]::new);
            VBox taskListContainer = new VBox(taskLists);
            taskListContainer.setSpacing(45.0);
            AnchorPane.setTopAnchor(taskListContainer, 150.0);
            AnchorPane.setLeftAnchor(taskListContainer, 150.0);

            HBox hbox = new HBox(taskListContainer);
            hbox.setSpacing(20.0);
            hbox.setLayoutX(50.0);
            hbox.setLayoutY(50.0);

            getChildren().clear();
            getChildren().add(hbox);
        });
    }
    public void refresh() {
        update(listBoard.get());
    }
}

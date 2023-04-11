package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.*;

import javax.inject.Inject;

public class AddTagComponent extends VBox {

    private Long boardId;
    private Task task;
    private MainCtrl mainCtrl;

    private SimpleObjectProperty<Board> board;

    private SimpleObjectProperty<Tag> observableTag;


    private ServerUtils server;

    private static final String style = "-fx-background-color: #615f5e; -fx-border-width: 2; -fx-border-color: #615f5e;"
            + "-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";

    @Inject
    public AddTagComponent(SimpleObjectProperty<Board> board, MainCtrl mainCtrl, ServerUtils serverUtils) {
        super();
        this.board = board;
        this.observableTag = new SimpleObjectProperty<Tag>();
        this.mainCtrl = mainCtrl;
        this.server = serverUtils;
        board.addListener((observable, oldValue, newValue) -> update(newValue));
    }

    private void update(Board board) {
        Platform.runLater(
                () -> {
                    getChildren().clear();
                    HBox hbox = new HBox();
                    if(board.getTagList() != null || board.getTagList().size() > 0) {
                        SubTagComponent[] nestedTasks = board.getTagList().stream()
                                .map((Tag tag1) -> new SubTagComponent(tag1, mainCtrl, board.getId(), server))
                                .toArray(SubTagComponent[]::new);
                        VBox taskListContainer = new VBox(nestedTasks);
                        AnchorPane.setTopAnchor(taskListContainer, 150.0);
                        AnchorPane.setLeftAnchor(taskListContainer, 150.0);

                        hbox.getChildren().add(taskListContainer);
                    }
                    getChildren().add(hbox);
                });

    }

}
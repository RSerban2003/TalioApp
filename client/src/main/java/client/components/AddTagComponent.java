package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import javax.inject.Inject;

public class AddTagComponent extends VBox {

    private Long boardId;
    private Long listId;
    private Long taskId;
    private Task task;
    private MainCtrl mainCtrl;
    private SimpleObjectProperty<Task> task2;

    private SimpleObjectProperty<Board> board;
    private ServerUtils server;

    private static final String style = "-fx-background-color: #615f5e; -fx-border-width: 2; -fx-border-color: #615f5e;"
            + "-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";

    @Inject
    public AddTagComponent(SimpleObjectProperty<Board> board, SimpleObjectProperty<Task> task, MainCtrl mainCtrl, ServerUtils serverUtils) {
        super();
        this.board = board;
        this.mainCtrl = mainCtrl;
        this.server = serverUtils;
        this.task2 = task;
        task2.addListener((observable, oldValue, newValue) -> update(newValue));
    }

    public void setIds(Long boardId, Long listId, Long taskId) {
        this.boardId = boardId;
        this.listId = listId;
        this.taskId = taskId;
    }

    private void update(Task task) {
        Platform.runLater(
                () -> {
                    var tagsList = server.getBoard(boardId).getTagList();

                    System.out.println(tagsList);

                    getChildren().clear();
                    for (Tag tag : tagsList) {
                        if (task.getTagList().contains(tag)) System.out.println("\n\n\n");;
                        HBox box = new HBox();
                        Label name = new Label("");
                        name.setText(tag.getName());
                        name.setStyle("-fx-text-fill: #FFFFFF");
                        Button addButton = new Button("add to task");
                        addButton.setStyle("-fx-background-color: #454342; -fx-text-fill: #FFFFFF");
                        addButton.setOnAction(event -> {
                            server.addTag(task.getTaskList().getBoard().getId(), task.getTaskList().getId(), taskId, tag.getId());
                        });
                        box.getChildren().addAll(name,addButton);
                        box.setSpacing(20);
                        getChildren().add(box);
                    }
            });

    }

}
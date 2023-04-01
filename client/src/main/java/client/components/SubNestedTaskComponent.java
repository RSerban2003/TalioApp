package client.components;

import client.scenes.MainCtrl;
import commons.Board;
import commons.NestedTask;
import commons.Task;
import commons.TaskList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class SubNestedTaskComponent extends VBox {
    private final long nestedId;
    private static final String style = "-fx-background-color: #f7f7f5; -fx-border-width: 2; -fx-border-color: gray;  -fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";

    private MainCtrl mainCtrl;
    @Inject
    public SubNestedTaskComponent(NestedTask nestedTask, Task task, TaskList taskList, Board board, MainCtrl mainCtrl) {
        super();
        this.nestedId = nestedTask.getId();
        this.mainCtrl = mainCtrl;
        //Creates the name box
        Label nameLabel = new Label(nestedTask.getName());
        HBox topRow = new HBox(nameLabel);
        topRow.setAlignment(Pos.CENTER);

        setMaxSize(300, 40);
        setMinSize(300, 40);
        getChildren().add(topRow);
        setAlignment(Pos.CENTER);
        setStyle(style);
        setSpacing(10.0);
    }
}

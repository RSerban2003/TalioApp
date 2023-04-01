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
    private MainCtrl mainCtrl;
    @Inject
    public SubNestedTaskComponent(NestedTask nestedTask, Task task, TaskList taskList, Board board, MainCtrl mainCtrl) {
        super();
        this.mainCtrl = mainCtrl;
        //Creates the name box
        Label nameLabel = new Label(nestedTask.getName());
        HBox topRow = new HBox(nameLabel);
        topRow.setAlignment(Pos.CENTER);
    }
}

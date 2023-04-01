package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.NestedTask;
import commons.Task;
import commons.TaskList;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class NestedTaskComponent extends AnchorPane {
    private SimpleObjectProperty<Task> task;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    public NestedTaskComponent(SimpleObjectProperty<Task> task, ServerUtils server, MainCtrl mainCtrl) {
        super();
        this.task = task;
        this.server = server;
        this.mainCtrl = mainCtrl;
        task.addListener((observable, oldValue, newValue) -> update(newValue, task.get().getTaskList(), task.get().getTaskList().getBoard()));
    }

    public void update(Task task, TaskList list, Board board){
        Platform.runLater(
                () -> {
                    SubNestedTaskComponent[] nestedTask = task.getNestedTasks().stream().map((NestedTask nestedTask1) -> new SubNestedTaskComponent(nestedTask1, task, list, board, mainCtrl)).toArray(SubNestedTaskComponent[]::new);
                    HBox nestedTaskContainer = new HBox(nestedTask);
                    nestedTaskContainer.setSpacing(45.0);
                    AnchorPane.setTopAnchor(nestedTaskContainer, 150.0);
                    AnchorPane.setLeftAnchor(nestedTaskContainer, 150.0);
                    getChildren().clear();
                    getChildren().add(nestedTaskContainer);
        });
    }
}

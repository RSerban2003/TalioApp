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
import javafx.scene.layout.VBox;

public class NestedTaskComponent extends AnchorPane {
    private SimpleObjectProperty<Task> task;
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private Long boardId;
    private Long taskListId;

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public void setTaskListId(Long taskListId) {
        this.taskListId = taskListId;
    }

    public NestedTaskComponent(SimpleObjectProperty<Task> task, ServerUtils server, MainCtrl mainCtrl) {
        super();
        this.task = task;
        this.server = server;
        this.mainCtrl = mainCtrl;
        task.addListener((observable, oldValue, newValue) -> update(newValue));
    }

    public void update(Task task){
        Platform.runLater(
                () -> {
                SubNestedTaskComponent[] nestedTask = task.getNestedTasks().stream()
                        .map((NestedTask nestedTask1) -> new SubNestedTaskComponent(nestedTask1, task, taskListId, boardId, mainCtrl))
                        .toArray(SubNestedTaskComponent[]::new);
                VBox taskListContainer = new VBox(nestedTask);
                AnchorPane.setTopAnchor(taskListContainer, 150.0);
                AnchorPane.setLeftAnchor(taskListContainer, 150.0);

                HBox hbox = new HBox(taskListContainer);

                getChildren().clear();
                getChildren().add(hbox);
        });
    }
}

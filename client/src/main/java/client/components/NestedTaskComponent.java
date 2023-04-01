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
                        .map((NestedTask nestedTask1) -> new SubNestedTaskComponent(nestedTask1, task, null, null, mainCtrl))
                        .toArray(SubNestedTaskComponent[]::new);
                VBox taskListContainer = new VBox(nestedTask);
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
}

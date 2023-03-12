package client.components;

import commons.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TaskComponent extends VBox {
    public TaskComponent(Task task) {
        super();
        Label title = new Label(task.getName());
        this.getChildren().add(title);
    }
}

package client.components;

import commons.TaskList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TaskListComponent extends VBox {
    private static final String style = "-fx-background-color: #c7c7c7; -fx-border-width: 2; -fx-border-color: gray;";
    public TaskListComponent(TaskList taskList) {
        super();
        Node[] tasks = taskList.getTaskList().stream().map(TaskComponent::new).toArray(Node[]::new);
        this.getChildren().add(new Label(taskList.getName()));
        this.getChildren().addAll(tasks);
        this.setStyle(style);
        this.setPrefSize(270, 700);
    }
}

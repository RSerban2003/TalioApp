package client.components;

import commons.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TaskComponent extends VBox {
    private static final String style = "-fx-background-color: #f7f7f5; -fx-border-width: 2; -fx-border-color: gray;  -fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";
    private long taskId;
    public TaskComponent(Task task) {
        super();
        Label title = new Label(task.getName());
        getChildren().add(title);
        setMaxSize(200, 80);
        setMinSize(200, 80);
        setStyle(style);
        setAlignment(Pos.CENTER);
        this.taskId = task.getId();
    }
    public long getTaskId() {
        return taskId;
    }
}

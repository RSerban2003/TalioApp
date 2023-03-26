package client.components;

import commons.TaskList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

public class TaskListComponent extends VBox {
    private static final String style = "-fx-background-color: #c7c7c7; -fx-border-width: 2; -fx-border-color: gray; -fx-font-weight: bold; -fx-border-radius: 10 10 10 10; -fx-background-radius: 10 10 10 10;";
    public TaskListComponent(TaskList taskList) {
        super();
        TaskComponent[] tasks = taskList.getTaskList().stream().map(TaskComponent::new).toArray(TaskComponent[]::new);
        for (TaskComponent task: tasks) {
            task.setOnDragDetected(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    Dragboard db = task.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(Long.toString(task.getTaskId()));
                    db.setContent(content);
                    event.consume();
                }
            });
        }
        getChildren().add(new Label(taskList.getName()));
        getChildren().addAll(tasks);
        setStyle(style);
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(270, 700);
        setSpacing(20.0);
    }
}

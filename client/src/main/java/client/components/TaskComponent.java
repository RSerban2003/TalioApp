package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TaskComponent extends VBox {
    private static final String style = "-fx-background-color: #f7f7f5; -fx-border-width: 2; -fx-border-color: gray;  -fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";
    private final long taskId;
    private MainCtrl mainCtrl;
    public TaskComponent(Task task, TaskList taskList, Board board, MainCtrl mainCtrl) {
        super();
        this.mainCtrl = mainCtrl;

        //Creates the name box
        Label nameLabel = new Label(task.getName());
        HBox topRow = new HBox(nameLabel);
        topRow.setAlignment(Pos.TOP_LEFT);

        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);

        // Creates button for editing tasks
        Button editButton = new Button("Edit");
        editButton.setMinWidth(80);
        editButton.setMaxWidth(80);
        editButton.setOnAction(event -> {
            mainCtrl.setTask(task);
            mainCtrl.showEditTask();
        });

        // Creates button for deleting tasks
        Button deleteButton = new Button("X");
        AnnotationConfigApplicationContext context
            = new AnnotationConfigApplicationContext();
        context.scan("client");
        context.refresh();
        ServerUtils server = context.getBean(ServerUtils.class);
        deleteButton.setOnAction(event -> server.deleteTask(board.getId(), taskList.getId(), task.getId()));

        //Adds delete button to topRow box
        topRow.getChildren().add(deleteButton);
        topRow.setPadding(new Insets(0, 10, 10, 10));

        //Creates individual box for edit button
        HBox editButtonBox = new HBox(editButton);
        editButtonBox.setAlignment(Pos.CENTER);
        editButtonBox.setPadding(new Insets(5, 0, 0, 0));

        VBox container = new VBox(topRow, editButtonBox);
        container.setAlignment(Pos.CENTER);

        getChildren().add(container);

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

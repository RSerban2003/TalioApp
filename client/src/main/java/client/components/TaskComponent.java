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
        VBox nameBox = new VBox(nameLabel);
        nameBox.setAlignment(Pos.TOP_LEFT);
        nameBox.setPadding(new Insets(0, 0, 0, 20));

        // Creates button for editing tasks
        Button editButton = new Button("Edit");
        editButton.setMinWidth(80);
        editButton.setMaxWidth(80);
        editButton.setOnAction(event -> {
            mainCtrl.setTask(task);
            mainCtrl.showEditTask();
        });

        // Creates button for deleting tasks
        Button deleteButton = new Button("Delete");
        deleteButton.setMinWidth(80);
        deleteButton.setMaxWidth(80);
        AnnotationConfigApplicationContext context
            = new AnnotationConfigApplicationContext();
        context.scan("client");
        context.refresh();
        ServerUtils server = context.getBean(ServerUtils.class);
        deleteButton.setOnAction(event -> server.deleteTask(board.getId(), taskList.getId(), task.getId()));

        //Creates individual boxes for each button
        HBox editButtonBox = new HBox(editButton);
        editButtonBox.setAlignment(Pos.BOTTOM_LEFT);

        HBox deleteButtonBox = new HBox(deleteButton);
        deleteButtonBox.setAlignment(Pos.BOTTOM_RIGHT);

        //Adds the 'Edit' and 'Delete' button boxes to the same box
        HBox buttonsBox = new HBox(editButton, deleteButton);
        buttonsBox.setSpacing(20.0);
        buttonsBox.setPadding(new Insets(10, 0, 0, 8));

        //Adds the buttons box and name box to the same box
        VBox container = new VBox(nameBox, buttonsBox);
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

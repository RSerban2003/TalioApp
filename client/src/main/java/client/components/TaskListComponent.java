package client.components;

import client.utils.ServerUtils;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;


public class TaskListComponent extends VBox {
    private static final String style = "-fx-background-color: #c7c7c7; -fx-border-width: 2; -fx-border-color: gray; -fx-font-weight: bold; -fx-border-radius: 10 10 10 10; -fx-background-radius: 10 10 10 10;";
    public static final DataFormat mapFormat = new DataFormat("map");
    private final TaskList taskList;

    public TaskListComponent(TaskList taskList, Board board) {
        super();
        TaskComponent[] tasks = taskList.getTask().stream().map((Task task) -> new TaskComponent(task, taskList, board)).toArray(TaskComponent[]::new);
        for (TaskComponent task: tasks) {
            task.setOnDragDetected(event -> {
                Dragboard db = task.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.put(mapFormat, Map.of("taskId", task.getTaskId(), "taskListId", taskList.getId()));
                db.setContent(content);
                event.consume();
            });
        }

        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext();
        context.scan("client");
        context.refresh();
        ServerUtils server = context.getBean(ServerUtils.class);

        // Create label for task list name
        Label nameLabel = new Label(taskList.getName());
        nameLabel.setAlignment(Pos.CENTER);

        //VBox nameBox = new VBox(nameLabel);
        //nameBox.setAlignment(Pos.TOP_CENTER);

        // Create Edit button to edit label
        Button editButton = new Button("Edit");
        TextField nameField = new TextField();
        nameField.setPromptText("e.g. School");
        Button saveButton = new Button("Save");

        // hide text field and save button at the start
        nameField.setVisible(false);
        nameField.setMaxWidth(200);
        nameField.setAlignment(Pos.CENTER);
        saveButton.setVisible(false);

        // Create delete button
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> server.deleteTaskList(board.getId(), taskList.getId()));
        //HBox buttonBox = new HBox(deleteButton);
        //buttonBox.setAlignment(Pos.TOP_RIGHT);

        // puts all top elements in a GridPane
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setHgap(10.0);
        gridPane.setVgap(10.0);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        gridPane.add(deleteButton, 0, 0);
        gridPane.add(nameLabel, 1, 1);
        gridPane.add(nameField, 1, 1);
        gridPane.add(editButton, 2,1);
        gridPane.add(saveButton, 2, 1);

        // enables user to enter new label
        editButton.setOnAction(e -> {
            nameLabel.setVisible(false);
            editButton.setVisible(false);
            nameField.setVisible(true);
            saveButton.setVisible(true);
        });

        saveButton.setOnAction(e ->{
            if (!(nameField.getText().trim().isEmpty() || nameField.getText() == null)){
                nameLabel.setText(nameField.getText());
            }
            nameLabel.setVisible(true);
            editButton.setVisible(true);
            nameField.setVisible(false);
            saveButton.setVisible(false);
        });

        getChildren().add(gridPane);
        getChildren().addAll(tasks);
        setStyle(style);
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(300, 700);
        setSpacing(20.0);
        this.taskList = taskList;
    }
    public TaskList getTaskList() {
        return taskList;
    }
}

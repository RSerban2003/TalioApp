package client.components;

import java.util.Map;
import client.utils.ServerUtils;
import client.scenes.MainCtrl;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class TaskListComponent extends VBox {
    private static final String style = "-fx-background-color: #c7c7c7; -fx-border-width: 2; -fx-border-color: gray; -fx-font-weight: bold; -fx-border-radius: 10 10 10 10; -fx-background-radius: 10 10 10 10; -fx-padding: 0 0 50 0;";

    private final MainCtrl mainCtrl;
    public static final DataFormat mapFormat = new DataFormat("map");
    private final TaskList taskList;
    public TaskListComponent(TaskList taskList, Board board, ServerUtils server, MainCtrl mainCtrl) {
        super();
        this.mainCtrl = mainCtrl;
        TaskComponent[] tasks = taskList.getTask().stream().map((Task task) -> new TaskComponent(task, taskList, board, mainCtrl)).toArray(TaskComponent[]::new);
        for (TaskComponent task: tasks) {
            task.setOnDragDetected(event -> {
                Dragboard db = task.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.put(mapFormat, Map.of("taskId", task.getTaskId(), "taskListId", taskList.getId()));
                db.setContent(content);
                event.consume();
            });
        }

        // Create label for task list name
        Label nameLabel = new Label(taskList.getName());
        nameLabel.setAlignment(Pos.CENTER);


        // Create buttons for adding tasks
        HBox addTasksButton = new HBox();
        addTasksButton.setStyle("-fx-spacing: 0;" +
                "-fx-border-radius: 3;" + "-fx-background-color: transparent;");

        Button addDefaultTaskButton = new Button("+");
        addDefaultTaskButton.setStyle("-fx-font-weight: bold;");

        TextField taskTitleField = new TextField();

        taskTitleField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                taskTitleField.setText(oldValue);
            }
        });
        taskTitleField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });

        taskTitleField.setText("New Task");
        Button saveTaskTitleButton = new Button("Save");
        Button addCustomTaskButton = new Button("Add Custom Task");
        Button cancelDefaultTaskButton = new Button("Cancel");

        StackPane overlappingButtons = new StackPane();
        overlappingButtons.getChildren().addAll(taskTitleField, addCustomTaskButton);

        cancelDefaultTaskButton.setVisible(false);
        taskTitleField.setVisible(false);
        taskTitleField.setMaxWidth(120);
        taskTitleField.setMinWidth(120);
        addCustomTaskButton.setMaxWidth(120);
        addCustomTaskButton.setMinWidth(120);
        saveTaskTitleButton.setMaxWidth(50);
        saveTaskTitleButton.setMinWidth(50);
        cancelDefaultTaskButton.setMinWidth(70);
        cancelDefaultTaskButton.setMaxWidth(70);

        saveTaskTitleButton.setVisible(false);

        addDefaultTaskButton.setOnAction(event -> {

            taskTitleField.setVisible(true);
            saveTaskTitleButton.setVisible(true);
            addCustomTaskButton.setVisible(false);
            cancelDefaultTaskButton.setVisible(true);

        });

        saveTaskTitleButton.setOnAction(event -> {
            String title = taskTitleField.getText();
            if(title.trim().isEmpty() || title.trim() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Task name cannot be empty. Please enter a name for the task.");
                alert.showAndWait();
                return;
            }
            else {
                taskTitleField.setVisible(false);
                saveTaskTitleButton.setVisible(false);
                addCustomTaskButton.setVisible(true);

                mainCtrl.setTaskList(taskList.getId());
                mainCtrl.addDefaultTask(title);
            }
        });

        cancelDefaultTaskButton.setOnAction(event -> {
            taskTitleField.setText("New Task");
            cancelDefaultTaskButton.setVisible(false);
            saveTaskTitleButton.setVisible(false);
            taskTitleField.setVisible(false);
            addCustomTaskButton.setVisible(true);
        });
        addCustomTaskButton.setOnAction(event -> {
            mainCtrl.setTaskList(taskList.getId());
            mainCtrl.showAddTask();
        });

        addTasksButton.getChildren().addAll(addDefaultTaskButton, overlappingButtons, saveTaskTitleButton, cancelDefaultTaskButton);

        // Create Edit button to edit label
        Button editButton = new Button("Edit");
        editButton.setMinWidth(50);
        editButton.setMaxWidth(50);
        TextField nameField = new TextField(nameLabel.getText());
        Button saveButton = new Button("Save");
        saveButton.setMinWidth(50);
        saveButton.setMaxWidth(50);

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                nameField.setText(oldValue);
            }
        });
        nameField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });

        // hide text field and save button at the start
        nameField.setVisible(false);
        nameField.setMaxWidth(200);
        nameField.setAlignment(Pos.CENTER);
        saveButton.setVisible(false);

        // Create delete button
        Button deleteButton = new Button("X");
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Delete Task List");
            alert.setContentText("Are you sure you want to delete this task list? This action cannot be undone.");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                server.deleteTaskList(board.getId(), taskList.getId());
            }
            else {
                alert.close();
            }
        });

        // puts all top elements in a GridPane
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setHgap(10.0);
        gridPane.setVgap(10.0);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        gridPane.add(deleteButton, 0, 0);
        gridPane.add(nameLabel, 1, 1);
        GridPane.setHalignment(nameField, HPos.CENTER);
        gridPane.add(nameField, 1, 1);
        gridPane.add(editButton, 2,1);
        gridPane.add(saveButton, 2, 1);
        gridPane.add(addTasksButton, 1,0);

        // enables user to enter new label
        editButton.setOnAction(e -> {
            nameLabel.setVisible(false);
            editButton.setVisible(false);
            nameField.setVisible(true);
            saveButton.setVisible(true);
        });

        // saves changes of task list name to server
        saveButton.setOnAction(e ->{
            if (!(nameField.getText().trim().isEmpty() || nameField.getText() == null)){
                String nameString = nameField.getText();
                nameLabel.setText(nameString);
                server.renameTaskList(board.getId(), taskList.getId(), nameString);
            }
            nameLabel.setVisible(true);
            editButton.setVisible(true);
            nameField.setVisible(false);
            saveButton.setVisible(false);
        });

        // Puts gridPane with its elements in the task list
        getChildren().add(gridPane);
        getChildren().addAll(tasks);
        setStyle(style);
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(300, 800);
        setSpacing(20.0);
        this.taskList = taskList;
    }
    public TaskList getTaskList() {
        return taskList;
    }
}

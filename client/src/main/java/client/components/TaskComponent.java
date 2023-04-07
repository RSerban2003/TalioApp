package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.NestedTask;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import javax.inject.Inject;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class TaskComponent extends VBox {
    private static final String style = "-fx-background-color: #f7f7f5; -fx-border-width: 2; -fx-border-color: gray;  -fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";
    private final long taskId;
    private TaskList taskList;
    private Task task;
    private MainCtrl mainCtrl;
    @Inject
    public TaskComponent(Task task, TaskList taskList, Board board, MainCtrl mainCtrl) {
        super();
        this.mainCtrl = mainCtrl;
        this.taskList = taskList;
        this.task = task;

        //Creates the name box
        Label nameLabel = new Label(task.getName());
        HBox topRow = new HBox(nameLabel);
        topRow.setAlignment(Pos.TOP_LEFT);

        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);

        // Creates button for editing tasks
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #8d8d8d; -fx-text-fill: #000000");
        editButton.setMinWidth(50);
        editButton.setMaxWidth(50);
        editButton.setOnAction(event -> {
            mainCtrl.setTaskList(taskList.getId());
            mainCtrl.setTask(task);
            mainCtrl.showTaskOverview();
        });

        // Creates button for deleting tasks
        Button deleteButton = new Button("X");
        deleteButton.setStyle("-fx-background-color: #8d8d8d; -fx-text-fill: #000000");
        AnnotationConfigApplicationContext context
            = new AnnotationConfigApplicationContext();
        context.scan("client");
        context.refresh();
        ServerUtils server = context.getBean(ServerUtils.class);
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Task");
            alert.setHeaderText("Are you sure you want to delete this task? This action cannot be undone.");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                server.deleteTask(board.getId(), taskList.getId(), task.getId());
            }
            else {
                alert.close();
            }
        });

        // implementation for description indicator
        Label descriptionLabel = new Label("- See Description");
        descriptionLabel.setStyle("-fx-font-size: 10px;");
        descriptionLabel.setTextFill(Color.GRAY);
        hasDescription(descriptionLabel);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(80);
        if(task.getNestedTasks() == null || task.getNestedTasks().size() == 0) {
            progressBar.setVisible(false);
        }
        else {
            double subTasks = task.getNestedTasks().size();
            double completedSubtasks = task.getNestedTasks().stream().filter(NestedTask::getComplete).count();
            progressBar.setProgress(completedSubtasks / subTasks);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        //Adds delete button to topRow box
        topRow.getChildren().add(deleteButton);
        topRow.setPadding(new Insets(0, 10, 0, 10));

        // creates individual box for description indicator
        HBox descriptionIndicatorBox = new HBox(descriptionLabel);
        descriptionIndicatorBox.setPadding(new Insets(0, 10, 2, 10));
        descriptionIndicatorBox.setAlignment(Pos.CENTER_LEFT);

        //Creates individual box for edit button
        HBox editButtonBox = new HBox(progressBar, spacer, editButton);
        editButtonBox.setAlignment(Pos.CENTER);
        editButtonBox.setPadding(new Insets(0, 10, 0, 10));

        VBox container = new VBox(topRow, descriptionIndicatorBox, editButtonBox);
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

    public void hasDescription(Label iconLabel) {
        if (task.getDescription() == null || task.getDescription().trim().equals("")
                || task.getDescription().trim().isEmpty()) {
            iconLabel.setVisible(false);
            return;
        }
        iconLabel.setVisible(true);
    }
}

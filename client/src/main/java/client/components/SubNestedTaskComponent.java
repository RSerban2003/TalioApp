package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.NestedTask;
import commons.Task;
import commons.TaskList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.inject.Inject;

public class SubNestedTaskComponent extends VBox {
    private final long nestedId;
    private static final String style = "-fx-background-color: #f7f7f5; -fx-border-width: 2; -fx-border-color: gray;  -fx-border-radius: 1 1 1 1;-fx-background-radius: 10 10 10 10;";

    private MainCtrl mainCtrl;
    @Inject
    public SubNestedTaskComponent(NestedTask nestedTask, Task task, Long taskListId, Long boardId, MainCtrl mainCtrl) {
        super();
        this.nestedId = nestedTask.getId();
        this.mainCtrl = mainCtrl;
        //Creates the name box
        Label nameLabel = new Label(nestedTask.getName());
        nameLabel.setAlignment(Pos.CENTER);

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

        // create checkbox
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(nestedTask.getComplete());
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext();
        context.scan("client");
        context.refresh();
        ServerUtils server = context.getBean(ServerUtils.class);
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                server.editNestedTask(boardId, taskListId, task.getId(), nestedTask.getId(), nestedTask.getName(), true);
            } else {
                server.editNestedTask(boardId, taskListId, task.getId(), nestedTask.getId(), nestedTask.getName(), false);
            }
        });

        // Create delete button
        Button deleteButton = new Button("X");
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Delete Task");
            alert.setContentText("Are you sure you want to delete this task? This action cannot be undone.");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                server.deleteNestedTask(boardId, taskListId, task.getId(), nestedTask.getId());
            }
            else {
                alert.close();
            }
        });


        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setHgap(5.0);
        gridPane.setVgap(10.0);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.add(checkBox, 0, 0);
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        gridPane.add(nameLabel, 1, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(editButton, 2,0);
        gridPane.add(saveButton, 2, 0);
        gridPane.add(deleteButton, 3, 0);

        editButton.setOnAction(e -> {
            nameLabel.setVisible(false);
            editButton.setVisible(false);
            nameField.setVisible(true);
            saveButton.setVisible(true);
        });

        saveButton.setOnAction(e ->{
            if (!(nameField.getText().trim().isEmpty() || nameField.getText() == null)){
                String nameString = nameField.getText();
                nameLabel.setText(nameString);
                server.editNestedTask(boardId, taskListId, task.getId(), nestedTask.getId(), nameString, nestedTask.getComplete());
            }
            nameLabel.setVisible(true);
            editButton.setVisible(true);
            nameField.setVisible(false);
            saveButton.setVisible(false);
        });


        setMaxSize(499, 40);
        setMinSize(499, 40);
        getChildren().add(gridPane);
        setAlignment(Pos.CENTER);
        setStyle(style);
        setSpacing(10.0);
    }
    public long getNestedId() {
        return nestedId;
    }
}

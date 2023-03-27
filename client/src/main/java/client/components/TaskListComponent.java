package client.components;

import client.Main;
import client.utils.ServerUtils;
import client.scenes.MainCtrl;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TaskListComponent extends VBox {
    private static final String style = "-fx-background-color: #c7c7c7; -fx-border-width: 2; -fx-border-color: gray; -fx-font-weight: bold; -fx-border-radius: 10 10 10 10; -fx-background-radius: 10 10 10 10;";
    private MainCtrl mainCtrl;
    public TaskListComponent(TaskList taskList, Board board, ServerUtils server, MainCtrl mainCtrl) {
        super();
        this.mainCtrl = mainCtrl;
        Node[] tasks = taskList.getTask().stream().map((Task task) -> new TaskComponent(task, taskList, board, server)).toArray(Node[]::new);

        // Create label for task list name
        Label nameLabel = new Label(taskList.getName());
        VBox nameBox = new VBox(nameLabel);
        nameBox.setAlignment(Pos.TOP_CENTER);

        // Create button
        Button button = new Button("Delete");
        button.setOnAction(event -> server.deleteTaskList(board.getId(), taskList.getId()));
        HBox buttonBox = new HBox(button);
        buttonBox.setAlignment(Pos.TOP_RIGHT);

        // Create button for adding tasks
        Button addButton = new Button("Add Task");
        addButton.setOnAction(event -> {
            mainCtrl.getTaskList(taskList.getId());
            mainCtrl.showAddTask();
        });
        HBox addButtonBox = new HBox(addButton);

        // Add nameBox, buttonBox and taskBox to top row
        HBox topRow = new HBox(nameBox, buttonBox, addButtonBox);
        topRow.setSpacing(20.0);
        topRow.setAlignment(Pos.TOP_CENTER);

        // Add topRow and tasks to TaskListComponent
        getChildren().add(topRow);

        getChildren().addAll(tasks);
        setStyle(style);
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(270, 700);
        setSpacing(20.0);
    }
}

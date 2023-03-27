package client.components;

import client.utils.ServerUtils;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TaskComponent extends VBox {
    private static final String style = "-fx-background-color: #f7f7f5; -fx-border-width: 2; -fx-border-color: gray;  -fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";
    public TaskComponent(Task task, TaskList taskList, Board board, ServerUtils server) {
        super();
        Label nameLabel = new Label(task.getName());
        VBox nameBox = new VBox(nameLabel);
        nameBox.setAlignment(Pos.TOP_CENTER);


        Button button = new Button("Delete");
        button.setOnAction(event -> server.deleteTask(board.getId(), taskList.getId(), task.getId()));
        HBox buttonBox = new HBox(button);
        buttonBox.setAlignment(Pos.CENTER);

        // Add nameBox and buttonBox to top row
        HBox topRow = new HBox(nameBox, buttonBox);
        topRow.setSpacing(20.0);
        topRow.setAlignment(Pos.TOP_CENTER);
        getChildren().add(topRow);

        setMaxSize(200, 80);
        setMinSize(200, 80);
        setStyle(style);
        setAlignment(Pos.CENTER);
    }
}

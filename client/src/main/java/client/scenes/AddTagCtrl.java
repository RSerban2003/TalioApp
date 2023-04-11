package client.scenes;

import client.components.AddTagComponent;
import client.components.SubListBoardComponent;
import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import commons.Task;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AddTagCtrl {

    private static final String style = "-fx-background-color: #c7c7c7; -fx-border-width: 2; -fx-border-color: gray;  -fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";

    @FXML
    private Button LeaveButton;
    @FXML
    private VBox tagsBox;
    private AnchorPane anchorPane;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    private SimpleObjectProperty<Task> observableTask;
    private SimpleObjectProperty<Board> observableBoard;

    AddTagComponent addTagComponent;

    private Long boardID;
    private Long listID;
    private Long taskID;
    private Task task;


    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

        observableTask = new SimpleObjectProperty<Task>();
        observableBoard = new SimpleObjectProperty<Board>();

        addTagComponent = new AddTagComponent(observableBoard,observableTask,mainCtrl,server);
    }

    public void setIDs(Long boardID, Long listID, Long taskID) {
        this.boardID = boardID;
        this.listID = listID;
        this.taskID = taskID;

        addTagComponent.setIds(boardID,listID,taskID);

        server.registerForMessages("/topic/"+boardID+"/"+listID+"/"+taskID, Task.class, b -> {
            observableTask.set(b);
            setTask(b);
            mainCtrl.setTask(b);
            update(b);
        });
    }

    public void updateScene(Board board) {
        observableBoard.set(board);
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(tagsBox);
        tagsBox.getChildren().clear();
        tagsBox.getChildren().add(addTagComponent);
    }

    public void setTask(Task task) {
        this.task = task;
    }
    @FXML
    public void onLeaveButtonClicked() {
        mainCtrl.showTaskOverview();
    }

    public void update(Task task) {
        observableTask.set(task);

        /*tagsBox.getChildren().clear();
        for (Tag tag : tagsList) {
            if (task.getTagList().contains(tag)) continue;
            HBox box = new HBox();
            Label name = new Label("");
            name.setText(tag.getName());
            name.setStyle("-fx-text-fill: #FFFFFF");
            Button addButton = new Button("add to task");
            addButton.setStyle("-fx-background-color: #454342; -fx-text-fill: #FFFFFF");
            addButton.setOnAction(event -> {
                server.addTag(boardID,listID,taskID,tag.getId());
            });
            box.getChildren().addAll(name,addButton);
            box.setSpacing(20);
            tagsBox.getChildren().add(box);
        }*/

    }


}

package client.scenes;

import client.components.BoardComponent;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class BoardCtrl {
    @FXML
    private VBox boardVBox;
    @FXML
    private AnchorPane TaskListAnchorPaneID;
    private MainCtrl mainCtrl;
    @Inject
    public BoardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        TaskListAnchorPaneID = new AnchorPane();
    }
    public void updateBoard(Board board) {
        if(TaskListAnchorPaneID.getChildren().size() > 0) {
            TaskListAnchorPaneID.getChildren().clear();
        }
        TaskListAnchorPaneID.getChildren().add(new BoardComponent(board));
    }
    public void disconnectBoard(){
        mainCtrl.showBoardinput();
    }

    public void disconnectServer(){
        mainCtrl.showConnect();
    }

    public void addTaskList(){
        mainCtrl.showAddTaskList();
    }
}

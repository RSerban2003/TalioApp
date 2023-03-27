package client.scenes;

import client.components.BoardComponent;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class BoardCtrl {
    @FXML
    private VBox boardVBox;
    @FXML
    private AnchorPane boardAnchor;
    private MainCtrl mainCtrl;
    private ServerUtils server;

    @FXML
    private TextField boardName;

    @FXML
    private Label boardIdLabel;

    @Inject
    public BoardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        boardAnchor = new AnchorPane();
    }
    public void updateBoard(Board board) {
        boardIdLabel.setText("BoardId: " + board.getId());
        if(boardAnchor.getChildren().size() > 0) {
            boardAnchor.getChildren().clear();
        }
        boardAnchor.getChildren().add(new BoardComponent(board));
    }
    public void disconnectBoard(){
        mainCtrl.showBoardinput();
    }

    public void disconnectServer(){
        mainCtrl.showConnect();
    }

    public void changeName() {
        boardName.setDisable(false);
        boardName.setEditable(true);
    }

    public void saveNewBoardName(KeyEvent key) {
        if (key.getCode().equals(KeyCode.ENTER)) {
            // save the new boardName
            server.changeBoardName(boardName.getText(),Long.parseLong(boardIdLabel.getText().split(" ")[1]));

            // set Disables
            boardName.setDisable(true);
            boardName.setEditable(false);
        }
    }
}

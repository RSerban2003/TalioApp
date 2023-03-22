package client.scenes;

import client.components.BoardComponent;
import commons.Board;
import javafx.fxml.FXML;
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

    @FXML
    private TextField boardName;

    @Inject
    public BoardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        boardAnchor = new AnchorPane();
    }
    public void updateBoard(Board board) {
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

            // set Disables
            boardName.setDisable(true);
            boardName.setEditable(false);
        }
    }
}

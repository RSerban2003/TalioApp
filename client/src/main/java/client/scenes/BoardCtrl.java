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
    private AnchorPane boardAnchor;
    private MainCtrl mainCtrl;
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
<<<<<<< HEAD
    public void disconnect(){
        mainCtrl.showConnect();
    }
=======

>>>>>>> main
}

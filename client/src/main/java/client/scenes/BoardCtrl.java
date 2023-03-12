package client.scenes;

import client.components.BoardComponent;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardCtrl {
    @FXML
    public VBox boardVBox;
    @FXML
    AnchorPane boardAnchor;
    private MainCtrl mainCtrl;
    @Inject
    public BoardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;

    }
    public void updateBoard(Board board) {
        boardAnchor.getChildren().add(new BoardComponent(board));
    }
}

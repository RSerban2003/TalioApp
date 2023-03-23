package client.scenes;

import client.components.BoardComponent;
import client.utils.ServerUtils;
import commons.Board;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardCtrl  {
    @FXML
    private VBox boardVBox;
    @FXML
    private AnchorPane boardAnchor;
    private final ServerUtils server;
    private MainCtrl mainCtrl;
    private long boardID;
    @Inject
    public BoardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        boardAnchor = new AnchorPane();
    }
    public void updateBoard(Board board) {
        if(boardAnchor.getChildren().size() > 0) {
            boardAnchor.getChildren().clear();
        }
        setBoardID(board.getId());
        boardAnchor.getChildren().add(new BoardComponent(board));
    }

    public long getBoardID() {
        return boardID;
    }

    public void setBoardID(long boardID) {
        if(boardID != 0){
            System.out.println("/topic/"+boardID);
            server.registerForMessages("/topic/100", Board.class, q ->{
                System.out.println(q.toString());
                this.updateBoard(q);
            });
        }
        this.boardID = boardID;
    }

    public void disconnectBoard(){
        mainCtrl.showBoardinput();
        server.unregisterForMessages("/topic/"+this.boardID);
    }

    public void disconnectServer(){
        mainCtrl.showConnect();
        server.unregisterForMessages("/topic/"+this.boardID);
    }

}

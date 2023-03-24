package client.scenes;

import client.components.BoardComponent;
import client.utils.ServerUtils;
import commons.Board;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class BoardCtrl {
    @FXML
    private VBox boardVBox;
    @FXML
    private AnchorPane boardAnchor;
    private final ServerUtils server;
    private MainCtrl mainCtrl;
    private long boardID;
    private SimpleObjectProperty<Board> observableBoard;
    private BoardComponent boardComponent;
    @Inject
    public BoardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        boardAnchor = new AnchorPane();
        observableBoard = new SimpleObjectProperty<Board>();
        boardComponent = new BoardComponent(observableBoard);
    }
    
    public void updateBoard(Board board) {
        observableBoard.set(board);
        setBoardID(board.getId());
        boardAnchor.getChildren().clear();
        boardAnchor.getChildren().add(boardComponent);
    }

    public long getBoardID() {
        return boardID;
    }

    public void setBoardID(long boardID) {
        if(boardID != 0){
            server.registerForMessages("/topic/"+boardID, Board.class, q -> {
                observableBoard.set(q);
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

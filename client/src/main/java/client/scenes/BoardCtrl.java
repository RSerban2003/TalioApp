package client.scenes;

import client.components.BoardComponent;
import client.utils.ServerUtils;
import commons.Board;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.inject.Inject;
import java.util.Map;

public class BoardCtrl {
    @FXML
    private AnchorPane boardAnchor;
    private final ServerUtils server;
    private MainCtrl mainCtrl;
    @FXML
    private TextField boardName;

    private long boardID;
    @FXML
    private Text textBoardName;
    @FXML
    private Button buttonEditBoardName;
    @FXML
    private TextField textFieldBoardName;
    @FXML
    private Button buttonSaveBoardName;
    private Board board;

    private SimpleObjectProperty<Board> observableBoard;
    private BoardComponent boardComponent;

    @Inject
    public BoardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        boardAnchor = new AnchorPane();
        observableBoard = new SimpleObjectProperty<Board>();
        boardComponent = new BoardComponent(observableBoard, server, mainCtrl);
    }
    
    public void updateBoard(Board board) {
        this.board = board;
        observableBoard.set(board);
        setBoardID(board.getId());
        boardAnchor.getChildren().clear();
        boardAnchor.getChildren().add(boardComponent);
        if (board.getTitle().length() > 10) textBoardName.setText(board.getTitle().substring(0,10)+ "..");
        else textBoardName.setText(board.getTitle());
    }

    public long getBoardID() {
        return boardID;
    }

    public void setBoardID(long boardID) {
        if(boardID != 0){
            server.registerForMessages("/topic/"+boardID, Board.class, q -> {
                observableBoard.set(q);
                this.board = q;
                if (board.getTitle().length() > 10) textBoardName.setText(board.getTitle().substring(0,10) + "..");
                else textBoardName.setText(board.getTitle());
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

    public void onEditBoardNameClick(){
        textFieldBoardName.setText(board.getTitle());
        buttonEditBoardName.setVisible(false);
        textBoardName.setVisible(false);
        textFieldBoardName.setVisible(true);
        buttonSaveBoardName.setVisible(true);
    }

    public void onSaveBoardNameClick(){
        server.changeBoardName(Map.of("name", textFieldBoardName.getText()), boardID);
        if (board.getTitle().length() > 10) textBoardName.setText(board.getTitle().substring(0,10)+ "..");
         else textBoardName.setText(board.getTitle());
        buttonEditBoardName.setVisible(true);
        textBoardName.setVisible(true);
        textFieldBoardName.setVisible(false);
        buttonSaveBoardName.setVisible(false);
    }
    public void onCopyInviteKeyClicked(){
        Long invite = observableBoard.get().getId();
        String inviteString = invite.toString();

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(inviteString);
        clipboard.setContent(clipboardContent);
    }

    public void addTaskList(){
        mainCtrl.showAddTaskList();
    }
}

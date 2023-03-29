package client.scenes;

import client.components.BoardComponent;
import client.utils.ServerUtils;
import commons.Board;
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
        boardComponent = new BoardComponent(observableBoard);
    }
    
    public void updateBoard(Board board) {
        observableBoard.set(board);
        setBoardID(board.getId());
        boardAnchor.getChildren().clear();
        boardAnchor.getChildren().add(boardComponent);
        textBoardName.setText(board.getTitle());
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

    public void onEditBoardNameClick(){
        textFieldBoardName.setText(textBoardName.getText());
        buttonEditBoardName.setVisible(false);
        textBoardName.setVisible(false);
        textFieldBoardName.setVisible(true);
        buttonSaveBoardName.setVisible(true);
    }

    public void onSaveBoardNameClick(){
        server.changeBoardName(Map.of("name",textFieldBoardName.getText()),boardID);
        textBoardName.setText(textFieldBoardName.getText());
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

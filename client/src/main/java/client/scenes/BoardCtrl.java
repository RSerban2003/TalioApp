package client.scenes;

import client.components.BoardComponent;
import client.utils.ServerUtils;
import commons.Board;
import commons.TaskList;
import jakarta.ws.rs.client.ClientBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.glassfish.jersey.client.ClientConfig;

import javax.inject.Inject;

public class BoardCtrl {
    @FXML
    private VBox boardVBox;
    @FXML
    private AnchorPane boardAnchor;
    private final ServerUtils server;
    private MainCtrl mainCtrl;
    private long boardID;
    private AnchorPane TaskListAnchorPaneID;
    @FXML
    private Text textBoardName;
    @FXML
    private Button buttonEditBoardName;
    @FXML
    private TextField textFieldBoardName;
    @FXML
    private Button buttonSaveBoardName;
    private Board board;

    @Inject
    public BoardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        boardAnchor = new AnchorPane();
        TaskListAnchorPaneID = new AnchorPane();
    }
    public void updateBoard(Board board) {
        if(TaskListAnchorPaneID.getChildren().size() > 0) {
            TaskListAnchorPaneID.getChildren().clear();
        }
        setBoardID(board.getId());
        boardAnchor.getChildren().add(new BoardComponent(board));
        TaskListAnchorPaneID.getChildren().add(new BoardComponent(board));
    }
    // after switching boards, this method updates the appointed board in this class
    public void currentBoard(Board board){
        this.board = board;
    }

    public long getBoardID() {
        return boardID;
    }

    public void setBoardID(long boardID) {
        this.boardID = boardID;
    }

    public void disconnectBoard(){
        mainCtrl.showBoardinput();
    }

    public void disconnectServer(){
        mainCtrl.showConnect();
    }

    public void hideEditFields(){
        textFieldBoardName.setVisible(false);
        buttonSaveBoardName.setVisible(false);
    }

    public void onEditBoardNameClick(){
        buttonEditBoardName.setVisible(false);
        textBoardName.setVisible(false);
        textFieldBoardName.setVisible(true);
        buttonSaveBoardName.setVisible(true);
    }

    public void onSaveBoardNameClick(){
        textBoardName.setText(textFieldBoardName.getText());
        buttonEditBoardName.setVisible(true);
        textBoardName.setVisible(true);
        textFieldBoardName.setVisible(false);
        buttonSaveBoardName.setVisible(false);
    }
    public void onCopyInviteKeyClicked(){
        Long invite = board.getId();
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

package client.scenes;

import client.components.BoardComponent;
import client.components.ClientBoardList;
import client.utils.ServerUtils;
import client.utils.WorkspaceUtils;
import commons.Board;
import commons.ListOfBoards;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.Map;
import javafx.fxml.Initializable;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardCtrl implements Initializable {
    @FXML
    private AnchorPane boardAnchor;
    private final ServerUtils server;
    private final WorkspaceUtils workspaceUtils;
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
    @FXML
    private ClientBoardList boardList;
    private ObservableList<Board> boardListSource;
    private Board board;

    private SimpleObjectProperty<Board> observableBoard;
    private BoardComponent boardComponent;

    @Inject
    public BoardCtrl(MainCtrl mainCtrl, ServerUtils server, WorkspaceUtils workspaceUtils) {
        this.workspaceUtils = workspaceUtils;
        this.mainCtrl = mainCtrl;
        this.server = server;
        boardAnchor = new AnchorPane();
        observableBoard = new SimpleObjectProperty<Board>();
        boardComponent = new BoardComponent(observableBoard, server, mainCtrl);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardList.setWorkspaceUtils(workspaceUtils);
        boardList.setMainCtrl(mainCtrl);
    }
    
    public void updateBoard(Board board) {
        this.board = board;
        observableBoard.set(board);
        setBoardID(board.getId());
        boardAnchor.getChildren().clear();
        boardAnchor.getChildren().add(boardComponent);
        if (board.getTitle().length() > 10) textBoardName.setText(board.getTitle().substring(0,10)+ "..");
        else textBoardName.setText(board.getTitle());
        refreshBoardList();
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

    public void onDeleteButtonClicked(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Warning! Are you sure you want to delete this board?");
        alert.setContentText("This action cannot be undone. " +
                "All task lists and tasks in this board will be deleted as well.");
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")){
            server.deleteBoard(boardID);
        }
        else {
            alert.close();
        }
    }

    public void onTagManagementButtonClicked() {
        mainCtrl.setBoard(board);
        mainCtrl.showTagManagement();
    }

    public void disconnectServer(){
        mainCtrl.showConnect();
        server.unregisterForMessages("/topic/"+this.boardID);
        server.unregisterForMessages("topic/boardView");
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
        updateBoard(board);
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
    public void refreshBoardList() {
        List<Long> boardIds = new ArrayList<>();
        try {
            List<Long> allIds = workspaceUtils.getFromFile(ServerUtils.getHost());
            for(Long id: allIds) {
                if(!server.boardExists(id.toString())) {
                    workspaceUtils.deleteFromFile(ServerUtils.getHost(), id);
                } else boardIds.add(id);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        boardListSource = FXCollections.observableList(boardIds.stream().map(server::getBoard).toList());
        boardList.setItems(boardListSource);
    }
}

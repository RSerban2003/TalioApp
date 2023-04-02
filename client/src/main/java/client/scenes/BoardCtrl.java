package client.scenes;

import client.components.BoardComponent;
import client.components.ClientBoardList;
import client.utils.ServerUtils;
import client.utils.WorkspaceUtils;
import commons.Board;
import commons.TaskList;
import jakarta.ws.rs.client.ClientBuilder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BoardCtrl implements Initializable {
    @FXML
    private AnchorPane boardAnchor;
    private final ServerUtils server;
    private final WorkspaceUtils workspaceUtils;
    private MainCtrl mainCtrl;
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
        boardList.setBoardCtrl(this);
    }
    
    public void updateBoard(Board board) {
        observableBoard.set(board);
        setBoardID(board.getId());
        boardAnchor.getChildren().clear();
        boardAnchor.getChildren().add(boardComponent);
        refreshBoardList();
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

    public void onDeleteButtonClicked(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Warning! Are you sure you want to delete this board?");
        alert.setContentText("This action cannot be undone. " +
                "All task lists and tasks in this board will be deleted as well.");
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")){
            server.deleteBoard(boardID);
            mainCtrl.showBoardinput();
        }
        else {
            alert.close();
        }
    }

    public void disconnectServer(){
        mainCtrl.showConnect();
        server.unregisterForMessages("/topic/"+this.boardID);
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
        File file = new File("client/src/main/resources/workspaces/" + server.getHost());
        if(!file.exists()) return;
        List<Long> boardIds = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            boardIds = workspaceUtils.getBoardIds(scanner);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        boardListSource = FXCollections.observableList(boardIds.stream().map(server::getBoard).toList());
        boardList.setItems(boardListSource);
    }
}

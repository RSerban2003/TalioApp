package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import client.utils.WorkspaceUtils;
import commons.Board;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.springframework.stereotype.Component;
@Component
public class ClientBoardList extends ListView<Board> {
    private WorkspaceUtils workspaceUtils;
    private MainCtrl mainCtrl;
    private class BoardCell extends ListCell<Board> {
        private Board board;
        private Label boardTitle;
        private Button leave;
        private Button open;
        private Button copyId;
        private HBox root;
        private Region padding;
        //("-fx-background-color: #454342")
        private ListView listView;
        public BoardCell(ListView<Board> boardListView) {
            super();
            super.setStyle("-fx-background-color: #615f5e");
            this.listView = boardListView;
            root = new HBox(5);
            root.setAlignment(Pos.CENTER_LEFT);
            root.setPadding(new Insets(10, 10, 10, 10));
            root.setStyle("-fx-background-color: #615f5e");
            leave = new Button("Leave");
            leave.setStyle("-fx-background-color: #454342; -fx-text-fill: #FFFFFF");
            leave.setOnAction(a -> {
                workspaceUtils.deleteFromFile(ServerUtils.getHost(), board.getId());
                if(mainCtrl.getBoardId() == board.getId()) mainCtrl.showBoardinput();
                mainCtrl.refreshBoardList();

            });
            open = new Button("Open");
            open.setStyle("-fx-background-color: #454342; -fx-text-fill: #FFFFFF");
            open.setOnAction(event -> {
                mainCtrl.updateBoard(board);
                mainCtrl.showBoard();
            });
            copyId = new Button("copy ID");
            copyId.setStyle("-fx-background-color: #454342; -fx-text-fill: #FFFFFF");
            copyId.setOnAction(event -> {
                Long invite = board.getId();
                String inviteString = invite.toString();

                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(inviteString);
                clipboard.setContent(clipboardContent);
            });
            boardTitle = new Label();
            padding = new Region();
            HBox.setHgrow(padding, Priority.ALWAYS);
            setText(null);
            root.getChildren().addAll(boardTitle, padding, open, leave, copyId);
        }
        @Override
        protected void updateItem(Board board, boolean empty) {
            super.updateItem(board, empty);
            if (board == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                boardTitle.setStyle("-fx-text-fill: #FFFFFF");
                boardTitle.setText(board.getTitle());
                this.board = board;
                setGraphic(root);
            }
        }
    }
    public ClientBoardList() {
        super();
        this.setCellFactory(BoardCell::new);
    }
    //This should be done with dependency injection, but I can't get it to work
    public void setWorkspaceUtils(WorkspaceUtils workspaceUtils) {
        this.workspaceUtils = workspaceUtils;
    }
    public void setMainCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }
}

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
        private HBox root;
        private Region padding;
        private ListView listView;
        public BoardCell(ListView<Board> boardListView) {
            super();
            this.listView = boardListView;
            root = new HBox(10);
            root.setAlignment(Pos.CENTER_LEFT);
            root.setPadding(new Insets(5, 5, 5, 5));
            leave = new Button("Leave");
            leave.setOnAction(a -> {
                workspaceUtils.deleteFromFile(ServerUtils.getHost(), board.getId());
                mainCtrl.refreshBoardList();
                if(mainCtrl.getBoardId() == board.getId()) mainCtrl.updateBoard(new Board(0L, "Join a board"));

            });
            open = new Button("Open");
            open.setOnAction(event -> {
                mainCtrl.updateBoard(board);
            });
            boardTitle = new Label();
            padding = new Region();
            HBox.setHgrow(padding, Priority.ALWAYS);
            setText(null);
            root.getChildren().addAll(boardTitle, padding, open, leave);
        }
        @Override
        protected void updateItem(Board board, boolean empty) {
            super.updateItem(board, empty);
            if (board == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
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

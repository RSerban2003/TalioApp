package client.components;

import client.utils.ServerUtils;
import client.utils.WorkspaceUtils;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
@Component
public class ClientBoardList extends ListView<Board> {
    @Autowired
    public ServerUtils serverUtils;
    @Autowired
    public WorkspaceUtils workspaceUtils;
    private class BoardCell extends ListCell<Board> {
        private Board board;
        private Label boardTitle;
        private Button leave;
        private Button open;
        private HBox root;
        private Region padding;
        public BoardCell(ListView<Board> boardListView) {
            root = new HBox(10);
            root.setAlignment(Pos.CENTER_LEFT);
            root.setPadding(new Insets(5, 5, 5, 5));
            leave = new Button("Leave");
            leave.setOnAction(a -> {
                leave.setText("Clicked");
//                            File file = new File("client/src/main/resources/workspaces/" + serverUtils.getHost());
//                            try{
//                                Scanner scanner = new Scanner(file);
//                                FileWriter fileWriter = new FileWriter(file, false);
//                                workspaceUtils.removeBoardId(scanner, fileWriter, board.getId());
//                            } catch (IOException ioe) {
//                                ioe.printStackTrace();
//                            }

            });
            open = new Button("Open");
            open.setOnMouseEntered(event -> {

            });
            boardTitle = new Label();
            padding = new Region();
            HBox.setHgrow(padding, Priority.ALWAYS);
            setText(null);
            root.getChildren().addAll(boardTitle, padding, open, leave);
        }
        @Override
        protected void updateItem(Board board, boolean empty) {
            if (board == null || empty) {
                setText(null);
            } else {
                boardTitle.setText(board.getTitle());
                this.board = board;
                setGraphic(root);
            }
        }
    }
    @Inject
    public ClientBoardList() {
        super();
        this.setCellFactory(BoardCell::new);
    }

}

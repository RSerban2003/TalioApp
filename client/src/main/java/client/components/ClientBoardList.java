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
    @Inject
    public ClientBoardList() {
        super();
        this.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Board board, boolean empty) {
                if (board == null || empty) {
                    setText(null);
                } else {
                    HBox root = new HBox(10);
                    root.setAlignment(Pos.CENTER_LEFT);
                    root.setPadding(new Insets(5, 5, 5, 5));
                    root.getChildren().add(new Label(board.getTitle()));

                    Region region = new Region();
                    HBox.setHgrow(region, Priority.ALWAYS);
                    root.getChildren().add(region);

                    Button leaveBoard = new Button("Leave");
                    leaveBoard.setOnAction(a -> {
                        leaveBoard.setText("Clicked");
//                            File file = new File("client/src/main/resources/workspaces/" + serverUtils.getHost());
//                            try{
//                                Scanner scanner = new Scanner(file);
//                                FileWriter fileWriter = new FileWriter(file, false);
//                                workspaceUtils.removeBoardId(scanner, fileWriter, board.getId());
//                            } catch (IOException ioe) {
//                                ioe.printStackTrace();
//                            }
                        System.out.println("did stuff");
                    });
                    Button openBoard = new Button("Open");
                    openBoard.setOnMouseEntered(event -> {
                        openBoard.setText("Bruh");
                    });
                    root.getChildren().addAll(leaveBoard, openBoard);
                    setText(null);
                    setGraphic(root);
                }

            }
        });
    }
}

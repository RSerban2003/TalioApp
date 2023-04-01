package client.components;

import client.Main;
import client.utils.ServerUtils;
import client.scenes.MainCtrl;
import commons.Board;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SubListBoardComponent extends VBox {
    private static final String style = "-fx-background-color: #f7f7f5; -fx-border-width: 2; -fx-border-color: gray;  -fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";
    private final Board board;

    private MainCtrl mainCtrl;

    public SubListBoardComponent(Board board, MainCtrl mainCtrl, ServerUtils server){
        super();
        this.mainCtrl = mainCtrl;

        // Create label for board
        Label boardLabel = new Label(board.getTitle());
        VBox boardBox = new VBox(boardLabel);
        boardBox.setAlignment(Pos.CENTER);

        // create label for board id
        Label idLabel = new Label(String.valueOf( board.getId()));
        VBox idBox = new VBox(idLabel);
        idBox.setAlignment(Pos.CENTER);

        // Create button
        Button deleteButton = new Button("Delete");
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext();
        context.scan("client");
        context.refresh();
        server = context.getBean(ServerUtils.class);
        ServerUtils finalServer = server;
        deleteButton.setOnAction(event -> finalServer.deleteBoard(board.getId()));
        HBox deleteButtonBox = new HBox(deleteButton);
        deleteButtonBox.setAlignment(Pos.TOP_RIGHT);

        // Create button
        Button joinButton = new Button("Join");
        MainCtrl finalMainCtrl = mainCtrl;
        joinButton.setOnAction(event -> {
            finalMainCtrl.updateBoard(board);
            finalMainCtrl.showBoard();
        });
        HBox buttonBox = new HBox(joinButton);
        buttonBox.setAlignment(Pos.TOP_CENTER);

        HBox topRow = new HBox(boardBox, idBox, joinButton, deleteButtonBox);
        topRow.setSpacing(20.0);
        topRow.setAlignment(Pos.TOP_CENTER);

        // Add topRow
        setMaxSize(300, 40);
        setMinSize(300, 40);
        getChildren().add(topRow);
        setAlignment(Pos.CENTER);
        setStyle(style);
        setSpacing(10.0);
        this.board = board;
    }

}
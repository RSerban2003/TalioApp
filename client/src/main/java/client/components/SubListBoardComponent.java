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

    private final Board board;

    private MainCtrl mainCtrl;

    public SubListBoardComponent(Board board, MainCtrl mainCtrl, ServerUtils server){
        super();
        this.mainCtrl = mainCtrl;

        // Create label for board
        Label boardLabel = new Label(board.getTitle());
        VBox boardBox = new VBox(boardLabel);
        boardBox.setAlignment(Pos.TOP_LEFT);

        // Create button
        Button button = new Button("Delete");
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext();
        context.scan("client");
        context.refresh();
        server = context.getBean(ServerUtils.class);
        ServerUtils finalServer = server;
        button.setOnAction(event -> finalServer.deleteBoard(board.getId()));
        HBox buttonBox = new HBox(button);
        buttonBox.setAlignment(Pos.TOP_RIGHT);

        HBox topRow = new HBox(boardBox, buttonBox);
        topRow.setSpacing(20.0);
        topRow.setAlignment(Pos.TOP_CENTER);

        // Add topRow
        setMaxSize(200, 80);
        setMinSize(200, 80);
        getChildren().add(topRow);
        setAlignment(Pos.TOP_CENTER);

        setSpacing(20.0);
        this.board = board;
    }

}

package client.components;

import client.utils.ServerUtils;
import client.scenes.MainCtrl;
import commons.Board;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SubListBoardComponent extends VBox {
    private static final String style = "-fx-background-color: #c7c7c7; -fx-border-width: 2; -fx-border-color: gray;  -fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";
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
        deleteButton.setStyle("-fx-background-color: #8d8d8d; -fx-text-fill: #000000");
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
        joinButton.setStyle("-fx-background-color: #8d8d8d; -fx-text-fill: #000000");
        MainCtrl finalMainCtrl = mainCtrl;
        joinButton.setOnAction(event -> {
            Board newBoard = finalServer.getBoard(board.getId());
            if(newBoard == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Failed to retrieve board with id" + board.getId());
                alert.showAndWait();
                return;
            }
            finalMainCtrl.updateBoard(newBoard);
            finalMainCtrl.showBoard();
            finalMainCtrl.getPrimaryStage().setMaximized(true);
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

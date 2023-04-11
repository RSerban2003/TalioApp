package client.scenes;

import client.components.ListBoardComponent;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import commons.Board;
import commons.ListOfBoards;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.List;

public class AdminDashboardCtrl {
    @FXML
    private AnchorPane boardAnchor;
    private final WebSocketUtils webSocket;
    private MainCtrl mainCtrl;
    private Board board;
    private ServerUtils server;
    private SimpleObjectProperty<List<Board>> observableList;
    private ListBoardComponent boardComponent;

    @Inject
    public AdminDashboardCtrl(MainCtrl mainCtrl, WebSocketUtils webSocket, ServerUtils server) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.webSocket = webSocket;
        boardAnchor = new AnchorPane();
        observableList = new SimpleObjectProperty<List<Board>>();
        boardComponent = new ListBoardComponent(observableList, server, mainCtrl);
    }

    public void updateAdmin(List<Board> boardList) {
        observableList.set(boardList);
        boardComponent.refresh();
        if(boardAnchor.getChildren().size() == 0) {
            boardAnchor.getChildren().add(boardComponent);
        }
    }

    public void addBoardToList(Board board){
        observableList.get().add(board);
        updateAdmin(observableList.get());
    }

    public void getUpdates(){
        server.registerForUpdates(board -> {
            System.out.println(board);
            addBoardToList(board);
        });

        webSocket.registerForMessages("/topic/admin", ListOfBoards.class, q ->{
            List<Board> boardList = q.getBoardList();
            observableList.set(boardList);
        });
    }

    public void disconnectBoard(){
        mainCtrl.showBoardinput();
        webSocket.unregisterForMessages("/topic/admin");
    }

    public void disconnectServer(){
        mainCtrl.showConnect();
        server.stopAndRestart();
        webSocket.unregisterForMessages("/topic/admin");
    }
}

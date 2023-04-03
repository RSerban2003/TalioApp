package client.scenes;

import client.components.ListBoardComponent;
import client.utils.ServerUtils;
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
    private final ServerUtils server;
    private MainCtrl mainCtrl;
    private Board board;

    private SimpleObjectProperty<List<Board>> observableList;
    private ListBoardComponent boardComponent;

    @Inject
    public AdminDashboardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        boardAnchor = new AnchorPane();
        observableList = new SimpleObjectProperty<List<Board>>();
        boardComponent = new ListBoardComponent(observableList, server, mainCtrl);
    }

    public void updateAdmin(List<Board> boardList) {
        observableList.set(boardList);
        boardAnchor.getChildren().clear();
        boardAnchor.getChildren().add(boardComponent);
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

        server.registerForMessages("/topic/admin", ListOfBoards.class, q ->{
            List<Board> boardList = q.getBoardList();
            observableList.set(boardList);
        });
    }

    public void disconnectBoard(){
        mainCtrl.showBoardinput();
        server.unregisterForMessages("/topic/admin");
    }

    public void disconnectServer(){
        mainCtrl.showConnect();
        server.unregisterForMessages("/topic/admin");
    }
}

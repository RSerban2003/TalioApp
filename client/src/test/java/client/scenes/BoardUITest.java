package client.scenes;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;
import static javafx.application.Application.launch;

public class BoardUITest extends Application {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var board = FXML.load(BoardCtrl.class, "client", "scenes", "Board.fxml");
        var boardCtrl = board.getKey();
        var boardscene = new Scene(board.getValue(), 1900, 1000);
        primaryStage.setScene(boardscene);
        primaryStage.show();
        Board testBoard = new Board(123L,"Title");
        TaskList a = new TaskList(1L, "a");
        TaskList b = new TaskList(2L, "b");
        a.add(new Task(1L, "1", ""));
        a.add(new Task(2L, "2", ""));
        b.add(new Task(3L, "3", ""));
        b.add(new Task(4L, "4", ""));
        testBoard.add(a);
        testBoard.add(b);
        boardCtrl.updateBoard(testBoard);
    }
}

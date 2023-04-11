package client.scenes;

import client.components.ClientBoardList;
import client.utils.ServerUtils;
import client.utils.WorkspaceUtils;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.glassfish.jersey.client.ClientConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class BoardInputCtrl implements Initializable {
    @FXML
    private TextField boardIdTextField;

    private ServerUtils server;
    private MainCtrl mainCtrl;
    private BoardCtrl boardCtrl;
    private WorkspaceUtils workspaceUtils;
    @FXML
    private ClientBoardList boardList;
    private ObservableList<Board> boardListSource;


    @Inject
    public BoardInputCtrl(ServerUtils server, MainCtrl mainCtrl, BoardCtrl boardCtrl, WorkspaceUtils workspaceUtils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardCtrl = boardCtrl;
        this.workspaceUtils = workspaceUtils;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardList.setWorkspaceUtils(workspaceUtils);
        boardList.setMainCtrl(mainCtrl);
    }

    /**
     * Takes input and makes a get request to retrieve the board with id entered
     */
    public void retrieveBoard() {
        // check if the input field is either empty or null to display a warning
        String textBoardId = boardIdTextField.getText();
        if (textBoardId == null || textBoardId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter a board ID");
            alert.showAndWait();
            return;
        }
        else if (!textBoardId.matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Board ID can only contain numbers");
            alert.showAndWait();
            return;
        }

        Response response;
        try {
            // If there is an input make a get request with the board id to retrieve it
            Client client = ClientBuilder.newClient();
            response = client.target(server.getServerUrl()).path("api/boards/" + textBoardId + "/get/").request().get();
            // if there is no board with such id, put up a warning and wait for another input
            if (response.getStatus() == 404) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Board with ID " + textBoardId + " does not exist");
                alert.showAndWait();
                return;
            }
            else if (response.getStatus() != 200) {
                throw new RuntimeException("Failed to retrieve board: HTTP error code " + response.getStatus());
            }
            Board board = ClientBuilder.newClient(new ClientConfig()).target(server.getServerUrl())
                    .path("api/boards/" + textBoardId + "/get").request(APPLICATION_JSON).accept(APPLICATION_JSON).get(new GenericType<Board>() {});
            clearFields();
            mainCtrl.showBoard();
            mainCtrl.getPrimaryStage().setMaximized(true);
            try {
                File file = new File("client/src/main/resources/workspaces/" + server.getHost());
                Long boardId = Long.parseLong(textBoardId);
                System.out.println(file.toPath().toAbsolutePath());
                if(!file.exists()) file.createNewFile();
                if(!workspaceUtils.getBoardIds(new Scanner(file)).contains(boardId)) {
                    workspaceUtils.addBoardId(new FileWriter(file, true), Long.parseLong(textBoardId));
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            mainCtrl.updateBoard(board);
            mainCtrl.refreshBoardList();
        } catch (ProcessingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to retrieve board: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                retrieveBoard();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

    public void createBoardButton(){
        mainCtrl.showCreateBoard();
    }

    public void cancel() {
        clearFields();
        mainCtrl.showConnect();
        server.stopAndRestart();
    }

    public void admin(){
        mainCtrl.showAdminPass();
    }

    private void clearFields() {
        boardIdTextField.clear();
    }


    public void refreshBoardList() {
        List<Long> boardIds = new ArrayList<>();
        try {
            List<Long> allIds = workspaceUtils.getFromFile(ServerUtils.getHost());
            for(Long id: allIds) {
                if(!server.boardExists(id.toString())) {
                    workspaceUtils.deleteFromFile(ServerUtils.getHost(), id);
                } else boardIds.add(id);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        boardListSource = FXCollections.observableList(boardIds.stream().map(server::getBoard).toList());
        boardList.setItems(boardListSource);
    }
}

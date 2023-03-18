package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.glassfish.jersey.client.ClientConfig;

import java.util.ArrayList;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class BoardInputCtrl {
    @FXML
    private TextField boardIdTextField;

    private ServerUtils server;
    private MainCtrl mainCtrl;
    private BoardCtrl boardCtrl;

    @Inject
    public BoardInputCtrl(ServerUtils server, MainCtrl mainCtrl, BoardCtrl boardCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardCtrl = boardCtrl;
    }

    /**
     * Takes input and makes a get request to retrieve the board with id entered
     */
    public void retrieveBoard() {
        // check if the input field is either empty or null to display a warning
        String boardId = boardIdTextField.getText();
        if (boardId == null || boardId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter a board ID");
            alert.showAndWait();
            return;
        }
        Response response;
        try {
            // If there is an input make a get request with the board id to retrieve it
            Client client = ClientBuilder.newClient();
            response = client.target(server.getServerUrl()).path("api/boards/" + boardId).request().get();
            // if there is no board with such id, put up a warning and wait for another input
            if (response.getStatus() == 404) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Board with ID " + boardId + " does not exist");
                alert.showAndWait();
                return;
            }
            else if (response.getStatus() != 200) {
                throw new RuntimeException("Failed to retrieve board: HTTP error code " + response.getStatus());
            }
            Board board = ClientBuilder.newClient(new ClientConfig()).target(server.getServerUrl())
                    .path("api/boards/" + boardId).request(APPLICATION_JSON).accept(APPLICATION_JSON).get(new GenericType<Board>() {});
            mainCtrl.showBoard();
            mainCtrl.updateBoard(board);
            } catch (ProcessingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to retrieve board: " + e.getMessage());
            alert.showAndWait();
        }
    }


}

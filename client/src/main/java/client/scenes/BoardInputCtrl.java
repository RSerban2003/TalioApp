package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class BoardInputCtrl {
    @FXML
    private TextField boardIdTextField;

    private ServerUtils server;
    private MainCtrl mainCtrl;

    @Inject
    public BoardInputCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void retrieveBoard() {
        String boardId = boardIdTextField.getText();
        if (boardId == null || boardId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter a board ID");
            alert.showAndWait();
            return;
        }

        Client client = ClientBuilder.newClient();
        Response response = client.target(server.getServerUrl()).path("api/boards/" + boardId).request().get();

        if (response.getStatus() == 404) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Board with ID " + boardId + " does not exist");
            alert.showAndWait();
            return;
        }

        Board board = response.readEntity(Board.class);
        // TODO: do something with the retrieved board
    }


}

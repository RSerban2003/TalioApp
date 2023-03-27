package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.glassfish.jersey.client.ClientConfig;

import java.util.HashMap;
import java.util.Map;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CreateBoardCtrl {
    @FXML
    private TextField boardNameTextField;
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private BoardCtrl boardCtrl;


    @Inject
    public CreateBoardCtrl(ServerUtils server, MainCtrl mainCtrl, BoardCtrl boardCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardCtrl = boardCtrl;
    }


    public void createBoard(){
        // check if the input field is either empty or null to display a warning
        String name = boardNameTextField.getText();
        if (name == null || name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter a board ID");
            alert.showAndWait();
            return;
        }
        Map<String, String> body = new HashMap<>();
        if (name.trim().isEmpty()) {
            // Display a warning if the name field in the map is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("The task list name in the request body cannot be empty");
            alert.showAndWait();
            return;
        }
        body.put("name", name.trim());
        // Send a POST request to add the task list to the board
        Response response = null;
        try {
            Client client = ClientBuilder.newClient();
            response = client.target(server.getServerUrl()).path("/api/boards/")
                    .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                    .post(Entity.entity(body, APPLICATION_JSON));

            if (response.getStatus() != 200) {
                System.out.println(response.getStatus());
                // Display an error message if the request was unsuccessful
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Was not able to make request to create the board");
                alert.showAndWait();
                return;
            }
            Board boardCreated = response.readEntity(Board.class);
            Long boardId = boardCreated.getId();
            clearFields();
            mainCtrl.showBoard();
            mainCtrl.updateBoard(boardCreated);
        } catch (Exception e) {
            // Display an error message if the request failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to add task list: " + e.getMessage());
            alert.showAndWait();
        } finally {
            if (response != null) {
                response.close();
            }
      }
    }


    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:

                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

    public void cancel() {
        clearFields();
        mainCtrl.showConnect();
    }

    private void clearFields() {
        boardNameTextField.clear();
    }
}


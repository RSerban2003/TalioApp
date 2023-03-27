package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.TaskList;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class AddTaskListCtrl {

    @FXML
    private Text TaskListNameId;
    @FXML
    private Button submitButton;
    @FXML
    private TextField TextFieldId;
    @FXML
    private Button CancelButtonId;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    @Inject
    public AddTaskListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    private void onCancelButtonClicked() {
        mainCtrl.showBoard();
    }
    @FXML
    private void onSubmitButtonClicked() {
        String name = TextFieldId.getText();
        if (name == null || name.isEmpty()) {
            // Display a warning if the name field is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter a name for the task list");
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
            response = client.target(server.getServerUrl()).path("api/boards/100/tasklist")
                    .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                    .post(Entity.entity(body, APPLICATION_JSON));
            if (response.getStatus() != 200) {
                System.out.println(response.getStatus());
                // Display an error message if the request was unsuccessful
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Was not able to make request to add the tasklist");
                alert.showAndWait();
                return;
            }
            // Show the board with the updated task list
            Board board = response.readEntity(Board.class);
            mainCtrl.showBoard();
            mainCtrl.updateBoard(board);
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
                onSubmitButtonClicked();
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
        mainCtrl.showBoard();
    }
    private void clearFields() {
        TextFieldId.clear();
    }
}

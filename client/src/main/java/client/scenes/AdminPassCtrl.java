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
import java.util.List;
import java.util.Map;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


public class AdminPassCtrl {
    @FXML
    private TextField adminPass;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    @Inject
    public AdminPassCtrl(ServerUtils server, MainCtrl mainCtrl, BoardCtrl boardCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void checkPass(){
        // check if the input field is either empty or null to display a warning
        String password = adminPass.getText();
        if (password == null || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter a board ID");
            alert.showAndWait();
            return;
        }
        else if (!password.matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Board ID can only contain numbers");
            alert.showAndWait();
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("pass", password);

        Response response = null;
        try{
            Client client = ClientBuilder.newClient();
            response = client.target(server.getServerUrl()).path("api/admin")
                    .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                    .post(Entity.entity(body, APPLICATION_JSON));
            if(response.getStatus() != 200){
                System.out.println(response.getStatus());
                // Display an error message if the request was unsuccessful
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Was not able to check password");
                alert.showAndWait();
                return;
            }
            // check if password was accepted by server
            if(!response.readEntity(Boolean.class)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Password was incorrect");
                alert.showAndWait();
                return;
            }

            retrieveAllBoards();

        } catch (Exception e){
            // Display an error message if the request failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to check password: " + e.getMessage());
            alert.showAndWait();
        } finally {
            if(response != null){
                response.close();
            }
        }
    }

    public void retrieveAllBoards(){
        Response response;
        try {
            // If there is an input make a get request with the board id to retrieve it
            Client client = ClientBuilder.newClient();
            response = client.target(server.getServerUrl()).path("api/boards/").request().get();
            if (response.getStatus() == 404) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Failed to retrieve board: HTTP error code " + response.getStatus());
                alert.showAndWait();
                return;
            }
            List<Board> boardList = ClientBuilder.newClient(new ClientConfig()).target(server.getServerUrl())
                    .path("api/boards/").request(APPLICATION_JSON).accept(APPLICATION_JSON).get(new GenericType<List<Board>>() {});
            clearFields();
            mainCtrl.showAdminDash();
            mainCtrl.updateAdminDash(boardList);
        } catch (ProcessingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to retrieve board: " + e.getMessage());
            alert.showAndWait();
        }
    }
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                checkPass();
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
        adminPass.clear();
    }

    public void generatePass() {
        Response response = null;
        try{
            Client client = ClientBuilder.newClient();
            response = client.target(server.getServerUrl()).path("api/admin").request().get();
            if(response.getStatus() != 200){
                System.out.println(response.getStatus());
                // Display an error message if the request was unsuccessful
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Was not able to generate password!");
                alert.showAndWait();
                return;
            }
            // tel user password was created
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Password generated!");
            alert.showAndWait();

        } catch (Exception e){
            // Display an error message if the request failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to check password: " + e.getMessage());
            alert.showAndWait();
        } finally {
            if(response != null){
                response.close();
            }
        }
    }
}

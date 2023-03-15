package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
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
        if (!server.boardExists(boardId)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("The board with ID " + boardId + " does not exist");
            alert.showAndWait();
            return;
        }
        // board exists, do something here
    }

}

package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ConnectCtrl {
    private final ServerUtils server;
    private MainCtrl mainCtrl;
    @FXML
    private TextField hostname;
    @FXML
    private Button connect;
    @Inject
    public ConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void connectServer() {
        server.setHost(hostname.getText());
        if(server.ping()) {
            mainCtrl.showBoardinput();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a valid hostname");
            alert.show();
        }
        clearFields();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                connectServer();
                break;
            default:
                break;
        }
    }

    private void clearFields() {
        hostname.clear();
    }
}

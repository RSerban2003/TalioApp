package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;

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
        mainCtrl.showOverview();
    }
}

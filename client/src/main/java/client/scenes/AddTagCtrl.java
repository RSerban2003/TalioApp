package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class AddTagCtrl {

    @FXML
    private Button LeaveButton;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    private Long boardID;
    private Long listID;
    private Long taskId;


    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    @FXML
    public void onLeaveButtonClicked() {
        mainCtrl.getPopUpStage().close();
    }
}

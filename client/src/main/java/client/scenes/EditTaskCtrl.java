package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;

public class EditTaskCtrl {
    @FXML
    private Button editTitleButton2;

    @FXML
    private Button editDescriptionButton2;

    @FXML
    private Button submitButton2;

    @FXML
    private Button cancelButton2;

    @FXML
    private TextArea titleTextArea2;

    @FXML
    private TextArea descriptionTextArea2;

    @FXML
    private AnchorPane anchorPane2;

    private ServerUtils server;

    private MainCtrl mainCtrl;

    private String initialTitle;

    private String initialDescription;

    private long boardID;
    private long tasklistID;

    private long taskID;

    @Inject
    public EditTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setIDs(long boardID, long tasklistID, long taskID) {
        this.boardID = boardID;
        this.tasklistID = tasklistID;
    }
}

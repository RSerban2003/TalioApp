package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

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
        this.taskID = taskID;
    }

    public void updateScene(Task task) {

    }

    @FXML
    private void onEditTitleButtonClicked2() {
        initialTitle = titleTextArea2.getText();
        titleTextArea2.setEditable(true);
        titleTextArea2.requestFocus();
    }

    @FXML
    private void initialize() {
        titleTextArea2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                titleTextArea2.setText(oldValue);
            }
        });
        titleTextArea2.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });
    }

    @FXML
    private void onEditDescriptionButtonClicked2() {
        initialDescription = descriptionTextArea2.getText();
        descriptionTextArea2.setEditable(true);
        descriptionTextArea2.requestFocus();
    }

    @FXML
    private void onSubmitButtonClicked2() {

    }

    @FXML
    private void onCancelButtonClicked2() {
        this.resetFields();
        mainCtrl.showBoard();
    }

    private void removeFocus() {
        anchorPane2.requestFocus();
    }

    private void resetFields() {
        titleTextArea2.setText("New Task");
        titleTextArea2.setEditable(false);
        descriptionTextArea2.setEditable(false);
        descriptionTextArea2.clear();
    }

    public void keyPressed(KeyEvent e) {
        if(e.isControlDown() && e.getCode() == KeyCode.S) {
            if (titleTextArea2.isFocused()) {
                titleTextArea2.setEditable(false);
                removeFocus();
            } else if (descriptionTextArea2.isFocused()) {
                descriptionTextArea2.setEditable(false);
                removeFocus();
            }
        }
        switch (e.getCode()) {
            case ENTER:
                if (titleTextArea2.isFocused()) {
                    titleTextArea2.setEditable(false);
                    removeFocus();
                } else if (descriptionTextArea2.isFocused()) {
                    descriptionTextArea2.setEditable(false);
                    removeFocus();
                } else {
                    onSubmitButtonClicked2();
                }
                break;
            case ESCAPE:
                if (titleTextArea2.isFocused()) {
                    titleTextArea2.setText(initialTitle);
                    titleTextArea2.setEditable(false);
                    removeFocus();
                } else if (descriptionTextArea2.isFocused()) {
                    descriptionTextArea2.setText(initialDescription);
                    descriptionTextArea2.setEditable(false);
                    removeFocus();
                } else {
                    onCancelButtonClicked2();
                }
                break;
            default:
                break;
        }
    }
}

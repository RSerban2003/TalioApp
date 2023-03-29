package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
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
    private Button editTitleButton;

    @FXML
    private Button editDescriptionButton;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextArea titleTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private AnchorPane anchorPane;

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
        titleTextArea.setText(task.getName());
        descriptionTextArea.setText(task.getDescription());
    }

    @FXML
    private void onEditTitleButtonClicked() {
        initialTitle = titleTextArea.getText();
        titleTextArea.setEditable(true);
        titleTextArea.requestFocus();
    }

    @FXML
    private void initialize() {
        titleTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                titleTextArea.setText(oldValue);
            }
        });
        titleTextArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });
    }

    @FXML
    private void onEditDescriptionButtonClicked() {
        initialDescription = descriptionTextArea.getText();
        descriptionTextArea.setEditable(true);
        descriptionTextArea.requestFocus();
    }

    @FXML
    private void onSubmitButtonClicked() {
        String title = titleTextArea.getText();
        String description = descriptionTextArea.getText();
        if (title == null || title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Task name cannot be empty. Please enter a name for the task.");
            alert.showAndWait();
            return;
        }
        Map<String, String> body = new HashMap<>();
        if (title.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Task name cannot be empty. Please enter a name for the task.");
            alert.showAndWait();
            return;
        }
        if (!server.editTask(boardID, tasklistID, taskID, title, description)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Failed to add the task: Unable to send the request.");
            alert.showAndWait();
            return;
        }
        mainCtrl.showBoard();
        resetFields();
    }

    @FXML
    private void onCancelButtonClicked() {
        this.resetFields();
        mainCtrl.showBoard();
    }

    private void removeFocus() {
        anchorPane.requestFocus();
    }

    private void resetFields() {
        titleTextArea.setText("New Task");
        titleTextArea.setEditable(false);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.clear();
    }

    public void keyPressed(KeyEvent e) {
        if(e.isControlDown() && e.getCode() == KeyCode.S) {
            if (titleTextArea.isFocused()) {
                titleTextArea.setEditable(false);
                removeFocus();
            } else if (descriptionTextArea.isFocused()) {
                descriptionTextArea.setEditable(false);
                removeFocus();
            }
        }
        switch (e.getCode()) {
            case ENTER:
                if (titleTextArea.isFocused()) {
                    titleTextArea.setEditable(false);
                    removeFocus();
                } else if (descriptionTextArea.isFocused()) {
                    descriptionTextArea.setEditable(false);
                    removeFocus();
                } else {
                    onSubmitButtonClicked();
                }
                break;
            case ESCAPE:
                if (titleTextArea.isFocused()) {
                    titleTextArea.setText(initialTitle);
                    titleTextArea.setEditable(false);
                    removeFocus();
                } else if (descriptionTextArea.isFocused()) {
                    descriptionTextArea.setText(initialDescription);
                    descriptionTextArea.setEditable(false);
                    removeFocus();
                } else {
                    onCancelButtonClicked();
                }
                break;
            default:
                break;
        }
    }
}
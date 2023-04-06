package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class TagManagementCtrl {
    @FXML
    private Button addTagButton;

    @FXML
    private Text tagNameText;

    @FXML
    private TextArea tagNameTextField;

    @FXML
    private Button editNameButton;

    @FXML
    private Button saveNameButton;

    @FXML
    private Button cancelNameButton;

    @FXML
    private Button createTagButton;

    @FXML
    private Button cancelTagButton;

    @FXML
    private Button backButton;

    private ServerUtils server;

    private MainCtrl mainCtrl;

    private String initialName;

    private long boardID;
    private long tasklistID;
    private long taskID;

    private long tagID;

    @Inject
    public TagManagementCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setIDs(long boardID, long tasklistID, long taskID) {
        this.boardID = boardID;
        this.tasklistID = tasklistID;
        this.taskID = taskID;
    }

    @FXML
    private void initialize() {
        tagNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 20) {
                tagNameTextField.setText(oldValue);
            }
        });
        tagNameTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });
    }

    @FXML
    private void onAddTagButtonClicked() {
        tagNameText.setVisible(true);
        tagNameTextField.setVisible(true);
        editNameButton.setVisible(true);
        createTagButton.setVisible(true);
        cancelTagButton.setVisible(true);
    }

    @FXML
    private void onEditNameButtonClicked() {
        tagNameTextField.setEditable(true);
        editNameButton.setVisible(false);
        saveNameButton.setVisible(true);
        cancelNameButton.setVisible(true);
        initialName = tagNameTextField.getText();
    }

    @FXML
    private void onCancelNameButtonClicked() {
        tagNameTextField.setText(initialName);
        saveNameButton.setVisible(false);
        cancelNameButton.setVisible(false);
        editNameButton.setVisible(true);
        tagNameTextField.setEditable(false);
    }

    @FXML
    private void onSaveNameButtonClicked() {
        saveNameButton.setVisible(false);
        cancelNameButton.setVisible(false);
        editNameButton.setVisible(true);
        tagNameTextField.setEditable(false);
    }

    @FXML
    private void onCancelTagButtonClicked() {
        tagNameText.setVisible(false);
        tagNameTextField.setVisible(false);
        editNameButton.setVisible(false);
        createTagButton.setVisible(false);
        cancelTagButton.setVisible(false);
        saveNameButton.setVisible(false);
        cancelNameButton.setVisible(false);
        tagNameTextField.setText("New Tag");
        tagNameTextField.setEditable(false);
    }

    @FXML
    private void onCreateTagButtonClicked() {
        tagNameText.setVisible(false);
        tagNameTextField.setVisible(false);
        editNameButton.setVisible(false);
        createTagButton.setVisible(false);
        cancelTagButton.setVisible(false);
        saveNameButton.setVisible(false);
        cancelNameButton.setVisible(false);
        tagNameTextField.setText("New Tag");
        tagNameTextField.setEditable(false);

        //add tag component to the list and tag entity to the database

    }

    @FXML
    private void onBackButtonClicked() {
        this.resetFields();
        mainCtrl.getPopUpStage().close();
    }

    private void resetFields() {
        tagNameTextField.setText("New Tag");
    }
}

package client.scenes;

import client.components.TagComponent;
import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import commons.Task;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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

    @FXML
    private AnchorPane tagAnchorPane;

    private SimpleObjectProperty<Board> observableBoard;

    private TagComponent tagComponent;

    private ServerUtils server;

    private MainCtrl mainCtrl;

    private String initialName;

    private long boardID;
    private long tasklistID;
    private long taskID;
    private long tagID;
    private Board board;
    private VBox tagsVBox;
    private SimpleObjectProperty<Tag> observableTag;

    @Inject
    public TagManagementCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        tagAnchorPane = new AnchorPane();
        observableBoard = new SimpleObjectProperty<Board>();
        this.tagsVBox = new VBox(3);
        tagsVBox.setAlignment(Pos.CENTER);

        observableBoard.addListener((obs, oldBoard, newBoard) -> updateScene(newBoard));
    }

    public void setIDs(long boardID, long tasklistID, long taskID, Board board) {
        this.board = board;
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
        tagAnchorPane.getChildren().add(tagsVBox);
        server.registerForMessages("/topic/" + boardID + "/add-tag", Tag.class, t -> {
            observableTag.set(t);
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
        tagNameTextField.requestFocus();
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

        String name = tagNameTextField.getText();
        if(name.trim().isEmpty() || name.trim() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Task name cannot be empty. Please enter a name for the task.");
            alert.showAndWait();
            return;
        }
        else {
            saveNameButton.setVisible(false);
            cancelNameButton.setVisible(false);
            editNameButton.setVisible(true);
            tagNameTextField.setEditable(false);
        }
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
        String name = tagNameTextField.getText();
        tagNameText.setVisible(false);
        tagNameTextField.setVisible(false);
        editNameButton.setVisible(false);
        createTagButton.setVisible(false);
        cancelTagButton.setVisible(false);
        saveNameButton.setVisible(false);
        cancelNameButton.setVisible(false);
        tagNameTextField.setText("New Tag");
        tagNameTextField.setEditable(false);

        Tag newTag = server.createTag(boardID, name);

        if (newTag==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Failed to add the tag: Unable to send the request.");
            alert.showAndWait();
        }
        else {
            TagComponent newTagComponent = new TagComponent(observableBoard, mainCtrl, this, server);
            newTagComponent.setTag(newTag, boardID);
            tagsVBox.getChildren().add(newTagComponent);
            server.send("/topic/" + boardID + "/add-tag", newTag);
        }
    }

    @FXML
    private void onBackButtonClicked() {
        this.resetFields();
        mainCtrl.getPopUpStage().close();
    }

    public void updateScene(Board board) {

        observableBoard.set(board);
        tagAnchorPane.getChildren().clear();
        tagAnchorPane.getChildren().add(tagsVBox);
        tagsVBox.getChildren().clear();

        for (Tag tag : board.getTagList()) {
            TagComponent tagComponent = new TagComponent(observableBoard, mainCtrl, this, server);
            tagComponent.setTag(tag, boardID);
            tagsVBox.getChildren().add(tagComponent);
        }

    }

    public VBox getTagsVBox() {
        return tagsVBox;
    }

    public void unregisterMessages() {
        server.unregisterForMessages("/topic/" + boardID + "/add-tag");
    }

    private void resetFields() {
        tagNameTextField.setText("New Tag");
        tagNameTextField.setVisible(false);
        tagNameText.setVisible(false);
        createTagButton.setVisible(false);
        cancelTagButton.setVisible(false);
        editNameButton.setVisible(false);
        saveNameButton.setVisible(false);
        cancelNameButton.setVisible(false);
    }
}

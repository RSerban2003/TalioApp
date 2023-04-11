package client.scenes;

import client.components.NestedTaskComponent;
import client.components.TagTaskDetailComponent;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import commons.Task;
import javafx.beans.property.SimpleObjectProperty;
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

public class TaskOverviewCtrl {
    @FXML
    private Button editTitleButton;

    @FXML
    private Button editDescriptionButton;

    @FXML
    private Button backButton;

    @FXML
    private Button saveTitleButton;

    @FXML
    private Button saveDescriptionButton;

    @FXML
    private TextArea titleTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane nestedTaskAnchorPane;
    @FXML
    private AnchorPane tagAnchorPane;

    private ServerUtils server;

    private final WebSocketUtils webSocket;

    private MainCtrl mainCtrl;

    private String initialTitle;

    private String initialDescription;
    private SimpleObjectProperty<Task> observableTask;

    private NestedTaskComponent nestedTaskComponent;
    private TagTaskDetailComponent tagTaskDetailComponent;

    private long boardID;
    private long tasklistID;
    private long taskID;

    @Inject
    public TaskOverviewCtrl(ServerUtils server, WebSocketUtils webSocket, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.webSocket = webSocket;
        nestedTaskAnchorPane = new AnchorPane();
        tagAnchorPane = new AnchorPane();
        observableTask = new SimpleObjectProperty<Task>();
        nestedTaskComponent = new NestedTaskComponent(observableTask, server, mainCtrl);
        tagTaskDetailComponent = new TagTaskDetailComponent(observableTask, server, mainCtrl);
    }

    public void setIDs(long boardID, long tasklistID, long taskID) {
        this.boardID = boardID;
        this.tasklistID = tasklistID;
        this.taskID = taskID;
        nestedTaskComponent.setBoardId(boardID);
        nestedTaskComponent.setTaskListId(tasklistID);
        tagTaskDetailComponent.setBoardId(boardID);
        tagTaskDetailComponent.setTaskListId(tasklistID);
        if(boardID != 0) {
            webSocket.registerForMessages("/topic/" + boardID + "/" + tasklistID + "/" + taskID, Task.class, q -> {
                observableTask.set(q);
            });
        }
    }

    public void updateScene(Task task) {
        titleTextArea.setText(task.getName());
        descriptionTextArea.setText(task.getDescription());
        observableTask.set(task);
        nestedTaskAnchorPane.getChildren().clear();
        nestedTaskAnchorPane.getChildren().add(nestedTaskComponent);
        tagAnchorPane.getChildren().clear();
        tagAnchorPane.getChildren().add(tagTaskDetailComponent);
    }

    @FXML
    private void onEditTitleButtonClicked() {
        initialTitle = titleTextArea.getText();
        titleTextArea.setEditable(true);
        titleTextArea.requestFocus();
        editTitleButton.setVisible(false);
        saveTitleButton.setVisible(true);
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
        editDescriptionButton.setVisible(false);
        saveDescriptionButton.setVisible(true);
    }

    @FXML
    private void addNestedTask(){
        server.addNestedTask(boardID, tasklistID, taskID);
    }

    @FXML
    private void onSaveTitleButtonClicked() {
        saveTitleButton.setVisible(false);
        editTitleButton.setVisible(true);
        String title = titleTextArea.getText().trim();
        String description = descriptionTextArea.getText().trim();
        if (title == null || title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Task name cannot be empty. Please enter a name for the task.");
            alert.showAndWait();
            return;
        }
        Map<String, String> body = new HashMap<>();
        if (title.isEmpty()) {
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
        webSocket.unregisterForMessages("/topic/"+boardID+"/"+tasklistID+"/"+taskID);
        mainCtrl.getPopUpStage().close();
        titleTextArea.setEditable(false);
    }

    @FXML
    private void onSaveDescriptionButtonClicked() {
        saveDescriptionButton.setVisible(false);
        editDescriptionButton.setVisible(true);
        String title = titleTextArea.getText().trim();
        String description = descriptionTextArea.getText().trim();
        Map<String, String> body = new HashMap<>();
        if (!server.editTask(boardID, tasklistID, taskID, title, description)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Failed to add the task: Unable to send the request.");
            alert.showAndWait();
            return;
        }
        webSocket.unregisterForMessages("/topic/"+boardID+"/"+tasklistID+"/"+taskID);
        descriptionTextArea.setEditable(false);
    }

    @FXML
    private void onBackButtonClicked() {
        this.resetFields();
        webSocket.unregisterForMessages("/topic/"+boardID+"/"+tasklistID+"/"+taskID);
        mainCtrl.getPopUpStage().close();
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
                    onBackButtonClicked();
                }
                break;
            default:
                break;
        }
    }

    public long getBoardID() {
        return boardID;
    }

    public long getTaskListID() {
        return tasklistID;
    }

    public long getTaskID() {
        return taskID;
    }

    public NestedTaskComponent getNestedTaskComponent() {
        return nestedTaskComponent;
    }

    public void setObservableTask(SimpleObjectProperty<Task> observableTask) {
        this.observableTask = observableTask;
    }
}

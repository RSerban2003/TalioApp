package client.components;

import client.scenes.MainCtrl;
import client.scenes.TagManagementCtrl;
import client.utils.ServerUtils;
import commons.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.inject.Inject;

public class TagComponent extends VBox {

    private Long boardId;
    private Tag tag;
    private MainCtrl mainCtrl;

    private SimpleObjectProperty<Board> observableBoard;

    private String initialName;

    private TagManagementCtrl tagManagementCtrl;

    private ServerUtils server;

    private SimpleObjectProperty<Tag> observableTag;

    private static final String style = "-fx-background-color: #615f5e; -fx-border-width: 2; -fx-border-color: #615f5e;"
            + "-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";

    @Inject
    public TagComponent(SimpleObjectProperty<Board> observableBoard, MainCtrl mainCtrl, TagManagementCtrl tagManagementCtrl, ServerUtils serverUtils) {
        super();
        this.observableBoard = observableBoard;
        this.mainCtrl = mainCtrl;
        this.tagManagementCtrl = tagManagementCtrl;
        this.server = serverUtils;

    }
    private void initializeTagComponent() {
        //Tag name box
        Label tagNameLabel = new Label();
        tagNameLabel.setText(tag.getName());

        TextField tagNameField = new TextField();
        tagNameField.setText(tag.getName());
        tagNameField.setVisible(false);

        tagNameLabel.setPadding(new Insets(5, 0, 5, 6));
        tagNameField.setPadding(new Insets(5, 0, 5, 5));

        tagNameLabel.setStyle("-fx-background-color: #615f5e; -fx-text-fill: white;");
        tagNameField.setStyle("-fx-background-color: #8c8886; -fx-text-fill: white;");

        tagNameLabel.setMinWidth(150);
        tagNameLabel.setMaxWidth(150);
        tagNameField.setMinWidth(150);
        tagNameField.setMaxWidth(150);

        //Tag delete button
        Button deleteButton = new Button("X");
        deleteButton.setStyle("-fx-background-color: #454342; -fx-text-fill:  #FFFFFF;");
        deleteButton.setMinWidth(25);
        deleteButton.setMaxWidth(25);

        // Tag name edit buttons
        Button editNameButton = new Button("Edit");
        Button saveNameButton = new Button("Save");
        Button cancelNameButton = new Button("Cancel");

        editNameButton.setStyle("-fx-background-color: #454342; -fx-text-fill: white;");
        saveNameButton.setStyle("-fx-background-color: #454342; -fx-text-fill: white;");
        cancelNameButton.setStyle("-fx-background-color: #454342; -fx-text-fill: white;");

        saveNameButton.setVisible(false);
        cancelNameButton.setVisible(false);

        editNameButton.setMinWidth(53);
        editNameButton.setMaxWidth(53);
        saveNameButton.setMinWidth(53);
        saveNameButton.setMaxWidth(53);
        cancelNameButton.setMinWidth(62);
        cancelNameButton.setMaxWidth(62);

        //Make the name field and name label overlap
        StackPane overlappingFields = new StackPane();
        overlappingFields.getChildren().addAll(tagNameLabel, tagNameField);

        //Make the Edit and Save buttons overlap
        StackPane overlappingButtons = new StackPane();
        overlappingButtons.getChildren().addAll(editNameButton, saveNameButton);

        //Add all the UI elements to the body of the component
        HBox topRow = new HBox(5);
        topRow.getChildren().addAll(deleteButton, overlappingButtons, cancelNameButton);

        VBox tagBody = new VBox(5);
        tagBody.getChildren().addAll(topRow, overlappingFields);
        tagBody.setPadding(new Insets(5, 5, 5, 5));

        // Add the body to the component itself
        this.getChildren().addAll(tagBody);

        // Apply the style to the component
        this.setStyle(style);

        // Add functionalities for all UI elements
        // Add character limit for name field
        tagNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 20) {
                tagNameField.setText(oldValue);
            }
        });
        tagNameField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });

        // Delete button
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Tag");
            alert.setHeaderText("This tag will be deleted and removed from all associated tasks. Are you sure you want to proceed? ");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {

                tagManagementCtrl.getTagsVBox().getChildren().remove(this);

                if(!server.deleteTag(boardId, tag.getId())) {
                    Alert deletionAlert = new Alert(Alert.AlertType.ERROR);
                    deletionAlert.setContentText("Failed to delete the tag: Unable to send the request.");
                    deletionAlert.showAndWait();
                }
            } else {
                alert.close();
            }
        });

        // Edit button
        editNameButton.setOnAction(event -> {
            tagNameLabel.setVisible(false);
            saveNameButton.setVisible(true);
            cancelNameButton.setVisible(true);
            tagNameField.setVisible(true);
            tagNameField.setEditable(true);
            tagNameField.setText(tagNameLabel.getText());
            initialName = tagNameField.getText();
        });

        // Save button
        saveNameButton.setOnAction(event -> {
            String name = tagNameField.getText();

            if(name.trim().isEmpty() || name.trim() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Tag name cannot be empty. Please enter a valid name.");
                alert.showAndWait();
                return;
            }
            else {
                tagNameLabel.setText(name);
                saveNameButton.setVisible(false);
                cancelNameButton.setVisible(false);
                editNameButton.setVisible(true);
                tagNameField.setVisible(false);
                tagNameField.setEditable(false);
                tagNameLabel.setVisible(true);

                if (!server.editTag(boardId, tag.getId(), name)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Failed to edit the tag: Unable to send the request.");
                    alert.showAndWait();
                }
            }
        });

        // Cancel button
        cancelNameButton.setOnAction(event -> {
            tagNameField.setText(tagNameLabel.getText());
            saveNameButton.setVisible(false);
            cancelNameButton.setVisible(false);
            editNameButton.setVisible(true);
            tagNameField.setVisible(false);
            tagNameField.setEditable(false);
            tagNameLabel.setVisible(true);

        });
    }

    public void setTag(Tag tag, Long boardId) {
        this.tag = tag;
        this.boardId = boardId;
        if(!(boardId == null)) {
            initializeTagComponent();
        }
        else {
            System.out.println("Error: Board is null in the TagComponent.");
        }
    }
}

package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.yaml.snakeyaml.nodes.Tag;

import javax.inject.Inject;

public class TagComponent extends VBox {

    private long tagId;

    private Board board;

    private Tag tag;
    private MainCtrl mainCtrl;

    private String initialName;

    @Inject
    public TagComponent(Board board, Tag tag, MainCtrl mainCtrl, ServerUtils serverUtils) {
        super();
        this.mainCtrl = mainCtrl;
        this.tag = tag;


        //Tag name box
        Label tagNameLabel = new Label();
        tagNameLabel.setText(tag.getName());

        TextField tagNameField = new TextField();
        tagNameField.setText(tag.getName());
        tagNameField.setVisible(false);

        StackPane overlappingFields = new StackPane();
        overlappingFields.getChildren().addAll(tagNameLabel, tagNameField);

        //Tag delete button
        Button deleteButton = new Button("X");
        deleteButton.setOnAction(event -> {
            AnnotationConfigApplicationContext context
                    = new AnnotationConfigApplicationContext();
            context.scan("client");
            context.refresh();
            ServerUtils server = context.getBean(ServerUtils.class);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Tag");
            alert.setHeaderText("This tag will be deleted and removed from all associated tasks. Are you sure you want to proceed? ");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                server.deleteTag(board.getId(), tag.getId());
            }
            else {
                alert.close();
            }
        });

        tagNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                tagNameField.setText(oldValue);
            }
        });
        tagNameField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });

        tagNameLabel.setMinWidth(200);
        tagNameLabel.setMaxWidth(200);
        tagNameField.setMinWidth(200);
        tagNameField.setMaxWidth(200);

        HBox topRow = new HBox();
        topRow.getChildren().addAll(overlappingFields, deleteButton);


        // Tag name edit buttons
        Button editNameButton = new Button("Edit");
        Button saveNameButton = new Button("Save");
        Button cancelNameButton = new Button("Cancel");

        editNameButton.setMinWidth(50);
        editNameButton.setMaxWidth(50);
        saveNameButton.setMinWidth(50);
        saveNameButton.setMaxWidth(50);
        cancelNameButton.setMinWidth(50);
        cancelNameButton.setMaxWidth(50);

        editNameButton.setOnAction(event -> {
            tagNameLabel.setVisible(false);
            saveNameButton.setVisible(true);
            cancelNameButton.setVisible(true);
            tagNameField.setVisible(true);
            tagNameField.setEditable(true);
            tagNameField.setText(tagNameLabel.getText());
            initialName = tagNameField.getText();
        });

        saveNameButton.setOnAction(event -> {
            String name = tagNameField.getText();

            if(name.trim().isEmpty() || name.trim() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Tag name cannot be empty. Please enter a valid name.");
                alert.showAndWait();
                return;
            }
            else {

                AnnotationConfigApplicationContext context
                        = new AnnotationConfigApplicationContext();
                context.scan("client");
                context.refresh();
                ServerUtils server = context.getBean(ServerUtils.class);
                server.editTag(board.getId(), tag.getId(), name);

                tagNameLabel.setText(name);
                saveNameButton.setVisible(false);
                cancelNameButton.setVisible(false);
                editNameButton.setVisible(true);
                tagNameField.setVisible(false);
                tagNameField.setEditable(false);
                tagNameLabel.setVisible(true);
            }
        });

        cancelNameButton.setOnAction(event -> {
            tagNameField.setText(tagNameLabel.getText());
            saveNameButton.setVisible(false);
            cancelNameButton.setVisible(false);
            editNameButton.setVisible(true);
            tagNameField.setVisible(false);
            tagNameField.setEditable(false);
            tagNameLabel.setVisible(true);

        });

        HBox bottomRow = new HBox();
        bottomRow.getChildren().addAll(saveNameButton,editNameButton,cancelNameButton);

        VBox tagButtons = new VBox();
        tagButtons.getChildren().addAll(topRow, bottomRow);
    }
}

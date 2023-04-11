package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.NestedTask;
import commons.Tag;
import commons.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.inject.Inject;

public class SubTagTaskDetailComponent extends VBox {
    private final long tagId;
    private static final String style = "-fx-background-color: #f7f7f5; -fx-border-width: 2; -fx-border-color: gray;  -fx-border-radius: 1 1 1 1;-fx-background-radius: 10 10 10 10;";
    private MainCtrl mainCtrl;
    @Inject
    public SubTagTaskDetailComponent(Tag tag, Long taskId, Long taskListId, Long boardId, MainCtrl mainCtrl) {
        super();
        this.tagId = tag.getId();
        this.mainCtrl = mainCtrl;

        //Creates the name box
        Label nameLabel = new Label(tag.getName());
        nameLabel.setAlignment(Pos.CENTER);
        //get server instance
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext();
        context.scan("client");
        context.refresh();
        ServerUtils server = context.getBean(ServerUtils.class);

        // Create delete button
        Button deleteButton = new Button("X");
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Delete Tag");
            alert.setContentText("Are you sure you want to delete this tag? This action cannot be undone.");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                server.removeTag(boardId, taskListId, taskId, tagId);
            }
            else {
                alert.close();
            }
        });
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setHgap(5.0);
        gridPane.setVgap(50.0);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(deleteButton, 1, 0);
        setMaxSize(tag.getName().length()*7+50, 40);
        setMinSize(tag.getName().length()*7+50, 40);
        getChildren().add(gridPane);
        setAlignment(Pos.CENTER);
        setStyle(style);
        setSpacing(10.0);
    }

    public long getTagId() {
        return tagId;
    }
}

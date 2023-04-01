package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.NestedTask;
import commons.Task;
import commons.TaskList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.inject.Inject;

public class SubNestedTaskComponent extends VBox {
    private final long nestedId;
    private static final String style = "-fx-background-color: #f7f7f5; -fx-border-width: 2; -fx-border-color: gray;  -fx-border-radius: 1 1 1 1;-fx-background-radius: 10 10 10 10;";

    private MainCtrl mainCtrl;
    @Inject
    public SubNestedTaskComponent(NestedTask nestedTask, Task task, Long taskListId, Long boardId, MainCtrl mainCtrl) {
        super();
        this.nestedId = nestedTask.getId();
        this.mainCtrl = mainCtrl;
        //Creates the name box
        Label nameLabel = new Label(nestedTask.getName());
        HBox topRow = new HBox(nameLabel);
        topRow.setAlignment(Pos.CENTER);



        // create checkbox
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(nestedTask.getComplete());
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext();
        context.scan("client");
        context.refresh();
        ServerUtils server = context.getBean(ServerUtils.class);
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                server.editNestedTask(boardId, taskListId, task.getId(), nestedTask.getId(), nestedTask.getName(), true);
            } else {
                server.editNestedTask(boardId, taskListId, task.getId(), nestedTask.getId(), nestedTask.getName(), false);
            }
        });



        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setHgap(10.0);
        gridPane.setVgap(10.0);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.add(topRow, 1, 0);
        gridPane.add(checkBox, 0, 0);


        setMaxSize(300, 40);
        setMinSize(300, 40);
        getChildren().add(gridPane);
        setAlignment(Pos.CENTER);
        setStyle(style);
        setSpacing(10.0);
    }
}

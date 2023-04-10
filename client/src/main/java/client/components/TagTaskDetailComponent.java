package client.components;


import client.scenes.MainCtrl;
import client.scenes.TaskOverviewCtrl;
import client.utils.ServerUtils;
import commons.*;
import javafx.beans.property.SimpleObjectProperty;
import commons.Task;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class TagTaskDetailComponent extends AnchorPane {

    private Long boardId;
    private Long taskListId;
    private MainCtrl mainCtrl;

    private SimpleObjectProperty<Task> taskSimpleObjectProperty;

    private String initialName;

    private TaskOverviewCtrl taskOverviewCtrl;

    private ServerUtils server;

    private static final String style = "-fx-background-color: #615f5e; -fx-border-width: 2; -fx-border-color: #615f5e;"
            + "-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;";

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public void setTaskListId(Long taskListId) {
        this.taskListId = taskListId;
    }

    @Inject
    public TagTaskDetailComponent(SimpleObjectProperty<Task> taskSimpleObjectProperty, ServerUtils server, MainCtrl mainCtrl) {
        super();
        this.taskSimpleObjectProperty = taskSimpleObjectProperty;
        this.mainCtrl = mainCtrl;
        this.server = server;
        taskSimpleObjectProperty.addListener((observable, oldValue, newValue) -> update(newValue));
    }

    public void update(Task task) {
        Platform.runLater(
                () -> {
                    getChildren().clear();
                    if(task.getNestedTasks() != null){
                        SubTagTaskDetailComponent[] tags = task.getTagList().stream()
                                .map((Tag tag1) -> new SubTagTaskDetailComponent(tag1, task.getId(), taskListId, boardId, mainCtrl))
                                .toArray(SubTagTaskDetailComponent[]::new);
                        HBox tagContainer = new HBox(tags);
                        tagContainer.setSpacing(10.0);
                        AnchorPane.setTopAnchor(tagContainer, 0.0);
                        AnchorPane.setLeftAnchor(tagContainer, 10.0);
                        getChildren().clear();
                        getChildren().add(tagContainer);
                    }
                });
    }

}

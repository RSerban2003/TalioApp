package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.NestedTask;
import commons.Task;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

import static client.components.TaskListComponent.mapFormat;

public class NestedTaskComponent extends AnchorPane {
    private SimpleObjectProperty<Task> task;
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private Long boardId;
    private Long taskListId;
    private final int NESTEDOFFSET = 340;
    private final int NESTEDHEIGHT = 40;


    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public void setTaskListId(Long taskListId) {
        this.taskListId = taskListId;
    }

    public NestedTaskComponent(SimpleObjectProperty<Task> task, ServerUtils server, MainCtrl mainCtrl) {
        super();
        this.task = task;
        this.server = server;
        this.mainCtrl = mainCtrl;
        task.addListener((observable, oldValue, newValue) -> update(newValue));
    }

    public void update(Task task){
        Platform.runLater(
                () -> {
                    getChildren().clear();
                    HBox hbox = new HBox();
                    if(task.getNestedTasks() != null) {
                        SubNestedTaskComponent[] nestedTasks = task.getNestedTasks().stream()
                            .map((NestedTask nestedTask1) -> new SubNestedTaskComponent(nestedTask1, task, taskListId, boardId, mainCtrl))
                            .toArray(SubNestedTaskComponent[]::new);
                        for (SubNestedTaskComponent nested : nestedTasks) {
                            nested.setOnDragOver(event -> {
                                event.acceptTransferModes(TransferMode.ANY);
                                event.consume();
                            });
                            nested.setOnDragDropped(event -> {
                                Dragboard db = event.getDragboard();
                                event.setDropCompleted(db.hasString());
                                AnnotationConfigApplicationContext context
                                    = new AnnotationConfigApplicationContext();
                                context.scan("client");
                                context.refresh();
                                ServerUtils server = context.getBean(ServerUtils.class);
                                Map<String, Long> params = (Map<String, Long>) db.getContent(mapFormat);
                                int index = (int) ((event.getSceneY() - NESTEDOFFSET) / NESTEDHEIGHT);
                                server.moveNestedTask(boardId, taskListId, task.getId(), params.get("nestedTaskId"), index);
                                event.consume();
                            });
                            nested.setOnDragDetected(event -> {
                                Dragboard db = nested.startDragAndDrop(TransferMode.ANY);
                                ClipboardContent content = new ClipboardContent();
                                content.put(mapFormat, Map.of("nestedTaskId", nested.getNestedId(), "taskId", task.getId()));
                                db.setContent(content);
                                event.consume();
                            });
                        }
                        VBox taskListContainer = new VBox(nestedTasks);
                        AnchorPane.setTopAnchor(taskListContainer, 150.0);
                        AnchorPane.setLeftAnchor(taskListContainer, 150.0);

                        hbox.getChildren().add(taskListContainer);

                    }
                getChildren().add(hbox);
            });
    }
}

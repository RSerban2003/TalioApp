package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.Task;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;

public class NestedTaskComponent extends AnchorPane {
    private SimpleObjectProperty<Task> task;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    public NestedTaskComponent(SimpleObjectProperty<Task> task, ServerUtils server, MainCtrl mainCtrl) {
        super();
        this.task = task;
        this.server = server;
        this.mainCtrl = mainCtrl;
        task.addListener((observable, oldValue, newValue) -> update(newValue));
    }

    public void update(Task task){

    }
}

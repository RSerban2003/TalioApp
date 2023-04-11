package client.scenes;

import client.components.SubListBoardComponent;
import client.utils.ServerUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.List;

public class AddTagCtrl {

    @FXML
    private Button LeaveButton;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    private Long boardID;
    private Long listID;
    private Long taskId;


    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

        Platform.runLater( () -> {
            List tagsList = server.getBoard(boardID).getTagList();
            VBox taskListContainer = new VBox((Node) tagsList);
            taskListContainer.setSpacing(45.0);
            AnchorPane.setTopAnchor(taskListContainer, 150.0);
            AnchorPane.setLeftAnchor(taskListContainer, 150.0);

            HBox hbox = new HBox(taskListContainer);
            hbox.setSpacing(20.0);
            hbox.setLayoutX(50.0);
            hbox.setLayoutY(50.0);
            hbox.setPrefSize(1000, 1300 );

            getChildren().clear();
            getChildren().add(hbox);
        });
    }
    @FXML
    public void onLeaveButtonClicked() {
        mainCtrl.showTaskOverview();
    }
}

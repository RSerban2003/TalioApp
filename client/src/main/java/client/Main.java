/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;


import client.utils.ServerUtils;
import com.google.inject.Injector;

import client.scenes.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var connect = FXML.load(ConnectCtrl.class, "client", "scenes", "Connect.fxml");
        var boardInput = FXML.load(BoardInputCtrl.class, "client", "scenes", "BoardInput.fxml");
        var board = FXML.load(BoardCtrl.class, "client", "scenes", "Board.fxml");
        var taskList1 = FXML.load(AddTaskListCtrl.class, "client", "scenes", "TaskListPanel.fxml");
        var addTask = FXML.load(AddTaskCtrl.class, "client", "scenes", "AddTask.fxml");
        var adminPass = FXML.load(AdminPassCtrl.class, "client", "scenes", "AdminPass.fxml");
        var adminDash = FXML.load(AdminDashboardCtrl.class, "client", "scenes", "AdminDashboard.fxml");
        var tagManagement = FXML.load(TagManagementCtrl.class, "client", "scenes", "TagManagement.fxml");
        var taskOverview = FXML.load(TaskOverviewCtrl.class, "client", "scenes", "TaskOverview.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        ServerUtils server = new ServerUtils();
        var createBoard = FXML.load(CreateBoardCtrl.class, "client", "scenes", "CreateBoard.fxml");

        primaryStage.setOnCloseRequest(event -> {
            mainCtrl.stop();
        });
        mainCtrl.initialize(primaryStage, connect, boardInput, board, taskList1, addTask, taskOverview, createBoard, server, adminPass, adminDash, tagManagement);
    }
}
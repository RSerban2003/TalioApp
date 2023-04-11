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
package client.scenes;

import commons.Task;
import client.utils.ServerUtils;
import commons.Board;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCtrl {

    private Stage primaryStage;
    private Stage popUpStage;
    private ConnectCtrl connectCtrl;
    private Scene connect;
    private BoardInputCtrl boardInputCtrl;
    private Scene boardInput;
    private Scene taskList1;
    private AddTaskListCtrl addTaskListCtrl;
    private Scene board;
    private Scene createBoard;
    private ServerUtils server;
    private BoardCtrl boardCtrl;
    private CreateBoardCtrl createBoardCtrl;

    private Scene addTask;
    private AddTaskCtrl addTaskCtrl;

    private Scene adminPass;
    private AdminPassCtrl adminPassCtrl;

    private Scene adminDashboard;
    private AdminDashboardCtrl adminDashboardCtrl;

    private Scene taskOverview;
    private TaskOverviewCtrl taskOverviewCtrl;
    private Scene editTask;

    private long boardID;
    private long taskListID;
    private long taskID;
    private Task task;

    public void initialize(Stage primaryStage, Pair<ConnectCtrl, Parent> connect, Pair<BoardInputCtrl, Parent> boardInput,
                           Pair<BoardCtrl, Parent> board, Pair<AddTaskListCtrl, Parent> taskList1, Pair<AddTaskCtrl, Parent> addTask,
                           Pair<TaskOverviewCtrl, Parent> taskOverview, Pair<CreateBoardCtrl, Parent> createBoard, ServerUtils server,
                           Pair<AdminPassCtrl, Parent> adminPass, Pair<AdminDashboardCtrl, Parent> admindash) {

        this.primaryStage = primaryStage;

        this.popUpStage = new Stage();
        popUpStage.initModality(Modality.APPLICATION_MODAL);

        this.connectCtrl = connect.getKey();
        this.connect = new Scene(connect.getValue(), 900, 500);

        this.boardInputCtrl = boardInput.getKey();
        this.boardInput = new Scene(boardInput.getValue(), 900, 500);

        this.boardCtrl = board.getKey();
        this.board = new Scene(board.getValue(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

        this.addTaskCtrl = addTask.getKey();
        this.addTask = new Scene(addTask.getValue(), 900, 500);

        this.server = server;
        server.startUpEXEC();

        this.addTaskListCtrl = taskList1.getKey();
        this.taskList1 = new Scene(taskList1.getValue(),900, 500);

        this.createBoardCtrl = createBoard.getKey();
        this.createBoard = new Scene(createBoard.getValue(), 900, 500);

        this.taskOverviewCtrl = taskOverview.getKey();
        this.taskOverview = new Scene(taskOverview.getValue(), 900, 650);

        this.adminPassCtrl = adminPass.getKey();
        this.adminPass = new Scene(adminPass.getValue(), 900, 500);

        this.adminDashboardCtrl = admindash.getKey();
        this.adminDashboard = new Scene(admindash.getValue(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

        showConnect();
        primaryStage.getIcons().add(new Image("taliologo.png"));
        primaryStage.show();
    }

    public Stage getPopUpStage() {
        return popUpStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void stop(){
        server.stop();
    }
    public void showConnect() {
        primaryStage.setResizable(false);
        primaryStage.setTitle("Connect: select a hostname");
        primaryStage.setScene(connect);
        primaryStage.setX(Screen.getPrimary().getVisualBounds().getWidth() / 2 - connect.getWidth() / 2);
        primaryStage.setY(Screen.getPrimary().getVisualBounds().getHeight() / 2 - connect.getHeight() / 2);
        connect.setOnKeyPressed(e -> connectCtrl.keyPressed(e));

    }
    public void showBoard(){
        primaryStage.setResizable(true);
        primaryStage.setTitle("Taskboard");
        primaryStage.setScene(board);
        primaryStage.setMaximized(true);
    }

    public void showTaskOverview() {
        popUpStage.setResizable(false);
        primaryStage.setResizable(false);
        taskOverviewCtrl.setIDs(boardID, taskListID, taskID);
        taskOverviewCtrl.updateScene(task);
        popUpStage.setTitle("Edit task");
        popUpStage.setScene(taskOverview);
        popUpStage.show();
        popUpStage.setX(Screen.getPrimary().getVisualBounds().getWidth() / 2 - taskOverview.getWidth() / 2);
        popUpStage.setY(Screen.getPrimary().getVisualBounds().getHeight() / 2 - taskOverview.getHeight() / 2);
        taskOverview.setOnKeyPressed(e -> taskOverviewCtrl.keyPressed(e));
    }

    public void showCreateBoard(){
        primaryStage.setResizable(false);
        primaryStage.setTitle("Create a Board");
        primaryStage.setScene(createBoard);
        primaryStage.setX(Screen.getPrimary().getVisualBounds().getWidth() / 2 - createBoard.getWidth() / 2);
        primaryStage.setY(Screen.getPrimary().getVisualBounds().getHeight() / 2 - createBoard.getHeight() / 2);
    }

    public void showBoardinput() {
        primaryStage.setResizable(false);
        primaryStage.setTitle("Board: select a board id");
        primaryStage.setScene(boardInput);
        boardInput.setOnKeyPressed(e -> boardInputCtrl.keyPressed(e));
        primaryStage.setX(Screen.getPrimary().getVisualBounds().getWidth() / 2 - boardInput.getWidth() / 2);
        primaryStage.setY(Screen.getPrimary().getVisualBounds().getHeight() / 2 - boardInput.getHeight() / 2);
    }
    public void updateBoard(Board board) {
        this.boardID = board.getId();
        boardCtrl.updateBoard(board);
    }
    public void showAddTaskList() {
        popUpStage.setResizable(false);
        addTaskListCtrl.setIDs(boardID);
        popUpStage.setTitle("Create a new TaskList");
        popUpStage.setScene(taskList1);
        popUpStage.show();
        popUpStage.setX(Screen.getPrimary().getVisualBounds().getWidth() / 2 - taskList1.getWidth() / 2);
        popUpStage.setY(Screen.getPrimary().getVisualBounds().getHeight() / 2 - taskList1.getHeight() / 2);
        taskList1.setOnKeyPressed(e -> addTaskListCtrl.keyPressed(e));
    }
    public void setTask(Task task) {
        this.taskID = task.getId();
        this.task = task;
    }

    public void setTaskList(long taskListID) {
        this.taskListID = taskListID;
    }
    public void showAddTask() {
        popUpStage.setResizable(false);
        addTaskCtrl.setIDs(boardID, taskListID);
        popUpStage.setTitle("Add a new task");
        popUpStage.setScene(addTask);
        popUpStage.setX(Screen.getPrimary().getVisualBounds().getWidth() / 2 - addTask.getWidth() / 2);
        popUpStage.setY(Screen.getPrimary().getVisualBounds().getHeight() / 2 - addTask.getHeight() / 2);
        popUpStage.show();
        addTask.setOnKeyPressed(e -> addTaskCtrl.keyPressed(e));
    }

    public void addDefaultTask(String title) {
        String description = "";
        Map<String, String> body = new HashMap<>();
        body.put("name", title.trim());
        body.put("description", description);
        if (!server.addTask(boardID,taskListID,body)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Failed to add the task: Unable to send the request.");
            alert.showAndWait();
        }
    }
    public void showAdminPass(){
        primaryStage.setResizable(false);
        primaryStage.setTitle("Enter admin password");
        primaryStage.setScene(adminPass);
        adminPassCtrl.generatePass();
        primaryStage.setX(Screen.getPrimary().getVisualBounds().getWidth() / 2 - adminPass.getWidth() / 2);
        primaryStage.setY(Screen.getPrimary().getVisualBounds().getHeight() / 2 - adminPass.getHeight() / 2);
        adminPass.setOnKeyPressed(e -> adminPassCtrl.keyPressed(e));
    }

    public void showAdminDash(){
        primaryStage.setResizable(true);
        primaryStage.setTitle("Admin dashboard");
        primaryStage.setScene(adminDashboard);
        primaryStage.setMaximized(true);
        adminDashboardCtrl.getUpdates();
    }
    public void updateAdminDash(List<Board> board) {
        adminDashboardCtrl.updateAdmin(board);
    }
    public void refreshBoardList() {
        boardCtrl.refreshBoardList();
    }
    public Long getBoardId() {
        return boardID;
    }
}
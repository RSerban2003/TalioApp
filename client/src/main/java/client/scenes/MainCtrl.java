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

import client.components.BoardComponent;
import client.utils.ServerUtils;
import commons.Board;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;
    private ConnectCtrl connectCtrl;
    private Scene connect;
    private BoardInputCtrl boardInputCtrl;
    private Scene boardInput;
    private Scene taskList1;
    private AddTaskListCtrl addTaskListCtrl;
    private Scene board;
    private ServerUtils server;
    private BoardCtrl boardCtrl;

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
            Pair<AddQuoteCtrl, Parent> add, Pair<ConnectCtrl, Parent> connect, Pair<BoardInputCtrl, Parent> boardInput,
                           Pair<BoardCtrl, Parent> board, Pair<AddTaskListCtrl, Parent> taskList1, ServerUtils server) {

        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.connectCtrl = connect.getKey();
        this.connect = new Scene(connect.getValue());

        this.boardInputCtrl = boardInput.getKey();
        this.boardInput = new Scene(boardInput.getValue());

        this.boardCtrl = board.getKey();
        this.board = new Scene(board.getValue(), 1900, 1000);

        this.server = server;

        this.addTaskListCtrl = taskList1.getKey();
        this.taskList1 = new Scene(taskList1.getValue());

        showConnect();
        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showConnect() {
        primaryStage.setTitle("Connect: select a hostname");
        primaryStage.setScene(connect);
        connect.setOnKeyPressed(e -> connectCtrl.keyPressed(e));

    }
    public void showBoard(){
        primaryStage.setTitle("Taskboard");
        primaryStage.setScene(board);
        //boardCtrl.hideEditFields();
    }

    public void showBoardinput() {
        primaryStage.setTitle("Board: select a board id");
        primaryStage.setScene(boardInput);
        boardInput.setOnKeyPressed(e -> boardInputCtrl.keyPressed(e));
    }
    public void updateBoard(Board board) {
        boardCtrl.updateBoard(board);
    }
    public void showAddTaskList() {
        primaryStage.setTitle("Create a new TaskList");
        primaryStage.setScene(taskList1);
        taskList1.setOnKeyPressed(e -> addTaskListCtrl.keyPressed(e));
    }
}
package server.api;

import commons.Board;
import commons.Task;
import commons.TaskList;

import java.util.List;

public class BoardWithTaskListDto {

    private Board board;
    private List<TaskList> taskList;

    public BoardWithTaskListDto(Board board, List<TaskList> taskList) {
        this.board = board;
        this.taskList = taskList;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<TaskList> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskList> taskList) {
        this.taskList = taskList;
    }
}


package server.api;

import commons.Board;
import commons.Task;
import commons.TaskList;

import java.util.List;

public class BoardWithTaskListDto {

    private Board board;
    private List<TaskList> listOfTaskList;

    public BoardWithTaskListDto(Board board, List<TaskList> listOfTaskList) {
        this.board = board;
        this.listOfTaskList = listOfTaskList;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<TaskList> getListOfTaskList() {
        return listOfTaskList;
    }

    public void setListOfTaskList(List<TaskList> listOfTaskList) {
        this.listOfTaskList = listOfTaskList;
    }
}


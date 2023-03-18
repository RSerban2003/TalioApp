package server.api;


import commons.Board;
import commons.Task;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;
import java.util.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards/{board}")

public class TaskListController {

    private BoardRepository boardRepository;

    private TaskListRepository taskListRepository;

    public TaskListController(TaskListRepository taskListRepository, BoardRepository boardRepository){

        this.boardRepository = boardRepository;
        this.taskListRepository = taskListRepository;

    }

    @PostMapping(path = "/add-taskList")
    public ResponseEntity<TaskList> add(@RequestBody String taskListName, @PathVariable("board") long boardId) throws RuntimeException {

        TaskList taskList = new TaskList();
        taskList.setName(taskListName);
        TaskList savedTaskList = taskListRepository.save(taskList);
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));
        board.add(new TaskList());
        boardRepository.save(board);
        return ResponseEntity.ok(savedTaskList);
    }
}
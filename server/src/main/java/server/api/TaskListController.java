package server.api;


import commons.Board;
import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.TaskListRepository;


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
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));
        board.add(taskList);
        taskList.setBoard(board);
        taskListRepository.save(taskList);
        return ResponseEntity.ok(taskList);
    }
    @PostMapping(path = "/{list}/edit")
    public ResponseEntity<TaskList> edit(@PathVariable("board") long boardId, @PathVariable("list") long listId, @RequestParam String name) throws RuntimeException {
        TaskList taskList = taskListRepository.findById(listId).orElseThrow(() -> new RuntimeException("List not found"));
        taskList.setName(name);
        taskListRepository.save(taskList);
        return ResponseEntity.ok(taskList);
    }
}
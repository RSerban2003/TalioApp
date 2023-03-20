package server.api;


import commons.Board;
import commons.TaskList;
import org.springframework.http.HttpStatus;
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

    @PostMapping(path = "/tasklist")
    public ResponseEntity<TaskList> add(@RequestBody String name, @PathVariable("board") long boardId) throws RuntimeException {
        TaskList taskList = new TaskList();
        taskList.setName(name);
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));
        board.add(taskList);
        taskList.setBoard(board);
        taskListRepository.save(taskList);
        return ResponseEntity.ok(taskList);
    }
    @PostMapping(path = "/{list}/edit")
    public ResponseEntity<?> edit(@PathVariable("board") long boardId, @PathVariable("list") long listId, @RequestParam String name) throws RuntimeException {
        if(!boardRepository.existsById(boardId)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board not found");

        if(!taskListRepository.existsById(listId)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List not found");
        TaskList taskList = taskListRepository.findById(listId).get();

        if(taskList.getBoard().getId() != boardId) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("List not part of board");
        taskList.setName(name);
        taskListRepository.save(taskList);
        return ResponseEntity.ok(taskList);
    }
}
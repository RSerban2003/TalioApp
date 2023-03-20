package server.api;

import commons.Board;
import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.TaskListRepository;

@RestController
@RequestMapping("boards/{board}")
public class ListController {

    private BoardRepository boardRepository;
    private TaskListRepository taskListRepository;

    public ListController(BoardRepository boardRepository, TaskListRepository taskListRepository) {
        this.boardRepository = boardRepository;
        this.taskListRepository = taskListRepository;
    }

    @GetMapping("/")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(taskListRepository.findAll());
    }

    @DeleteMapping("/{list}")
    public ResponseEntity<Board> delete(@PathVariable("board") long boardId, @PathVariable("list") long listId) {

        // check if taskList exists
        var tL = taskListRepository.findById(listId);
        if (!tL.isPresent()) return ResponseEntity.badRequest().build();
        TaskList taskList = tL.get();

        // check if board exists
        var b = boardRepository.findById(boardId);
        if (!b.isPresent()) return  ResponseEntity.badRequest().build();
        Board board = b.get();

        board.remove(taskList);
        boardRepository.save(board);

        return ResponseEntity.ok(board);
    }
}

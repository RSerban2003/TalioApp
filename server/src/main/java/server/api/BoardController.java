package server.api;


import commons.Board;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/{id}")
    public ResponseEntity<BoardWithTaskListDto> getBoardById(@PathVariable Long id){
        Board board = boardRepository.findById(id).orElse(null);
        if (board == null){
            return ResponseEntity.notFound().build();
        }
        List<TaskList> taskList = board.getTasklist();
        BoardWithTaskListDto dto = new BoardWithTaskListDto(board, taskList);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardById(@PathVariable Long id){
        if(id < 0 || !boardRepository.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        boardRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/")
    public ResponseEntity<Board> add(@RequestBody Board board){
        if(board === null || isNullOrEmpty(board.getTitle()) || board.getTasklist() == null){
            return ResponseEntity.badRequest().build();
        }

        Board saved = boardRepository.save(board);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}

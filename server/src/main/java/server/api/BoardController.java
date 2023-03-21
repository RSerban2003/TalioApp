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
    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(board);
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
    public ResponseEntity<Board> add(@RequestParam String name){
        if(name == null || name.isBlank()){
            return ResponseEntity.badRequest().build();
        }

        Board board = new Board();
        board.setTitle(name);
        Board saved = boardRepository.save(board);
        return ResponseEntity.ok(saved);
    }


    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping(path = {"/", ""})
    public ResponseEntity<List<Board>> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return ResponseEntity.ok(boards);
    }

}

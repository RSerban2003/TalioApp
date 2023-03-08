package server.api;


import commons.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable Long id){
        Board board = boardRepository.findById(id).orElse(null);
        if (board == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(board);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBoardById(@PathVariable Long id){
        Board board = boardRepository.findById(id).orElse(null);
        if (board == null){
            return ResponseEntity.notFound().build();
        }
        boardRepository.delete(board);
        return ResponseEntity.noContent().build();
    }

}

package server.api;


import commons.Board;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;
    private SimpMessagingTemplate msgs;

    public BoardController(BoardRepository boardRepository, SimpMessagingTemplate msgs) {
        this.boardRepository = boardRepository;
        this.msgs = msgs;
    }

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

        msgs.convertAndSend("/topic", "Board "+id+" has been deleted");
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path ={"/", ""})
    public ResponseEntity<Board> add(@RequestBody Map<String, String> body){
        if(!body.containsKey("name")){
            return ResponseEntity.badRequest().build();
        }

        Board board = new Board();
        board.setTitle(body.get("name"));
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

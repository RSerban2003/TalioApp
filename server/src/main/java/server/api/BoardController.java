package server.api;


import commons.Board;
import commons.ListOfBoards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping(path = {"","/"})
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(boardRepository.findAll());
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(board);
    }

    @PutMapping(path = {"{board}/patch"})
    public ResponseEntity<?> changeName(@RequestBody Map<String,String> body, @PathVariable("board") long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null || body.get("name") == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board not found");
        board.setTitle(body.get("name"));

        Board saved = boardRepository.save(board);
        Board board1 = boardRepository.findById(boardId).get();

        List<Board> boardList = boardRepository.findAll();
        ListOfBoards ret = new ListOfBoards(boardList);

        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, board1);
        msgs.convertAndSend("/topic/admin", ret);

        return ResponseEntity.ok(board);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardById(@PathVariable Long id){
        if(id < 0 || !boardRepository.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        boardRepository.deleteById(id);

        List<Board> boardList = boardRepository.findAll();
        ListOfBoards ret = new ListOfBoards(boardList);
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + id, new Board());
        msgs.convertAndSend("/topic/admin", ret);

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
}

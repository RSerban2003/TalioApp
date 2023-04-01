package server.api;


import commons.Board;
import commons.ListOfBoards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.BoardRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;
    private SimpMessagingTemplate msgs;
    private LongPollingController longPollingController;

    public BoardController(BoardRepository boardRepository, SimpMessagingTemplate msgs, LongPollingController longPollingController) {
        this.boardRepository = boardRepository;
        this.msgs = msgs;
        this.longPollingController = longPollingController;
    }

    @GetMapping(path = {"","/"})
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(boardRepository.findAll());
    }

    @GetMapping(path = {"/updates"})
    public DeferredResult<ResponseEntity<?>> getUpdates() {
        var res = new DeferredResult<ResponseEntity<?>>(1000L);
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        var key = new Object();
        longPollingController.listeners.put(key, q -> {
            res.setResult(ResponseEntity.ok(q));
        });
        res.onCompletion(() -> longPollingController.listeners.remove(key));

        return res;
    }

    @GetMapping("/{id}/get")
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

        List<Board> boardList = boardRepository.findAll();
        ListOfBoards ret = new ListOfBoards(boardList);
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + id, new Board());
        msgs.convertAndSend("/topic/admin", ret);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(path ={"/", ""})
    public ResponseEntity<Board> add(@RequestBody Map<String, String> body){
        if(!body.containsKey("name") || isNullOrEmpty(body.get("name"))
                || body.get("name").length() > 20 || body.get("name").length() < 3){
            return ResponseEntity.badRequest().build();
        }

        Board board = new Board();
        board.setTitle(body.get("name"));

        // send update to client through long polling
        Board saved = boardRepository.save(board);
        longPollingController.listeners.forEach((k, v) -> v.accept(saved));

        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

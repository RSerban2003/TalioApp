package server.api;


import commons.Board;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;
import java.util.Map;

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
        List<TaskList> listOfTaskList = board.getListOfTaskList();
        BoardWithTaskListDto dto = new BoardWithTaskListDto(board, listOfTaskList);
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

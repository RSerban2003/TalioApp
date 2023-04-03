package server.api;
import commons.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private BoardRepository boardRepository;
    private SimpMessagingTemplate msgs;
    private String password;

    public AdminController(BoardRepository boardRepository, SimpMessagingTemplate msgs, String password) {
        this.boardRepository = boardRepository;
        this.msgs = msgs;
        this.password = password;
    }

    public AdminController() {
    }

    public String getPassword() {
        return password;
    }

    @GetMapping(path = {"","/"})
    public void setPassword() {
        Random random = new Random();
        Long pass = random.nextLong();
        if(pass < 0) pass *= -1;
        this.password = String.valueOf(pass);
        System.out.println(password);
    }


    @PostMapping(path = {"","/"})
    public ResponseEntity<?> checkPass(@RequestBody Map<String, String> body) {
        String pass = body.get("pass");
        if(password == null || password.isEmpty() || pass == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(true);
    }
}

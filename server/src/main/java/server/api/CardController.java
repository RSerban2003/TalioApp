package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import commons.Board;
import commons.Tag;
import commons.Task;
import commons.TaskList;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/boards/{board}/{list}")
public class CardController {

    private final TaskListRepository taskListRepository;
    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;
    private SimpMessagingTemplate msgs;

    public CardController(BoardRepository boardRepository, TaskListRepository taskListRepository, TaskRepository taskRepository, SimpMessagingTemplate msgs) {
        this.boardRepository = boardRepository;
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
        this.msgs = msgs;
    }

    @GetMapping(path = {"/", ""})
    public ResponseEntity<?> showAll() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping(path = "/{card}/get")
    public ResponseEntity<?> getById(@PathVariable("card") long cardId) {
        //TODO : do the check if parents are good
        var TL = taskRepository.findById(cardId);
        if (TL.isPresent()) return ResponseEntity.ok(taskRepository.findById(cardId).get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
    }


    @PostMapping(path = "/card")
    public ResponseEntity<?> add(@RequestBody Map<String, String> body, @PathVariable("list") long listId,@PathVariable("board") long boardId) throws RuntimeException {
        if(body.get("name") == null || body.get("description") == null) return ResponseEntity.badRequest().build();
        Task task = new Task(body.get("name"), body.get("description"));
        TaskList taskList = taskListRepository.findById(listId).orElseThrow(() -> new RuntimeException("Task list not found"));
        taskList.add(task);
        task.setTaskList(taskList);

        taskRepository.save(task);

        Board board1 = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, board1);

        return ResponseEntity.ok(task);
    }

    @PostMapping("{task}/edit-card")
    public ResponseEntity<Task> edit(@RequestParam("name") String name, @RequestParam("description") String description, @PathVariable("task") long taskId, @PathVariable("board") long boardId, @PathVariable("list") long listId) throws JsonProcessingException {
        // check if board, list and task exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();

        // check if they are in relation
        Task t = taskRepository.findById(taskId).get();
        if (t.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (t.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();

        t.setName(name);
        t.setDescription(description);
        Task ta = taskRepository.save(t);

        Board board1 = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, board1);

        return ResponseEntity.ok(ta);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Object> deleteTask(@PathVariable("cardId") long cardId, @PathVariable("list") long listId, @PathVariable("board") long boardId) {

        // check if the task exists
        if (!taskRepository.existsById(cardId)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        Task task = taskRepository.findById(cardId).get();

        // check if the listId is valid
        if (!taskListRepository.existsById(listId)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tasklist not found");
        TaskList taskList = taskListRepository.findById(listId).get();
        if (task.getTaskList().getId() != listId) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not part of tasklist");

        for(Tag tag : task.getListOfTags()) {
            tag.removeTask(task);
            task.removeTag(tag);
        }

        // update the taskList and save
        taskList.remove(task);
        task.setTaskList(null);

        taskRepository.deleteById(cardId);

        taskListRepository.save(taskList);

        Board board1 = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, board1);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/{card}/move")
    public ResponseEntity<?> move(@PathVariable("card") long cardId, @PathVariable("list") long listId,
                                  @PathVariable("board") long boardId, @RequestBody Map<String, Long> body) {
        if(!body.containsKey("listTo") || !body.containsKey("index")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no");
        Optional<Task> task = taskRepository.findById(cardId);
        Optional<TaskList> taskListFrom = taskListRepository.findById(listId);
        long listToId = body.get("listTo");
        int index = body.get("index").intValue();
        Optional<TaskList> taskListTo = taskListRepository.findById(listToId);
        Optional<Board> board = boardRepository.findById(boardId);

        if (task.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        if (taskListFrom.isEmpty() || taskListTo.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tasklist not found");
        if (task.get().getTaskList().getId() != listId) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not part of tasklist");

        if(board.isEmpty()) return ResponseEntity.badRequest().build();
        if(taskListFrom.get().getBoard().getId() != boardId || taskListTo.get().getBoard().getId() != boardId) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("List not part of board");

        taskListFrom.get().remove(task.get());
        task.get().setTaskList(null);
        if (index > taskListTo.get().getTask().size()) {
            index = taskListTo.get().getTask().size();
        }
        taskListTo.get().add(index, task.get());
        task.get().setTaskList(taskListTo.get());
        taskListRepository.save(taskListFrom.get());
        taskListRepository.save(taskListTo.get());


        Board board1 = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, board1);

        return ResponseEntity.ok(taskListTo);
    }
}

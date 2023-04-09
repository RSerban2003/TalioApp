package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import commons.Board;
import commons.Tag;
import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.TagRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards/{board}")
public class TagController {
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final BoardRepository boardRepository;
    private SimpMessagingTemplate msgs;

    public TagController(TagRepository tagRepository, TaskRepository taskRepository, TaskListRepository taskListRepository, BoardRepository boardRepository, SimpMessagingTemplate msgs) {
        this.tagRepository = tagRepository;
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
        this.boardRepository = boardRepository;
        this.msgs = msgs;
    }
    @GetMapping(path = {"/all-tags"})
    public ResponseEntity<?> showAll() {
        return ResponseEntity.ok(tagRepository.findAll());
    }

    @GetMapping(path = "/{tag}/get")
    public ResponseEntity<?> getById(@PathVariable("tag") long tagId, @PathVariable("board") long boardId) {
        //TODO : do the check if parents are good
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!tagRepository.existsById(tagId)) return ResponseEntity.notFound().build();

        Tag tag = tagRepository.findById(tagId).get();
        if (tag.getBoard().getId() != boardId) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(tag);
    }

    @PostMapping(path = "/add-tag")
    public ResponseEntity<?> add(@RequestBody Map<String, String> body, @PathVariable("board") long boardId) throws RuntimeException {
        if(body.get("name") == null) return ResponseEntity.badRequest().build();
        Tag tag = new Tag(body.get("name"));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));
        board.add(tag);
        tag.setBoard(board);

        Tag tag1 = tagRepository.save(tag);

        Board board1 = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, board1);

        return ResponseEntity.ok(tag1);
    }

    @PostMapping("{tag}/edit-tag")
    public ResponseEntity<?> edit(@RequestBody Map<String, String> body, @PathVariable("tag") long tagId, @PathVariable("board") long boardId) throws JsonProcessingException {
        // check if board, list and task exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!tagRepository.existsById(tagId)) return ResponseEntity.notFound().build();
        if(body.get("name") == null) return ResponseEntity.badRequest().build();

        // check if they are in relation
        Tag tag = tagRepository.findById(tagId).get();
        if (tag.getBoard().getId() != boardId) return ResponseEntity.badRequest().build();

        tag.setName(body.get("name"));
        Tag tag1 = tagRepository.save(tag);

        Board board1 = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, board1);

        return ResponseEntity.ok(tag1);
    }

    @DeleteMapping("/{tag}/delete-tag")
    public ResponseEntity<Object> deleteTask(@PathVariable("tag") long tagId, @PathVariable("board") long boardId) {

        // check if the task exists
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!tagRepository.existsById(tagId)) return ResponseEntity.notFound().build();

        // check if they are in relation
        Tag tag = tagRepository.findById(tagId).get();
        if (tag.getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        Board board = boardRepository.findById(boardId).get();


        if(tag.getTaskList() != null && tag.getTaskList().size() > 0){
            List<Task> tasks = tag.getTaskList();
            for (Task task : tasks){
                task.remove(tag);
            }
            tag.setTaskList(null);
        }

        board.remove(tag);
        tag.setBoard(null);

        tagRepository.deleteById(tagId);
        boardRepository.save(board);

        Board board1 = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, board1);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "{list}/{card}/{tag}")
    public ResponseEntity<?> add(@PathVariable("tag") long tagId, @PathVariable("card") long taskId, @PathVariable("list") long listId,@PathVariable("board") long boardId) throws RuntimeException {
        // check if board, list and task exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();

        // check if they are in relation
        Task task = taskRepository.findById(taskId).get();
        if (task.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (task.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        Tag tag = tagRepository.findById(tagId).get();
        if(task.getTagList().contains(tag)) return ResponseEntity.badRequest().build();
        if (tag.getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        Board board = boardRepository.findById(boardId).get();

        task.add(tag);
        tag.add(task);
        Task task1 = taskRepository.save(task);
        Tag tag1 = tagRepository.save(tag);

        Board board1 = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, board1);

        return ResponseEntity.ok(task1);
    }

    @PostMapping(path = "{list}/{card}/{tag}/remove")
    public ResponseEntity<?> remove(@PathVariable("tag") long tagId, @PathVariable("card") long taskId, @PathVariable("list") long listId,@PathVariable("board") long boardId) throws RuntimeException {
        // check if board, list and task exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();

        // check if they are in relation
        Task task = taskRepository.findById(taskId).get();
        if (task.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (task.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        Tag tag = tagRepository.findById(tagId).get();
        if(!task.getTagList().contains(tag) || !tag.getTaskList().contains(task)) return ResponseEntity.badRequest().build();
        if (tag.getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        Board board = boardRepository.findById(boardId).get();

        task.remove(tag);
        tag.remove(task);
        Task task1 = taskRepository.save(task);
        Tag tag1 = tagRepository.save(tag);

        Board board1 = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, board1);

        return ResponseEntity.ok(task1);
    }
}

package server.api;

import commons.Board;
import commons.NestedTask;
import commons.Task;
import commons.TaskList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.NestedTaskRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/boards/{board}/{list}/{task}")
public class NestedTaskController {
    private final TaskListRepository taskListRepository;
    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;
    private final NestedTaskRepository nestedTaskRepository;
    private SimpMessagingTemplate msgs;

    public NestedTaskController(TaskListRepository taskListRepository, TaskRepository taskRepository, BoardRepository boardRepository, NestedTaskRepository nestedTaskRepository, SimpMessagingTemplate msgs) {
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
        this.boardRepository = boardRepository;
        this.nestedTaskRepository = nestedTaskRepository;
        this.msgs = msgs;
    }

    @GetMapping(path = {"/", ""})
    public ResponseEntity<?> showAll() {
        return ResponseEntity.ok(nestedTaskRepository.findAll());
    }

    @GetMapping(path = "/{nested}/get")
    public ResponseEntity<?> getById(@PathVariable("task") long taskId, @PathVariable("board") long boardId, @PathVariable("list") long listId, @PathVariable("nested") long nestedId) {
        // check if board, list and task exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();
        if (!nestedTaskRepository.existsById(nestedId)) return ResponseEntity.notFound().build();

        // check if they are in relation
        NestedTask n = nestedTaskRepository.findById(nestedId).get();
        Task t = taskRepository.findById(taskId).get();
        if (t.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (t.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        if (!t.getNestedTasks().contains(n)) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(nestedTaskRepository.findById(nestedId).get());
    }

    @PostMapping(path = "/nestedTask")
    public ResponseEntity<?> add(@RequestBody Map<String, String> body, @PathVariable("task") long taskId, @PathVariable("list") long listId, @PathVariable("board") long boardId) throws RuntimeException {
        if (body.get("name") == null) return ResponseEntity.badRequest().build();
        // check if board, list and task exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();

        Task t = taskRepository.findById(taskId).get();
        if (t.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (t.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();

        NestedTask nested = new NestedTask(body.get("name"), false);
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        task.add(nested);
        nested.setTask(task);

        nestedTaskRepository.save(nested);

        Task retTask = taskRepository.findById(taskId).get();
        Board retBoard = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, retBoard);
        msgs.convertAndSend("/topic/"+boardId+"/"+listId+"/"+taskId, retTask);
        return ResponseEntity.ok(nested);
    }

    @PostMapping("{nested}/edit-nested")
    public ResponseEntity<?> edit(@RequestBody Map<String, String> body, @PathVariable("nested") long nestedId, @PathVariable("task") long taskId, @PathVariable("list") long listId, @PathVariable("board") long boardId) throws RuntimeException {
        if (body.get("name") == null || body.get("isCompleted") == null) return ResponseEntity.badRequest().build();
        // check if board, list and task exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();
        if (!nestedTaskRepository.existsById(nestedId)) return ResponseEntity.notFound().build();

        // check if they are in relation
        NestedTask n = nestedTaskRepository.findById(nestedId).get();
        Task t = taskRepository.findById(taskId).get();
        if (t.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (t.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        if (!t.getNestedTasks().contains(n)) return ResponseEntity.badRequest().build();

        n.setName(body.get("name"));
        n.setComplete(body.get("isCompleted").equals("true"));

        NestedTask saved = nestedTaskRepository.save(n);

        Task retTask = taskRepository.findById(taskId).get();
        Board retBoard = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, retBoard);
        msgs.convertAndSend("/topic/"+boardId+"/"+listId+"/"+taskId, retTask);

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{nested}")
    public ResponseEntity<?> deleteNested(@PathVariable("nested") long nestedId, @PathVariable("task") long taskId, @PathVariable("list") long listId, @PathVariable("board") long boardId){
        // check if board, list and task exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();
        if (!nestedTaskRepository.existsById(nestedId)) return ResponseEntity.notFound().build();

        // check if they are in relation
        NestedTask n = nestedTaskRepository.findById(nestedId).get();
        Task t = taskRepository.findById(taskId).get();
        if (t.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (t.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        if (!t.getNestedTasks().contains(n)) return ResponseEntity.badRequest().build();

        // update task and save
        t.remove(n);
        n.setTask(null);

        nestedTaskRepository.deleteById(nestedId);
        taskRepository.save(t);

        Task retTask = taskRepository.findById(taskId).get();
        Board retBoard = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, retBoard);
        msgs.convertAndSend("/topic/"+boardId+"/"+listId+"/"+taskId, retTask);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{nested}/move")
    public ResponseEntity<?> move(@PathVariable("nested") long nestedId, @PathVariable("task") long taskId, @PathVariable("list") long listId,
                                  @PathVariable("board") long boardId, @RequestBody Map<String, Long> body){
        if(!body.containsKey("index")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no");
        // check if board, list and task exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();
        if (!nestedTaskRepository.existsById(nestedId)) return ResponseEntity.notFound().build();

        // check if they are in relation
        NestedTask n = nestedTaskRepository.findById(nestedId).get();
        Task t = taskRepository.findById(taskId).get();
        if (t.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (t.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        if (!t.getNestedTasks().contains(n)) return ResponseEntity.badRequest().build();

        NestedTask nestedTask = nestedTaskRepository.findById(nestedId).get();
        Task task = taskRepository.findById(taskId).get();
        int index = body.get("index").intValue();
        TaskList taskList = taskListRepository.findById(listId).get();
        Board board = boardRepository.findById(boardId).get();

        task.remove(nestedTask);
        nestedTask.setTask(null);
        if(index > task.getNestedTasks().size()){
            index = task.getNestedTasks().size();
        }
        task.getNestedTasks().add(index, nestedTask);
        nestedTask.setTask(task);

        taskRepository.save(task);

        Task retTask = taskRepository.findById(taskId).get();
        Board retBoard = boardRepository.findById(boardId).get();
        // send update to client using WebSocket
        msgs.convertAndSend("/topic/" + boardId, retBoard);
        msgs.convertAndSend("/topic/"+boardId+"/"+listId+"/"+taskId, retTask);

        return ResponseEntity.ok(task);
    }
}

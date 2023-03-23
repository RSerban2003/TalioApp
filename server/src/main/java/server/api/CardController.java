package server.api;

import commons.Board;
import commons.Task;
import commons.TaskList;

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

    public CardController(BoardRepository boardRepository, TaskListRepository taskListRepository, TaskRepository taskRepository) {
        this.boardRepository = boardRepository;
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping(path = {"/", ""})
    public ResponseEntity<Object> showAll() {
        return ResponseEntity.ok(taskRepository.findAll());
    }


    @PostMapping(path = "/card")
    public ResponseEntity<?> add(@RequestBody Map<String, String> body, @PathVariable("list") long listId,                                    @PathVariable("board") long boardId) throws RuntimeException {
        if(body.get("name") == null || body.get("description") == null) return ResponseEntity.badRequest().build();
        Task task = new Task(body.get("name"), body.get("description"));
        TaskList taskList = taskListRepository.findById(listId).orElseThrow(() -> new RuntimeException("Task list not found"));
        taskList.add(task);
        task.setTaskList(taskList);
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    @PostMapping("{task}/edit-card")
    public ResponseEntity<Task> edit(@RequestParam("name") String name, @RequestParam("description") String description, @PathVariable("task") long taskId, @PathVariable("board") long boardId, @PathVariable("list") long listId) {
        // check if board, list and task exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();

        // check if they are in relation
        Task t = taskRepository.getById(taskId);
        if (t.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (t.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();

        t.setName(name);
        t.setDescription(description);

        Task ta = taskRepository.save(t);

        return ResponseEntity.ok(ta);
    }

    @GetMapping("/card")
    public ResponseEntity<Object> get() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Object> deleteTask(@PathVariable("cardId") long cardId, @PathVariable("list") long listId) {

        // check if the task exists
        if (!taskRepository.existsById(cardId)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        Task task = taskRepository.findById(cardId).get();

        // check if the listId is valid
        if (!taskListRepository.existsById(listId)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tasklist not found");
        TaskList taskList = taskListRepository.getById(listId);
        if (task.getTaskList().getId() != listId) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not part of tasklist");

        // update the taskList and save
        taskList.remove(task);
        task.setTaskList(null);
        taskListRepository.save(taskList);

        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{card}/move")
    public ResponseEntity<?> move(@PathVariable("card") long cardId, @PathVariable("list") long listId,
                                  @PathVariable("board") long boardId, @RequestBody Map<String, Object> body) {
        if(!body.containsKey("listTo") || !body.containsKey("index")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no");
        Optional<Task> task = taskRepository.findById(cardId);
        Optional<TaskList> taskListFrom = taskListRepository.findById(listId);
        long listToId = Long.valueOf((int) body.get("listTo"));
        int index = (int) body.get("index");
        Optional<TaskList> taskListTo = taskListRepository.findById(listToId);
        Optional<Board> board = boardRepository.findById(boardId);

        if (task.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        if (taskListFrom.isEmpty() || taskListTo.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tasklist not found");
        if (task.get().getTaskList().getId() != listId) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not part of tasklist");

        if(board.isEmpty()) return ResponseEntity.badRequest().build();
        if(taskListFrom.get().getBoard().getId() != boardId || taskListTo.get().getBoard().getId() != boardId) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("List not part of board");
        if(listId == listToId && taskListFrom.get().getTaskList().indexOf(task.get()) < index) {
            taskListFrom.get().getTaskList().remove(task.get());
            taskListTo.get().getTaskList().add(index - 1, task.get());
        } else {
            taskListTo.get().getTaskList().add(index, task.get());
            task.get().setTaskList(taskListTo.get());
        }
        return ResponseEntity.ok(taskListTo);
    }
}

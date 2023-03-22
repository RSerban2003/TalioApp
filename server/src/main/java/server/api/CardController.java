package server.api;

import commons.Task;
import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.BoardRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/boards/{board}/{list}")
public class CardController {

    private final TaskListRepository taskListRepository;
    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;

    public CardController(BoardRepository boardRepository, TaskListRepository taskListRepository, TaskRepository taskRepository) {
        this.boardRepository = boardRepository;
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    @PostMapping(path = "/add-card")
    public ResponseEntity<Task> add(@RequestBody Task task, @PathVariable("list") long listId,
                                    @PathVariable("board") long boardId) throws RuntimeException {

        if (task.getName() == null || task.getDescription() == null) {
            return ResponseEntity.badRequest().build();
        }

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

    @PostMapping(path = {"{task}/drag_and_drop"})
    public ResponseEntity<?> drag_drop(@PathVariable("board") long boardId, @PathVariable("list") long listId, @PathVariable("task") long taskId,
        @RequestParam("new_list") long newListId, @RequestParam("new_index") long newIndex) {
        // check if board, list and task exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();

        // check if they are in relation
        Task t = taskRepository.getById(taskId);
        if (t.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (t.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();

        TaskList tl = taskListRepository.getById(newListId);
        t.setTaskList(tl);
        tl.getTaskList().add((int)newIndex,t);

        taskRepository.save(t);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/card")
    public ResponseEntity<Object> get() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Object> deleteTask(@PathVariable("cardId") long cardId, @PathVariable("list") long listId) {

        // check if the task exists
        if (!taskRepository.existsById(cardId)) return ResponseEntity.badRequest().build();
        Task task = taskRepository.getById(cardId);

        // check if the listId is valid
        if (!taskListRepository.existsById(listId)) return ResponseEntity.badRequest().build();
        TaskList taskList = taskListRepository.getById(listId);

        // update the taskList and save
        taskList.remove(task);
        taskListRepository.save(taskList);

        return ResponseEntity.ok().build();
    }
}

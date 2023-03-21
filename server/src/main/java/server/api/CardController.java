package server.api;

import commons.Task;
import commons.TaskList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/boards/{board}/{list}")
public class CardController {

    private TaskListRepository taskListRepository;
    private TaskRepository taskRepository;

    public CardController(TaskListRepository taskListRepository, TaskRepository taskRepository){
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping(path = {"/", ""})
    public ResponseEntity<Object> showAll() {
        return ResponseEntity.ok(taskRepository.findAll());
    }


    @PostMapping(path = "/card")
    public ResponseEntity<?> add(@RequestBody Map<String, String> body, @PathVariable("list") long listId,
                                    @PathVariable("board") long boardId) throws RuntimeException {
        if(body.get("name") == null || body.get("description") == null) return ResponseEntity.badRequest().build();
        Task task = new Task(body.get("name"), body.get("description"));
        TaskList taskList = taskListRepository.findById(listId).orElseThrow(() -> new RuntimeException("Task list not found"));
        taskList.add(task);
        task.setTaskList(taskList);
        taskRepository.save(task);
        return ResponseEntity.ok(task);
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

    /**
     * Checks if a sting is null or empty
     * @param s     The Sting to be checked
     * @return      Boolean
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

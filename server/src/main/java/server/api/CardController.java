package server.api;

import commons.Task;
import commons.TaskList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.List;

@RestController
@RequestMapping("/api/boards/list/task")
public class CardController {

    private TaskListRepository taskListRepository;
    private TaskRepository taskRepository;

    public CardController(TaskListRepository taskListRepository, TaskRepository taskRepository){
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping(path = {"/test"})
    public String oi() {
        return "oi";
    }

    @PostMapping(path = "/add-card")
    public ResponseEntity<Task> add(@RequestBody Task task){        // , @RequestBody TaskList taskList

        if (task.getName() == null || task.getDescription() == null) {
            return ResponseEntity.badRequest().build();
        }

        //taskList.add(task);
        taskRepository.save(task);
        // TODO: tasklist adding

        return ResponseEntity.ok(task);
    }

    @GetMapping("/get-all-cards")
    public ResponseEntity<List<Task>> get() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @DeleteMapping("/delete-card")
    public ResponseEntity<Object> deleteTask(@RequestBody Task task) {
        if (task == null || isNullOrEmpty(task.getDescription()) || isNullOrEmpty(task.getName()) || task.getId() == null) return ResponseEntity.badRequest().build();
        if (!taskRepository.exists(Example.of(task))) return ResponseEntity.badRequest().build();
        //return ResponseEntity.ok(taskRepository.getById(task.getId()));
        if (!task.equals(taskRepository.findAll(Example.of(task)).get(0))) return ResponseEntity.badRequest().build();
        taskRepository.delete(task);
        // TODO: delete the entry in the taskList
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

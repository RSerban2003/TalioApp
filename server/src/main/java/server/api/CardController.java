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
@RequestMapping("/api/{boards}/{list}")
public class CardController {

    private TaskListRepository taskListRepository;
    private TaskRepository taskRepository;

    public CardController(TaskListRepository taskListRepository, TaskRepository taskRepository){
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
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

    /**
     * Checks if a sting is null or empty
     * @param s     The Sting to be checked
     * @return      Boolean
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

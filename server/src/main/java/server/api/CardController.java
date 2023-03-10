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

    @DeleteMapping("/delete-card")
    public ResponseEntity<Object> deleteTask(@RequestBody Task task, @PathVariable("list") long listId) {
        // check if the task is valid
        if (task == null || isNullOrEmpty(task.getDescription()) || isNullOrEmpty(task.getName()) || task.getId() == null) return ResponseEntity.ok(1);//ResponseEntity.badRequest().build();
        // check if the task exists
        if (!taskRepository.exists(Example.of(task))) return ResponseEntity.ok(2);//ResponseEntity.badRequest().build();
        // check if the tasks are equal
        if (!task.equals(taskRepository.findAll(Example.of(task)).get(0))) return ResponseEntity.ok(3);//ResponseEntity.badRequest().build();

        // check if the listId is valid
        if (!taskListRepository.existsById(listId)) return ResponseEntity.ok(4); //ResponseEntity.badRequest().build();
        TaskList taskList = taskListRepository.getById(listId);
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

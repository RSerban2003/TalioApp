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

import java.util.Optional;

@RestController
@RequestMapping("/boards/{board}/{list}")
public class CardController {

    private TaskListRepository taskListRepository;
    private TaskRepository taskRepository;

    public CardController(TaskListRepository taskListRepository, TaskRepository taskRepository){
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public ResponseEntity<Object> showAll() {
        return ResponseEntity.ok(taskRepository.findAll());
    }


    @PostMapping(path = "/add-card")
    public ResponseEntity<Task> add(@RequestBody Task task, @PathVariable("list") long listId,
                                    @PathVariable("board") long boardId) throws RuntimeException {

        if (task.getName() == null || task.getDescription() == null) {
            return ResponseEntity.badRequest().build();
        }

        TaskList taskList = taskListRepository.findById(listId).orElseThrow(() -> new RuntimeException("Task list not found"));
        taskList.add(task);
        taskListRepository.save(taskList);
        return ResponseEntity.ok(task);
    }
    @PostMapping("/edit-card")
    public ResponseEntity<Object> edit(@RequestBody Task task, @PathVariable("list") long listId) {
        var t = taskListRepository.findById(listId);
        if (!t.isPresent()) return ResponseEntity.badRequest().build();
        TaskList taskList = t.get();

        for (Task tasky : taskList.getTaskList()) {
            if (task.getId() == tasky.getId()) {
                taskList.remove(tasky);
                taskList.add(task);
                break;
            }
        }

        taskListRepository.save(taskList);

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

    /**
     * Checks if a sting is null or empty
     * @param s     The Sting to be checked
     * @return      Boolean
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

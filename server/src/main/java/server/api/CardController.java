package server.api;

import commons.Task;
import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TaskListRepository;
import server.database.TaskRepository;

@RestController
@RequestMapping("/")
public class CardController {

    private TaskListRepository taskListRepository;
    private TaskRepository taskRepository;

    public CardController(TaskListRepository taskListRepository, TaskRepository taskRepository){
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    @PostMapping(path = "/boards/list/add-card")
    public ResponseEntity<Task> add(@RequestBody Task task, @RequestBody TaskList taskList){

        if (task.getName() == null || task.getDescription() == null) {
            return ResponseEntity.badRequest().build();
        }

        taskList.add(task);

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
}

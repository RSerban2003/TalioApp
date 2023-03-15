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


        return ResponseEntity.ok().build();
    }

}

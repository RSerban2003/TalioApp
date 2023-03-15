package server.api;

import commons.Task;
import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class CardController {

    private TaskListRepository taskListRepository;
    private TaskRepository taskRepository;

    public CardController(TaskListRepository taskListRepository, TaskRepository taskRepository){
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    @PostMapping(path = "/boards/{board-id}/{list-id}/add-card")
    public ResponseEntity<Task> add(@RequestBody Task task, @PathVariable("list-id") long listId,
                                    @PathVariable("board-id") long boardId) throws RuntimeException {

        if (task.getName() == null || task.getDescription() == null) {
            return ResponseEntity.badRequest().build();
        }

        TaskList taskList = taskListRepository.findById(listId).orElseThrow(() -> new RuntimeException("Task list not found"));
        taskList.add(task);
        taskListRepository.save(taskList);
        return ResponseEntity.ok(task);
    }
}

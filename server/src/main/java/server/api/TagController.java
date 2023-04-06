package server.api;

import commons.*;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.*;

import java.util.Map;

@RestController
@RequestMapping("/api/boards/{board}")
public class TagController {
    private TaskListRepository taskListRepository;
    private TaskRepository taskRepository;
    private BoardRepository boardRepository;
    private TagRepository tagRepository;
    private SimpMessagingTemplate msgs;

    public TagController(BoardRepository boardRepository,TaskListRepository taskListRepository, TaskRepository taskRepository, TagRepository tagRepository, SimpMessagingTemplate msgs) {

        this.boardRepository = boardRepository;
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
        this.tagRepository = tagRepository;
        this.msgs = msgs;
    }

    @GetMapping(path = "/get-tags")
    public ResponseEntity<?> showAll() {

        return ResponseEntity.ok(tagRepository.findAll());
    }

    @GetMapping(path = "/{tag}/get-tag")
    public ResponseEntity<?> getById(@PathVariable("board") long boardId, @PathVariable("tag") long tagId) {

        // check if the board and tag exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!tagRepository.existsById(tagId)) return ResponseEntity.notFound().build();

        // check if the board and tag are in relation
        Tag t = tagRepository.findById(tagId).get();
        Board b = boardRepository.findById(boardId).get();
        if (t.getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        if (!b.getListOfTags().contains(t)) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(tagRepository.findById(tagId).get());
    }

    @PostMapping(path = "/add-tag")
    public ResponseEntity<?> createTag(@RequestBody Map<String, String> body, @PathVariable("board") long boardId) throws RuntimeException {

        //check if the name is not empty
        if (body.get("name") == null) return ResponseEntity.badRequest().build();

        // check if the board exists
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();

        Tag tag = new Tag(body.get("name"));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));

        board.addTag(tag);
        tag.setBoard(board);

        tagRepository.save(tag);
        boardRepository.save(board);

        Board retBoard = boardRepository.findById(boardId).get();

        // send update to clients
        msgs.convertAndSend("/topic/" + boardId, retBoard);

        return ResponseEntity.ok(tag);
    }

    @PostMapping("/{tag}/edit-tag")
    public ResponseEntity<?> editTag(@RequestBody Map<String, String> body, @PathVariable("board") long boardId, @PathVariable("tag") long tagId) throws RuntimeException {

        // check if the new name is not empty
        if (body.get("name") == null) return ResponseEntity.badRequest().build();

        // check if the board and tag exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!tagRepository.existsById(tagId)) return ResponseEntity.notFound().build();

        // check if the board and tag are in relation
        Tag tag = tagRepository.findById(tagId).get();
        Board board = boardRepository.findById(boardId).get();
        if (tag.getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        if (!board.getListOfTags().contains(tag)) return ResponseEntity.badRequest().build();

        tag.setName(body.get("name"));

        tagRepository.save(tag);
        boardRepository.save(board);

        // send update to clients
        Board retBoard = boardRepository.findById(boardId).get();
        msgs.convertAndSend("/topic/" + boardId, retBoard);

        for (Task retTask : tag.getListOfTasks()) {
            retTask.getListOfTags().get(retTask.getListOfTags().indexOf(tag)).setName(body.get("name"));
            taskRepository.save(retTask);
            msgs.convertAndSend("/topic/" + boardId + "/" + retTask.getTaskList().getId() + "/" + retTask.getId(), retTask);
        }

        return ResponseEntity.ok(tag);
    }

    @DeleteMapping("/{tag}/delete-tag")
    public ResponseEntity<?> deleteTag(@PathVariable("board") long boardId, @PathVariable("tag") long tagId){

        // check if the board and tag exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!tagRepository.existsById(tagId)) return ResponseEntity.notFound().build();

        // check if the board and tag are in relation
        Tag t = tagRepository.findById(tagId).get();
        Board b = boardRepository.findById(boardId).get();
        if (t.getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        if (!b.getListOfTags().contains(t)) return ResponseEntity.badRequest().build();

        // update board and tag repository
        b.removeTag(t);
        t.setBoard(null);

        // remove the tag from all the tasks, save the tasks and send update to clients
        for(Task retTask : t.getListOfTasks()) {
            retTask.getListOfTags().remove(t);
            taskRepository.save(retTask);
            msgs.convertAndSend("/topic/" + boardId + "/" + retTask.getTaskList().getId() + "/" + retTask.getId(), retTask);
        }

        // delete the tag from the repository and save the board
        tagRepository.delete(t);
        boardRepository.save(b);

        Board retBoard = boardRepository.findById(boardId).get();

        // send update to clients
        msgs.convertAndSend("/topic/" + boardId, retBoard);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{list}/{task}/{tag}")
    public ResponseEntity<?> addTag(@PathVariable("board") long boardId,  @PathVariable("list") long listId,
                                    @PathVariable("task") long taskId, @PathVariable("tag") long tagId) throws RuntimeException {

        // check if the board, task list, task and tag exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();
        if (!tagRepository.existsById(tagId)) return ResponseEntity.notFound().build();

        // check if the board, task list and task are in relation
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        if (task.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (task.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();

        // check if the board and tag are in relation
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag not found"));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));
        if(tag.getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        if(!board.getListOfTags().contains(tag)) return  ResponseEntity.badRequest().build();

        // assign the tag to the task and save the changes
        task.addTag(tag);
        tag.addTask(task);

        tagRepository.save(tag);
        taskRepository.save(task);
        boardRepository.save(board);

        Task retTask = taskRepository.findById(taskId).get();

        // send update to clients
        msgs.convertAndSend("/topic/"+boardId+"/"+listId+"/"+taskId, retTask);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{list}/{task}/{tag}/remove")
    public ResponseEntity<?> removeTag(@PathVariable("board") long boardId, @PathVariable("list") long listId,
                                       @PathVariable("task") long taskId, @PathVariable("tag") long tagId){

        // check if the board, task list, task and tag exist
        if (!boardRepository.existsById(boardId)) return ResponseEntity.notFound().build();
        if (!taskListRepository.existsById(listId)) return ResponseEntity.notFound().build();
        if (!taskRepository.existsById(taskId)) return ResponseEntity.notFound().build();
        if (!tagRepository.existsById(tagId)) return  ResponseEntity.notFound().build();


        // check if the board, task list and task are in relation
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        if (task.getTaskList().getId() != listId) return ResponseEntity.badRequest().build();
        if (task.getTaskList().getBoard().getId() != boardId) return ResponseEntity.badRequest().build();

        // check if the board and tag are in relation
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag not found"));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));
        if(tag.getBoard().getId() != boardId) return ResponseEntity.badRequest().build();
        if(!board.getListOfTags().contains(tag)) return  ResponseEntity.badRequest().build();

        // remove the tag from the task and save the changes
        task.removeTag(tag);
        tag.removeTask(task);

        taskRepository.save(task);
        tagRepository.save(tag);

        Task retTask = taskRepository.findById(taskId).get();

        // send update to clients
        msgs.convertAndSend("/topic/"+ boardId + "/" + listId + "/" + taskId, retTask);

        return ResponseEntity.ok(task);
    }
}
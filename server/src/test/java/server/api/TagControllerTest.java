package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.database.BoardRepository;
import server.database.TagRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TagControllerTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskListRepository taskListRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private TagController tagController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowAll() {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("Tag 1"));
        tags.add(new Tag("Tag 2"));

        when(tagRepository.findAll()).thenReturn(tags);

        ResponseEntity<?> response = tagController.showAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tags, response.getBody());
    }

    @Test
    void testAddTagToBoard() {
        Board board = new Board(1L, "Board1");

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        Map<String, String> body = new HashMap<>();
        body.put("name", "Tag1");

        ResponseEntity<?> response = tagController.add(body, 1L);

        Tag expectedTag = new Tag("Tag1");
        expectedTag.setBoard(board);
        Tag actualTag = (Tag) response.getBody();

        assertNotNull(actualTag);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTag.getName(), actualTag.getName());
        assertEquals(board, actualTag.getBoard());
    }

    @Test
    void testEditTag() throws JsonProcessingException {
        Board board = new Board(1L, "Board1");
        Tag tag = new Tag(2L, "Tag1", board);
        tag.setBoard(board);
        board.add(tag);

        Map<String, String> body = new HashMap<>();
        body.put("name", "EditedTag");

        when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(boardRepository.existsById(board.getId())).thenReturn(true);
        when(tagRepository.existsById(tag.getId())).thenReturn(true);
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> {
            tag.setName(body.get("name"));
            return Optional.of(tag).get();
        });

        ResponseEntity<?> response = null;

        response = tagController.edit(body, tag.getId(), board.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("EditedTag", ((Tag) response.getBody()).getName());
        assertEquals("EditedTag", tag.getName());
    }

    @Test
    void testDeleteTag() {
        Board board = new Board(1L, "Board1");
        Tag tag = new Tag(2L, "Tag1", board);

        when(tagRepository.findById(2L)).thenReturn(Optional.of(tag));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(boardRepository.existsById(board.getId())).thenReturn(true);
        when(tagRepository.existsById(tag.getId())).thenReturn(true);

        ResponseEntity<?> response = tagController.deleteTag(2L, 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddTagToTask() {
        Board board = new Board(1L, "Board1");
        TaskList taskList = new TaskList(2L, "TaskList1");
        Task task = new Task(3L,"Task1","");
        Tag tag = new Tag(4L,"Tag1", board);

        board.add(taskList);
        taskList.add(task);
        board.add(tag);
        tag.setBoard(board);

        when(tagRepository.findById(4L)).thenReturn(Optional.of(tag));
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(taskListRepository.findById(2L)).thenReturn(Optional.of(taskList));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        when(boardRepository.existsById(board.getId())).thenReturn(true);
        when(tagRepository.existsById(tag.getId())).thenReturn(true);
        when(taskListRepository.existsById(taskList.getId())).thenReturn(true);
        when(taskRepository.existsById(task.getId())).thenReturn(true);

        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> {
            tag.add(task);
            task.add(tag);
            return Optional.of(tag).get();
        });
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            task.remove(tag);
            tag.remove(task);
            return Optional.of(task).get();
        });

        ResponseEntity<?> response = tagController.add(tag.getId(), task.getId(), taskList.getId(), board.getId());
        Tag returnedTag = (Tag) response.getBody();

        assertTrue(returnedTag.getTaskList().contains(task));
    }

    @Test
    void testRemoveTagFromTask() {
        Board board = new Board(1L, "Board1");
        TaskList taskList = new TaskList(2L, "TaskList1");
        Task task = new Task(3L, "Task1", "");
        Tag tag = new Tag(4L, "Tag1", board);

        board.add(taskList);
        taskList.add(task);
        board.add(tag);
        tag.setBoard(board);
        task.add(tag);
        tag.add(task);

        when(tagRepository.findById(4L)).thenReturn(Optional.of(tag));
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(taskListRepository.findById(2L)).thenReturn(Optional.of(taskList));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        when(boardRepository.existsById(board.getId())).thenReturn(true);
        when(tagRepository.existsById(tag.getId())).thenReturn(true);
        when(taskListRepository.existsById(taskList.getId())).thenReturn(true);
        when(taskRepository.existsById(task.getId())).thenReturn(true);

        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> {
            tag.remove(task);
            task.remove(tag);
            return Optional.of(tag).get();
        });
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            task.remove(tag);
            tag.remove(task);
            return Optional.of(task).get();
        });

        ResponseEntity<?> response = tagController.remove(4L, 3L, 2L, 1L);

        Task actualTask = (Task) response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(actualTask.getTagList().contains(tag));
    }
}
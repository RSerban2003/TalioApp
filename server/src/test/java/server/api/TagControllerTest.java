package server.api;

import commons.Board;
import commons.Tag;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.BoardRepository;
import server.database.TagRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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

    @InjectMocks
    private TagController tagController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowAll() {
        List<Tag> tags = Arrays.asList(new Tag("tag1"), new Tag("tag2"));
        when(tagRepository.findAll()).thenReturn(tags);

        ResponseEntity<?> response = tagController.showAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tags, response.getBody());
    }

    @Test
    void testGetById() {
        Board board = new Board();
        Tag tag = new Tag("tag1");
        board.addTag(tag);
        tag.setBoard(board);

        when(boardRepository.existsById(1L)).thenReturn(true);
        when(tagRepository.existsById(2L)).thenReturn(true);
        when(tagRepository.findById(2L)).thenReturn(Optional.of(tag));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        ResponseEntity<?> response = tagController.getById(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tag, response.getBody());
    }

    @Test
    void testCreateTag() {
        Board board = new Board();
        Tag tag = new Tag("tag1");
        board.addTag(tag);
        tag.setBoard(board);

        Map<String, String> body = new HashMap<>();
        body.put("name", "newTag");

        when(boardRepository.existsById(1L)).thenReturn(true);
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        ResponseEntity<?> response = tagController.createTag(body, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("newTag", ((Tag) response.getBody()).getName());
        assertEquals(board, ((Tag) response.getBody()).getBoard());
    }

    @Test
    void testEditTag() {
        Board board = new Board();
        Tag tag = new Tag("tag1");
        board.addTag(tag);
        tag.setBoard(board);

        Map<String, String> body = new HashMap<>();
        body.put("name", "editedTag");

        when(boardRepository.existsById(1L)).thenReturn(true);
        when(tagRepository.existsById(2L)).thenReturn(true);
        when(tagRepository.findById(2L)).thenReturn(Optional.of(tag));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        ResponseEntity<?> response = tagController.editTag(body, 1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("editedTag", ((Tag) response.getBody()).getName());
    }

    @Test
    void testDeleteTag() {
        Board board = new Board();
        Tag tag = new Tag("tag1");
        board.addTag(tag);
        tag.setBoard(board);

        when(boardRepository.existsById(1L)).thenReturn(true);
        when(tagRepository.existsById(2L)).thenReturn(true);
        when(tagRepository.findById(2L)).thenReturn(Optional.of(tag));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        ResponseEntity<?> response = tagController.deleteTag(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(board.getListOfTags().contains(tag));
        assertNull(tag.getBoard());
    }

    @Test
    void testAddTag() {
        Board board = new Board();
        TaskList taskList = new TaskList();
        Task task = new Task();
        Tag tag = new Tag("tag1");
        board.add(taskList);
        taskList.add(task);
        board.addTag(tag);
        tag.setBoard(board);

        when(boardRepository.existsById(1L)).thenReturn(true);
        when(taskListRepository.existsById(2L)).thenReturn(true);
        when(taskRepository.existsById(3L)).thenReturn(true);
        when(tagRepository.existsById(4L)).thenReturn(true);
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(tagRepository.findById(4L)).thenReturn(Optional.of(tag));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(taskListRepository.findById(2L)).thenReturn(Optional.of(taskList));

        ResponseEntity<?> response = tagController.addTag(1L, 2L, 3L, 4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(task.getListOfTags().contains(tag));
    }

    @Test
    void testRemoveTag() {
        Board board = new Board();
        TaskList taskList = new TaskList();
        Task task = new Task();
        Tag tag = new Tag("tag1");
        board.add(taskList);
        taskList.add(task);
        board.addTag(tag);
        tag.setBoard(board);
        task.addTag(tag);

        when(boardRepository.existsById(1L)).thenReturn(true);
        when(taskListRepository.existsById(2L)).thenReturn(true);
        when(taskRepository.existsById(3L)).thenReturn(true);
        when(tagRepository.existsById(4L)).thenReturn(true);
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(tagRepository.findById(4L)).thenReturn(Optional.of(tag));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        ResponseEntity<?> response = tagController.removeTag(1L, 2L, 3L, 4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(task.getListOfTags().contains(tag));
        assertFalse(board.getListOfTags().contains(tag));
        assertNull(tag.getBoard());
    }
}

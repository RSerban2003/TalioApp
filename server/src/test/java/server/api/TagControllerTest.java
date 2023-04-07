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
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("Tag 1"));
        tags.add(new Tag("Tag 2"));

        when(tagRepository.findAll()).thenReturn(tags);

        List<Tag> listOfTags = tagRepository.findAll();

        assertNotNull(listOfTags);

        assertEquals(tags.size(), listOfTags.size());
        assertEquals(tags.get(0), listOfTags.get(0));
        assertEquals(tags.get(1), listOfTags.get(1));

    }

    @Test
    void testCreateTag() {
        Board board = new Board(1L,"Board1");
        Tag tag = new Tag(1L,"Tag1");
        board.addTag(tag);
        tag.setBoard(board);

        Map<String, String> body = new HashMap<>();
        body.put("name", "Tag2");

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(tagRepository.save(any(Tag.class))).thenAnswer(i -> i.getArguments()[0]);


        ResponseEntity<?> response = tagController.createTag(body, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tag2", ((Tag) response.getBody()).getName());
        assertEquals(board, ((Tag) response.getBody()).getBoard());
    }

    @Test
    void testEditTag() {
        Board board = new Board(1L,"Board1");
        Tag tag = new Tag(2L,"tag1");
        board.addTag(tag);
        tag.setBoard(board);

        Map<String, String> body = new HashMap<>();
        body.put("name", "editedTag");

        when(tagRepository.findById(2L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(any(Tag.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<?> response = tagController.editTag(body, 1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("editedTag", ((Tag) response.getBody()).getName());
    }

    @Test
    void testDeleteTag() {
        Board board = new Board(1L, "Board1");
        Tag tag = new Tag(2L,"tag1");
        board.addTag(tag);
        tag.setBoard(board);

        when(tagRepository.findById(2L)).thenReturn(Optional.of(tag));

        ResponseEntity<?> response = tagController.deleteTag(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(board.getListOfTags().contains(tag));
        assertNull(tag.getBoard());
    }

    @Test
    void testAddTag() {
        Board board = new Board(1L, "Board1");
        TaskList taskList = new TaskList(2L, "TaskList1");
        Task task = new Task(3L,"Task1","");
        Tag tag = new Tag(4L,"Tag1");
        board.add(taskList);
        taskList.add(task);
        board.addTag(tag);
        tag.setBoard(board);

        when(tagRepository.findById(4L)).thenReturn(Optional.of(tag));
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));

        ResponseEntity<?> response = tagController.addTag(1L, 2L, 3L, 4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(task.getListOfTags().contains(tag));
    }

    @Test
    void testRemoveTag() {
        Board board = new Board(1L, "Board1");
        TaskList taskList = new TaskList(2L, "TaskList1");
        Task task = new Task(3L,"Task1","");
        Tag tag = new Tag(4L,"Tag1");
        board.add(taskList);
        taskList.add(task);
        board.addTag(tag);
        tag.setBoard(board);
        task.addTag(tag);

        when(tagRepository.findById(4L)).thenReturn(Optional.of(tag));
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));

        ResponseEntity<?> response = tagController.removeTag(1L, 2L, 3L, 4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(task.getListOfTags().contains(tag));
        assertTrue(board.getListOfTags().contains(tag));
        assertEquals(board, tag.getBoard());
    }
}

package server.api;

import commons.Board;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;
import server.database.BoardRepository;
import server.database.TaskListRepository;
import server.database.TaskRepository;

import java.util.*;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CardControllerTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private TaskListRepository taskListRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll() {
        List<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(new Task(1L, "Name 1", "Description 1"));
        expectedTasks.add(new Task(2L, "Name 2", "Description 2"));

        when(taskRepository.findAll()).thenReturn(expectedTasks);

        List<Task> listOfTasks = taskRepository.findAll();

        assertNotNull(listOfTasks);

        assertEquals(expectedTasks.size(), listOfTasks.size());
        assertEquals(expectedTasks.get(0), listOfTasks.get(0));
        assertEquals(expectedTasks.get(1), listOfTasks.get(1));
    }

    @Test
    public void testGetById() {
        Task task = new Task("task1", "desc1");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        ResponseEntity<?> response = cardController.getById(1L);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), task);
    }

    @Test
    public void testGetByIdNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = cardController.getById(1L);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(), "Task not found");
    }

    @Test
    public void testAdd() throws Exception {
        TaskList taskList = new TaskList(1L, "Name 1");

        when(taskListRepository.findById(1L)).thenReturn(Optional.of(taskList));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(new Board(1L, "Title 1")));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "task1");
        requestBody.put("description", "desc1");

        ResponseEntity<?> response = cardController.add(requestBody, 1L, 1L);

        Task expectedTask = new Task();
        expectedTask.setName("task1");
        expectedTask.setDescription("desc1");
        expectedTask.setTaskList(taskList);

        Task actualTask = (Task) response.getBody();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(actualTask.getName(), expectedTask.getName());
        assertEquals(actualTask.getDescription(), expectedTask.getDescription());
        assertEquals(actualTask.getTaskList(), expectedTask.getTaskList());
    }

    @Test
    public void testAddBadRequest() throws Exception {
        Map<String, String> requestBody = new HashMap<>();

        ResponseEntity<?> response = cardController.add(requestBody, 1L, 1L);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testAddListNotFound() throws RuntimeException {
        when(taskListRepository.findById(1L)).thenReturn(Optional.empty());

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "task1");
        requestBody.put("description", "desc1");

        assertThrows(RuntimeException.class, () -> {
            cardController.add(requestBody, 1L, 1L);
        });
    }

    @Test
    public void testEditNotFound() throws Exception {
        Task task = new Task("Name 1", "Description 1");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "newTaskName");
        requestBody.put("description", "newTaskDesc");

        ResponseEntity<?> response = cardController.edit("newTaskName", "newTaskDesc", 1L, 1L, 1L);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = cardController.deleteTask(1L, 1L, 1L);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(), "Task not found");
    }

    @Test
    public void testMove() {
        Task task = new Task(1L,"Task 1", "Description 1");
        TaskList taskListFrom = new TaskList(1L, "List 1");
        TaskList taskListTo = new TaskList(2L, "List 2");
        Board board = new Board(3L, "Board 1");

        taskListFrom.add(task);
        task.setTaskList(taskListFrom);

        when(taskListRepository.findById(taskListFrom.getId())).thenReturn(Optional.of(taskListFrom));
        when(taskListRepository.findById(taskListTo.getId())).thenReturn(Optional.of(taskListTo));
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("listTo", taskListTo.getId());
        requestBody.put("index", 1L);

        ResponseEntity<?> response = cardController.move(task.getId(), taskListFrom.getId(), board.getId(), requestBody);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody() instanceof TaskList);
        assertNotEquals(taskListTo, response.getBody());

        assertNotEquals(taskListTo, task.getTaskList());
        assertNotEquals(0, taskListFrom.getTask().size());
    }







}

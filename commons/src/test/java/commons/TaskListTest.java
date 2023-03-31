package commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskListTest {

    private Board board;
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        board = new Board(1L, "Board");
        taskList = new TaskList(1L, "Task List");
        board.add(taskList);
    }

    @Test
    public void testAddTaskList() {
        List<TaskList> taskLists = board.getListOfTaskList();
        Assertions.assertEquals(1, taskLists.size());
        Assertions.assertEquals(taskList, taskLists.get(0));
    }

    @Test
    void testGetName() {
        String name = "Test Task";
        Task task = new Task(name, "Test description");
        Assertions.assertEquals(name, task.getName());
    }

    @Test
    public void testRemoveTaskList() {
        board.remove(taskList);
        List<TaskList> taskLists = board.getListOfTaskList();
        Assertions.assertEquals(0, taskLists.size());
    }

    @Test
    public void testAddTask() {
        Task task = new Task(1L, "Task", "Description");
        taskList.add(task);
        List<Task> tasks = taskList.getTaskList();
        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals(task, tasks.get(0));
    }

    @Test
    public void testRemoveTask() {
        Task task = new Task(1L, "Task", "Description");
        taskList.add(task);
        taskList.remove(task);
        List<Task> tasks = taskList.getTaskList();
        Assertions.assertEquals(0, tasks.size());
    }
    @Test
    public void testEqualsAndHashCode() {
        Board board1 = new Board(1L, "Board");
        Board board2 = new Board(1L, "Board");
        TaskList taskList1 = new TaskList(1L, "Task List");
        TaskList taskList2 = new TaskList(1L, "Task List");
        Assertions.assertEquals(board1, board2);
        Assertions.assertEquals(board1.hashCode(), board2.hashCode());
        Assertions.assertEquals(taskList1, taskList2);
        Assertions.assertEquals(taskList1.hashCode(), taskList2.hashCode());
    }

    @Test
    public void testSetName() {
        TaskList taskList = new TaskList();
        taskList.setName("New Task List");
        Assertions.assertEquals("New Task List", taskList.getName());
    }

    @Test
    public void testSetTaskList() {
        TaskList taskList = new TaskList();
        List<Task> taskItems = new ArrayList<>();
        taskItems.add(new Task("Task 1", "Description 1"));
        taskList.setTaskList(taskItems);
        Assertions.assertEquals(taskItems, taskList.getTaskList());
    }

    @Test
    public void testSetId() {
        TaskList taskList = new TaskList();
        taskList.setId(1L);
        Assertions.assertEquals(1L, taskList.getId());
    }

    @Test
    public void testSetBoard() {
        TaskList taskList = new TaskList();
        Board board = new Board();
        taskList.setBoard(board);
        Assertions.assertEquals(board, taskList.getBoard());
    }

    @Test
    void testToString() {
        taskList.setName("Test TaskList");
        String expected = "commons.TaskList@";
        assertTrue(taskList.toString().startsWith(expected));
    }
}
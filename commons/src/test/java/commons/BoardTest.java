package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testSetTitle() {
        board.setTitle("Test Board");
        assertEquals("Test Board", board.getTitle());
    }

    @Test
    void testAddTaskList() {
        TaskList taskList = new TaskList();
        board.add(taskList);
        assertTrue(board.getListOfTaskList().contains(taskList));
    }

    @Test
    void testRemoveTaskList() {
        TaskList taskList = new TaskList();
        board.add(taskList);
        board.remove(taskList);
        assertFalse(board.getListOfTaskList().contains(taskList));
    }

    @Test
    void testEqualsAndHashCode() {
        Board board1 = new Board(1L, "Test Board");
        Board board2 = new Board(1L, "Test Board");
        assertEquals(board1, board2);
        assertEquals(board1.hashCode(), board2.hashCode());
    }

    @Test
    void testToString() {
        board.setTitle("Test Board");
        String expected = "commons.Board@";
        assertTrue(board.toString().startsWith(expected));
    }

    @Test
    public void testSetId() {
        Long expectedId = 1L;
        board.setId(expectedId);
        Long actualId = board.getId();
        assertEquals(expectedId, actualId);
    }
    @Test
    public void testSetListOfTaskList() {
        Board board = new Board();
        List<TaskList> taskListList = new ArrayList<>();
        taskListList.add(new TaskList(1L, "Task List 1"));
        taskListList.add(new TaskList(2L, "Task List 2"));

        board.setListOfTaskList(taskListList);

        assertEquals(2, board.getListOfTaskList().size());
        assertEquals("Task List 1", board.getListOfTaskList().get(0).getName());
        assertEquals("Task List 2", board.getListOfTaskList().get(1).getName());
    }
}
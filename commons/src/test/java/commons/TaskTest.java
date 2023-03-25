package commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTest {

    @Test
    public void testGetSetName() {
        Task task = new Task();
        task.setName("Task 1");
        assertEquals("Task 1", task.getName());
    }

    @Test
    public void testGetSetDescription() {
        Task task = new Task();
        task.setDescription("This is a task");
        assertEquals("This is a task", task.getDescription());
    }

    @Test
    public void testGetSetId() {
        Task task = new Task();
        task.setId(1L);
        assertEquals(1L, task.getId().longValue());
    }

    @Test
    public void testEqualsAndHashCode() {
        Task task1 = new Task(1L, "Task 1", "Description 1");
        Task task2 = new Task(1L, "Task 1", "Description 1");
        Task task3 = new Task(2L, "Task 2", "Description 2");

        assertEquals(task1, task2);
        assertNotEquals(task1, task3);

        assertEquals(task1.hashCode(), task2.hashCode());
        assertNotEquals(task1.hashCode(), task3.hashCode());
    }

    @Test
    public void testToString() {
        Task task = new Task(1L, "Task 1", "Description 1");
        String expected = "Task[id=1,name=Task 1,description=Description 1,taskList=null]";
        assertEquals(expected, task.toString());
    }

    @Test
    public void testGetSetTaskList() {
        Task task = new Task();
        TaskList taskList = new TaskList();
        task.setTaskList(taskList);
        assertEquals(taskList, task.getTaskList());
    }
}
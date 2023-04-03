package server.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import commons.Board;
import commons.TaskList;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.database.BoardRepository;
import server.database.TaskListRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TaskListControllerTest {
    @Mock
    TaskListRepository taskListRepository;

    @Mock
    BoardRepository boardRepository;

    @Mock
    SimpMessagingTemplate simpMessagingTemplate;

    @Test
    public void testGetAll() {
        TaskList taskList1 = new TaskList(1L, "List 1");
        TaskList taskList2 = new TaskList(2L, "List 2");

        List<TaskList> taskLists = new ArrayList<>();
        taskLists.add(taskList1);
        taskLists.add(taskList2);

        when(taskListRepository.findAll()).thenReturn(taskLists);

        TaskListController controller = new TaskListController(taskListRepository, boardRepository, simpMessagingTemplate);
        ResponseEntity<List<TaskList>> response = controller.getAll();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), taskLists);
    }

    @Test
    public void testGetById() {
        TaskList taskList = new TaskList(1L, "List 1");

        when(taskListRepository.findById(anyLong())).thenReturn(Optional.of(taskList));

        TaskListController controller = new TaskListController(taskListRepository, boardRepository, simpMessagingTemplate);
        ResponseEntity<?> response = controller.getById(1L);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), taskList);
    }

    @Test
    public void testGetByIdNotFound() {
        when(taskListRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskListController controller = new TaskListController(taskListRepository, boardRepository, simpMessagingTemplate);
        ResponseEntity<?> response = controller.getById(1L);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(), "List not found");
    }


    @Test
    public void testAddNoName() {
        Map<String, String> requestBody = new HashMap<>();

        TaskListController controller = new TaskListController(taskListRepository, boardRepository, simpMessagingTemplate);
        ResponseEntity<Board> response = controller.add(requestBody, 1L);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDelete() {
        long boardId = 1L;
        long listId = 2L;
        Board board = new Board(1L, "Title 1");
        TaskList taskList = new TaskList();
        taskList.setBoard(board);

        TaskListController controller = new TaskListController(taskListRepository, boardRepository, simpMessagingTemplate);

        when(taskListRepository.findById(listId)).thenReturn(Optional.of(taskList));
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        ResponseEntity<?> response = controller.delete(boardId, listId);

        verify(taskListRepository).deleteById(listId);
        verify(boardRepository).save(board);
        verify(simpMessagingTemplate).convertAndSend(eq("/topic/" + boardId), eq(board));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(board, response.getBody());
    }

    @Test
    public void testAdd() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "Test tasklist");
        long boardId = 1L;
        Board board = new Board();

        TaskListController controller = new TaskListController(taskListRepository, boardRepository, simpMessagingTemplate);

        when(boardRepository.findById(eq(boardId))).thenReturn(Optional.of(board));

        ResponseEntity<Board> response = controller.add(body, boardId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(board, response.getBody());

        verify(boardRepository, times(2)).findById(eq(boardId));
        verify(taskListRepository, times(1)).save(any(TaskList.class));
    }

    @Test
    void editShouldReturnBadRequestWhenNameisMissing() {
        // given
        Map<String, String> body = new HashMap<>();
        body.put("foo", "bar");

        TaskListController controller = new TaskListController(taskListRepository, boardRepository, simpMessagingTemplate);

        // when
        ResponseEntity<?> response = controller.edit(1L, 2L, body);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        verifyNoInteractions(boardRepository);
        verifyNoInteractions(taskListRepository);
        verifyNoInteractions(simpMessagingTemplate);
    }

    @Test
    void editShouldReturnNotFoundWhenBoardDoesNotExist() {
        // given
        Map<String, String> body = new HashMap<>();
        body.put("name", "New list name");
        when(boardRepository.existsById(1L)).thenReturn(false);

        TaskListController controller = new TaskListController(taskListRepository, boardRepository, simpMessagingTemplate);

        // when
        ResponseEntity<?> response = controller.edit(1L, 2L, body);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(), "Board not found");
        verify(boardRepository).existsById(1L);
        verifyNoMoreInteractions(boardRepository);
        verifyNoInteractions(taskListRepository);
        verifyNoInteractions(simpMessagingTemplate);
    }

    @Test
    void editsShouldReturnNotFoundWhenListDoesNotExist() {
        // given
        Map<String, String> body = new HashMap<>();
        body.put("name", "New list name");
        when(boardRepository.existsById(1L)).thenReturn(true);
        when(taskListRepository.existsById(2L)).thenReturn(false);

        TaskListController controller = new TaskListController(taskListRepository, boardRepository, simpMessagingTemplate);

        // when
        ResponseEntity<?> response = controller.edit(1L, 2L, body);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(), "List not found");
        verify(boardRepository).existsById(1L);
        verify(taskListRepository).existsById(2L);
        verifyNoMoreInteractions(boardRepository);
        verifyNoMoreInteractions(taskListRepository);
        verifyNoInteractions(simpMessagingTemplate);

    }

}
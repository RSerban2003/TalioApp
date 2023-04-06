package server.api;

import commons.Board;
import commons.ListOfBoards;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static server.api.BoardController.isNullOrEmpty;

public class BoardControllerTest {
    @Mock
    private BoardRepository boardRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private BoardController boardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll() {
        List<Board> expectedBoards = new ArrayList<>();
        expectedBoards.add(new Board(1L, "Board 1"));
        expectedBoards.add(new Board(2L, "Board 2"));

        when(boardRepository.findAll()).thenReturn(expectedBoards);

        ResponseEntity<?> responseEntity = boardController.getAll();
        List<Board> actualBoards = (List<Board>) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(actualBoards);
        assertEquals(expectedBoards.size(), actualBoards.size());
        assertEquals(expectedBoards.get(0), actualBoards.get(0));
        assertEquals(expectedBoards.get(1), actualBoards.get(1));
    }

    @Test
    public void testGetBoardById_BoardExists() {
        Long id = 1L;
        Board expectedBoard = new Board(id, "Board 1");

        when(boardRepository.findById(id)).thenReturn(java.util.Optional.of(expectedBoard));

        ResponseEntity<Board> responseEntity = boardController.getBoardById(id);
        Board actualBoard = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(actualBoard);
        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    public void testGetBoardById_BoardDoesNotExist() {
        Long id = 1L;

        when(boardRepository.findById(id)).thenReturn(java.util.Optional.empty());

        ResponseEntity<Board> responseEntity = boardController.getBoardById(id);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteBoardById_BoardDoesNotExist() {
        Long id = 1L;

        when(boardRepository.existsById(id)).thenReturn(false);

        ResponseEntity<Void> responseEntity = boardController.deleteBoardById(id);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteBoardById_BoardExists() {
        Long id = 1L;
        List<Board> expectedBoards = new ArrayList<>();
        expectedBoards.add(new Board(id, "Board 1"));
        expectedBoards.add(new Board(2L, "Board 2"));
        ListOfBoards expectedListOfBoards = new ListOfBoards(expectedBoards);

        when(boardRepository.existsById(id)).thenReturn(true);
        doNothing().when(boardRepository).deleteById(id);
        when(boardRepository.findAll()).thenReturn(expectedBoards);

        ResponseEntity<Void> responseEntity = boardController.deleteBoardById(id);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(boardRepository, times(1)).deleteById(id);
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/topic/" + id), any(Board.class));

    }

    @Test
    public void testAddValidBoard() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Board Name");
        Board board = new Board();
        board.setTitle(requestBody.get("name"));
        when(boardRepository.save(board)).thenReturn(board);
        ResponseEntity<Board> responseEntity = boardController.add(requestBody);
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    public void testAddInvalidBoard() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("test", "Board Name");
        Board board = new Board();
        board.setTitle(requestBody.get("name"));
        when(boardRepository.save(board)).thenReturn(board);
        ResponseEntity<Board> responseEntity = boardController.add(requestBody);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
    @Test
    void testIsNullOrEmptyWhenStringIsNull() {
        assertTrue(isNullOrEmpty(null));
    }

    @Test
    void testIsNullOrEmptyWhenStringIsEmpty() {
        assertTrue(isNullOrEmpty(""));
    }

    @Test
    void testIsNullOrEmptyWhenStringIsNotNullAndNotEmpty() {
        assertFalse(isNullOrEmpty("Hello"));
    }

}
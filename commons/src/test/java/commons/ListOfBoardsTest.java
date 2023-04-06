package commons;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListOfBoardsTest {

    @Test
    void testConstructorWithParameter() {
        List<Board> boards = new ArrayList<>();
        Board board1 = new Board(1L, "Board 1");
        Board board2 = new Board(2L, "Board 2");
        boards.add(board1);
        boards.add(board2);
        ListOfBoards listOfBoards = new ListOfBoards(boards);
        assertEquals(boards, listOfBoards.getBoardList());
    }

    @Test
    void testConstructorWithoutParameter() {
        ListOfBoards listOfBoards = new ListOfBoards();
        assertTrue(listOfBoards.getBoardList() == null);
    }

    @Test
    void testSetBoardList() {
        ListOfBoards listOfBoards = new ListOfBoards();
        List<Board> boards = new ArrayList<>();
        Board board1 = new Board(1L, "Board 1");
        Board board2 = new Board(2L, "Board 2");
        boards.add(board1);
        boards.add(board2);
        listOfBoards.setBoardList(boards);
        assertEquals(boards, listOfBoards.getBoardList());
    }

    @Test
    void testEquals() {
        List<Board> boards1 = new ArrayList<>();
        Board board1 = new Board(1L, "Board 1");
        Board board2 = new Board(2L, "Board 2");
        boards1.add(board1);
        boards1.add(board2);
        ListOfBoards listOfBoards1 = new ListOfBoards(boards1);

        List<Board> boards2 = new ArrayList<>();
        Board board3 = new Board(1L, "Board 1");
        Board board4 = new Board(2L, "Board 2");
        boards2.add(board3);
        boards2.add(board4);
        ListOfBoards listOfBoards2 = new ListOfBoards(boards2);

        assertTrue(listOfBoards1.equals(listOfBoards2));
    }

    @Test
    void testHashCode() {
        List<Board> boards1 = new ArrayList<>();
        Board board1 = new Board(1L, "Board 1");
        Board board2 = new Board(2L, "Board 2");
        boards1.add(board1);
        boards1.add(board2);
        ListOfBoards listOfBoards1 = new ListOfBoards(boards1);

        List<Board> boards2 = new ArrayList<>();
        Board board3 = new Board(1L, "Board 1");
        Board board4 = new Board(2L, "Board 2");
        boards2.add(board3);
        boards2.add(board4);
        ListOfBoards listOfBoards2 = new ListOfBoards(boards2);

        assertEquals(listOfBoards1.hashCode(), listOfBoards2.hashCode());
    }

    @Test
    void testToString() {
        List<Board> boards = new ArrayList<>();
        Board board1 = new Board(1L, "Board 1");
        Board board2 = new Board(2L, "Board 2");
        boards.add(board1);
        boards.add(board2);
        ListOfBoards listOfBoards = new ListOfBoards(boards);
        String expected = "ListOfBoards{boardList=[" + board1 + ", " + board2 + "]}";
        assertEquals(expected, listOfBoards.toString());
    }


}
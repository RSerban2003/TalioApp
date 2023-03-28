package commons;

import java.util.List;
import java.util.Objects;

public class ListOfBoards {
    private List<Board> boardList;

    public ListOfBoards(List<Board> boardList) {
        this.boardList = boardList;
    }

    @Override
    public String toString() {
        return "ListOfBoards{" +
                "boardList=" + boardList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListOfBoards that = (ListOfBoards) o;
        return Objects.equals(boardList, that.boardList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardList);
    }

    public List<Board> getBoardList() {
        return boardList;
    }

    public void setBoardList(List<Board> boardList) {
        this.boardList = boardList;
    }
}

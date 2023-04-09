package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "TAG")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "NAME")
    private String name;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "tag_tasks",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> taskList;

    public Tag(Long id, String name, Board board) {
        this.id = id;
        this.name = name;
        this.board = board;
        this.taskList = new ArrayList<>();
    }

    public Tag() {}

    public Tag(String name) {
        this.name = name;
        this.taskList = new ArrayList<>();
    }

    public Tag(String name, Board board) {
        this.name = name;
        this.board = board;
        this.taskList = new ArrayList<>();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    public void add(Task task){
        taskList.add(task);
    }

    public void remove(Task task){
        if(!taskList.contains(task)) return;
        this.taskList.remove(task);
    }
}

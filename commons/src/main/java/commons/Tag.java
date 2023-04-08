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
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<TaskTag> taskTags;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public Tag () {
        this.taskTags = new ArrayList<>();
    }

    public Tag(String name) {
        this.name = name;
        this.taskTags = new ArrayList<>();
    }

    public Tag (Long id, String name) {
        this.id = id;
        this.name = name;
        this.taskTags = new ArrayList<>();
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

    public List<TaskTag> getListOfTasks() {
        return taskTags;
    }

    public void setListOfTasks(List<TaskTag> listOfTasks) {
        this.taskTags = listOfTasks;
    }

    public void addTask(TaskTag taskTag) {
        if(taskTags.contains(taskTag)) return;
        taskTags.add(taskTag);
    }
    public void removeTask(TaskTag taskTag) {
        if(!taskTags.contains(taskTag)) return;
        taskTags.remove(taskTag);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return this.board;
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

}

package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "TaskList")
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "task")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "task")
    private List<Task> task;


    public TaskList(Long id, String name) {
        this.id = id;
        this.name = name;
        this.task = new ArrayList<>();
    }

    public TaskList() {
        this.task = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTaskList() {
        return task;
    }

    public void setTaskList(List<Task> task) {
        this.task = task;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public void add(Task taskItem) {
        task.add(taskItem);
    }

    public void remove(Task taskItem) {
        if (!task.contains(taskItem)) return;
        this.task.remove(taskItem);
    }
}

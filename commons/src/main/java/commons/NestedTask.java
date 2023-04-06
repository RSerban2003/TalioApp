package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "NESTEDTASK")
public class NestedTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "ISCOMPLETE")
    private Boolean isComplete;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    @Column
    private Integer index;
    public NestedTask(Long id, String name, boolean isComplete) {
        this.id = id;
        this.name = name;
        this.isComplete = isComplete;
        this.index = -1;
    }
    public NestedTask(Long id, String name, boolean isComplete, Integer index) {
        this.id = id;
        this.name = name;
        this.isComplete = isComplete;
        this.index = index;
    }
    public NestedTask() {
    }

    public NestedTask(Long id, String name, Boolean isComplete, Task task, Integer index) {
        this.id = id;
        this.name = name;
        this.isComplete = isComplete;
        this.task = task;
        this.index = index;
    }

    public NestedTask(String name, Boolean isComplete) {
        this.name = name;
        this.isComplete = isComplete;
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

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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

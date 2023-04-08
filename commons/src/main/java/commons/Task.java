package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @OrderColumn(name = "index")
    private List<NestedTask> nestedTasks;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tasklist_id")
    private TaskList taskList;
    @JsonManagedReference
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskTag> taskTags;

    @Column
    private Integer index;

    public Task(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.nestedTasks = new ArrayList<>();
        this.index = -1;
        this.taskTags = new ArrayList<>();
    }
    public Task(Long id, String name, String description, int index) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.nestedTasks = new ArrayList<>();
        this.index = index;
        this.taskTags = new ArrayList<>();
    }

    public Task() {
        this.nestedTasks = new ArrayList<>();
        this.taskTags = new ArrayList<>();
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.taskTags = new ArrayList<>();
        this.nestedTasks = new ArrayList<>();
    }

    public void add(NestedTask nestedTask){
        nestedTasks.add(nestedTask);
    }

    public List<NestedTask> getNestedTasks() {
        return nestedTasks;
    }

    public void remove(NestedTask nestedTask){
        if(!nestedTasks.contains(nestedTask)) return;
        this.nestedTasks.remove(nestedTask);
    }

    public void add(int index, NestedTask nestedTask){
        nestedTasks.add(index, nestedTask);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public void addTag(TaskTag taskTag) {
        this.taskTags.add(taskTag);
        taskTag.setTask(this);
    }

    public void removeTag(TaskTag taskTag) {
        if(!taskTags.contains(taskTag)) return;
        taskTags.remove(taskTag);
        taskTag.setTask(null);
    }

    public void setNestedTasks(List<NestedTask> nestedTasks) {
        this.nestedTasks = nestedTasks;
    }

    public List<TaskTag> getTaskTags() {
        return taskTags;
    }

    public void setTaskTags(List<TaskTag> taskTags) {
        this.taskTags = taskTags;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

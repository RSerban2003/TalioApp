package commons;

import javax.persistence.*;

@Entity
@Table(name = "task_tag")
public class TaskTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public TaskTag(Task task, Tag tag) {
        this.task = task;
        this.tag = tag;
    }

    public TaskTag(Long id, Task task, Tag tag) {
        this.id = id;
        this.task = task;
        this.tag = tag;
    }

    public TaskTag() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
    public void setTagName(String name) {
        this.tag.setName(name);
    }
    public String getTagName() {
        return this.tag.getName();
    }
}
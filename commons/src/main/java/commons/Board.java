package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "BOARD")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @OrderColumn(name = "index")
    private List<Tag> listOfTags;
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<TaskList> listOfTaskList;


    public Board(Long id, String title) {
        this.id = id;
        this.title = title;
        this.listOfTaskList = new ArrayList<>();
        this.listOfTags = new ArrayList<>();
    }

    public Board() {
        this.listOfTaskList = new ArrayList<>();
        this.listOfTags = new ArrayList<>();
    }

    public void setListOfTaskList(List<TaskList> listOfTaskList) {
        this.listOfTaskList = listOfTaskList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<TaskList> getListOfTaskList() {
        return listOfTaskList;
    }

    public void add(TaskList list){
        if(list == null) return;
        listOfTaskList.add(list);
    }

    public void remove(TaskList taskList) {
        if (!listOfTaskList.contains(taskList)) return;
        this.listOfTaskList.remove(taskList);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void addTag (Tag tag) {
        this.listOfTags.add(tag);
    }

    public void removeTag (Tag tag){
        if(!listOfTags.contains(tag)) return;
        this.listOfTags.remove(tag);
    }

    public List<Tag> getListOfTags () {
        return listOfTags;
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

package commons;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tags {
    @Id
    private Long id;
    private String name;

    public Tags(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tags() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

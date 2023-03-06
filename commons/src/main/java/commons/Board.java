package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Board {

    @Id
    private Long id;
    private String title;
    private String nameOfCreator;
    @OneToMany
    private List<Card> cardlist;
    private boolean isPasswordProtected;
    private String password;
    private String color;


    public Board(Long id, String title, List<Card> cardlist,
                 boolean isPasswordProtected, String password,
                 String color, String nameOfCreator) {
        this.id = id;
        this.title = title;
        this.cardlist = cardlist;
        this.isPasswordProtected = isPasswordProtected;
        this.password = password;
        this.color = color;
        this.nameOfCreator = nameOfCreator;
    }

    public Board() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNameOfCreator() {
        return nameOfCreator;
    }

    public void setNameOfCreator(String nameOfCreator) {
        this.nameOfCreator = nameOfCreator;
    }

    public List<Card> getCardlist() {
        return cardlist;
    }

    public void setCardlist(List<Card> cardlist) {
        this.cardlist = cardlist;
    }

    public boolean isPasswordProtected() {
        return isPasswordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        isPasswordProtected = passwordProtected;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

}

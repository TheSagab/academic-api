package siakng.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private int sks;
    private Date dateCreated;

    public Course(String name, int sks) {
        this.name = name;
        this.sks = sks;
        this.dateCreated = new Date();
    }

    public Course(int id, String name, int sks) {
        this.id = id;
        this.name = name;
        this.sks = sks;
        this.dateCreated = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSks() {
        return sks;
    }

    public void setSks(int sks) {
        this.sks = sks;
    }

    public Date getDateCreated() {
        return dateCreated;
    }
}

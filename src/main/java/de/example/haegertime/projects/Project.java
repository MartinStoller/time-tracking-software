package de.example.haegertime.projects;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity // This tells Hibernate to make a table out of this class
@Table(name="projects") // this allows the mapping to our specific table in the db
public class Project {
    @Id
    private Long id;
    private String title;

    public Project(){} //Empty Constructor needed for hibernate

    public Project(Long id, String title){
        this.id = id;
        this.title = title;
    }
    //TODO: implement attributes (getter/setter/equals will be handled by annotation)
}

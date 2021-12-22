package de.example.haegertime.projects;

import lombok.Data;
import org.apache.tomcat.jni.Local;
import org.sonatype.inject.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

@Data
@Entity // This tells Hibernate to make a table out of this class
@Table(name="projects") // this allows the mapping to our specific table in the db
@Validated
public class Project {
    @Id
    @SequenceGenerator(
            name="project_sequence",
            sequenceName = "project_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "project_sequence"
    )
    private Long id;
    @NotNull @NotBlank
    private String title;
    @NotNull
    private LocalDate start;
    @Nullable
    private LocalDate end;
    @NotNull @NotBlank
    private String customer;
    @NotNull @NotBlank
    private String billingAddress;


    public Project(){} //Empty Constructor needed for hibernate

    public Project(String title, LocalDate start, LocalDate end, String customer, String billingAddress){
        this.title = title;
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.billingAddress = billingAddress;
    }
    //TODO: implement attributes (getter/setter/equals will be handled by annotation)
}

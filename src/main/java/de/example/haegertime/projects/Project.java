package de.example.haegertime.projects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.example.haegertime.timetables.TimeTableDay;
import lombok.Data;
import org.sonatype.inject.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private List<TimeTableDay> timeTableDays;


    public Project(){} //Empty Constructor needed for hibernate

    public Project(String title, LocalDate start, LocalDate end){
        this.title = title;
        this.start = start;
        this.end = end;
        this.timeTableDays = Collections.emptyList();
    }
}

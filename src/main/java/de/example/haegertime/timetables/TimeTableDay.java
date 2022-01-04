package de.example.haegertime.timetables;

import lombok.Data;
import org.sonatype.inject.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "timetable")
@Validated
public class TimeTableDay {
    @Id
    @SequenceGenerator(
            name="user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;  //not sure yet if we really need an Id, but probably cant hurt to have a unique identifier for each line
    private Long employeeId;  // this will be the foreign key. TODO: MYSQL needs to know that this is a foreign key -> check how to declear that
    //also, we´ll need some sort of validation if that employeeID does exist(maybe this is already taken care of when we declare it as foreign key)
    private LocalDate date;
    @Nullable
    private Time startTime;
    @Nullable
    private Time endTime;
    private float breakLength;
    private float expectedHours;
    private float actualHours;
    private boolean sickDay;
    private boolean holiday;
    private Long projectId; //We need some sort of validation that project does exist in project DB



}

package de.example.haegertime.timetables;

import lombok.Data;
import org.sonatype.inject.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "timetable")
@Validated
//TODO: If end or starttime is null the other must be null as well (+breaklength = null +expected and actual hours must be zero) and absence status must not be null
//TODO: If absence staus is not null, all teh duration parameters must be null/zero
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
    private LocalTime startTime;
    @Nullable
    private LocalTime endTime;
    @Nullable
    private Duration breakLength; //TODO: find suitable Datatype for durations
    private float expectedHours;//TODO: find suitable Datatype for durations
    private float actualHours;//TODO: find suitable Datatype for durations
    @Nullable
    private AbsenceStatus absenceStatus;
    private Long projectId; //We need some sort of validation that project does exist in project DB (Foreign key?)
    private boolean finalized;

    public TimeTableDay(){}

    public TimeTableDay(Long employeeId, LocalDate date, LocalTime startTime, LocalTime endTime, Duration breakLength,
                        float expectedHours, float actualHours, AbsenceStatus absenceStatus, Long projectId){
        this.employeeId = employeeId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakLength = breakLength;
        this.expectedHours = expectedHours;
        this.actualHours = actualHours;
        this.absenceStatus = absenceStatus;
        this.projectId = projectId;
        this.finalized = false;
    }

}

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
    private Long workdayId;  //serves as a unique identifier of the object to simplify deleting/editing single datapoints
    private Long employeeId;  // this will be the foreign key. TODO: MYSQL needs to know that this is a foreign key -> check how to declear that
    //also, we´ll need some sort of validation if that employeeID does exist(maybe this is already taken care of when we declare it as foreign key)
    private LocalDate date;
    @Nullable
    private LocalTime startTime;
    @Nullable
    private LocalTime endTime;
    private float breakLength; //TODO: breaklength cannot be negative or over 24h
    private float expectedHours;//TODO: expectedHours cannot be negative or over 24h
    private float actualHours;//TODO: actualHours cannot be negative or over 24h
    @Nullable
    private AbsenceStatus absenceStatus;
    private Long projectId; //We need some sort of validation that project does exist in project DB (Foreign key?)
    private boolean finalized;

    public TimeTableDay(){}

    public TimeTableDay(Long employeeId, LocalDate date, LocalTime startTime, LocalTime endTime, float breakLength,
                        float expectedHours, AbsenceStatus absenceStatus, Long projectId){
        this.employeeId = employeeId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakLength = DatesAndDurationsCalculator.convertDurationToString(breakLength);
        this.expectedHours = DatesAndDurationsCalculator.convertDurationToString(expectedHours);
        this.absenceStatus = absenceStatus;
        this.projectId = projectId;
        this.finalized = false;
        this.actualHours = calculateActualHours(this.startTime, this.endTime, this.breakLength);
    }

    public String calculateActualHours(LocalTime startTime, LocalTime endTime, String breakLength) {
        if (this.startTime == null) { //if sick or on holiday, the expected hours are achieved
            this.actualHours = expectedHours;
            return actualHours;
        } else { //otherwise calculate from start-,end- and breaktime
            String timeAtWork = DatesAndDurationsCalculator.getDurationBetweenLocalTimes(this.endTime, this.startTime);
            return DatesAndDurationsCalculator.substractDurationStrings(timeAtWork, this.breakLength);
        }
    }

}

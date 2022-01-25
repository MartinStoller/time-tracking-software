package de.example.haegertime.timetables;

import de.example.haegertime.projects.Project;
import de.example.haegertime.users.User;
import lombok.Data;
import org.sonatype.inject.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "timetable")
@Validated
public class TimeTableDay {

    @Id
    @SequenceGenerator(
            name = "timetable_sequence",
            sequenceName = "timetable_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "timetable_sequence"
    )
    private Long workdayId;  //serves as a unique identifier of the object to simplify deleting/editing single datapoints

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private User employee;

    @NotNull
    private LocalDate date;
    @Nullable
    private LocalTime startTime;
    @Nullable
    private LocalTime endTime;
    @Min(value = 0)
    @Max(value = 24)
    private double breakLength;
    @Min(value = 0)
    @Max(value = 24)
    private double expectedHours;
    @Nullable
    private AbsenceStatus absenceStatus;
    @Min(value = 0)
    @Max(value = 24)
    private double holidayHours;
    @Min(value = 0)
    @Max(value = 24)
    private double sickHours;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    private boolean finalized;

    @AssertTrue(message = "If startTime is null, so must be endTime")
    private boolean isInputValid() { //this is a validation method and its name is supposed to start with "is..." or the program will not recognize it!
        if (this.startTime == null) {
            return this.endTime == null && this.breakLength == 0;
        } else {
            return this.endTime != null;
        }
    }

    public TimeTableDay() {
    }

    public TimeTableDay(LocalDate date, LocalTime startTime, LocalTime endTime, double breakLength,
                        double expectedHours, AbsenceStatus absenceStatus) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;

        this.breakLength = breakLength;
        this.expectedHours = expectedHours;
        this.absenceStatus = absenceStatus;

        this.finalized = false;

        if (this.absenceStatus == null) {
            this.sickHours = 0;
            this.holidayHours = 0;
        } else {
            calculateSickAndHoldiayHours(); // calculates sickness and holiday hours
        }
    }

    public double calculateActualHours() {

        if (this.absenceStatus != null) { //if sick or on holiday, the expected hours are achieved
            return expectedHours;
        } else if (this.startTime == null) { //this covers weekends/public holidays -> starttime usually null without absence status
            return 0;
        } else { //otherwise calculate from start-,end- and breaktime
            Duration timeAtWork = Duration.between(startTime, endTime);
            //convert Duration to float hours:
            Long minutes = timeAtWork.toMinutes();
            return minutes / 60.0 - this.breakLength;
        }
    }

    private void calculateSickAndHoldiayHours() {
        if (this.startTime == null && this.absenceStatus == AbsenceStatus.HOLIDAY){ // if employee never started to work

            this.holidayHours = this.expectedHours;
        } else if (this.startTime == null && this.absenceStatus == AbsenceStatus.SICK) { // if employee never started to work
            this.sickHours = this.expectedHours;
        }
        //All remaining cases: if employee did start with work...
        else if (this.absenceStatus == AbsenceStatus.HOLIDAY) {
            this.sickHours = 0;
            Duration timeAtWork = Duration.between(startTime, endTime);
            Long minutes = timeAtWork.toMinutes();
            double workedHours = minutes / 60.0 - this.breakLength;
            this.holidayHours = this.expectedHours - workedHours;
        } else if (this.absenceStatus == AbsenceStatus.SICK) {
            this.holidayHours = 0;
            Duration timeAtWork = Duration.between(startTime, endTime);
            Long minutes = timeAtWork.toMinutes();
            double workedHours = minutes / 60.0 - this.breakLength;
            this.sickHours = this.expectedHours - workedHours;
        }
    }

    public void assignEmployee(User employee) {
        this.employee = employee;
    }

    public void assignProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        if(project == null && employee == null){
            return "TimeTableDay{" +
                    "workdayId=" + workdayId +
                    ", employeeId=" + "null" +
                    ", date=" + date +
                    ", startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", breakLength=" + breakLength +
                    ", expectedHours=" + expectedHours +
                    ", actualHours=" + calculateActualHours() +
                    ", absenceStatus=" + absenceStatus +
                    ", holidayHours=" + holidayHours +
                    ", sickHours=" + sickHours +
                    ", projectId=" + "null" +
                    ", finalized=" + finalized +
                    '}';
        }
        else if (project == null){
            return "TimeTableDay{" +
                    "workdayId=" + workdayId +
                    ", employeeId=" + employee.getId() +
                    ", date=" + date +
                    ", startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", breakLength=" + breakLength +
                    ", expectedHours=" + expectedHours +
                    ", actualHours=" + calculateActualHours() +
                    ", absenceStatus=" + absenceStatus +
                    ", holidayHours=" + holidayHours +
                    ", sickHours=" + sickHours +
                    ", projectId=" + "null" +
                    ", finalized=" + finalized +
                    '}';
        }
        else if(employee == null){
            return "TimeTableDay{" +
                    "workdayId=" + workdayId +
                    ", employeeId=" + "null" +
                    ", date=" + date +
                    ", startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", breakLength=" + breakLength +
                    ", expectedHours=" + expectedHours +
                    ", actualHours=" + calculateActualHours() +
                    ", absenceStatus=" + absenceStatus +
                    ", holidayHours=" + holidayHours +
                    ", sickHours=" + sickHours +
                    ", projectId=" + project.getId() +
                    ", finalized=" + finalized +
                    '}';
        }
        return "TimeTableDay{" +
                "workdayId=" + workdayId +
                ", employeeId=" + employee.getId() +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", breakLength=" + breakLength +
                ", expectedHours=" + expectedHours +
                ", actualHours=" + calculateActualHours() +
                ", absenceStatus=" + absenceStatus +
                ", holidayHours=" + holidayHours +
                ", sickHours=" + sickHours +
                ", projectId=" + project.getId() +
                ", finalized=" + finalized +
                '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeTableDay)) return false;
        TimeTableDay that = (TimeTableDay) o;
        //get Ids properly in order to avoid nullpointerExceptions in case project/employee is null:
        Long thisProjectId = null;
        Long thatProjectId = null;
        Long thatEmployeeId = null;
        Long thisEmployeeId = null;

        if (this.project != null) thisProjectId = getProject().getId();
        if (that.project != null) thatProjectId = that.getProject().getId();
        if (this.employee != null) thisEmployeeId = getEmployee().getId();
        if (that.employee != null) thatEmployeeId = that.getEmployee().getId();

        return Double.compare(that.getBreakLength(), getBreakLength()) == 0 && Double.compare(that.getExpectedHours(), getExpectedHours()) == 0 && Double.compare(that.calculateActualHours(), calculateActualHours()) == 0 && Double.compare(that.getHolidayHours(), getHolidayHours()) == 0 && Double.compare(that.getSickHours(), getSickHours()) == 0 && isFinalized() == that.isFinalized() && getWorkdayId().equals(that.getWorkdayId()) && Objects.equals(thisEmployeeId, thatEmployeeId) && getDate().equals(that.getDate()) && Objects.equals(getStartTime(), that.getStartTime()) && Objects.equals(getEndTime(), that.getEndTime()) && getAbsenceStatus() == that.getAbsenceStatus() && Objects.equals(thisProjectId, thatProjectId);
    }

}

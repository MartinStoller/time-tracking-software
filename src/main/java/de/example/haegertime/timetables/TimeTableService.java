package de.example.haegertime.timetables;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.example.haegertime.advice.InvalidInputException;
import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.email.EmailService;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.management.InstanceNotFoundException;
import javax.transaction.Transactional;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class TimeTableService {

    private final TimeTableRepository ttRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final EmailService emailService;

    public List<TimeTableDay> getTimetable(){return ttRepository.findAll();}

    public TimeTableDay getTimetableDay(Long id) throws InstanceNotFoundException{
        TimeTableDay ttd = ttRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException("Day with Id " + id + " not found"));
        return ttd;
    }

    public TimeTableDay assignEmployeeToDay(Long dayId, Long employeeId) throws ItemNotFoundException {
        /**
         TimeTableDays are always created with  this.employee = null. This function takes care of the assignment of a Employee(=Foreign key).
         It´s given a dayId and EmployeeId and first finds the 2 Objects in the DB.
         Then it updates the List of Employees which are linked to an employee.
         Finally, the employee is assigned to the workday.
         */
        TimeTableDay day = ttRepository.findById(dayId).orElseThrow(() -> new ItemNotFoundException("Day with id " + dayId + " not found."));
        User user = userRepository.findById(employeeId).orElseThrow(() -> new ItemNotFoundException("Employee with id " + employeeId + " not found."));
        List<TimeTableDay> newList= user.getTimeTableDayList();
        newList.add(day);
        user.setTimeTableDayList(newList);
        day.assignUser(user);
        return ttRepository.save(day);
    }

    public TimeTableDay assignProjectToDay(Long dayId, Long projectId) throws ItemNotFoundException {
        /**
         TimeTableDays are always created with  this.project = null. This function takes care of the assignment of a Project (=Foreign key).
         It´s given a dayId and ProjectId and first finds the 2 Objects in the DB.
         Then it updates the Set of Workdays which are linked to a project.
         Finally, the Project is assigned to the workday.
        */
        TimeTableDay day = ttRepository.findById(dayId).orElseThrow(() -> new ItemNotFoundException("Day with id " + dayId + " not found."));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ItemNotFoundException("Project with id  " + projectId + " not found."));
        day.assignProject(project);
        List<TimeTableDay> newTimeTableDays = project.getTimeTableDays();
        newTimeTableDays.add(day);
        project.setTimeTableDays(newTimeTableDays);
        return ttRepository.save(day);
    }

    public List<TimeTableDay> actualHourShow(Long id) {
        return ttRepository.getTimeTableDayByEmployeeId(id);
    }


    public List<List<Double>> totalHoursAllEmployeeOnAProject(Long projectId) {
        return ttRepository.getTotalHoursAllEmployeeOnAProject(projectId);
    }

    public void registerNewTimeTable(TimeTableDay timeTableDay) {
        timeTableDay.setActualHours(timeTableDay.calculateActualHours());
        ttRepository.save(timeTableDay);
    }

    public void finalizeTimeTableDay(Long dayId) {
        TimeTableDay day = ttRepository.findById(dayId).orElseThrow(() -> new ItemNotFoundException("Dieser Arbeitstag " +
                "wurde nicht in der DB gefunden."));
        day.setFinalized(true);
        ttRepository.save(day);
    }


    public String overUnterHoursShow(Long employeeId) {
        List<List<Double>> totalHours = ttRepository.getTotalActualHoursExpectedHoursByEmployeeId(employeeId);
        double totalActualHours = totalHours.get(0).get(0);
        double totalExpectedHours = totalHours.get(0).get(1);
        double sub = totalActualHours - totalExpectedHours;
        if (sub < 0) {
            return "Unter-Stunde: "+sub;
        } else {
            return "Über-Stunde "+sub;
        }
    }

    /**
     * Change status of employee to holiday in timetable day,
     * update the actual hours and the holidays of employees
     * @param employeeId
     * @param dayId
     * @param duration
     * @return
     */
    @Transactional
    public String changeAbsenceStatusToHoliday(Long employeeId, Long dayId, double duration) {
        TimeTableDay ttd = ttRepository.findById(dayId).orElseThrow(
                () -> new ItemNotFoundException("Dieser Arbeitstag " +
                        "wurde nicht in der DB gefunden.")
        );
        if (ttd.getEmployee().getId() == employeeId ) {
            ttd.setAbsenceStatus(AbsenceStatus.valueOf("HOLIDAY"));
            User user = userRepository.findById(employeeId).get();
            if (duration <= 0) {
                throw new InvalidInputException("Der Dauer darf nicht kleiner als 0 sein");
            } else if(duration <= 4.0 && duration > 0) {
                //halber Tag Urlaub
                ttd.setHolidayHours(4.0);
                ttd.setActualHours(4.0);
                user.setUrlaubstage(user.getUrlaubstage() - 0.5); //update Urlaubstage
            } else {
                //ganz Tag Urlaub
                ttd.setHolidayHours(8.0);
                ttd.setActualHours(8.0);
                user.setUrlaubstage(user.getUrlaubstage() - 1.0); //update Urlaubstage
            }
            ttRepository.save(ttd);
            userRepository.save(user);
            emailService.send(user.getEmail(), "Beantragen akzeptiert", "Dein Beantragen wurde akzeptiert");
            return "Das AbsenceStatus wurde zu HOLIDAY gewechselt, die Urlaubstunde wurde aktualisiert";

        } else {
            throw new InvalidInputException("Die eingegebene employeeId passt nicht mit der ID von TimeTable");
        }
    }


    public String changeAbsenceStatusToSick(Long employeeId, Long dayId, double duration) {
        TimeTableDay ttd = ttRepository.findById(dayId).orElseThrow(
                () -> new ItemNotFoundException("Dieser Arbeitstag " +
                        "wurde nicht in der DB gefunden.")
        );
        if (ttd.getEmployee().getId() == employeeId) {
            ttd.setAbsenceStatus(AbsenceStatus.valueOf("SICK"));
            ttd.setActualHours(ttd.getActualHours() + duration);
            ttRepository.save(ttd);
            return "Das AbsenceStatus wurde zu SICK gewechselt, die Ist-Stunde wurde angepasst";
        } else {
            throw new InvalidInputException("Die eingegebene employeeId passt nicht mit der ID von TimeTable");
        }


    }

    public List<User> showAllEmployeesInHoliday(LocalDate date) {
        List<TimeTableDay> ttds = ttRepository.getAllEmployeesInHoliday(date);
        List<User> employees = new ArrayList<>();
        for (TimeTableDay ttd : ttds) {
            User user = ttd.getEmployee();
            employees.add(user);
        }
        return employees;
    }

    public List<User> showAllSickEmployees(LocalDate date) {
        List<TimeTableDay> ttds = ttRepository.getAllSickEmployees(date);
        List<User> employees = new ArrayList<>();
        for (TimeTableDay ttd : ttds) {
            User user = ttd.getEmployee();
            employees.add(user);
        }
        return employees;
    }


}

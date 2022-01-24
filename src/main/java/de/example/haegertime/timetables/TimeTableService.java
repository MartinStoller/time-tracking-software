package de.example.haegertime.timetables;

import de.example.haegertime.advice.InvalidInputException;
import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.email.EmailService;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        /*
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
        day.assignEmployee(user);
        return ttRepository.save(day);
    }

    public TimeTableDay assignProjectToDay(Long dayId, Long projectId) throws ItemNotFoundException {
        /*
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

    public List<TimeTableDay> getTimeTableDayByEmployeeId(Long id) {
        return ttRepository.getTimeTableDayByEmployeeId(id);
    }


    public List<List<Double>> getTotalWorkingHoursOnAProjectGroupedByEmployeeId(Long projectId) {
        return ttRepository.getTotalWorkingHoursOnAProjectGroupedByEmployeeId(projectId);

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


    public double showOverTimeOfEmployeeById(Long employeeId) {
        List<List<Double>> totalActualAndExpectedHours = ttRepository.getTotalActualHoursExpectedHoursByEmployeeId(employeeId);
        double totalActualHours = totalActualAndExpectedHours.get(0).get(0);
        double totalExpectedHours = totalActualAndExpectedHours.get(0).get(1);
        double overTime = totalActualHours - totalExpectedHours;
        return overTime;
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
    public void changeAbsenceStatusToHoliday(Long employeeId, Long dayId, double duration) {
        TimeTableDay ttd = ttRepository.findById(dayId).orElseThrow(
                () -> new ItemNotFoundException("Dieser Arbeitstag " +
                        "wurde nicht in der DB gefunden.")
        );
        if (Objects.equals(ttd.getEmployee().getId(), employeeId)) {
            ttd.setAbsenceStatus(AbsenceStatus.valueOf("HOLIDAY"));
            User user = userRepository.findById(employeeId).orElseThrow(
                    () -> new ItemNotFoundException("Der Employee mit Id "+employeeId+
                            " wurde nicht gefunden")
            );
            double actualHours = setActualHour(duration);
            ttd.setActualHours(actualHours);
            ttd.setHolidayHours(actualHours);
            if (actualHours<=4){
                user.setUrlaubstage(user.getUrlaubstage()-0.5);
            } else {
                user.setUrlaubstage(user.getUrlaubstage()-1.0);
            }
            ttRepository.save(ttd);
            userRepository.save(user);
            emailService.send(user.getEmail(), "Beantragen akzeptiert", "Dein Beantragen wurde akzeptiert");
        } else {
            throw new InvalidInputException("Die eingegebene employeeId passt nicht mit der ID von TimeTable");
        }
    }


    public void changeAbsenceStatusToSick(Long employeeId, Long dayId, double duration) {
        TimeTableDay ttd = ttRepository.findById(dayId).orElseThrow(
                () -> new ItemNotFoundException("Dieser Arbeitstag " +
                        "wurde nicht in der DB gefunden.")
        );
        if (Objects.equals(ttd.getEmployee().getId(), employeeId)) {
            ttd.setAbsenceStatus(AbsenceStatus.valueOf("SICK"));
            ttd.setActualHours(ttd.getActualHours() + duration);
            ttRepository.save(ttd);
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



    private double setActualHour(double duration) {
        if(duration <= 0) {
            throw new InvalidInputException("Der Dauer darf nicht kleiner oder gleich 0 sein");
        } else if (duration <= 4) {
            return 4;
        } else {
            return 8;
        }
    }


}

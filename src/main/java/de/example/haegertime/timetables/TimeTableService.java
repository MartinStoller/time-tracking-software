package de.example.haegertime.timetables;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class TimeTableService {

    private final TimeTableRepository ttRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    public List<TimeTableDay> getEntireTimetable(){return ttRepository.findAll();}

    public TimeTableDay getTimetableDay(Long id) throws InstanceNotFoundException{
        TimeTableDay ttd = ttRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException("Day with Id " + id + " not found"));
        return ttd;
    }

    public void assignEmployeeToDay(Long dayId, Long employeeId) throws ItemNotFoundException {
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
    }

    public void assignProjectToDay(Long dayId, Long projectId) throws ItemNotFoundException {
        /**
         TimeTableDays are always created with  this.project = null. This function takes care of the assignment of a Project (=Foreign key).
         It´s given a dayId and ProjectId and first finds the 2 Objects in the DB.
         Then it updates the Set of Workdays which are linked to a project.
         Finally, the Project is assigned to the workday.
         */
        TimeTableDay day = ttRepository.findById(dayId).orElseThrow(() -> new ItemNotFoundException("Day with id " + dayId + " not found."));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ItemNotFoundException("Project with id  " + projectId + " not found."));
        day.assignProject(project);
        Set<TimeTableDay> newTimeTableDays = project.getTimeTableDays();
        newTimeTableDays.add(day);
        project.setTimeTableDays(newTimeTableDays);
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



    public String overUnterHoursShow(Long employeeId) {
        List<List<Double>> totalHours = ttRepository
                .getTotalActualHoursExpectedHoursByEmployeeId(employeeId);
        double sumActualHours = totalHours.get(0).get(0);
        double sumExpectedHours = totalHours.get(0).get(1);
        double hours = sumActualHours - sumExpectedHours;
        if(hours < 0) {
            return ("Unter-Stunde: "+Math.abs(hours));
        } else {
            return ("Über-Stunde: "+hours);
        }
    }

}

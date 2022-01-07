package de.example.haegertime.timetables;

import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import de.example.haegertime.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
public class TimeTableService {

    private final TimeTableRepository ttRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TimeTableService(TimeTableRepository ttr, UserRepository usr, ProjectRepository pr) {
        this.ttRepository = ttr;
        this.userRepository = usr;
        this.projectRepository = pr;
    }

    public List<TimeTableDay> getEntireTimetable(){return ttRepository.findAll();}

    public TimeTableDay getTimetableDay(Long id) throws InstanceNotFoundException{
        TimeTableDay ttd = ttRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException("Day with Id " + id + " not found"));
        return ttd;
    }

    public TimeTableDay assignEmployeeToDay(Long dayId, Long employeeId) throws ItemNotFoundException {
        TimeTableDay day = ttRepository.findById(dayId).orElseThrow(() -> new ItemNotFoundException("Day with id " + dayId + " not found."));
        User user = userRepository.findById(employeeId).orElseThrow(() -> new ItemNotFoundException("Employee with id " + employeeId + " not found."));;
        day.assignUser(user);
        return ttRepository.save(day);
    }

    public TimeTableDay assignProjectToDay(Long dayId, Long projectId) throws ItemNotFoundException {
        TimeTableDay day = ttRepository.findById(dayId).orElseThrow(() -> new ItemNotFoundException("Day with id " + dayId + " not found."));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ItemNotFoundException("Project with id  " + projectId + " not found."));;
        day.assignProject(project);
        return ttRepository.save(day);
    }
}

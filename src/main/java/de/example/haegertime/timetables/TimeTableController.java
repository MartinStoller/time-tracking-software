package de.example.haegertime.timetables;

import de.example.haegertime.advice.ItemNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path="api/timetable")
public class TimeTableController {

    private final TimeTableService ttService;

    public TimeTableController(TimeTableService ttService){this.ttService = ttService;}

    @GetMapping
    public List<TimeTableDay> getEntireTimetable(){return ttService.getEntireTimetable();}

    @GetMapping("/{id}")
    public TimeTableDay getTimetableDay(@PathVariable("id") Long id) throws InstanceNotFoundException {
        return ttService.getTimetableDay(id);
    }

    @PutMapping("/assignEmployee{employeeId}/toDay{dayId}")
    public void assignEmployeeToDay(@PathVariable Long dayId, @PathVariable Long employeeId) throws ItemNotFoundException {
        ttService.assignEmployeeToDay(dayId, employeeId);
    }

    @PutMapping("/assignProject{projectId}/toDay{dayId}")
    public void assignProjectToDay(@PathVariable Long projectId, @PathVariable Long dayId) throws ItemNotFoundException {
        ttService.assignProjectToDay(dayId, projectId);
    }
}

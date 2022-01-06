package de.example.haegertime.timetables;

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

    @PutMapping("/assignDay/{dayId}/toEmployee/{employeeId}")
    public void assignDayToEmployee(@PathVariable Long dayId, @PathVariable Long employeeId) throws InstanceNotFoundException {
        ttService.assignDayToEmployee(dayId, employeeId);
    }
}

package de.example.haegertime.timetables;

import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.*;

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
/*
    @PutMapping("/assignDay/{dayId}/toEmployee/{employeeId}")
    public void assignDayToEmployee(@PathVariable Long dayId, @PathVariable Long employeeId) throws InstanceNotFoundException {
        ttService.assignDayToEmployee(dayId, employeeId);
    }

 */

    // TODO: return only actual hours with dates, the result should be sorted according to dates
    @GetMapping("/actualhours/{id}")
    public List<TimeTableDay> actualHourShow(@PathVariable("id") Long id){
        return ttService.actualHourShow(id);
    }

}

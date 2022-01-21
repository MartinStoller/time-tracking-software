package de.example.haegertime.timetables;

import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.users.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/assignDay/{dayId}/toEmployee/{employeeId}")
    public void assignDayToEmployee(@PathVariable Long dayId, @PathVariable Long employeeId) throws InstanceNotFoundException {
        ttService.assignEmployeeToDay(dayId, employeeId);
    }


    // TODO: return only actual hours with dates, the result should be sorted according to dates
    @GetMapping("/actualhours/{id}")
    public List<TimeTableDay> actualHourShow(@PathVariable("id") Long id,
                                             @RequestParam(required = false) String startDate,
                                             @RequestParam(required = false) String endDate)
    {
        return ttService.getTimeTableDayByEmployeeId(id);
    }

    @GetMapping("/hours/employees/{id}")
    public List<List<Double>> totalHoursEmployeeShow(@PathVariable("id") Long projectId) {
        return ttService.getTotalWorkingHoursOnAProjectGroupedByEmployeeId(projectId);
    }


    @PutMapping("/assignEmployee/{employeeId}/toDay/{dayId}")

    public void assignEmployeeToDay(@PathVariable Long dayId, @PathVariable Long employeeId) throws ItemNotFoundException {
        ttService.assignEmployeeToDay(dayId, employeeId);
    }

    @PutMapping("/assignProject/{projectId}/toDay/{dayId}")
    public void assignProjectToDay(@PathVariable Long projectId, @PathVariable Long dayId) throws ItemNotFoundException {
        ttService.assignProjectToDay(dayId, projectId);
    }

    @PostMapping
    public ResponseEntity<?> registerNewTimeTable(@RequestBody TimeTableDay timeTableDay) {
        ttService.registerNewTimeTable(timeTableDay);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/finalize/{dayId}")
    public void  finalizeTimeTableDay(@PathVariable Long dayId) throws ItemNotFoundException {
        ttService.finalizeTimeTableDay(dayId);
    }


    @GetMapping("/overhours/{id}")
    public double showOverTimeOfEmployeeById(@PathVariable("id") Long employeeId) {
        return ttService.showOverTimeOfEmployeeById(employeeId);
    }

    @PutMapping("/absence/holiday/employee/{id}")
    public void changeAbsenceStatusToHoliday(@PathVariable("id") Long employeeId,
                                               @RequestParam Long dayId,@RequestParam Double duration) {
        ttService.changeAbsenceStatusToHoliday(employeeId, dayId, duration);
    }

    @PutMapping("/absence/sick/employee/{id}")
    public void changeAbsenceStatusToSick(@PathVariable("id") Long employeeId,
                                            @RequestParam Long dayId,
                                            @RequestParam double duration) {
        ttService.changeAbsenceStatusToSick(employeeId, dayId, duration);
    }

    @GetMapping("/employees/status/holiday")
    public List<User> showAllEmployeesInHoliday(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date) {
        return ttService.showAllEmployeesInHoliday(date);
    }

    @GetMapping("/employees/status/sick")
    public List<User> showAllSickEmployees(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date) {
        return ttService.showAllSickEmployees(date);
    }
}


package de.example.haegertime.timetables;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TimeTableService {

    private final TimeTableRepository ttRepository;

    @Autowired
    public TimeTableService(TimeTableRepository ttr) {
        this.ttRepository = ttr;

    }

    public List<TimeTableDay> getEntireTimetable(){return ttRepository.findAll();}

    public TimeTableDay getTimetableDay(Long id) throws InstanceNotFoundException{
        TimeTableDay ttd = ttRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException("Day with Id " + id + " not found"));
        return ttd;
    }
    /*
    public TimeTableDay assignDayToEmployee(Long dayId, Long employeeId) throws InstanceNotFoundException {
        TimeTableDay day = ttRepository.findById(dayId).get();
        User user = userRepository.findById(employeeId).get();
        day.assignUser(user);
        return ttRepository.save(day);
    }
    */

    public List<TimeTableDay> actualHourShow(Long id) {
        return ttRepository.getTimeTableDayByEmployeeId(id);
    }


    public List<List<Double>> totalHoursAllEmployeeOnAProject(Long projectId) {
        return ttRepository.getTotalHoursAllEmployeeOnAProject(projectId);
    }

}

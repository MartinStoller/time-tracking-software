package de.example.haegertime.timetables;

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

    public TimeTableService(TimeTableRepository ttr, UserRepository usr) {
        this.ttRepository = ttr;
        this.userRepository = usr;
    }

    public List<TimeTableDay> getEntireTimetable(){return ttRepository.findAll();}

    public TimeTableDay getTimetableDay(Long id) throws InstanceNotFoundException{
        TimeTableDay ttd = ttRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException("Day with Id " + id + " not found"));
        return ttd;
    }

    public TimeTableDay assignDayToEmployee(Long dayId, Long employeeId) throws InstanceNotFoundException {
        TimeTableDay day = ttRepository.findById(dayId).get();
        User user = userRepository.findById(employeeId).get();
        day.assignUser(user);
        return ttRepository.save(day);
    }
}

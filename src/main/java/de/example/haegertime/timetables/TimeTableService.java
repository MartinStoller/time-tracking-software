package de.example.haegertime.timetables;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeTableService {
    private final TimeTableRepository ttRepository;

    public TimeTableService(TimeTableRepository ttr) {this.ttRepository = ttr;}

    public List<TimeTableDay> getEntireTimetable(){return ttRepository.findAll();}
}

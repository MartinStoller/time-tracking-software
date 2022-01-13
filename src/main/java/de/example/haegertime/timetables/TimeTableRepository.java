package de.example.haegertime.timetables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTableDay, Long> {



    @Query("SELECT s FROM TimeTableDay s INNER JOIN User u ON u.id = s.employee.id WHERE u.id = ?1")
    List<TimeTableDay> getTimeTableDayByEmployeeId(Long id);

    @Query("SELECT SUM(s.actualHours), s.employee.id FROM TimeTableDay s WHERE s.projectId = ?1" +
            " GROUP BY s.employee.id")
    List<List<Double>> getTotalHoursAllEmployeeOnAProject(Long id);

    @Query("SELECT SUM(s.actualHours), SUM(s.expectedHours) FROM TimeTableDay s WHERE s.employee.id = ?1")
    List<List<Double>> getTotalActualHoursExpectedHoursByEmployeeId(Long employeeId);


}

package de.example.haegertime.timetables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTableDay, Long> {

    @Query("SELECT s FROM TimeTableDay s INNER JOIN User u ON u.id = s.employee.id WHERE u.id = ?1")
    List<TimeTableDay> getTimeTableDayByEmployeeId(Long id);

    @Query("SELECT SUM(s.actualHours), s.employee.id FROM TimeTableDay s WHERE s.project.id = ?1" +
            " GROUP BY s.employee.id")
    List<List<Double>> getTotalWorkingHoursOnAProjectGroupedByEmployeeId(Long id);

    @Query("SELECT SUM(s.actualHours), SUM(s.expectedHours) FROM TimeTableDay s WHERE s.employee.id = ?1")
    List<List<Double>> getTotalActualHoursExpectedHoursByEmployeeId(Long employeeId);

    @Query("SELECT t FROM TimeTableDay t WHERE t.date = ?1 AND t.absenceStatus = 1")
    List<TimeTableDay> getAllEmployeesInHoliday(LocalDate date);

    @Query("SELECT t FROM TimeTableDay t WHERE t.date = ?1 AND t.absenceStatus = 0")
    List<TimeTableDay> getAllSickEmployees(LocalDate date);
}

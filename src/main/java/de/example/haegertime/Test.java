package de.example.haegertime;

import de.example.haegertime.timetables.DatesAndDurationsCalculator;
import de.example.haegertime.timetables.TimeTableDay;
import de.example.haegertime.users.Role;
import de.example.haegertime.users.User;
import org.joda.time.Hours;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class Test {
    public static void main(String[] args) throws ParseException {
        Duration timeAtWork = DatesAndDurationsCalculator.getDurationBetweenLocalTimes(LocalTime.of(7,30), LocalTime.of(10,15));
        System.out.println(timeAtWork);
        Long minutes = timeAtWork.toMinutes();
        System.out.println(minutes);
        double actualHours = minutes/60.0;
        System.out.println(actualHours);


    }
}

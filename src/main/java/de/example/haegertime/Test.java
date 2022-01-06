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
        TimeTableDay ttd1 = new TimeTableDay(
                1L, LocalDate.of(2022, Month.JANUARY, 1), null,
                null, Duration.parse("PT0H0M"), Duration.parse("PT0H0M"), null, 1L);
        System.out.println(ttd1.toString());
    }
}

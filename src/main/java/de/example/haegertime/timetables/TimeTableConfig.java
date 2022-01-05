package de.example.haegertime.timetables;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@Configuration
public class TimeTableConfig {
    /*
    @Bean
    CommandLineRunner clrTimeTable(TimeTableRepository repository){
        return args -> {
            TimeTableDay ttd1 = new TimeTableDay(
                    1L, LocalDate.of(2022, Month.JANUARY, 1), null,
                    null, Duration.parse("PT0H0M"), 0, 0, null, 1L);
            TimeTableDay ttd2 = new TimeTableDay(
                    1L, LocalDate.of(2022, Month.JANUARY, 2), null,
                    null, Duration.parse("PT0H0M"), 0, 0, null, 1L);
            TimeTableDay ttd3 = new TimeTableDay(
                    1L, LocalDate.of(2022, Month.JANUARY, 3), LocalTime.of(8, 15),
                    LocalTime.of(17, 30), Duration.parse("PT0H45M"), 8.0f, 95.0f, null, 1L);
            TimeTableDay ttd4 = new TimeTableDay(
                    1L, LocalDate.of(2022, Month.JANUARY, 4), LocalTime.of(8, 0),
                    null, Duration.parse("PT1H0M"), 8.0f, 99.0f, null, 3L);
            TimeTableDay ttd5 = new TimeTableDay(
                    1L, LocalDate.of(2022, Month.JANUARY, 5), LocalTime.of(7, 50),
                    null, Duration.parse("PT1H15M"), 8.0f, 98.0f, null, 1L);
            TimeTableDay ttd6 = new TimeTableDay(5L, LocalDate.of(2022, Month.JANUARY, 1), null,
                    null, Duration.parse("PT0H0M"), 0, 0, null, 2L);
            TimeTableDay ttd7 = new TimeTableDay(
                    5L, LocalDate.of(2022, Month.JANUARY, 2), null,
                    null, Duration.parse("PT0H0M"), 0, 0, null, 2L);
            TimeTableDay ttd8 = new TimeTableDay(
                    5L, LocalDate.of(2022, Month.JANUARY, 3), null,
                    null, Duration.parse("PT0H0M"), 0, 0, AbsenceStatus.SICK, 2L);
            TimeTableDay ttd9 = new TimeTableDay(
                    5L, LocalDate.of(2022, Month.JANUARY, 4), null,
                    null, Duration.parse("PT0H0M"), 0, 0, AbsenceStatus.HOLIDAY, 2L);
            TimeTableDay ttd10 = new TimeTableDay(
                    5L, LocalDate.of(2022, Month.JANUARY, 5), null,
                    null, Duration.parse("PT0H0M"), 0, 0, AbsenceStatus.HOLIDAY, 2L);
        repository.saveAll(List.of(ttd1, ttd2, ttd3, ttd4, ttd5, ttd6, ttd7, ttd8, ttd9, ttd10));
        };
        //TODO: FInd a way to automatically calculate actual hours
        //TODO: SIck days still have normal working hours for correct billing

    }
    */

}

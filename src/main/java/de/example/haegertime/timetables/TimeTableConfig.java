package de.example.haegertime.timetables;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@Configuration
public class TimeTableConfig {
    @Bean
    CommandLineRunner clrTimeTable(TimeTableRepository repository){
        return args -> {
            TimeTableDay ttd1 = new TimeTableDay(
                    1L, LocalDate.of(2022, Month.JANUARY, 1), null,
                    null, Duration.parse("PT0H0M"), Duration.parse("PT0H0M"), null, 1L);
            TimeTableDay ttd2 = new TimeTableDay(
                    1L, LocalDate.of(2022, Month.JANUARY, 2), null,
                    null, Duration.parse("PT0H0M"), Duration.parse("PT0H0M"), null, 1L);
            TimeTableDay ttd3 = new TimeTableDay(
                    1L, LocalDate.of(2022, Month.JANUARY, 3), LocalTime.of(8, 15),
                    LocalTime.of(17, 30), Duration.parse("PT0H45M"), Duration.parse("PT8H0M"), null, 1L);
            TimeTableDay ttd4 = new TimeTableDay(
                    1L, LocalDate.of(2022, Month.JANUARY, 4), LocalTime.of(8, 0),
                    LocalTime.of(18, 15), Duration.parse("PT1H0M"), Duration.parse("PT1H10M"), null, 3L);
            TimeTableDay ttd5 = new TimeTableDay(
                    1L, LocalDate.of(2022, Month.JANUARY, 5), LocalTime.of(7, 50),
                    LocalTime.of(16, 50), Duration.parse("PT1H15M"), Duration.parse("PT0H0M"), null, 1L);
            TimeTableDay ttd6 = new TimeTableDay(5L, LocalDate.of(2022, Month.JANUARY, 1), null,
                    null, Duration.parse("PT0H0M"), Duration.parse("PT0H0M"), null, 2L);
            TimeTableDay ttd7 = new TimeTableDay(
                    5L, LocalDate.of(2022, Month.JANUARY, 2), null,
                    null, Duration.parse("PT0H0M"), Duration.parse("PT0H0M"), null, 2L);
            TimeTableDay ttd8 = new TimeTableDay(
                    5L, LocalDate.of(2022, Month.JANUARY, 3), null,
                    null, Duration.parse("PT0H0M"), Duration.parse("PT7H00M"), AbsenceStatus.SICK, 2L);
            TimeTableDay ttd9 = new TimeTableDay(
                    5L, LocalDate.of(2022, Month.JANUARY, 4), null,
                    null, Duration.parse("PT0H0M"), Duration.parse("PT8H00M"), AbsenceStatus.HOLIDAY, 2L);
            TimeTableDay ttd10 = new TimeTableDay(
                    5L, LocalDate.of(2022, Month.JANUARY, 5), null,
                    null, Duration.parse("PT0H0M"), Duration.parse("PT8H00M"), AbsenceStatus.HOLIDAY, 2L);
        repository.saveAll(List.of(ttd1, ttd2, ttd3, ttd4, ttd5, ttd6, ttd7, ttd8, ttd9, ttd10));
        };
    }
}

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
                    LocalTime.of(17, 30), Duration.parse("PT0H45M"), 0, 0, null, 1L);
            TimeTableDay ttd4 = new TimeTableDay(
                    1L, LocalDate.of(2022, Month.JANUARY, 4), LocalTime.of(8, 0),
                    null, Duration.parse("PT1H0M"), 0, 0, null, 1L);
        repository.saveAll(List.of(ttd1, ttd2, ttd3, ttd4));
        };

    }
}

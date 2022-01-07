package de.example.haegertime.timetables;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@Configuration
public class TimeTableConfig {

/*    @Bean
    CommandLineRunner clrTimeTable(TimeTableRepository repository){
        return args -> {
            TimeTableDay ttd1 = new TimeTableDay(
                    LocalDate.of(2022, Month.JANUARY, 1), null, null,
                    null, 0, 0, null, null);
            TimeTableDay ttd2 = new TimeTableDay(
                    LocalDate.of(2022, Month.JANUARY, 2), null,null,
                    null, 0, 0, null, null);
            TimeTableDay ttd3 = new TimeTableDay(
                    LocalDate.of(2022, Month.JANUARY, 3), null, LocalTime.of(8, 15),
                    LocalTime.of(17, 30), 0.75, 8, null, null);
            TimeTableDay ttd4 = new TimeTableDay(
                    LocalDate.of(2022, Month.JANUARY, 4), null, LocalTime.of(8, 0),
                    LocalTime.of(18, 15), 1, 8, null, null);
            TimeTableDay ttd5 = new TimeTableDay(
                    LocalDate.of(2022, Month.JANUARY, 5), null, LocalTime.of(7, 30),
                    LocalTime.of(13, 50), 1.2, 8, null, null);
            TimeTableDay ttd6 = new TimeTableDay(LocalDate.of(2022, Month.JANUARY, 1), null, null,
                    null, 0, 0, null, null);
            TimeTableDay ttd7 = new TimeTableDay(
                    LocalDate.of(2022, Month.JANUARY, 2), null, null,
                    null, 0, 0, null, null);
            TimeTableDay ttd8 = new TimeTableDay(
                    LocalDate.of(2022, Month.JANUARY, 3), null, null,
                    null, 0, 4, AbsenceStatus.SICK, null);
            TimeTableDay ttd9 = new TimeTableDay(
                    LocalDate.of(2022, Month.JANUARY, 4), null, LocalTime.of(7, 50),
                    LocalTime.of(13, 20), 0.75, 8, AbsenceStatus.HOLIDAY, null);
            TimeTableDay ttd10 = new TimeTableDay(
                    LocalDate.of(2022, Month.JANUARY, 5), null, null,
                    null, 0, 6, AbsenceStatus.HOLIDAY, null);
        repository.saveAll(List.of(ttd1, ttd2, ttd3, ttd4, ttd5, ttd6, ttd7, ttd8, ttd9, ttd10));
        };
    }
    */
}

package de.example.haegertime.timetables;

import de.example.haegertime.timetables.AbsenceStatus;
import de.example.haegertime.timetables.TimeTableDay;
import de.example.haegertime.timetables.TimeTableRepository;
import de.example.haegertime.users.Role;
import de.example.haegertime.users.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@Component
public class TestDataTimeTable {
//    @Bean
//    CommandLineRunner getTestDataTimeTable(TimeTableRepository repository){
//        return args -> {
//            TimeTableDay ttd1 = new TimeTableDay(
//                    LocalDate.of(2022, Month.JANUARY, 1), null,
//                    null, 0, 0, null);
//            TimeTableDay ttd2 = new TimeTableDay(
//                    LocalDate.of(2022, Month.JANUARY, 2),null,
//                    null, 0, 0, null);
//            TimeTableDay ttd3 = new TimeTableDay(
//                    LocalDate.of(2022, Month.JANUARY, 3),  LocalTime.of(8, 15),
//                    LocalTime.of(17, 30), 0.75, 8, null);
//            TimeTableDay ttd4 = new TimeTableDay(
//                    LocalDate.of(2022, Month.JANUARY, 4), LocalTime.of(8, 0),
//                    LocalTime.of(18, 15), 1, 8, null);
//            TimeTableDay ttd5 = new TimeTableDay(
//                    LocalDate.of(2022, Month.JANUARY, 5), LocalTime.of(7, 30),
//                    LocalTime.of(13, 50), 1.2, 8, null);
//            TimeTableDay ttd6 = new TimeTableDay(LocalDate.of(2022, Month.JANUARY, 1), null,
//                    null, 0, 0, null);
//            TimeTableDay ttd7 = new TimeTableDay(
//                    LocalDate.of(2022, Month.JANUARY, 2), null,
//                    null, 0, 0, null);
//            TimeTableDay ttd8 = new TimeTableDay(
//                    LocalDate.of(2022, Month.JANUARY, 3), null,
//                    null, 0, 4, AbsenceStatus.SICK);
//            TimeTableDay ttd9 = new TimeTableDay(
//                    LocalDate.of(2022, Month.JANUARY, 4), LocalTime.of(7, 50),
//                    LocalTime.of(13, 20), 0.75, 8, AbsenceStatus.HOLIDAY);
//            TimeTableDay ttd10 = new TimeTableDay(
//                    LocalDate.of(2022, Month.JANUARY, 5), null,
//                    null, 0, 6, AbsenceStatus.HOLIDAY);
//
//            repository.saveAll(List.of(ttd1, ttd2, ttd3, ttd4, ttd5, ttd6, ttd7, ttd8, ttd9, ttd10));
//        };
//    }
}

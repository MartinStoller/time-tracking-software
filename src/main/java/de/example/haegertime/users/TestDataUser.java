package de.example.haegertime.users;

import de.example.haegertime.timetables.AbsenceStatus;
import de.example.haegertime.timetables.TimeTableDay;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestDataUser {
    @Bean
    CommandLineRunner getTestDataUsers(UserRepository userRepository){
        return  args -> {
            User usr1 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller3@gmx.de", Role.EMPLOYEE);
            User usr2 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);
            User usr3 = new User("Nick", "Petersen", "alarm1", "stoller.martin555@gmx.de", Role.ADMIN);
            User usr4 = new User("Albert", "Gartenzwerg", "alarm1", "gartenzwergl@gmail.com", Role.ADMIN);
            User usr5 = new User("Admiral", "Schneider", "fussball95", "spamfilter@gmail.com", Role.EMPLOYEE);
            User usr6 = new User("Franziska", "Frankenstein", "123Bergsteiger", "frafra@gmx.de", Role.BOOKKEEPER);
            User usr7 = new User("Waldo", "Holzkopf", "redbull!", "holzkopf@gmx.net", Role.EMPLOYEE);
            User usr8 = new User("Waldo", "Holzkopf", "redbull!", "holzkopfer@gmx.net", Role.EMPLOYEE);
            TimeTableDay ttd8 = new TimeTableDay(
                    LocalDate.of(2022, Month.JANUARY, 3), null,
                    null, 0, 4, AbsenceStatus.SICK);
            TimeTableDay ttd9 = new TimeTableDay(
                    LocalDate.of(2022, Month.JANUARY, 4), LocalTime.of(7, 50),
                    LocalTime.of(13, 20), 0.75, 8, AbsenceStatus.HOLIDAY);
            List<TimeTableDay> ttds = new ArrayList<>();
            ttds.add(ttd8);
            ttds.add(ttd9);
            usr8.setTimeTableDayList(ttds);
            userRepository.saveAll(List.of(usr1, usr2, usr3, usr4, usr5, usr6, usr7, usr8));
        };
    }
}

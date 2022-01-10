package de.example.haegertime.users;

import de.example.haegertime.timetables.TimeTableDay;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class UserConfig {
    /*
   @Bean
    CommandLineRunner clrUsers(UserRepository userRepository) {
       return args -> {
           User usr1 = new User("Anton", "Aus Tirol", "1234567", "anton.austirol@gmx.de", Role.EMPLOYEE);
           User usr2 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);
           User usr3 = new User("Nick", "Petersen", "alarm1", "nickelberry@gmail.com", Role.ADMIN);
           TimeTableDay ttd1 = new TimeTableDay(LocalDate.of(2022, Month.JANUARY, 3),
                   LocalTime.of(8, 15), LocalTime.of(17, 30),
                   0.75, 8, null, 1L);

           List<TimeTableDay> timeTableDaySet = new ArrayList<>();
           timeTableDaySet.add(ttd1);
           usr1.setTimeTableDayList(timeTableDaySet);


           //User usr4 = new User("Albert", "Gartenzwerg", "alarm1", "gartenzwergl@gmail.com", Role.ADMIN);
           //User usr5 = new User("Admiral", "Schneider", "fussball95", "spamfilter@gmail.com", Role.EMPLOYEE);
           //User usr6 = new User("Franziska", "Frankenstein", "123Bergsteiger", "frafra@gmx.de", Role.BOOKKEEPER);
           //User usr7 = new User("Waldo", "Holzkopf", "redbull!", "holzkopf@gmx.net", Role.EMPLOYEE);
           userRepository.saveAll(List.of(usr1, usr2, usr3));
       };

   } */
}






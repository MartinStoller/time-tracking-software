package de.example.haegertime.timetables;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;

@Configuration
public class TimeTableConfig {
    @Bean
    CommandLineRunner clrTimeTable(TimeTableRepository repository){
        return args -> {
            TimeTableDay ttd1 = new TimeTableDay(1L, LocalDate.of(2022, Month.JANUARY, 1), T)
        }
    }
}

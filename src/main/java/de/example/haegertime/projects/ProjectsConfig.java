package de.example.haegertime.projects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Configuration
public class ProjectsConfig {
    @Bean
    CommandLineRunner clr_projects(ProjectRepository repository) {
        return args -> {
            Project proj1 = new Project("title 1", LocalDate.of(1995, Month.JULY, 22), LocalDate.of(2021, Month.AUGUST, 2), "BMW", "bmwler Straße 22");
            Project proj2 = new Project("blabla", LocalDate.of(1992, Month.JULY, 13), null, "VW", "Vwler Straße 21");;
            repository.saveAll(List.of(proj1, proj2));
        };
    }
}

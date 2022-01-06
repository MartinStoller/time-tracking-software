package de.example.haegertime.projects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class ProjectsConfig {
    /*
    @Bean
    CommandLineRunner clrProjects(ProjectRepository repository) {
        return args -> {
            Project proj1 = new Project("Homepage Maintenance BMW", LocalDate.of(1995, Month.JULY, 22), LocalDate.of(2021, Month.AUGUST, 2), "BMW AG", "80375 München, BlaBlaStraße 4");
            Project proj2 = new Project("WebAppXY Extension SAP", LocalDate.of(1992, Month.JULY, 13), null, "VW", "84034 Landshut, Franz-Marc-Str. 18");
            Project proj3 = new Project("Building the next Google", LocalDate.of(2005, Month.JULY, 23), null, "Startup3000", "50823 Köln, Liebigstraße 155");
            Project proj4 = new Project("Cloud Computing project 500", LocalDate.of(2020, Month.JANUARY, 3), LocalDate.of(2021, Month.DECEMBER, 1), "Deutsche Bahn", "12053 Berlin, Berlinerstraße 12");
            Project proj5 = new Project("Mission: Building the next Google", LocalDate.of(1992, Month.JULY, 13), LocalDate.of(2009, Month.JUNE, 12), "Nick&Marty AG", "19544 Schleswig, Nickelpeterstraße 2");
            Project proj6 = new Project("Top Secret Spy Stuff", LocalDate.of(1992, Month.JULY, 13), null, "Baidu", "China, 59422145 Wuh Han, Chinesenweg 3");
            Project proj7 = new Project("Bohrmaschinenautomatisierungsprojekt XY", LocalDate.of(1992, Month.JULY, 13), null, "Hilti", "Österreich, 20533 Bludenz, Am Ösieck 122");
            repository.saveAll(List.of(proj1, proj2, proj3, proj4, proj5, proj6, proj7));
        };
    }
    */

}

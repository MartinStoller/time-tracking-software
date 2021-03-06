package de.example.haegertime.customer;

import de.example.haegertime.projects.Project;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestDataCustomer {

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            Customer c1 = new Customer("AXA", "Hansa Alle 39, 44444 Düsseldorf");
            Customer c2 = new Customer("Techniker Krankenkasse", "Hansa Alle 50, 44444 Düsseldorf");
            Customer c3 = new Customer("AOK Krankenkasse", "Hansa Alle 60, 44444 Düsseldorf");
            Customer c4 = new Customer("DAK Krankenkasse", "Hansa Alle 70, 44444 Düsseldorf");
            Customer c5 = new Customer("Allianz", "Hanse Alle 80, 44444 Düsseldorf");

            Project project1 = new Project("Homepage AXA", LocalDate.of(2020, Month.AUGUST,30),
                    LocalDate.of(2021, Month.JANUARY, 15));
            Project project15 = new Project("Homepage AXA New Gen", LocalDate.of(2020, Month.AUGUST,30),
                    LocalDate.of(2021, Month.JANUARY, 15));
            List<Project> projects = new ArrayList<>();
            projects.add(project1);
            projects.add(project15);
            c1.setProjects(projects);

            Project project2 = new Project("Auto Versicherung", LocalDate.of(2021, Month.JANUARY,30),
                    LocalDate.of(2021, Month.AUGUST, 15));
            projects = new ArrayList<>();
            projects.add(project2);
            c2.setProjects(projects);

            Project project3 = new Project("AOK Kundenservice", LocalDate.of(2019, Month.SEPTEMBER,30),
                    LocalDate.of(2021, Month.FEBRUARY, 15));
            projects = new ArrayList<>();
            projects.add(project3);
            c3.setProjects(projects);

            Project project4 = new Project("Homepage", LocalDate.of(2020, Month.MARCH,30),
                    LocalDate.of(2021, Month.APRIL, 30));
            projects = new ArrayList<>();
            projects.add(project4);
            c4.setProjects(projects);

            customerRepository.saveAll(List.of(c1,c2,c3,c4,c5));
        };
    }

}

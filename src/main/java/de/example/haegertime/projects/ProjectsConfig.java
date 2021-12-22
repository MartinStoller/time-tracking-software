package de.example.haegertime.projects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProjectsConfig {
    @Bean
    CommandLineRunner clr_projects(ProjectRepository repository) {
        return args -> {
            Project proj1 = new Project(111L, "title 1");
            Project proj2 = new Project(999L, "la li li  lu");
            repository.saveAll(List.of(proj1, proj2));
        };
    }
}

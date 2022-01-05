package de.example.haegertime.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT n FROM Project n WHERE n.title = ?1")
    Optional<Project> findProjectByName(String title);
}

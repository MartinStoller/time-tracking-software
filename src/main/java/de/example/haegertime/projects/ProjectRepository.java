package de.example.haegertime.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT n FROM Project n WHERE n.title = ?1")
    Optional<Project> findProjectByName(String title);

    @Query("SELECT p FROM Project p WHERE p.customer.id = ?1 AND p.id = ?2")
    Optional<Project> existsProjectByIdAndCustomerID(Long customerId, Long projectId);
}

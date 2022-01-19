package de.example.haegertime.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findProjectByTitle(String title);


    @Query("SELECT p FROM Project p WHERE p.customer.id = ?1 AND p.id = ?2")
    Optional<Project> existsProjectByIdAndCustomerID(Long customerId, Long projectId);

    boolean existsProjectByTitle(String title);
}

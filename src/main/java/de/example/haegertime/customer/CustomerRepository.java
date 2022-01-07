package de.example.haegertime.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT n FROM Customer n WHERE n.name = ?1")
    Optional<Customer> findCustomerByName(String name);
}

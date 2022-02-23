package de.example.haegertime.customer;

import de.example.haegertime.projects.Project;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping
    public ResponseEntity<List<Customer>> findAllCustomer() {
        return ResponseEntity.ok(customerService.findAll());
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOOKKEEPER')")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOOKKEEPER') or hasRole('EMPLOYEE')")
    public Customer findByIdCustomer(@PathVariable long id) {
        return customerService.findById(id);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOOKKEEPER')")
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }


    @PostMapping("/{id}/projects")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOOKKEEPER')")
    public ResponseEntity<Customer> addProjectToExistingCustomer(@PathVariable long id,@RequestBody Project project) {
        return new ResponseEntity<>(customerService.addProjectToExistingCustomer(id, project), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOOKKEEPER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerById(@PathVariable long id) {
        customerService.deleteCustomerById(id);
    }

}
package de.example.haegertime.customer;

import de.example.haegertime.projects.Project;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> findAllCustomer() {
        return ResponseEntity.ok(customerService.findAll());
    }


    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Customer findByIdCustomer(@PathVariable long id) {
        return customerService.findById(id);
    }

    @PutMapping
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }

    @PostMapping("/{id}/addproject")
    public ResponseEntity<Customer> addProjectToExistingCustomer(@PathVariable long id,@RequestBody Project project) {
        return new ResponseEntity<>(customerService.addProjectToExistingCustomer(id, project), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerById(@PathVariable long id) {
        customerService.deleteCustomerById(id);
    }

}
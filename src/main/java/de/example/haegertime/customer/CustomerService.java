package de.example.haegertime.customer;

import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.projects.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        //TODO die Methode darf nur von BookKeeper durchgeführt, die Mitarbeiter soll keinen Zugriff auf diese Methode
        if(!customer.getProjectListe().isEmpty()) {
            customerRepository.save(customer);
            return customer;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public List<Customer> findAllCustomer() {
        return customerRepository.findAll();
    }

    @Transactional  //für Sicherheit, Rollback, falls etwas schiefgelaufen ist
    public Customer updateCustomer(Customer customer) {
        Optional<Customer> customerOptional = customerRepository.findById(customer.getId());
        if(customerOptional.isPresent()) {
            Customer updateCustomer = customerOptional.get();
            updateCustomer.setName(customer.getName());
            updateCustomer.setAddress(customer.getAddress());
            updateCustomer.setProjectListe(customer.getProjectListe());
            customerRepository.save(updateCustomer);
            return updateCustomer;
        } else {
            throw new ItemNotFoundException("Diese Kunde ist nicht in der Datenbank");
        }
    }

    public Customer findByIdCustomer(long id) {
        if(customerRepository.findById(id).isPresent()) {
            return customerRepository.getById(id);
        } else {
            throw new ItemNotFoundException("Diese Kunde ist nicht in der Datenbank");
        }
    }

    public Customer addProjectCustomer(long id, Project project) {
        Customer updateCustomer = customerRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Diese Kunde ist nicht in der Datenbank")
        );
        List<Project> projectList = updateCustomer.getProjectListe();
        projectList.add(project);
        updateCustomer.setProjectListe(projectList);
        customerRepository.save(updateCustomer);
        return updateCustomer;
    }

    /**
     * Löschen die Kunde mit der eingegebene ID, die zu Kunden gehörten Projekte
     * werden auch gelöscht.
     * @param id Customer Id
     */
    public void deleteCustomer(long id) {
        if(customerRepository.findById(id).isPresent()) {
            customerRepository.deleteById(id);
        } else {
            throw new ItemNotFoundException("Diese Kunde ist nicht in der Datenbank");
        }
    }

}

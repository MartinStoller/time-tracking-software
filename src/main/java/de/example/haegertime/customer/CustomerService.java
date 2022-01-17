package de.example.haegertime.customer;

import de.example.haegertime.advice.ItemAlreadyExistsException;
import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final ProjectRepository projectRepository;


    public CustomerService(CustomerRepository customerRepository
            , ProjectRepository projectRepository) {
        this.customerRepository = customerRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * Anlegen von neuen Kunden
     * @param customer der neue Kunde
     * @return der erzeugte Kunde
     */
    public Customer createCustomer(Customer customer) {
        Optional<Customer> optionalCustomer = customerRepository.findCustomerByName(customer.getName());
        if(optionalCustomer.isPresent()) {
            throw new ItemAlreadyExistsException("Der Name existiert bereits in der DB");
        }
        if(!customer.getProjects().isEmpty()) {
            for (Project project : customer.getProjects()) {
                String projectTitle = project.getTitle();
                Optional<Project> projectOptional = projectRepository.findProjectByTitle(projectTitle);
                if (projectOptional.isPresent()) {
                    throw new ItemAlreadyExistsException("Das Projekt mit dem Name " + projectTitle +
                            " existiert bereits in der DB");
                }
            }
        }
        customerRepository.save(customer);
        return customer;

    }

    /**
     * Auflisten aller Kunden in der Datenbank
     * @return Liste aller Kunden
     */
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    /**
     * Aktualisierung der Kunden
     * @param customer
     * @return updated Customer
     */
    public Customer updateCustomer(Customer customer) {
        Optional<Customer> customerOptional = customerRepository.findById(customer.getId());
        if(customerOptional.isPresent()) {
            Customer updateCustomer = customerOptional.get();
            updateCustomer.setName(customer.getName());
            updateCustomer.setAddress(customer.getAddress());
            updateCustomer.setProjects(customer.getProjects());
            customerRepository.save(updateCustomer);
            return updateCustomer;
        } else {
            throw new ItemNotFoundException("Diese Kunde ist nicht in der Datenbank");
        }
    }

    /**
     * Suchen Kunden nach ID-Nummer
     * @param id Customer ID
     * @return Customer
     */
    public Customer findById(long id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Diese Kunde ist nicht in der DB")
        );
    }

    /**
     * Hinzufügen ein neues Projekt zu einem Kunden
     * @param id Customer ID
     * @param project das neue Projekt
     * @return Customer
     */
    public Customer addProjectToExistingCustomer(long id, Project project) {
        Customer updateCustomer = customerRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Diese Kunde ist nicht in der Datenbank")
        );
        Optional<Project> projectOptional = projectRepository.findProjectByTitle(project.getTitle());
        if(!projectOptional.isPresent()) {
            List<Project> projectList = updateCustomer.getProjects();
            projectList.add(project);
            updateCustomer.setProjects(projectList);
            customerRepository.save(updateCustomer);
            return updateCustomer;
        } else {
            throw new ItemAlreadyExistsException("Das Projekt mit Title"+project.getTitle()+
                    " ist breits in der DB");
        }
    }

    /**
     * Löschen die Kunde mit der eingegebene ID, die zu Kunden gehörten Projekte
     * werden auch gelöscht.
     * @param id Customer ID
     */
    public void deleteCustomerById(long id) {
        if(customerRepository.findById(id).isPresent()) {
            customerRepository.deleteById(id);
        } else {
            throw new ItemNotFoundException("Diese Kunde ist nicht in der Datenbank");
        }
    }

}

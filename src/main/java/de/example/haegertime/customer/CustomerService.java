package de.example.haegertime.customer;

import de.example.haegertime.advice.ItemExistsException;
import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.advice.ListEmptyException;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final ProjectRepository projectRepository;

    @Autowired
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
        // TODO die Methode darf nur von BookKeeper durchgeführt, die Mitarbeiter soll keinen Zugriff auf diese Methode
        // TODO Checken ob der Customer-Name und Projekt-Title schon vorhanden in der Datenbank sind
        Optional<Customer> optionalCustomer = customerRepository.findCustomerByName(customer.getName());
        if(optionalCustomer.isPresent()) {
            throw new ItemExistsException("Der Name existiert bereits in der DB");
        }
        if(!customer.getProjectListe().isEmpty()) {
            for (Project project : customer.getProjectListe()) {
                String projectTitle = project.getTitle();
                Optional<Project> projectOptional = projectRepository.findProjectByName(projectTitle);
                if(projectOptional.isPresent()) {
                    throw new ItemExistsException("Das Projekt mit dem Name "+projectTitle+
                            " existiert bereits in der DB");
                }
            }
            customerRepository.save(customer);
            return customer;
        } else {
            throw new ListEmptyException("Die Projekt-Liste ist leer");
        }
    }

    /**
     * Auflisten aller Kunden in der Datenbank
     * @return Liste aller Kunden
     */
    public List<Customer> findAllCustomer() {
        return customerRepository.findAll();
    }

    /**
     * Aktualisierung der Kunden
     * @param customer
     * @return updated Customer
     */
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

    /**
     * Suchen Kunden nach ID-Nummer
     * @param id Customer ID
     * @return Customer
     */
    public Customer findByIdCustomer(long id) {
        if(customerRepository.findById(id).isPresent()) {
            return customerRepository.getById(id);
        } else {
            throw new ItemNotFoundException("Diese Kunde ist nicht in der Datenbank");
        }
    }

    /**
     * Hinzufügen ein neues Projekt zu einem Kunden
     * @param id Customer ID
     * @param project das neue Projekt
     * @return Customer
     */
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
     * @param id Customer ID
     */
    public void deleteCustomer(long id) {
        if(customerRepository.findById(id).isPresent()) {
            customerRepository.deleteById(id);
        } else {
            throw new ItemNotFoundException("Diese Kunde ist nicht in der Datenbank");
        }
    }

}

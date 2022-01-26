package de.example.haegertime.customer;

import de.example.haegertime.advice.ItemAlreadyExistsException;
import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ProjectRepository projectRepository;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerRepository, projectRepository);
    }

    @Test
    void findAll() {
        //given
        List<Customer> customers = new ArrayList<>();
        Customer c1 = new Customer("ABC","abc");
        Customer c2 = new Customer("ASD", "vbx");
        customers.add(c1);
        customers.add(c2);
        //when
        when(customerRepository.findAll()).thenReturn(customers);
        //then
        List<Customer> output = underTest.findAll();
        assertThat(output.size()).isEqualTo(2);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void itShouldCreateCustomer() {
        //given
        Customer customer = new Customer("ABC","XYZ");
        Project project = new Project("adac", LocalDate.of(2020, Month.JANUARY, 11), null);
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        customer.setProjects(projects);
        given(customerRepository.existsCustomerByName(customer.getName())).willReturn(false);
        given(projectRepository.existsProjectByTitle(project.getTitle())).willReturn(false);
        //when
        Customer expected = underTest.createCustomer(customer);
        //then
        assertThat(expected.getName()).isEqualTo(customer.getName());
        assertThat(expected.getAddress()).isEqualTo(customer.getAddress());
        verify(customerRepository, times(1)).save(customer);
    }


    @Test
    void createCustomerNameAlreadyExists() {
        Customer customer = new Customer("ABC","XYZ");
        //given
        given(customerRepository.existsCustomerByName(customer.getName())).willReturn(true);
        //then
        assertThatThrownBy(() -> underTest.createCustomer(customer))
                .isInstanceOf(ItemAlreadyExistsException.class)
                .hasMessageContaining("Der Name existiert bereits in der DB");
    }


    @Test
    void createCustomerProjectTitleAlreadyExists() {
        //given
        Customer customer = new Customer("ABC","XYZ");
        Project project = new Project("adac", LocalDate.of(2020, Month.JANUARY, 11), null);
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        customer.setProjects(projects);
        given(customerRepository.existsCustomerByName(customer.getName())).willReturn(false);
        given(projectRepository.existsProjectByTitle(project.getTitle())).willReturn(true);
        //then
        assertThatThrownBy(() -> underTest.createCustomer(customer))
                .isInstanceOf(ItemAlreadyExistsException.class)
                .hasMessageContaining("Das Projekt mit dem Name " +project.getTitle()+
                        " existiert bereits in der DB");
    }

    @Test
    void itShouldUpdateCustomer() {
        Customer customer = new Customer("ABC","XYZ");
        Project project = new Project("adac", LocalDate.of(2020, Month.JANUARY, 11), null);
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        customer.setProjects(projects);
        //when
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        //then
        Customer expected = underTest.updateCustomer(customer);
        assertEquals(expected.getName(), customer.getName());
        assertEquals(expected.getAddress(), customer.getAddress());
        assertEquals(expected.getProjects(), customer.getProjects());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void itShouldNotUpdateCustomerNameNotExists() {
        Customer customer = new Customer("ABC","XYZ");
        Project project = new Project("adac", LocalDate.of(2020, Month.JANUARY, 11), null);
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        customer.setProjects(projects);
        assertThatThrownBy(() -> underTest.updateCustomer(customer))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Diese Kunde ist nicht in der DB");
    }

    @Test
    void itShouldFindById() {
        Customer customer = new Customer("ABC","XYZ");
        Project project = new Project("adac", LocalDate.of(2020, Month.JANUARY, 11), null);
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        customer.setProjects(projects);
        Long customerId = 1L;
        //when
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Customer expected = underTest.findById(customerId);
        //then
        assertThat(expected.getName()).isEqualTo(customer.getName());
        assertThat(expected.getAddress()).isEqualTo(customer.getAddress());
        assertThat(expected.getProjects()).isEqualTo(customer.getProjects());
    }

    @Test
    void itShouldNotFindProjectById() {
        Long customerId = 1L;
        assertThatThrownBy(() -> underTest.findById(customerId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Diese Kunde ist nicht in der DB");
    }

    @Test
    void itShouldAddProjectToExistingCustomer() {
        Customer customer = new Customer("ABC","XYZ");
        Project project = new Project("adac", LocalDate.of(2020, Month.JANUARY, 11), null);
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        customer.setProjects(projects);
        Project addProject = new Project("asd",LocalDate.of(2000, Month.JANUARY, 11), null);
        Long customerId = 1L;
        //when
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(projectRepository.existsProjectByTitle(addProject.getTitle())).thenReturn(false);
        //then
        Customer expected = underTest.addProjectToExistingCustomer(customerId, addProject);
        assertThat(expected.getProjects().size()).isEqualTo(2);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void itShouldNotAddProjectToNotExistingCustomer() {
        Project addProject = new Project("asd",LocalDate.of(2000, Month.JANUARY, 11), null);
        Long customerId = 1L;
        assertThatThrownBy(() -> underTest.addProjectToExistingCustomer(customerId, addProject))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Diese Kunde ist nicht in der Datenbank");
    }


    @Test
    void itShouldNotAddExistingProjectToExistingCustomer() {
        Customer customer = new Customer("ABC","XYZ");
        Project project = new Project("adac", LocalDate.of(2020, Month.JANUARY, 11), null);
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        customer.setProjects(projects);
        Project addProject = new Project("asd",LocalDate.of(2000, Month.JANUARY, 11), null);
        Long customerId = 1L;
        //when
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(projectRepository.existsProjectByTitle(addProject.getTitle())).thenReturn(true);
        //then
        assertThatThrownBy(()->underTest.addProjectToExistingCustomer(customerId,addProject))
                .isInstanceOf(ItemAlreadyExistsException.class)
                .hasMessageContaining("Das Projekt mit Title "+addProject.getTitle()+
                        " ist bereits in der DB");
    }

    @Test
    void itShouldDeleteCustomerById() {
        Long customerId = 1L;
        Customer customer = new Customer("ABC","XYZ");
        Project project = new Project("adac", LocalDate.of(2020, Month.JANUARY, 11), null);
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        customer.setProjects(projects);
        //when
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        //then
        underTest.deleteCustomerById(customerId);
        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void itShouldNotDeleteCustomerById() {
        Long customerId = 1L;
        assertThatThrownBy(()->underTest.deleteCustomerById(customerId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Diese Kunde ist nicht in der Datenbank");
    }
}
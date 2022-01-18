package de.example.haegertime.projects;

import de.example.haegertime.customer.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository underTest;

    @Test
    void itShouldFindProjectByTitle() {
        //given
        String title = "ABC";
        Project project = new Project(title, LocalDate.of(2000, Month.JANUARY,13), null);
        underTest.save(project);
        //then
        assertThat(underTest.findProjectByTitle(title)).isPresent();
    }


    @Test
    void itShouldNotFindProjectByTitle() {
        //given
        String title = "ABC";
        //then
        assertThat(underTest.findProjectByTitle(title).isPresent()).isFalse();
    }

    @Test
    void itShouldExistingProjectByTitle() {
        //given
        String title = "ABC";
        Project project = new Project(title, LocalDate.of(2000, Month.JANUARY,13), null);
        underTest.save(project);
        //then
        assertThat(underTest.existsProjectByTitle(title)).isTrue();
    }

    @Test
    void itShouldNotExistingProjectByTitle() {
        //given
        String title = "ABC";
        //then
        assertThat(underTest.existsProjectByTitle(title)).isFalse();
    }

    @Test
    void itShouldExistsProjectByIdAndCustomerID() {
        //given
        Project project = new Project("ABC", LocalDate.of(2022, Month.JANUARY, 22),
                LocalDate.of(2022, Month.FEBRUARY, 12));
        Customer customer = new Customer("ABC","test");
        project.setCustomer(customer);
        underTest.save(project);
        //then
        assertThat(underTest.existsProjectByIdAndCustomerID(customer.getId(), project.getId())).isPresent();
    }

    @Test
    void itShouldNotExistsProjectByIdAndCustomerId() {
        //given
        Long customerId = 10L;
        Long projectId = 10L;
        //then
        assertThat(underTest.existsProjectByIdAndCustomerID(customerId, projectId).isPresent()).isFalse();
    }
}
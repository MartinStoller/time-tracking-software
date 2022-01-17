package de.example.haegertime.projects;

import de.example.haegertime.advice.ItemAlreadyExistsException;
import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.customer.Customer;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository repo;
    private ProjectService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ProjectService(repo);
    }

    @Test
    void canFindAll() {
        //given
        List<Project> projects = new ArrayList<>();
        Project p1 = new Project("ABC", LocalDate.of(2022, Month.FEBRUARY, 11), null);
        Project p2 = new Project("XYZ", LocalDate.of(2021, Month.APRIL, 13), null);
        projects.add(p1);
        projects.add(p2);
        //when
        when(repo.findAll()).thenReturn(projects);
        //then
        List<Project> expected = underTest.findAll();
        assertThat(expected.size()).isEqualTo(2);
        verify(repo, times(1)).findAll();
    }

    @Test
    void itShouldGetProjectById() {
        //given
        Project project = new Project("ABC",
                LocalDate.of(2020, Month.FEBRUARY, 12), null);
        doReturn(Optional.of(project)).when(repo).findById(1L);
        //then
        Project expected = underTest.getById(1L);
        assertThat(expected.getTitle()).isEqualTo("ABC");
        assertThat(expected.getStart()).isEqualTo(LocalDate.of(2020, Month.FEBRUARY, 12));
        assertThat(expected.getEnd()).isNull();
    }

    @Test
    void itShouldNotGetProjectById() {
        assertThatThrownBy(() -> underTest.getById(2L)).isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Das Projekt mit Id 2"
                        + " nicht in der DB");
    }

    @Test
    void itShouldUpdateProject() {
        //given
        Project project = new Project("ABC", LocalDate.of(2022, Month.JANUARY, 3), null);
        Project givenProject = new Project("XYZ", LocalDate.of(2011, Month.APRIL, 1),
                LocalDate.of(2018, Month.MARCH,10));
        doReturn(Optional.of(project)).when(repo).findById(1L);
        //when
        Project expected = underTest.updateProject(1L, givenProject);
        //then
        assertThat(expected.getTitle()).isEqualTo("XYZ");
        assertThat(expected.getStart()).isEqualTo(givenProject.getStart());
        assertThat(expected.getEnd()).isEqualTo(givenProject.getEnd());
        verify(repo, times(1)).save(expected);
    }

    @Test
    void updateProjectIdNotFound() {
        assertThatThrownBy(() -> underTest.updateProject(1L, null))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Das Projekt mit der Id 1"+
                        " nicht in der DB");
    }

    @Test
    void updateProjectTitleAlreadyExists() {
        //given
        Project project = new Project("ABC", LocalDate.of(2022, Month.JANUARY, 3), null);
        repo.save(project);
        doReturn(Optional.of(project)).when(repo).findById(1L);
        doReturn(Optional.of(project)).when(repo).findProjectByTitle("ABC");
        Project inputProject = new Project("ABC", LocalDate.of(2001, Month.APRIL, 6), null);
        //when
        assertThatThrownBy(() -> underTest.updateProject(1L, inputProject))
                .isInstanceOf(ItemAlreadyExistsException.class)
                .hasMessageContaining("Ein Projekt mit Title "+project.getTitle()
                        +" ist vorhanden in der DB");
    }

    @Test
    void itShouldExistsProjectByIdAndCustomerId() {
        //given
        Project project = new Project("ABC", LocalDate.of(2022, Month.JANUARY, 22),
                LocalDate.of(2022, Month.FEBRUARY, 12));
        Customer customer = new Customer("ABC","test");
        project.setCustomer(customer);
        repo.save(project);
        //when
        when(repo.existsProjectByIdAndCustomerID(customer.getId(), project.getId())).thenReturn(Optional.of(project));
        //then
        assertThat(underTest.existsProjectByIdAndCustomerId(customer.getId(), project.getId())).isTrue();
    }

    @Test
    void itShouldNotExistsProjectByIdAndCustomerId() {
        assertThat(underTest.existsProjectByIdAndCustomerId(2L,2L)).isFalse();
    }

    @Test
    void itShouldDeleteProjectById() {
        //given
        Project project = new Project("ABC", LocalDate.of(2022, Month.JANUARY, 22),
                LocalDate.of(2022, Month.FEBRUARY, 12));
        repo.save(project);
        given(repo.findById(1L)).willReturn(Optional.of(project));
        //when
        underTest.deleteById(1L);
        //then
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void itShouldNotDeleteProjectById() {
        assertThatThrownBy(()-> underTest.deleteById(1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Das Projekt mit Id 1"+" nicht in der DB");
    }
}
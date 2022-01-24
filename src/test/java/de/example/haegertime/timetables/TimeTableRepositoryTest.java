package de.example.haegertime.timetables;

import de.example.haegertime.customer.Customer;
import de.example.haegertime.customer.CustomerRepository;
import de.example.haegertime.fillReposWithTestdata.TestdataGenerator;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import de.example.haegertime.users.Role;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TimeTableRepositoryTest {

    @Autowired
    private TimeTableRepository testedTTDRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp(){
        TestdataGenerator testdataGenerator = new TestdataGenerator(customerRepository, projectRepository, testedTTDRepository, userRepository);
        double ggg = 0;
        testdataGenerator.fillAllRepos();

    }

    @AfterEach
    void tearDown(){
        testedTTDRepository.deleteAll();
    }

    @Test
    void shouldGetTimeTableDayByEmployeeId() {

        //Expected project:
        Project proj1 = new Project("Homepage Maintenance", LocalDate.of(1995, Month.JULY, 22), LocalDate.of(2021, Month.AUGUST, 2));
        proj1.setId(1L);
        List<Project> projects = new ArrayList<>();
        projects.add(proj1);
        Customer c1 = new Customer("AXA", "Hansa Alle 39, 44444 Düsseldorf");
        c1.setProjects(projects);
        proj1.setCustomer(c1);

        //Expected TimeTableDays:
        TimeTableDay ttd2 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 2),null,
                null, 0, 0, null);
        TimeTableDay ttd3 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 3),  LocalTime.of(8, 15),
                LocalTime.of(17, 30), 0.75, 8, null);
        ttd2.setWorkdayId(2L);
        ttd3.setWorkdayId(3L);
        ttd2.setProject(proj1);
        ttd3.setProject(proj1);

        //Expected employee:
        User usr1 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        usr1.setId(1L);
        List<TimeTableDay> newListUser= usr1.getTimeTableDayList();
        newListUser.add(ttd2);
        newListUser.add(ttd3);
        usr1.setTimeTableDayList(newListUser);
        ttd3.setEmployee(usr1);
        ttd2.setEmployee(usr1);

        //when
        List<TimeTableDay> returned = testedTTDRepository.getTimeTableDayByEmployeeId(1L);

        List<TimeTableDay> expected = List.of(ttd2, ttd3);

        //then
        assertEquals(expected, returned);
    }

    @Test
    void shouldGetSumOfHoursOfAllEmployeeOnAProject() {
        // hoursLists is expected to be a List of Lists of 2 Elements for each employee ([0] -> sum of hours for that employee, [1] employeeId

        List<List<Double>> hoursLists = testedTTDRepository.getTotalHoursOfProjectGroupedByEmployee(2L);
        System.out.println(testedTTDRepository.getTotalHoursOfProjectGroupedByEmployee(2L));
        System.out.println(testedTTDRepository.getTotalHoursOfProjectGroupedByEmployee(2L));
        System.out.println(testedTTDRepository.getTotalHoursOfProjectGroupedByEmployee(1L));



    }

    @Test
    void getTotalActualHoursExpectedHoursByEmployeeId() {
    }

    @Test
    void getAllEmployeesInHoliday() {
    }

    @Test
    void getAllSickEmployees() {
    }
}
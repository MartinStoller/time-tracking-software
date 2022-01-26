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
    void shouldGetAllWorkdaysWhereAbsenceStatusIsHOLIDAYAndDateIsAsGiven() {
        //Expected project:
        Project proj2 = new Project("WebAppXY Extension", LocalDate.of(1992, Month.JULY, 13), null);
        proj2.setId(2L);
        //Expected Users:
        User usr5 = new User("Admiral", "Schneider", "fussball95", "spamfilter@gmail.com", Role.EMPLOYEE);
        usr5.setId(5L);
        User usr7 = new User("Waldo", "Holzkopf", "redbull!", "holzkopf@gmx.net", Role.EMPLOYEE);
        usr7.setId(7L);
        //Expected TimeTableDays:
        TimeTableDay ttd10 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 5), null,
                null, 0, 6, AbsenceStatus.HOLIDAY);
        ttd10.setWorkdayId(10L);
        TimeTableDay ttd11 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 5), null,
                null, 0, 8, AbsenceStatus.HOLIDAY);
        ttd11.setWorkdayId(11L);
        ttd10.assignEmployee(usr7);
        ttd11.assignEmployee(usr5);
        ttd10.assignProject(proj2);
        ttd11.assignProject(proj2);

        List<TimeTableDay> expected = List.of(ttd10, ttd11);

        List<TimeTableDay> actual = testedTTDRepository.getAllHolidaysOnDate(LocalDate.of(2022, Month.JANUARY, 5));

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetAllWorkdaysWhereAbsenceStatusIsSICKAndDateIsAsGiven() {
        //Expected Project:
        Project proj1 = new Project("Homepage Maintenance", LocalDate.of(1995, Month.JULY, 22), LocalDate.of(2021, Month.AUGUST, 2));
        proj1.setId(1L);
        //Expected Employee:
        User usr7 = new User("Waldo", "Holzkopf", "redbull!", "holzkopf@gmx.net", Role.EMPLOYEE);
        usr7.setId(7L);
        //Expected TimeTableDay
        TimeTableDay ttd8 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 3), null,
                null, 0, 4, AbsenceStatus.SICK);
        ttd8.setWorkdayId(8L);
        ttd8.assignProject(proj1);
        ttd8.assignEmployee(usr7);

        List<TimeTableDay> expected = List.of(ttd8);

        List<TimeTableDay> actual = testedTTDRepository.getAllSickdaysOnDate(LocalDate.of(2022, Month.JANUARY, 3));

        assertEquals(expected, actual);
    }
}
package de.example.haegertime.timetables;

import de.example.haegertime.customer.CustomerRepository;
import de.example.haegertime.fillReposWithTestdata.TestdataGenerator;
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
        //given

        User usr1 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        User usr2 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);

        TimeTableDay ttd2 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 2),null,
                null, 0, 0, null);
        TimeTableDay ttd3 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 3),  LocalTime.of(8, 15),
                LocalTime.of(17, 30), 0.75, 8, null);

        usr1.setId(1L);
        ttd2.setEmployee(usr1);
        ttd2.setWorkdayId(2L);
        ttd3.setEmployee(usr1);
        ttd3.setWorkdayId(3L);

        //when
        List<TimeTableDay> returned = testedTTDRepository.getTimeTableDayByEmployeeId(1L);

        List<TimeTableDay> expected = List.of(ttd2, ttd3);

        //then
        System.out.println(testedTTDRepository.findAll().size());
        System.out.print(testedTTDRepository.findAll());
        assertEquals(expected, returned);
    }

    @Test
    void getTotalHoursAllEmployeeOnAProject() {
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
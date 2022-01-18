package de.example.haegertime.fillReposWithTestdata;

import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.customer.Customer;
import de.example.haegertime.customer.CustomerRepository;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import de.example.haegertime.timetables.AbsenceStatus;
import de.example.haegertime.timetables.TimeTableDay;
import de.example.haegertime.timetables.TimeTableRepository;
import de.example.haegertime.users.Role;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TestdataGenerator {
    private final CustomerRepository customerRepository;
    private final ProjectRepository projectRepository;
    private final TimeTableRepository timeTableRepository;
    private final UserRepository userRepository;

    public void fillProjectRepo() {
        Project proj1 = new Project("Homepage Maintenance", LocalDate.of(1995, Month.JULY, 22), LocalDate.of(2021, Month.AUGUST, 2));
        Project proj2 = new Project("WebAppXY Extension", LocalDate.of(1992, Month.JULY, 13), null);
        Project proj3 = new Project("Building the next Google", LocalDate.of(2005, Month.JULY, 23), null);
        Project proj4 = new Project("Cloud Computing project 500", LocalDate.of(2020, Month.JANUARY, 3), LocalDate.of(2021, Month.DECEMBER, 1));
        projectRepository.saveAll(List.of(proj1, proj2, proj3, proj4));
    }

    public void fillCustomerRepo() {
        Customer c1 = new Customer("AXA", "Hansa Alle 39, 44444 Düsseldorf");
        Customer c2 = new Customer("Techniker Krankenkasse", "Hansa Alle 50, 44444 Düsseldorf");
        Customer c3 = new Customer("AOK Krankenkasse", "Hansa Alle 60, 44444 Düsseldorf");
        Customer c4 = new Customer("DAK Krankenkasse", "Hansa Alle 70, 44444 Düsseldorf");

        Project project1 = projectRepository.findById(1L).orElseThrow(() -> new ItemNotFoundException("Did not find this id"));
        List<Project> projects = new ArrayList<>();
        projects.add(project1);
        c1.setProjects(projects);

        Project project2 = projectRepository.findById(2L).orElseThrow(() -> new ItemNotFoundException("Did not find this id"));
        projects = new ArrayList<>();
        projects.add(project2);
        c2.setProjects(projects);

        Project project3 = projectRepository.findById(3L).orElseThrow(() -> new ItemNotFoundException("Did not find this id"));
        projects = new ArrayList<>();
        projects.add(project3);
        c3.setProjects(projects);

        Project project4 = projectRepository.findById(4L).orElseThrow(() -> new ItemNotFoundException("Did not find this id"));
        projects = new ArrayList<>();
        projects.add(project4);
        c4.setProjects(projects);

        customerRepository.saveAll(List.of(c1, c2, c3, c4));
    }


    public void fillTimeTableRepo() {
        TimeTableDay ttd1 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 1), null,
                null, 0, 0, null);
        TimeTableDay ttd2 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 2), null,
                null, 0, 0, null);
        TimeTableDay ttd3 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 3), LocalTime.of(8, 15),
                LocalTime.of(17, 30), 0.75, 8, null);
        TimeTableDay ttd4 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 4), LocalTime.of(8, 0),
                LocalTime.of(18, 15), 1, 8, null);
        TimeTableDay ttd5 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 5), LocalTime.of(7, 30),
                LocalTime.of(13, 50), 1.2, 8, null);
        TimeTableDay ttd6 = new TimeTableDay(LocalDate.of(2022, Month.JANUARY, 1), null,
                null, 0, 0, null);
        TimeTableDay ttd7 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 2), null,
                null, 0, 0, null);
        TimeTableDay ttd8 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 3), null,
                null, 0, 4, AbsenceStatus.SICK);
        TimeTableDay ttd9 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 4), LocalTime.of(7, 50),
                LocalTime.of(13, 20), 0.75, 8, AbsenceStatus.HOLIDAY);
        TimeTableDay ttd10 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 5), null,
                null, 0, 6, AbsenceStatus.HOLIDAY);

        timeTableRepository.saveAll(List.of(ttd1, ttd2, ttd3, ttd4, ttd5, ttd6, ttd7, ttd8, ttd9, ttd10));
    }

    public void fillUserRepo() {
        User usr1 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        User usr2 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);
        User usr3 = new User("Nick", "Petersen", "alarm1", "stoller.martin@gmx.de", Role.ADMIN);
        User usr4 = new User("Albert", "Gartenzwerg", "alarm1", "gartenzwergl@gmail.com", Role.ADMIN);
        User usr5 = new User("Admiral", "Schneider", "fussball95", "spamfilter@gmail.com", Role.EMPLOYEE);
        User usr6 = new User("Franziska", "Frankenstein", "123Bergsteiger", "frafra@gmx.de", Role.BOOKKEEPER);
        User usr7 = new User("Waldo", "Holzkopf", "redbull!", "holzkopf@gmx.net", Role.EMPLOYEE);
        userRepository.saveAll(List.of(usr1, usr2, usr3, usr4, usr5, usr6, usr7));
    }

    public TimeTableDay assignProjectToDay(Long dayId, Long projectId) throws ItemNotFoundException {
        /**
         this is a copy of a method in the TimeTableDayService
         */
        TimeTableDay day = timeTableRepository.findById(dayId).orElseThrow(() -> new ItemNotFoundException("Day with id " + dayId + " not found."));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ItemNotFoundException("Project with id  " + projectId + " not found."));
        day.assignProject(project);
        List<TimeTableDay> newTimeTableDays = project.getTimeTableDays();
        newTimeTableDays.add(day);
        project.setTimeTableDays(newTimeTableDays);
        return timeTableRepository.save(day);
    }

    public TimeTableDay assignEmployeeToDay(Long dayId, Long employeeId) throws ItemNotFoundException {
        /**
         this is a copy of a method in the TimeTableDayService
         */
        TimeTableDay day = timeTableRepository.findById(dayId).orElseThrow(() -> new ItemNotFoundException("Day with id " + dayId + " not found."));
        User user = userRepository.findById(employeeId).orElseThrow(() -> new ItemNotFoundException("Employee with id " + employeeId + " not found."));
        List<TimeTableDay> newList= user.getTimeTableDayList();
        newList.add(day);
        user.setTimeTableDayList(newList);
        day.assignEmployee(user);
        return timeTableRepository.save(day);
    }

    public void fillAllRepos() {
        fillProjectRepo();
        fillCustomerRepo();
        fillTimeTableRepo();
        fillUserRepo();

        //create connections between ttd and user and projects
        assignEmployeeToDay(2L, 1L);
        assignEmployeeToDay(3L, 1L);
        assignEmployeeToDay(4L, 7L);
        assignEmployeeToDay(5L, 7L);
        assignEmployeeToDay(8L, 7L);
        assignEmployeeToDay(9L, 7L);
        assignEmployeeToDay(10L, 7L);

        assignProjectToDay(2L, 1L);
        assignProjectToDay(3L, 1L);
        assignProjectToDay(4L, 1L);
        assignProjectToDay(5L, 1L);
        assignProjectToDay(8L, 1L);
        assignProjectToDay(9L, 2L);
        assignProjectToDay(10L, 2L);
    }
}

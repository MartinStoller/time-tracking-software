package de.example.haegertime.users;

import de.example.haegertime.advice.EmailAlreadyExistsException;
import de.example.haegertime.advice.InvalidRoleException;
import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.email.EmailService;
import de.example.haegertime.projects.Project;
import de.example.haegertime.timetables.TimeTableDay;
import de.example.haegertime.timetables.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TimeTableRepository timeTableRepository;
    private final String bookkeeperEmail = "josalongmartin@gmail.com";


    public List<User> getAllUsers(String sortBy) {
        if (sortBy == null) {
            return userRepository.findAll();
        } else if (sortBy.equals("abc")) {
            return userRepository.findAll(Sort.by(Sort.Direction.ASC, "last"));
        } else if (sortBy.equals("role")) {
            return userRepository.findAll(Sort.by(Sort.Direction.ASC, "role"));
        } else {
            throw new InvalidParameterException("Requestparameter must either be abc, role or null.");
        }
    }


    public void createUser(User user) {
        String mail = user.getEmail();
        if (userRepository.existsByEmail(mail)) {
            throw new EmailAlreadyExistsException("Diese Email wird bereits verwendet.");
        }
        userRepository.save(user);
        emailService.send(user.getEmail(), "Dein Haegertime Account wurde erstellt",
                emailService.getEmailText(user.getFirstname(), "Neuerstellung deines Accounts"));
    }


    public User findById(long id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.getById(id);
        } else {
            throw new ItemNotFoundException("Dieser User ist nicht in der Datenbank");
        }
    }

    public List<User> findByLastByFirstByEmail(String keyword) {
        return userRepository.findBylastByFirstbyEmail(keyword);
    }


    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Dieser User ist nicht in der Datenbank"));
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            emailService.send(user.getEmail(), "Dein Haegertime Account wurde gelöscht",
                    emailService.getEmailText(user.getFirstname(), "Löschung deines Accounts"));
        }
    }

    public User getByUsername(String username) {
        return userRepository.getUserByEmail(username).orElseThrow(
                () -> new ItemNotFoundException("der user exisitiert nicht")
        );
    }


    public User updateUserDetails(User user, User loggedUser) {
        User updateUser = userRepository.getUserByEmail(loggedUser.getEmail()).orElseThrow(() -> new ItemNotFoundException(""));
        updateUser.setPassword(user.getPassword());
        updateUser.setFirstname(user.getFirstname());
        updateUser.setLastname(user.getLastname());
        updateUser.setEmail(loggedUser.getEmail()); //email darf nicht selbst ändern
        updateUser.setRole(loggedUser.getRole());   //role darf nicht selbst ändern
        updateUser.setId(loggedUser.getId());
        updateUser.setFrozen(loggedUser.isFrozen());
        userRepository.save(updateUser);
        return updateUser;
    }


    public User updateUserName(Long id, String newUserName) {
        User updateUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mit der Id " + id + "" +
                        " ist nicht in der DB")
        );
        String email = updateUser.getEmail();
        String first = updateUser.getFirstname();
        updateUser.setEmail(newUserName);
        userRepository.save(updateUser);
        emailService.send(email, "Dein Profil wurde geupdated",
                emailService.getEmailText(first, "Update deiner Accountdetails"));
        return updateUser;
    }

    public void deactivateUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User deactivUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mit der Id " + id +
                        " ist nicht in der DB")
        );
        deactivUser.setFrozen(false);
        userRepository.save(deactivUser);
    }


    public void reactivateUser(Long id) {
        User reactivUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mid Id " + id +
                        " ist nicht in der DB")
        );
        reactivUser.setFrozen(true);
        userRepository.save(reactivUser);
    }

    public User updateRole(Long id, String role) {
        User updateRoleUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mit der Id " + id + " ist nicht in der DB")
        );
        if (role.equals("ADMIN") || role.equals("EMPLOYEE") || role.equals("BOOKKEEPER")) {
            updateRoleUser.setRole(Role.valueOf(role));
            userRepository.save(updateRoleUser);
            return updateRoleUser;
        } else {
            throw new InvalidRoleException("Die eingegebene Role ist ungültig");
        }
    }

    public Set<Project> getMyProjects(String email) {
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new ItemNotFoundException(""));
        Set<Project> projects = new LinkedHashSet<>();
        List<TimeTableDay> allWorkdays = user.getTimeTableDayList(); //get a list of all workdays
        allWorkdays.forEach((day) -> projects.add(day.getProject())); //create Hashset which contains all projects
        return projects;
    }

    public List<Double> getOvertimeBalance(String email) {
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new ItemNotFoundException(""));
        List<TimeTableDay> allWorkdays = user.getTimeTableDayList(); //get a list of all workdays
        double actualHoursSum = 0;
        double expectedHoursSum = 0;
        for (TimeTableDay ttd : allWorkdays) {

            actualHoursSum += ttd.calculateActualHours();
            expectedHoursSum += ttd.calculateActualHours();

            actualHoursSum += ttd.calculateActualHours();
            expectedHoursSum += ttd.calculateActualHours();
        }
        return Arrays.asList(expectedHoursSum, actualHoursSum, actualHoursSum - expectedHoursSum);
    }

    public List<TimeTableDay> showOwnWorkdays(String email, LocalDate start, LocalDate end) {
        if (start == null) {
            start = LocalDate.of(1900, 1, 1);
        }
        if (end == null) {
            end = LocalDate.of(2099, 1, 1);
        }
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new ItemNotFoundException(""));
        List<TimeTableDay> workdays = user.getTimeTableDayList();
        List<TimeTableDay> foundWorkdays = new java.util.ArrayList<>();

        for (TimeTableDay ttd : workdays) {
            if (ttd.getDate().isAfter(start) && ttd.getDate().isBefore(end)) {
                foundWorkdays.add(ttd);
            }
        }
        //Alternative: via Query/SQL
        return foundWorkdays;
    }

    @Transactional
    public void registerNewTimeTable(TimeTableDay timeTableDay, String username) {
        User user = userRepository.getUserByEmail(username).orElseThrow(() -> new ItemNotFoundException(""));
        List<TimeTableDay> timeTableDayList = user.getTimeTableDayList();
        timeTableDayList.add(timeTableDay);
        user.setTimeTableDayList(timeTableDayList);

        timeTableRepository.save(timeTableDay);
        userRepository.save(user);
    }

    public double showMyRestHolidays(String username) {
        User user = userRepository.getUserByEmail(username).orElseThrow(() -> new ItemNotFoundException(""));
        //TODO Cedrik: Nullpointer check.
        return user.getUrlaubstage();
    }

    public void applyForHoliday(Long employeeId, Long dayId, double duration) {
        emailService.send(bookkeeperEmail, "Apply for holidays", "Employee Id " + employeeId +
                ", workdayId " + dayId + ", duration: " + duration
        );
    }

    public void declineForHoliday(Long employeeId) {
        User user = userRepository.findById(employeeId).orElseThrow(
                () -> new ItemNotFoundException("Der User mit Id " + employeeId + "" +
                        " nicht in der Datenbank")
        );
        String userEmail = user.getEmail();
        emailService.send(userEmail, "Decline for holidays", "Hi " + user.getFirstname()
                + ", your apply was cancelled");
    }

    public List<TimeTableDay> showAllMyHolidays(String email) {
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new ItemNotFoundException(""));
        List<TimeTableDay> ttd = user.getTimeTableDayList();
        List<TimeTableDay> htt = new ArrayList<>();
        for (TimeTableDay t : ttd) {
            if (t.getAbsenceStatus() != null && t.getAbsenceStatus().toString().equals("HOLIDAY")) {
                htt.add(t);
            }
        }
        return htt;
    }
}









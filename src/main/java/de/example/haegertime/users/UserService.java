package de.example.haegertime.users;

import de.example.haegertime.advice.InvalidRoleException;
import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.timetables.TimeTableDay;
import de.example.haegertime.timetables.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TimeTableRepository timeTableRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       TimeTableRepository timeTableRepository) {
        this.userRepository = userRepository;
        this.timeTableRepository = timeTableRepository;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public void createUser(User user) {
        userRepository.save(user);
    }


    public User findByIdUser(long id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.getById(id);
        } else {
            throw new ItemNotFoundException("Dieser User ist nicht in der Datenbank");
        }
    }

    public List<User> findByKeywordUser(String keyword) {
        return userRepository.findByKeyword(keyword);
    }


    //todo change to only Admin
    public void deleteUser(long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new ItemNotFoundException("Dieser User ist nicht in der Datenbank");
        }
    }

    public User getUserByUserName(String username) {
        return userRepository.getUserByUserEmail(username);
    }


    public User updateUserDetails(User user, User loggedUser) {
        User updateUser = userRepository.getUserByUserEmail(loggedUser.getEmail());
        updateUser.setPassword(user.getPassword());
        updateUser.setFirst(user.getFirst());
        updateUser.setLast(user.getLast());
        updateUser.setEmail(loggedUser.getEmail()); //email darf nicht selbst ändern
        updateUser.setRole(loggedUser.getRole());   //role darf nicht selbst ändern
        updateUser.setId(loggedUser.getId());
        updateUser.setFrozen(loggedUser.isFrozen());
        userRepository.save(updateUser);
        return updateUser;
    }


    public User updateUserName(Long id, String newUserName) {
        User updateUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mit Id "+id+"" +
                        " ist nicht in der DB")
        );
        updateUser.setEmail(newUserName);
        userRepository.save(updateUser);
        return updateUser;
    }


    public void deactivUser(Long id) {
        User deactivUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mid Id "+id+
                        " ist nicht in der DB")
        );
        deactivUser.setFrozen(false);
        userRepository.save(deactivUser);
    }


    public void reactivUser(Long id) {
        User reactivUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mid Id "+id+
                        " ist nicht in der DB")
        );
        reactivUser.setFrozen(true);
        userRepository.save(reactivUser);
    }

    public User updateRoleUser(Long id, String role) {
        User updateRoleUser = userRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Der Benutzer mid Id "+id+" ist nicht in der DB")
        );
        if(role.equals("ADMIN") || role.equals("EMPLOYEE") || role.equals("BOOKKEEPER") ) {
            updateRoleUser.setRole(Role.valueOf(role));
            userRepository.save(updateRoleUser);
            return updateRoleUser;
        } else {
            throw new InvalidRoleException("Die eingegebene Role ist ungültig");
        }
    }

    @Transactional
    public String registerNewTimeTable(TimeTableDay timeTableDay, String username) {
        User user = userRepository.getUserByUserEmail(username);
        List<TimeTableDay> timeTableDayList = user.getTimeTableDayList();
        double actualhours = timeTableDay.calculateActualHours();
        timeTableDay.setActualHours(actualhours);
        timeTableDayList.add(timeTableDay);
        user.setTimeTableDayList(timeTableDayList);
        timeTableRepository.save(timeTableDay);
        userRepository.save(user);
        return "New Time Table registered "+ " actual hours " + actualhours;
    }
}









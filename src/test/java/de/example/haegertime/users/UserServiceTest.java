package de.example.haegertime.users;

import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.email.EmailService;
import de.example.haegertime.timetables.TimeTableRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserRepository userRepository;
    private EmailService emailService;
    private TimeTableRepository timeTableRepository;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, emailService, timeTableRepository);
    }



    @Test
    void ShouldFindAllSortByRole() {
        //given
        List<User> users = new ArrayList<>();
        User user1 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);
        User user2 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        users.add(user1);
        users.add(user2);

        //when
        when(userRepository.findAll(Sort.by(Sort.Direction.ASC, "role"))).thenReturn(users);
        //then
        List<User> expected = underTest.getAllUsers("role");
        assertThat(expected.size()).isEqualTo(2);
        verify(userRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "role"));
    }

    @Test
    void ShouldFindAllSortByLast() {
        //given
        List<User> users = new ArrayList<>();
        User user1 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);
        User user2 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        users.add(user1);
        users.add(user2);

        //when
        when(userRepository.findAll(Sort.by(Sort.Direction.ASC, "last"))).thenReturn(users);
        //then
        List<User> expected = underTest.getAllUsers("abc");
        assertThat(expected.size()).isEqualTo(2);
        verify(userRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "last"));
    }

    @Test
    void ShouldFindAll() {
        //given
        List<User> users = new ArrayList<>();
        User user1 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);
        User user2 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);

        users.add(user1);
        users.add(user2);
        //when
        when(userRepository.findAll()).thenReturn(users);
        //then
        List<User> expected = underTest.getAllUsers(null);
        assertThat(expected.size()).isEqualTo(2);
        verify(userRepository, times(1)).findAll();
    }



//    @Test
//    @DisplayName("add an new User")
//    void shouldCreateUser() {
//        // given
//        User user = new User("Anton", "Aus Tirol", "1234567", "anton.austirol@gmx.de", Role.EMPLOYEE);
//
//        User user2 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);
//        // when
//        underTest.createUser(user);
//
//        // then
//        ArgumentCaptor<User> userArgumentCaptor =
//                ArgumentCaptor.forClass(User.class);
//
//        verify(userRepository)
//                .save(userArgumentCaptor.capture());
//
//        User capturedUser = userArgumentCaptor.getValue();
//        assertEquals(capturedUser, user);
//    }

    @Test
    @DisplayName("get User by id")
    void shouldFindUserById() {
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        doReturn(Optional.of(user)).when(userRepository).findById(1L);

        Optional<User> returnedUser = userRepository.findById(1L);

        Assertions.assertTrue(returnedUser.isPresent(), "User nicht gefunden");
        Assertions.assertSame(returnedUser.get(), user, "User gefunden");
    }

    @Test
    void ShouldFindByLastByFirstByEmail() {
    }

    @Test
    void shouldDeleteUser() {
    }

//
//    @Test
//    void ShouldDeleteUserById() {
//        //given
//        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
//        userRepository.save(user);
//        given(userRepository.findById(1L)).willReturn(Optional.of(user));
//        //when
//        underTest.deleteUser(1L);
//        //then
//        verify(userRepository, times(1)).deleteById(1L);
//    }



//    @Test
//    void ShouldNotDeleteUserById() {
//        assertThatThrownBy(()-> underTest.deleteUser(1L))
//                .isInstanceOf(ItemNotFoundException.class)
//                .hasMessageContaining("Der User ist nicht in der Datenbank");
//    }


    @Test
    @DisplayName("get User by UserName (Email)")
    void itShouldGetUserByUserName() {
        underTest.getByUsername("martin.stoller2@gmx.de");
        verify(userRepository).getUserByEmail("martin.stoller2@gmx.de");
    }


    @Test
    void updateUserDetails() {
    }

    @Test
    void updateUserName() {
    }

    @Test
    void deactivUser() {
    }

    @Test
    void reactivUser() {
    }

    @Test
    void updateRole() {
    }

    @Test
    void getMyProjects() {
    }

    @Test
    void getOvertimeBalance() {
    }

    @Test
    void showOwnWorkdays() {
    }

    @Test
    void registerNewTimeTable() {
    }

    @Test
    void showMyRestHolidays() {
    }

    @Test
    void applyForHoliday() {
    }

    @Test
    void declineForHoliday() {
    }

    @Test
    void showAllMyHolidays() {
    }
}
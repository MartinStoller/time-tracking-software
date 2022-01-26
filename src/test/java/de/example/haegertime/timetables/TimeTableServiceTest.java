package de.example.haegertime.timetables;

import de.example.haegertime.email.EmailService;
import de.example.haegertime.projects.ProjectRepository;
import de.example.haegertime.users.Role;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TimeTableServiceTest {
    @Mock
    private TimeTableRepository mockedTTDRepository;

    @Mock
    private UserRepository mockedUserRepository;

    @Mock
    private ProjectRepository mockedProjectRepository;

    @Mock
    private EmailService mockedEmailService;

    private TimeTableService testedTTDService;

    @BeforeEach
    void setUp(){
        testedTTDService = new TimeTableService(mockedTTDRepository, mockedUserRepository, mockedProjectRepository, mockedEmailService);
    }

    @Captor
    ArgumentCaptor<Long> argumentCaptorLong;
    @Captor
    ArgumentCaptor<String> argumentCaptorString1;
    @Captor
    ArgumentCaptor<String> argumentCaptorString2;
    @Captor
    ArgumentCaptor<String> argumentCaptorString3;

    @Test
    void shouldInvokeFindAllOnTTDRepository() {
        List<TimeTableDay> l = testedTTDService.getTimetable();
        Mockito.verify(mockedTTDRepository).findAll();
    }

    @Test
    void shouldInvokeFindByIdOnCorrectArgument() throws InstanceNotFoundException {
        TimeTableDay ttd2 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 4), LocalTime.of(8, 0),
                LocalTime.of(18, 15), 1, 8, null);
        ttd2.setWorkdayId(2L);
        BDDMockito.given(mockedTTDRepository.findById(2L)).willReturn(Optional.of(ttd2));

        TimeTableDay t = testedTTDService.getTimetableDay(2L);

        Mockito.verify(mockedTTDRepository).findById(argumentCaptorLong.capture());
        assertEquals(2L, argumentCaptorLong.getValue());

    }

    @Test
    void shouldAssignEmployeeToDayByIDs() {
        //given
        TimeTableDay ttd1 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 3),  LocalTime.of(8, 15),
                LocalTime.of(17, 30), 0.75, 8, null);
        ttd1.setWorkdayId(1L);

        TimeTableDay ttd2 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 4), LocalTime.of(8, 0),
                LocalTime.of(18, 15), 1, 8, null);
        ttd2.setWorkdayId(2L);

        User usr1 = new User("Admiral", "Schneider", "fussball95", "spamfilter@gmail.com", Role.EMPLOYEE);
        usr1.setId(1L);

        BDDMockito.given(mockedTTDRepository.findById(1L)).willReturn(Optional.of(ttd1));
        BDDMockito.given(mockedTTDRepository.findById(2L)).willReturn(Optional.of(ttd2));
        BDDMockito.given(mockedUserRepository.findById(1L)).willReturn(Optional.of(usr1));

        // when
        testedTTDService.assignEmployeeToDay(1L, 1L);
        testedTTDService.assignEmployeeToDay(2L, 1L);

        //then
        assertEquals(usr1, ttd1.getEmployee());
        assertEquals(usr1, ttd2.getEmployee());
        assertEquals(List.of(ttd1, ttd2), usr1.getTimeTableDayList());
    }


    @Test
    void shouldCallFinalizedSetterWithTrueAsArg() {
        TimeTableDay ttd2 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 4), LocalTime.of(8, 0),
                LocalTime.of(18, 15), 1, 8, null);
        ttd2.setWorkdayId(2L);
        BDDMockito.given(mockedTTDRepository.findById(2L)).willReturn(Optional.of(ttd2));

        boolean before = ttd2.isFinalized();
        testedTTDService.finalizeTimeTableDay(2L);

        assertTrue(ttd2.isFinalized());
        assertFalse(before);
    }

    @Test
    void shouldChangeAbsenceStatusToHolidayAndUpdateHolidayAccountOfEmployee() {
        User usr7 = new User("Waldo", "Holzkopf", "redbull!", "holzkopf@gmx.net", Role.EMPLOYEE);
        usr7.setId(7L);
        //Expected TimeTableDays:
        TimeTableDay ttd10 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 5), null,
                null, 0, 8, null);
        ttd10.setWorkdayId(10L);
        ttd10.assignEmployee(usr7);

        double before = usr7.getUrlaubstage();

        BDDMockito.given(mockedTTDRepository.findById(10L)).willReturn(Optional.of(ttd10));

        testedTTDService.changeAbsenceStatusToHoliday(10L, 8.0);

        assertEquals(AbsenceStatus.HOLIDAY, ttd10.getAbsenceStatus());
        assertEquals(before-1, usr7.getUrlaubstage());
        Mockito.verify(mockedEmailService).send(argumentCaptorString1.capture(), argumentCaptorString2.capture(), argumentCaptorString3.capture());
        assertEquals("holzkopf@gmx.net", argumentCaptorString1.getValue());
        assertEquals("Beantragen akzeptiert", argumentCaptorString2.getValue());
        assertEquals("Dein Beantragen wurde akzeptiert", argumentCaptorString3.getValue());
        }

    @Test
    void shouldThrowNullPointerException() {
        TimeTableDay ttd10 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 5), null,
                null, 0, 8, null);
        ttd10.setWorkdayId(10L);

        BDDMockito.given(mockedTTDRepository.findById(10L)).willReturn(Optional.of(ttd10));

        assertThrows(NullPointerException.class, () -> testedTTDService.changeAbsenceStatusToHoliday(10L, 8.0));
    }

    @Test
    void shouldReturnEmptySet() {
        BDDMockito.given(mockedTTDRepository.getAllHolidaysOnDate(LocalDate.of(2022, Month.JANUARY, 5))).willReturn(Collections.emptyList());
        Set<User> actual = testedTTDService.showAllEmployeesOnHoliday(LocalDate.of(2022, Month.JANUARY, 5));
        assertEquals(Collections.emptySet(), actual);
    }

    @Test
    void shouldReturnSetOfTwoEmployees() {
        //Expected Workdays
        TimeTableDay ttd10 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 5), null,
                null, 0, 6, AbsenceStatus.HOLIDAY);
        ttd10.setWorkdayId(10L);
        TimeTableDay ttd11 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 5), null,
                null, 0, 8, AbsenceStatus.HOLIDAY);
        ttd11.setWorkdayId(11L);

        //Expected Employees:
        User usr1 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
        usr1.setId(1L);
        User usr2 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.EMPLOYEE);
        usr2.setId(2L);

        ttd10.assignEmployee(usr1);
        ttd11.assignEmployee(usr2);

        BDDMockito.given(mockedTTDRepository.getAllHolidaysOnDate(LocalDate.of(2022, Month.JANUARY, 5))).willReturn(List.of(ttd10, ttd11));

        Set<User> actual = testedTTDService.showAllEmployeesOnHoliday(LocalDate.of(2022, Month.JANUARY, 5));

        assertEquals(Set.of(usr1, usr2), actual);
    }

// Ill stop writing tests here because i don´t see a learning effect that would come from this (I wouldnt use anything new)
}
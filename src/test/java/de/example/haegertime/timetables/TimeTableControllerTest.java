package de.example.haegertime.timetables;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(TimeTableController.class)
class TimeTableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private TimeTableService mockedTimeTableService;

    private TimeTableController testedController;

    @Test
    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
    void shouldInvokeGetTimetableInServiceLayer() {
        testedController.getTimetable();

    }

    @Test
    void getTimetableDay() {
    }

    @Test
    void actualHourShow() {
    }

    @Test
    void totalHoursEmployeeShow() {
    }

    @Test
    void assignEmployeeToDay() {
    }

    @Test
    void assignProjectToDay() {
    }

    @Test
    void registerNewTimeTable() {
    }

    @Test
    void finalizeTimeTableDay() {
    }

    @Test
    void showOverTimeOfEmployeeById() {
    }

    @Test
    void changeAbsenceStatusToHoliday() {
    }

    @Test
    void changeAbsenceStatusToSick() {
    }

    @Test
    void showAllEmployeesInHoliday() {
    }

    @Test
    void showAllSickEmployees() {
    }
}
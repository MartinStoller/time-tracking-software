package de.example.haegertime.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();




//    @Test
//    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
//    void getAllUsers() throws Exception {
//        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
//        User user1 = new User("Nick", "Petersen", "alarm1", "stoller.martin@gmx.de", Role.ADMIN);
//        List<User> users = new ArrayList<>();
//        users.add(user);
//        users.add(user1);
//
//        //when
//        Mockito.when(userService.getAllUsers()).thenReturn(users);
//        //then
//        String url = "/api/users";
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        String expected = objectMapper.writeValueAsString(users);
//        String output = response.getContentAsString();
//        assertThat(expected).isEqualTo(output);
//        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());
//    }

//    @Test
//    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
//    void createUser() throws Exception {
//        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gx.de", Role.EMPLOYEE);
//        String input = objectMapper.writeValueAsString(user);
//        //when
//        Mockito.when(UserService.createUser(Mockito.any())).thenReturn(user);
//        //then
//        String url = "/api/users";
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(input);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        String output = result.getResponse().getContentAsString();
//        assertThat(input).isEqualTo(output);
//        assertThat(HttpStatus.CREATED.value()).isEqualTo(response.getStatus());
//    }

@Test
@WithMockUser(username = "user", password = "123", roles = "ADMIN")
void getByIdUser() throws Exception {
    User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gx.de", Role.EMPLOYEE);
    Long userId = 1L;
    //when
    Mockito.when(userService.findById(userId)).thenReturn(user);
    //then
    String url = "/api/users/"+userId;
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();
    String expected = objectMapper.writeValueAsString(user);
    String output = response.getContentAsString();
    assertThat(expected).isEqualTo(output);
    assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());
}

    @Test
    void getMyProjects() {
    }

    @Test
    void getPersonalOvertimeBalance() {
    }

    @Test
    void getOvertimeBalanceByEmail() {
    }


    @Test
    @WithMockUser(username = "user", password = "abc", roles = "ADMIN")
    void shouldDeleteUserById() throws Exception {
        Long id = 1L;
        //when
        Mockito.doNothing().when(userService).deleteUser(id);
        //then
        String url = "/api/users/" + id;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertThat(HttpStatus.NO_CONTENT.value()).isEqualTo(response.getStatus());
        Mockito.verify(userService, Mockito.times(1)).deleteUser(id);
    }


    void getByEmailUser() throws Exception {
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gx.de", Role.EMPLOYEE);
        String email = "martin.stoller2@gx.de";
        //when
        Mockito.when(userService.getByUsername(email)).thenReturn(user);
        //then
        String url = "/api/users/"+email;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String expected = objectMapper.writeValueAsString(user);
        String output = response.getContentAsString();
        assertThat(expected).isEqualTo(output);
        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());
    }

    @Test
    void showOwnWorkdays() {
    }

//    @Test
//    @WithMockUser(username = "user", password = "123" , roles = "ADMIN")
//    void updateUserDetails() throws Exception {
//        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gx.de", Role.EMPLOYEE);
//        String input = objectMapper.writeValueAsString(user);
//        //when
//        Mockito.when(UserService.updateUserDetails(Mockito.any())).thenReturn(user);
//        //then
//        String url = "/api/users/current-user/update";
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(input);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        String output = response.getContentAsString();
//        assertThat(output).isEqualTo(input);
//        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());
//    }


//    @Test
//    @WithMockUser(username = "user", password = "123" , roles = "ADMIN")
//    void changeUserName() throws Exception {
//        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gx.de", Role.EMPLOYEE);
//        String input = objectMapper.writeValueAsString(user);
//        //when
//        Mockito.when(UserService.updateUserName(Mockito.any())).thenReturn(user);
//        //then
//        String url = "/api/users/update/username/{id}";
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(input);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        String output = response.getContentAsString();
//        assertThat(output).isEqualTo(input);
//        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());
//    }


    @Test
    void deactivateUser() {
    }

    @Test
    void reactivateUser() {
    }


//    @Test
//    @WithMockUser(username = "user", password = "123" , roles = "ADMIN")
//    void updateRoleUser() throws Exception {
//        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gx.de", Role.EMPLOYEE);
//        String input = objectMapper.writeValueAsString(user);
//        //when
//        Mockito.when(UserService.updateRole(Mockito.any())).thenReturn(user);
//        //then
//        String url = "/api/users/update/role/{id}";
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(input);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        String output = response.getContentAsString();
//        assertThat(output).isEqualTo(input);
//        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());
//    }

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
    void declineRequestedHoliday() {
    }

    @Test
    void showAllMyHolidays() {
    }
}

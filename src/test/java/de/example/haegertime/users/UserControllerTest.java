package de.example.haegertime.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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


    @Test
    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
    void getByIdUser() throws Exception {
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gx.de", Role.EMPLOYEE);
        Long userId = 1L;
        //when
        Mockito.when(userService.findById(userId)).thenReturn(user);
        //then
        String url = "/api/users/" + userId;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String expected = objectMapper.writeValueAsString(user);
        String output = response.getContentAsString();
        assertThat(expected).isEqualTo(output);
        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());
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

    @Test
    @Disabled
    @WithMockUser(username = "user", password = "abc", roles = "ADMIN")
    void getByEmailUser() throws Exception {
        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gx.de", Role.EMPLOYEE);
        String email = "martin.stoller2@gx.de";

        Mockito.when(userService.getByUsername(email)).thenReturn(user);

        String url = "/api/users/" + email;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String expected = objectMapper.writeValueAsString(user);
        String output = response.getContentAsString();
        assertThat(expected).isEqualTo(output);
        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());
    }
}

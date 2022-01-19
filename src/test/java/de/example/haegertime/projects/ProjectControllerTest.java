package de.example.haegertime.projects;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.example.haegertime.users.UserRepository;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProjectService projectService;
    @MockBean
    private ProjectRepository projectRepository;
    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
    void itShouldFindAllProject() throws Exception{
        Project p1 = new Project("ABC", LocalDate.of(2020, Month.APRIL,1), null);
        Project p2 = new Project("XYZ", LocalDate.of(2022, Month.JANUARY, 1), null);
        List<Project> projects = new ArrayList<>();
        projects.add(p1);
        projects.add(p2);
        //when
        Mockito.when(projectService.findAll()).thenReturn(projects);
        //then
        String url = "/api/projects";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String expected = objectMapper.writeValueAsString(projects);
        String output = response.getContentAsString();
        assertThat(expected).isEqualTo(output);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
    void itShouldUpdateProject() throws Exception{
        //given
        Project p1 = new Project("ABC", LocalDate.of(2020, Month.APRIL,1),
                LocalDate.of(2021, Month.JANUARY, 13));
        String inputJson = objectMapper.writeValueAsString(p1);
        //when
        Mockito.when(projectService.updateProject(Mockito.any(Long.TYPE), Mockito.any(Project.class))).thenReturn(p1);
        //then
        String url = "/api/projects/1";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String output = response.getContentAsString();
        assertThat(output).isEqualTo(inputJson);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
    void itShouldGetProjectById() throws Exception{
        Project p1 = new Project("ABC", LocalDate.of(2020, Month.APRIL,1), null);
        //when
        Mockito.when(projectService.getById(1L)).thenReturn(p1);
        //then
        String url = "/api/projects/1";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String expected = objectMapper.writeValueAsString(p1);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, response.getContentAsString());
    }


    @Test
    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
    void itShouldDeleteProjectById() throws Exception{
        //when
        Mockito.doNothing().when(projectService).deleteById(1L);
        //then
        String url = "/api/projects/1";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertThat(HttpStatus.NO_CONTENT.value()).isEqualTo(response.getStatus());
        Mockito.verify(projectService, times(1)).deleteById(1L);
    }
}
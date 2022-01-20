package de.example.haegertime.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.example.haegertime.projects.Project;
import de.example.haegertime.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private CustomerRepository customerRepository;
    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
    void findAllCustomer() throws Exception {
        Customer c1 = new Customer("abc","abc");
        Customer c2 = new Customer("asd","asd");
        List<Customer> customers = new ArrayList<>();
        customers.add(c1);
        customers.add(c2);
        Project p = new Project("abc", LocalDate.of(2020, Month.JANUARY, 11), null);
        List<Project> projects = new ArrayList<>();
        //when
        Mockito.when(customerService.findAll()).thenReturn(customers);
        //then
        String url = "/api/customers";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String expected = objectMapper.writeValueAsString(customers);
        String output = response.getContentAsString();
        assertThat(expected).isEqualTo(output);
        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());

    }

    @Test
    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
    void createCustomer() throws Exception {
        Customer customer = new Customer("abc","abc");
        String input = objectMapper.writeValueAsString(customer);
        //when
        Mockito.when(customerService.createCustomer(Mockito.any())).thenReturn(customer);
        //then
        String url = "/api/customers";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(input);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String output = result.getResponse().getContentAsString();
        assertThat(input).isEqualTo(output);
        assertThat(HttpStatus.CREATED.value()).isEqualTo(response.getStatus());
    }

    @Test
    @WithMockUser(username = "user", password = "123", roles = "ADMIN")
    void findByIdCustomer() throws Exception {
        Customer customer = new Customer("adm","adm");
        Long customerId = 1L;
        //when
        Mockito.when(customerService.findById(customerId)).thenReturn(customer);
        //then
        String url = "/api/customers/"+customerId;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String expected = objectMapper.writeValueAsString(customer);
        String output = response.getContentAsString();
        assertThat(expected).isEqualTo(output);
        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());
    }

    @Test
    @WithMockUser(username = "user", password = "123" , roles = "ADMIN")
    void updateCustomer() throws Exception {
        Customer customer = new Customer("abc","abc");
        String input = objectMapper.writeValueAsString(customer);
        //when
        Mockito.when(customerService.updateCustomer(Mockito.any())).thenReturn(customer);
        //then
        String url = "/api/customers";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(input);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String output = response.getContentAsString();
        assertThat(output).isEqualTo(input);
        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatus());
    }

    @Test
    @WithMockUser(username = "asd", password = "asd", roles = "ADMIN")
    void addProjectToExistingCustomer() throws Exception {
        Customer customer = new Customer("abc","abc");
        Project project = new Project("abc", LocalDate.of(2020, Month.APRIL, 1), null);
        Long customerId = 1L;
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        customer.setProjects(projects);
        customerRepository.save(customer);
        String input = objectMapper.writeValueAsString(project);
        //when
        Mockito.when(customerService.addProjectToExistingCustomer(Mockito.any(Long.TYPE), Mockito.any())).thenReturn(customer);
        String url = "/api/customers/"+customerId+"/addproject";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(input);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String output = response.getContentAsString();
        String expected = objectMapper.writeValueAsString(customer);
        assertThat(HttpStatus.CREATED.value()).isEqualTo(response.getStatus());
        assertThat(expected).isEqualTo(output);
    }

    @Test
    @WithMockUser(username = "user", password = "abc", roles = "ADMIN")
    void deleteCustomerById() throws Exception{
        Long customerId = 1L;
        //when
        Mockito.doNothing().when(customerService).deleteCustomerById(customerId);
        //then
        String url = "/api/customers/"+customerId;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertThat(HttpStatus.NO_CONTENT.value()).isEqualTo(response.getStatus());
        Mockito.verify(customerService, Mockito.times(1)).deleteCustomerById(customerId);
    }
}
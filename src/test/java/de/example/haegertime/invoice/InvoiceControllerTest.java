package de.example.haegertime.invoice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.example.haegertime.customer.Customer;
import de.example.haegertime.customer.CustomerController;
import de.example.haegertime.projects.Project;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InvoiceService invoiceService;
    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private UserRepository userRepository;


    @Test
    @WithMockUser(username = "user", password = "abc", authorities = {"ADMIN"})
    void exportToExcel() throws Exception {
        Long customerId = 1L;
        Long projectId = 1L;
        Mockito.doNothing().when(invoiceService).exportToExcel(Mockito.any(),
                Mockito.any(Long.TYPE), Mockito.any(Long.TYPE));
        //then
        String url = "/api/invoice/export/excel";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url).param(
                "customerId",String.valueOf(customerId))
                .param("projectId", String.valueOf(projectId))
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "user", password = "abc", authorities = {"ADMIN"})
    void exportToPdf() throws Exception {
        Long customerId = 1L;
        Long projectId = 1L;
        Mockito.doNothing().when(invoiceService).exportToPdf(Mockito.any(),
                Mockito.any(Long.TYPE), Mockito.any(Long.TYPE));
        //then
        String url = "/api/invoice/export/pdf";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url).param(
                "customerId",String.valueOf(customerId))
                .param("projectId", String.valueOf(projectId))
                .contentType(MediaType.APPLICATION_PDF_VALUE);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "user", password = "abc", authorities = {"ADMIN"})
    void exportToXML() throws Exception{
        Long customerId = 1L;
        Long projectId = 1L;
        Customer customer = new Customer("abc","abc");
        Project project = new Project("abc", LocalDate.of(2000, Month.APRIL, 1), null);
        List<User> employees = new ArrayList<>();
        List<Double> hours = new ArrayList<>();
        InvoiceXMLExporter invoiceXMLExporter = new InvoiceXMLExporter(customer, project, employees, hours);
        Mockito.when(invoiceService.exportToXML(Mockito.any(HttpServletResponse.class),
                Mockito.any(Long.TYPE), Mockito.any(Long.TYPE))).thenReturn(invoiceXMLExporter);
        //then
        String url = "/api/invoice/export/pdf";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url).param(
                "customerId",String.valueOf(customerId))
                .param("projectId", String.valueOf(projectId))
                .contentType(MediaType.APPLICATION_XML_VALUE);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }
}
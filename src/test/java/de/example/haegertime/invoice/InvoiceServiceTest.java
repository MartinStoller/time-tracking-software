package de.example.haegertime.invoice;

import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.customer.Customer;
import de.example.haegertime.customer.CustomerRepository;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import de.example.haegertime.timetables.TimeTableRepository;
import de.example.haegertime.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TimeTableRepository timeTableRepository;
    @Mock
    private CustomerRepository customerRepository;

    private InvoiceService underTest;

    @BeforeEach
    void setUp() {
        underTest = new InvoiceService(invoiceRepository, projectRepository,
                customerRepository, timeTableRepository, userRepository);
    }



    @Test
    void exportToPdf() throws IOException {
        Long customerId = 1L;
        Long projectId = 1L;
        Project project = new Project("abc", LocalDate.of(2000, Month.APRIL, 11), null);
        Customer customer = new Customer("abc", "abc");
        MockHttpServletResponse response = new MockHttpServletResponse();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename_invoice_"+currentDateTime+".pdf";
        response.setHeader(headerKey, headerValue);
        //when
        when(projectRepository.existsProjectByIdAndCustomerID(customerId, projectId)).thenReturn(Optional.of(project));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        underTest.exportToPdf(response, customerId, projectId);
        //then
        // TODO: was muss man hier verifizieren?
    }

    @Test
    void exportToPdfCustomerNotFound() {
        Long customerId = 1L;
        Long projectId = 1L;
        Project project = new Project("abc", LocalDate.of(2000, Month.APRIL, 11), null);

        MockHttpServletResponse response = new MockHttpServletResponse();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename_invoice_"+currentDateTime+".pdf";
        response.setHeader(headerKey, headerValue);
        //when
        when(projectRepository.existsProjectByIdAndCustomerID(customerId, projectId)).thenReturn(Optional.of(project));


        assertThatThrownBy(() -> underTest.exportToPdf(response, customerId, projectId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Der Kunde mit Id "+customerId+" nicht in der DB");
    }

    @Test
    void exportToPdfProjectNotFound() {
        Long customerId = 1L;
        Long projectId = 1L;
        Project project = new Project("abc", LocalDate.of(2000, Month.APRIL, 11), null);
        Customer customer = new Customer("abc", "abc");
        MockHttpServletResponse response = new MockHttpServletResponse();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename_invoice_"+currentDateTime+".pdf";
        response.setHeader(headerKey, headerValue);
        //when
        when(projectRepository.existsProjectByIdAndCustomerID(customerId, projectId)).thenReturn(Optional.of(project));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));


        assertThatThrownBy(() -> underTest.exportToPdf(response, customerId, projectId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Das Projekt mit Id "+projectId+" nicht in der DB");
    }


    @Test
    void exportToExcel() throws IOException {
        Long customerId = 1L;
        Long projectId = 1L;
        Project project = new Project("abc", LocalDate.of(2000, Month.APRIL, 11), null);
        Customer customer = new Customer("abc", "abc");
        MockHttpServletResponse response = new MockHttpServletResponse();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename_invoice_"+currentDateTime+".xlsx";
        response.setHeader(headerKey, headerValue);
        //when
        when(projectRepository.existsProjectByIdAndCustomerID(customerId, projectId)).thenReturn(Optional.of(project));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        underTest.exportToExcel(response, customerId, projectId);
    }

    @Test
    void exportToExcelCustomerNotFound() {
        Long customerId = 1L;
        Long projectId = 1L;
        Project project = new Project("abc", LocalDate.of(2000, Month.APRIL, 11), null);

        MockHttpServletResponse response = new MockHttpServletResponse();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename_invoice_"+currentDateTime+".xlsx";
        response.setHeader(headerKey, headerValue);
        //when
        when(projectRepository.existsProjectByIdAndCustomerID(customerId, projectId)).thenReturn(Optional.of(project));

        //then
        assertThatThrownBy(() -> underTest.exportToExcel(response, customerId, projectId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Der Kunde mit Id "+customerId+" nicht in der DB");
    }

    @Test
    void exportToExcelProjectNotFound() {
        Long customerId = 1L;
        Long projectId = 1L;
        Project project = new Project("abc", LocalDate.of(2000, Month.APRIL, 11), null);
        Customer customer = new Customer("abc", "abc");
        MockHttpServletResponse response = new MockHttpServletResponse();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename_invoice_"+currentDateTime+".xlsx";
        response.setHeader(headerKey, headerValue);
        //when
        when(projectRepository.existsProjectByIdAndCustomerID(customerId, projectId)).thenReturn(Optional.of(project));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        //then
        assertThatThrownBy(() -> underTest.exportToExcel(response, customerId, projectId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Das Projekt mit Id "+projectId+" nicht in der DB");
    }

    @Test
    void exportToXML() throws IOException {
        Long customerId = 1L;
        Long projectId = 1L;
        Project project = new Project("abc", LocalDate.of(2000, Month.APRIL, 11), null);
        Customer customer = new Customer("abc", "abc");
        MockHttpServletResponse response = new MockHttpServletResponse();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename_invoice_"+currentDateTime+".xml";
        response.setHeader(headerKey, headerValue);
        //when
        when(projectRepository.existsProjectByIdAndCustomerID(customerId, projectId)).thenReturn(Optional.of(project));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        InvoiceXMLExporter exporter = underTest.exportToXML(response, customerId, projectId);
        //then
        assertThat(exporter).isInstanceOf(InvoiceXMLExporter.class);
    }

    @Test
    void exportToXMLCustomerNotFound() {
        Long customerId = 1L;
        Long projectId = 1L;
        Project project = new Project("abc", LocalDate.of(2000, Month.APRIL, 11), null);

        MockHttpServletResponse response = new MockHttpServletResponse();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename_invoice_"+currentDateTime+".xml";
        response.setHeader(headerKey, headerValue);
        //when
        when(projectRepository.existsProjectByIdAndCustomerID(customerId, projectId)).thenReturn(Optional.of(project));

        assertThatThrownBy(() -> underTest.exportToXML(response, customerId, projectId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Der Kunde mit Id "+customerId+" nicht in der DB");

    }

    @Test
    void exportToXMLProjectNotFound() {
        Long customerId = 1L;
        Long projectId = 1L;
        Project project = new Project("abc", LocalDate.of(2000, Month.APRIL, 11), null);
        Customer customer = new Customer("abc", "abc");
        MockHttpServletResponse response = new MockHttpServletResponse();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename_invoice_"+currentDateTime+".xml";
        response.setHeader(headerKey, headerValue);
        //when
        when(projectRepository.existsProjectByIdAndCustomerID(customerId, projectId)).thenReturn(Optional.of(project));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        //when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        assertThatThrownBy(() -> underTest.exportToXML(response, customerId, projectId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("Das Projekt mit Id "+projectId+" nicht in der DB");

    }

    @Test
    void exportToXMLProjectByIdAndCustomerIdNotExists() {
        Long customerId = 1L;
        Long projectId = 1L;

        MockHttpServletResponse response = new MockHttpServletResponse();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename_invoice_"+currentDateTime+".xml";
        response.setHeader(headerKey, headerValue);

        assertThatThrownBy(() -> underTest.exportToXML(response, customerId, projectId))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Die Eingabe war nicht richtig");
    }

}
package de.example.haegertime.invoice;

import de.example.haegertime.advice.ItemNotFoundException;
import de.example.haegertime.customer.Customer;
import de.example.haegertime.customer.CustomerRepository;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import de.example.haegertime.timetables.TimeTableRepository;
import de.example.haegertime.timetables.TimeTableService;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;
    private final TimeTableService timeTableService;
    private final UserRepository userRepository;



    public void exportToPdf(HttpServletResponse response, Long customerId, Long projectId) throws IOException {
        boolean exists = projectRepository.existsProjectByIdAndCustomerID(customerId, projectId).isPresent();
        if(exists) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=invoice_"+currentDateTime+".pdf";
            response.setHeader(headerKey, headerValue);
            response.setContentType("application/pdf");
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ItemNotFoundException("Der Kunde mit Id "+customerId+" nicht in der DB")
            );
            Project project = projectRepository.findById(projectId).orElseThrow(
                    () -> new ItemNotFoundException("Das Projekt mit Id "+projectId+" nicht in der DB")
            );
            List<List<Double>> hoursAndEmployees = getEmployeesAndTotalHours(projectId);
            List<User> employees = getAllEmployees(hoursAndEmployees);
            List<Double> allSumOfActualHours = getTotalHours(hoursAndEmployees);
            InvoicePDFExporter exporter = new InvoicePDFExporter(customer, project, employees, allSumOfActualHours);
            exporter.export(response.getOutputStream());
        }
    }

    public void exportToExcel(HttpServletResponse response, Long customerId, Long projectId) throws IOException {
        boolean exists = projectRepository.existsProjectByIdAndCustomerID(customerId, projectId).isPresent();
        if(exists) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=invoice_"+currentDateTime+".xlsx";
            response.setHeader(headerKey, headerValue);
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ItemNotFoundException("Der Kunde mit Id "+customerId+" nicht in der DB")
            );
            Project project = projectRepository.findById(projectId).orElseThrow(
                    () -> new ItemNotFoundException("Das Projekt mit Id "+projectId+" nicht in der DB")
            );
            List<List<Double>> hoursAndEmployees = getEmployeesAndTotalHours(projectId);
            List<User> employees = getAllEmployees(hoursAndEmployees);
            List<Double> allSumOfActualHours = getTotalHours(hoursAndEmployees);
            InvoiceExcelExporter exporter = new InvoiceExcelExporter(customer, project, employees, allSumOfActualHours);
            exporter.export(response.getOutputStream());
        }
    }

    public InvoiceXMLExporter exportToXML(HttpServletResponse response, Long customerId, Long projectId) throws IOException {
        boolean exists = projectRepository.existsProjectByIdAndCustomerID(customerId, projectId).isPresent();
        if(exists) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=invoice_"+currentDateTime+".xml";
            response.setHeader(headerKey, headerValue);
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ItemNotFoundException("Der Kunde mit Id "+customerId+" nicht in der DB")
            );
            Project project = projectRepository.findById(projectId).orElseThrow(
                    () -> new ItemNotFoundException("Das Projekt mit Id "+projectId+" nicht in der DB")
            );
            List<List<Double>> hoursAndEmployees = getEmployeesAndTotalHours(projectId);
            List<User> employees = getAllEmployees(hoursAndEmployees);
            List<Double> allSumOfActualHours = getTotalHours(hoursAndEmployees);
            return new InvoiceXMLExporter(customer, project, employees, allSumOfActualHours);
        } else {
            throw new IOException("Die Eingabe war nicht richtig");
        }
    }


    private List<List<Double>> getEmployeesAndTotalHours(Long projectId) {
        return timeTableService.getTotalWorkingHoursOnAProjectGroupedByEmployeeId(projectId);
    }

    private List<User> getAllEmployees(List<List<Double>> list) {
        List<User> users = new ArrayList<>();
        for (List<Double> item : list) {
            long userId = item.get(1).longValue();
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new ItemNotFoundException("Der Kunde mit Id "+userId+" nicht in der DB")
            );
            users.add(user);
        }
        return users;
    }

    private List<Double> getTotalHours(List<List<Double>> list) {
        List<Double> totalHoursList = new ArrayList<>();
        for (List<Double> item : list) {
            Double hours = item.get(0);
            totalHoursList.add(hours);
        }
        return totalHoursList;
    }

}

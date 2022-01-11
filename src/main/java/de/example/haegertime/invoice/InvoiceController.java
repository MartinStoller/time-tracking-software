package de.example.haegertime.invoice;

import com.lowagie.text.DocumentException;
import de.example.haegertime.customer.Customer;
import de.example.haegertime.customer.CustomerService;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import de.example.haegertime.projects.ProjectService;
import de.example.haegertime.timetables.TimeTableRepository;
import de.example.haegertime.timetables.TimeTableService;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import de.example.haegertime.users.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.DoubleStream;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final CustomerService customerService;
    private final ProjectService projectService;
    private final TimeTableService timeTableService;
    private final UserService userService;

    public InvoiceController(InvoiceService invoiceService,
                             CustomerService customerService,
                             ProjectService projectService,
                             TimeTableService timeTableService, UserService userService) {
        this.invoiceService = invoiceService;
        this.customerService = customerService;
        this.projectService = projectService;
        this.timeTableService = timeTableService;
        this.userService = userService;
    }


    @GetMapping("/all")
    public List<Invoice> getAllInvoice() {
        return invoiceService.getAllInvoice();
    }


    @PostMapping("/create")
    public Invoice createInvoice(Invoice invoice) {
        return invoiceService.createInvoice(invoice);
    }


    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response, @RequestParam Long customerId,
                              @RequestParam Long projectId) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=invoice_"+currentDateTime+".xlsx";
        response.setHeader(headerKey, headerValue);

        Customer customer = customerService.findByIdCustomer(customerId);
        Project project = projectService.getById(projectId);
        List<List<Double>> hoursAndEmployees = getEmployeesAndTotalHours(projectId);
        List<User> employees = getAllEmployees(hoursAndEmployees);
        List<Double> totalHours = getTotalHours(hoursAndEmployees);
        InvoiceExcelExporter invoiceExcelExporter =
                new InvoiceExcelExporter(customer, project, employees, totalHours);
        invoiceExcelExporter.export(response);
    }


    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response,@RequestParam Long customerId,
                            @RequestParam Long projectId) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=invoice_"+currentDateTime+".pdf";
        response.setHeader(headerKey, headerValue);

        Customer customer = customerService.findByIdCustomer(customerId);
        Project project = projectService.getById(projectId);
        List<List<Double>> hoursAndEmployees = getEmployeesAndTotalHours(projectId);
        List<User> employees = getAllEmployees(hoursAndEmployees);
        List<Double> totalHours = getTotalHours(hoursAndEmployees);
        InvoicePDFExporter exporter = new InvoicePDFExporter(customer, project, employees, totalHours);
        exporter.export(response);
    }







    private List<List<Double>> getEmployeesAndTotalHours(Long projectId) {
        return timeTableService.totalHoursAllEmployeeOnAProject(projectId);
    }

    private List<User> getAllEmployees(List<List<Double>> list) {
        List<User> users = new ArrayList<>();

        for (int i=0; i < list.size(); i++) {
            List<Double> item = list.get(i);
            Long userId = item.get(1).longValue();
            User user = userService.findByIdUser(userId);
            users.add(user);
        }
        return users;
    }

    private List<Double> getTotalHours(List<List<Double>> list) {
        List<Double> totalHoursList = new ArrayList<>();
        for (int i=0; i < list.size(); i++) {
            List<Double> item = list.get(i);
            Double hours = item.get(0);
            totalHoursList.add(hours);
        }
        return totalHoursList;
    }

}

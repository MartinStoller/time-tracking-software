package de.example.haegertime.invoice;

import de.example.haegertime.customer.Customer;
import de.example.haegertime.customer.CustomerService;
import de.example.haegertime.projects.Project;
import de.example.haegertime.projects.ProjectRepository;
import de.example.haegertime.timetables.TimeTableRepository;
import de.example.haegertime.users.User;
import de.example.haegertime.users.UserRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final CustomerService customerService;
    private final ProjectRepository projectRepository;
    private final TimeTableRepository timeTableRepository;
    private final UserRepository userRepository;

    public InvoiceController(InvoiceService invoiceService,
                             CustomerService customerService,
                             ProjectRepository projectRepository,
                             TimeTableRepository timeTableRepository, UserRepository userRepository) {
        this.invoiceService = invoiceService;
        this.customerService = customerService;
        this.projectRepository = projectRepository;
        this.timeTableRepository = timeTableRepository;
        this.userRepository = userRepository;
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
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=invoice.xlsx";
        response.setHeader(headerKey, headerValue);

        Customer customer = customerService.findByIdCustomer(customerId);
        Project project = projectRepository.getById(projectId);
        //List<Object> hoursAndEmployees = getEmployeesAndTotalHours(projectId);
        //List<User> employees = getAllEmployees(hoursAndEmployees);
        //List<Double> totalHours = getTotalHours(hoursAndEmployees);
        InvoiceExcelExporter invoiceExcelExporter = new InvoiceExcelExporter(customer, project, null, null);
        invoiceExcelExporter.export(response);
    }
    /*

    private List<Object> getEmployeesAndTotalHours(Long projectId) {
        return timeTableRepository.getTotalHoursAllEmployeeOnAProject(projectId);
    }

    private List<User> getAllEmployees(List<Object> list) {
        List<User> users = new ArrayList<>();
        int size = list.size();
        Double[][] maptoDouble = (Double[][]) list.stream().toArray();

        for (int i=0; i < size; i++) {
            double userId = maptoDouble[i][1];
            Long userLongId = (long)userId;
            User user = userRepository.findById(userLongId).get();
            users.add(user);
        }
        return users;
    }

    private List<Double> getTotalHours(List<Object> list) {
        List<Double> totalHoursList = new ArrayList<>();
        int size = list.size();
        Double[][] maptoDouble = (Double[][]) list.stream().toArray();
        for (int i=0; i < size; i++) {
            double totalHours = maptoDouble[i][0];
            totalHoursList.add(totalHours);
        }
        return totalHoursList;
    }
    */
}

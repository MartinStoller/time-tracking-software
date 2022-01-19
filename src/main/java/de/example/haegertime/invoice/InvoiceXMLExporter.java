package de.example.haegertime.invoice;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import de.example.haegertime.customer.Customer;
import de.example.haegertime.projects.Project;
import de.example.haegertime.users.User;

import java.util.List;

@JacksonXmlRootElement
public class InvoiceXMLExporter {
    @JacksonXmlProperty
    private Customer customer;
    @JacksonXmlProperty
    private Project project;
    @JacksonXmlProperty
    private List<User> employees;
    @JacksonXmlProperty
    private List<Double> totalHours;

    public InvoiceXMLExporter(Customer customer, Project project,
                              List<User> employees, List<Double> totalHours) {
        this.customer = customer;
        this.project = project;
        this.employees = employees;
        this.totalHours = totalHours;
    }
}

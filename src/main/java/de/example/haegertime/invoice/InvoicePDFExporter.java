package de.example.haegertime.invoice;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import de.example.haegertime.customer.Customer;
import de.example.haegertime.projects.Project;
import de.example.haegertime.users.User;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

@JacksonXmlRootElement
public class InvoicePDFExporter {
    @JacksonXmlProperty
    private Customer customer;
    @JacksonXmlProperty
    private Project project;
    @JacksonXmlProperty
    private List<User> employees;
    @JacksonXmlProperty
    private List<Double> totalHours;

    public InvoicePDFExporter(Customer customer, Project project,
                              List<User> employees, List<Double> totalHours) {
        this.customer = customer;
        this.project = project;
        this.employees = employees;
        this.totalHours = totalHours;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.WHITE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("Customer Id"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Customer"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Customer Address"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Project Id"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Project"));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        table.addCell(String.valueOf(customer.getId()));
        table.addCell(customer.getName());
        table.addCell(customer.getAddress());

        table.addCell(String.valueOf(project.getId()));
        table.addCell(project.getTitle());

    }

    private void writeTableHeaderEmployee(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.WHITE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("Employee Id"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Employee First"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Employee Last"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Total Hours"));
        table.addCell(cell);
    }

    private void writeTableEmployeeData(PdfPTable table) {
        int index = 0;
        for (User user : employees) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getFirstname());
            table.addCell(user.getLastname());
            table.addCell(String.valueOf(totalHours.get(index)));
            index+=1;
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(16);
        font.setColor(Color.BLACK);

        Paragraph p = new Paragraph("Daten", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        Paragraph p2 = new Paragraph("Employees List", font);
        p2.setAlignment(Paragraph.ALIGN_CENTER);
        p2.setSpacingBefore(10);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        //Employee und Total Hours

        document.add(p2);

        PdfPTable table2 = new PdfPTable(4);
        table2.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table2.setSpacingBefore(30);

        writeTableHeaderEmployee(table2);
        writeTableEmployeeData(table2);

        document.add(table2);

        document.close();
    }
}

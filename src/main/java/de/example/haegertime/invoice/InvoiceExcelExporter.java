package de.example.haegertime.invoice;

import de.example.haegertime.customer.Customer;
import de.example.haegertime.projects.Project;
import de.example.haegertime.users.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class InvoiceExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private Customer customer;
    private Project project;
    private List<User> employees;
    private List<Double> totalHours;

    public InvoiceExcelExporter (Customer customer, Project project,
                                 List<User> employees, List<Double> totalHours) {
        this.customer = customer;
        this.project = project;
        this.employees = employees;
        this.totalHours = totalHours;
        workbook = new XSSFWorkbook();
    }

    public void writeHeaderLine() {
        sheet = workbook.createSheet("invoice");
        Row row = sheet.createRow(0);
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont xssfFont = workbook.createFont();
        xssfFont.setBold(true);
        xssfFont.setFontHeight(14);
        cellStyle.setFont(xssfFont);

        createCell(row, 0, "Customer ID", cellStyle);
        createCell(row, 1, "Customer Name", cellStyle);
        createCell(row, 2 , "Adresse", cellStyle);
        createCell(row, 3, "Project ID", cellStyle);
        createCell(row, 4, "Project", cellStyle);

        Row row2 = sheet.createRow(5);
        createCell(row2, 0, "Employee Id", cellStyle);
        createCell(row2, 1, "Employee First Name", cellStyle);
        createCell(row2, 2, "Employee Last Name", cellStyle);
        createCell(row2, 3, "Total Work Hours", cellStyle);
    }





    private void createCell(Row row, int columnCount, Object value, CellStyle cellStyle) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double){
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(cellStyle);
    }


    private void writeDataLines() {

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        style.setFont(font);

        Row row = sheet.createRow(1);
        createCell(row, 0, customer.getId(), style);
        createCell(row, 1, customer.getName(), style);
        createCell(row, 2, customer.getAddress(), style);
        createCell(row, 3, project.getId(), style);
        createCell(row, 4, project.getTitle(), style);

        int rowCount = 6;
        int totalHourIndex = 0;
        for (User employee : employees) {
            Row row2 = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row2, columnCount++, employee.getId(), style);
            createCell(row2, columnCount++, employee.getFirst(), style);
            createCell(row2, columnCount++, employee.getLast(), style);
            createCell(row2, columnCount++, totalHours.get(totalHourIndex), style);
            totalHourIndex++;
        }


    }

    public void export(OutputStream outputStream) throws IOException {
        writeHeaderLine();
        writeDataLines();

        //ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

}

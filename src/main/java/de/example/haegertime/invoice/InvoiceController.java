package de.example.haegertime.invoice;

import com.lowagie.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/invoice")
@AllArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;



    @GetMapping
    public List<Invoice> getAllInvoice() {
        return invoiceService.getAllInvoice();
    }


    @PostMapping
    public Invoice createInvoice(Invoice invoice) {
        return invoiceService.createInvoice(invoice);
    }


    @PostMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToExcel(HttpServletResponse response, @RequestParam Long customerId,
                              @RequestParam Long projectId) throws IOException{
        invoiceService.exportToExcel(response, customerId, projectId);
    }


    @PostMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void exportToPDF(HttpServletResponse response,@RequestParam Long customerId,
                            @RequestParam Long projectId) throws DocumentException, IOException {
        invoiceService.exportToPdf(response, customerId, projectId);
    }


    @PostMapping(value = "/export/xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceXMLExporter exportToXML(HttpServletResponse response, Long customerId, Long projectId)
            throws IOException {
        return invoiceService.exportToXML(response, customerId, projectId);
    }


}

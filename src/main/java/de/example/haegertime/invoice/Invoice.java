package de.example.haegertime.invoice;

import de.example.haegertime.customer.Customer;
import de.example.haegertime.timetables.TimeTableDay;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "Invoice")
@Data
@NoArgsConstructor
public class Invoice {


    @Id
    @SequenceGenerator(
            name = "invoice_sequence",
            sequenceName = "invoice_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "invoice_sequence"
    )
    private Long id;

    private Long customerId;

    private Long projektId;

}

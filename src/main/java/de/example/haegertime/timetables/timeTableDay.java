package de.example.haegertime.timetables;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "timetable")
@Validated
public class timeTableDay {
    @Id
    @SequenceGenerator(
            name="user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;  //not sure yet if we really need an Id, but probably cant hurt to have a unique identifier for each line
    private LocalDate date;

}

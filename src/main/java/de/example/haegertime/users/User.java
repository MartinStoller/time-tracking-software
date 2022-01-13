package de.example.haegertime.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.example.haegertime.projects.Project;
import de.example.haegertime.timetables.TimeTableDay;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="users")
@Validated
@AllArgsConstructor
public class User {
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
    private Long id;

    @JsonIgnore
    @OneToMany(targetEntity = TimeTableDay.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", referencedColumnName = "id")
    List<TimeTableDay> timeTableDayList;

    @NotBlank @NotNull
    private String first;
    @NotBlank @NotNull
    private String last;
    @NotBlank @Size(min=5, message = "Password is too short! It requires at least 5 characters.") @Size(max = 30, message = "Password is too long!")
    private String password;
    @Column(unique = true) @Email
    private String email;
    private Role role;
    private boolean frozen; //frozen = true -> activ, frozen = false -> deactiv :)

    //Ab hier Urlaubstage (double), da man auch halbe Tage nehmen kann
    private double urlaubstage;

    public User(){}

    public User(String first, String last, String password, String email, Role role){
        this.first = first;
        this.last = last;
        this.password = password;
        this.email = email;
        this.role = role;
        this.frozen = true;
        this.urlaubstage = 30;
    }

}

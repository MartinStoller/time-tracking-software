package de.example.haegertime;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public class Test {
    public static void main(String[] args) throws ParseException {
        DateFormat format = new SimpleDateFormat("HHmm");
        System.out.println(LocalTime.of(13, 55));
    }
}

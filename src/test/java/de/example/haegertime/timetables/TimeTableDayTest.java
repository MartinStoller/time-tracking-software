package de.example.haegertime.timetables;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class TimeTableDayTest {

    @Test
    void testEqualsIfOIsNull() {
        TimeTableDay ttd11 = new TimeTableDay(
                LocalDate.of(2022, Month.JANUARY, 5), null,
                null, 0, 8, AbsenceStatus.HOLIDAY);

        boolean actual = ttd11.equals(null);

        assertEquals(false, actual);
    }
}
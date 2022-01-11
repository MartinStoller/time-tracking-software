package de.example.haegertime.timetables;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.time.Duration;

public class DatesAndDurationsCalculator {
    public static String substractDurationStrings(String dur1, String dur2) {
        /**
         * Takes 2 Strings, which must be in the HH:MM Format as input and delivers the Difference of Input1-Input2.
         * Needed to calculate the actual working hours by substracting the break duration
         */
        PeriodFormatter durationFormatter = new PeriodFormatterBuilder()
                .printZeroAlways().minimumPrintedDigits(2)
                .appendHours().appendSuffix(":").appendMinutes().toFormatter();
        Period period1 = durationFormatter.parsePeriod(dur1);
        Period period2 = durationFormatter.parsePeriod(dur2);
        Period difference = period1.minus(period2).normalizedStandard();
        return durationFormatter.print(difference);
    }

    public static Duration getDurationBetweenLocalTimes(java.time.LocalTime lt1, java.time.LocalTime lt2) {
        /**
         * Takes 2 LocalTimes (HH:MM),delivers the Duration in between.
         * Needed to calculate the duration between starting and ending the workday.
         */
        return Duration.between(lt2, lt1);
    }

    public static String convertDurationToString(Duration dur1) {
        return dur1.toHoursPart() + ":" + dur1.toMinutesPart();
    }
}

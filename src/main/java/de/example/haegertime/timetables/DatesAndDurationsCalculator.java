package de.example.haegertime.timetables;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class DatesAndDurationsCalculator {
    private PeriodFormatter durationFormatter;
    private DateTimeFormatter localTimeFormatter;

    public DatesAndDurationsCalculator() {
        this.durationFormatter = new PeriodFormatterBuilder()
                .printZeroAlways().minimumPrintedDigits(2)
                .appendHours().appendSuffix(":").appendMinutes().toFormatter();
        this.localTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }

    public String substractDurationStrings(String dur1, String dur2) {
        /**
         * Takes 2 Strings, which must be in the HH:MM Format as input and delivers the Difference of Input1-Input2.
         * Needed to calculate the actual working hours by substracting the break duration
         */
        Period period1 = this.durationFormatter.parsePeriod(dur1);
        Period period2 = this.durationFormatter.parsePeriod(dur2);
        Period difference = period1.minus(period2).normalizedStandard();
        return this.durationFormatter.print(difference);
    }

    public String getDurationBetweenLocalTimes(java.time.LocalTime lt1, java.time.LocalTime lt2) {
        /**
         * Takes 2 LocalTimes (HH:MM),delivers the Duration in between.
         * Needed to calculate the duration between starting and ending the workday.
         */
        Duration duration = Duration.between(lt1, lt2);
        return duration.toHoursPart() + ":" + duration.toMinutesPart();
    }

    public java.time.LocalTime convertStringToLocalTime(String time1) {
        /**
         * Takes a Time in HH:MM format and returns it as java.time.LocalTime (don't confuse with joda.LocalTime!)
         */
        try {
            java.time.LocalTime localTime = java.time.LocalTime.parse(time1, this.localTimeFormatter);
            return localTime;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Input could not be converted to LocalTime. Input has to be a String of HH:MM Format!");
            return null;
        }
    }

    public String convertDurationToString(Duration dur1) {
        return dur1.toHoursPart() + ":" + dur1.toMinutesPart();
    }
}

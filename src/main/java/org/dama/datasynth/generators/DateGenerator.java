package org.dama.datasynth.generators;

import org.dama.datasynth.utils.MurmurHash;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by aprat on 19/04/16.
 * Generator that generates dates uniformly distributed within a given range.
 */
public class DateGenerator extends Generator {

    private long dateStart;
    private long dateEnd;
    private String format;
    private String timeZone = "GMT";
    final Random rand = new SecureRandom();

    /**
     * Initializes the generator
     * @param format The format of the output edges
     * @param dateStart The start date of the date range
     * @param dateEnd The end date of the date range
     */
    public void initialize(String format, String dateStart, String dateEnd) {
        this.format = format;
        GregorianCalendar calendar = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(dateStart);
            calendar.setTime(date);
            this. dateStart = calendar.getTimeInMillis();

            date = formatter.parse(dateEnd);
            calendar.setTime(date);
            this. dateEnd = calendar.getTimeInMillis();
        } catch (ParseException pE) {
            pE.printStackTrace();
        }
    }


    /**
     * Generates a date uniformly distributed, within the date range.
     * @return A new date uniformly distributed within the date range.
     */
    public String run() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        calendar.setTime(new Date((long)((dateEnd - dateStart)*rand.nextDouble() + dateStart)));
        return formatter.format(calendar.getTime());
    }

}

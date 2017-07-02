package lib;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Helpers {

    public static String englishPluralise(String singularWord, String pluralWord, int quantity) {
        if (quantity == 0) {
            return "no " + pluralWord;
        }
        if (quantity == 1) {
            return "one " + singularWord;
        }
        else {
            return String.format("%d %s", quantity, pluralWord);
        }
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Sets the hours, minutes and seconds in a date to zero.
     * eg Tue Apr 18 23:44:41 CEST 2017 becomes Tue Apr 18 00:00:00 CEST 2017
     * @param date
     * @return
     */
    public static Date truncateDate(Date date) {
        return stringToDate(dateToString(date));
    }

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public static Date stringToDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static List<Date> daysInWeek(Calendar calendar) {
        return daysInWeek(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR));
    }

    public static List<Date> daysInWeek(int year, int week) {
        List<Date> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.YEAR, year);
        // get first date of week
        Date date = calendar.getTime();
        days.add(date);
        for(int i=0; i<6; i++) {
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
            days.add(date);
        }
        return days;
    }

    public static int dateToWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int dateToYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static void notification(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext facesContext;
        facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static boolean isAtWeekend(Date date) {
        int dayOfWeek = getDayOfWeek(date);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }
}

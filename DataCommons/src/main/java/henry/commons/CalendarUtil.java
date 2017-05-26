package henry.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil
{

    public static DateFormat getDashDate()
    {
        return new SimpleDateFormat("yyyy-MM-dd");
    }
    
    public static DateFormat getSimplestDateFormat()
    {
        return new SimpleDateFormat("yyyyMMdd");
    }

    public static Date getDate(int year, int month, int day)
    {
        return getCalendar(year, month, day).getTime();
    }

    public static Date getFormattedDate(String str, String sep)
    {
        String[] strs = str.split(sep);
        if (strs == null || strs.length < 3)
            return null;

        return getDate(Integer.valueOf(strs[0]), Integer.valueOf(strs[1]), Integer.valueOf(strs[2]));
    }

    public static Calendar getCalendar(int year, int month, int day)
    {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        return c;
    }

    public static Calendar getCalendar(int year, int month, int day, int hoursOfDay, int minute, int second)
    {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day, hoursOfDay, minute, second);
        return c;
    }

    public static Calendar getCalendarByMilliSecond(long m)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(m);
        return c;
    }

    public static Calendar getToday()
    {
        Calendar c = Calendar.getInstance();
        return c;
    }

    public static Date getAfterDays(Calendar start, int days)
    {
        start.add(Calendar.DAY_OF_MONTH, days);
        return start.getTime();

    }

    public static void main(String[] args) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss a");
        Calendar c = getCalendar(2014, 9, 26, 10, 00, 0);
        System.out.println(sdf.format(c.getTime()));
        System.out.println(c.getTimeInMillis());
        c = getCalendar(2014, 9, 26, 13, 30, 0);
        System.out.println(sdf.format(c.getTime()));
        System.out.println(c.getTimeInMillis());

        Date d = getFormattedDate("2014-09-30", "-");
        System.out.println(getDashDate().format(d));

    }
}

package SecuriyCommonUtility;

import java.util.Calendar;
import java.util.Date;

public class CalendarFunctions 
{
	public static Date subtractDay(Date date,int iNoOfDays)
	{
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.DAY_OF_MONTH, -iNoOfDays);
	    return cal.getTime();
	}

}

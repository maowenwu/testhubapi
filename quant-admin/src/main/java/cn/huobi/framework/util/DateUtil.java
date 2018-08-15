/**
 * 
 */
package cn.huobi.framework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 邹瑞金 时间工具类 2016年4月25日15:33:26
 */
public class DateUtil {
	private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
	private final static String LONG_FROMATE = "yyyy-MM-dd HH:mm:ss";
	public final static String DATEFORMAT = "yyyy-MM-dd";
	private static final String YEARMONTHFORMATE = "yyyyMMdd";
	private static final String LONGFORMATE = "yyyy/MM/dd";

	public static void main(String[] args) throws ParseException {
		System.out.println(DateUtil.getNowDateShort());
	}

	/**
	 * 获取当前系统时间 前minutes分钟的时间
	 * 
	 * @param minutes
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getFrontMinDate(int minutes) {
		SimpleDateFormat formate = new SimpleDateFormat(LONG_FROMATE);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, -minutes);
		return formate.format(c.getTime());
	}

	/**
	 * 获取现在时间
	 * @throws ParseException 
	 * 
	 * @return返回短时间格式 yyyy-MM-dd
	 */
	public static Date getNowDateShort() throws ParseException {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		Date currentTime_2 = formatter.parse(dateString);
		return currentTime_2;
	}

	/**
	 * 
	 * 功能：解析数据库中的时间字符串 格式:yyyy-MM-dd
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseDateTime(String dateTimeStr) {
		Date date = null;
		try {
			SimpleDateFormat dataTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
			date = dataTimeFormat.parse(dateTimeStr);
		} catch (ParseException e) {
			log.error("异常:",e);
		}
		return date;
	}

	/**
	 * 功能：解析数据库中的时间字符串 格式:yyyy-MM-dd HH:mm:ss
	 * @param dateTimeStr
	 * @return
	 */
	public static Date parseLongDateTime(String dateTimeStr) {
		Date date = null;
		try {
			SimpleDateFormat dataTimeFormat = new SimpleDateFormat(LONG_FROMATE);
			date = dataTimeFormat.parse(dateTimeStr);
		} catch (ParseException e) {
			log.error("异常:",e);
		}
		return date;
	}

	/**
	 * 功能：获取当前日期 格式:yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
		return dateFormat.format(date);
	}
	/**
	 * 
	 * @return
	 */
	public static String getLongCurrentDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(LONG_FROMATE);
		return dateFormat.format(date);
	}

	public static String fromTimeToTime(String fromFormat, String toFormat, String fromTime, int subDay, int subHour,
			int subMinute, int subSecond) {
		SimpleDateFormat dataTimeFormat = new SimpleDateFormat(fromFormat);
		SimpleDateFormat df = new SimpleDateFormat(toFormat);
		Date date = null;
		String toTime = "";
		try {
			date = dataTimeFormat.parse(fromTime);
		} catch (ParseException e) {
			log.error("异常:",e);
			return toTime;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, subDay);
		c.set(Calendar.HOUR_OF_DAY, subHour);
		c.set(Calendar.MINUTE, subMinute);
		c.set(Calendar.SECOND, subSecond);
		toTime = df.format(c.getTime());
		return toTime;
	}
	/**
	 * 得到报文时间格式yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String getMessageTextTime() {

		String result = null;
		try {
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			result = df.format(new Date());
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return result;
	}
	/**
	 * 
	 * @param format1
	 *            "格式"
	 * @param date
	 * @return
	 */
	public static String getFormatDate(String format1, Date date) {

		String result = null;
		try {
			DateFormat df = new SimpleDateFormat(format1);
			result = df.format(date);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return result;
	}

	/**
	 * 格式化date为yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getDefaultFormatDate(Date date) {

		String result = null;
		try {
			DateFormat df = new SimpleDateFormat(DATEFORMAT);
			result = df.format(date);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return result;
	}
	
	/**
	 * 格式化date为yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String getLongFormatDate(Date date) {

		String result = null;
		try {
			DateFormat df = new SimpleDateFormat(LONG_FROMATE);
			result = df.format(date);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return result;
	}

	/**
	 * 获得指定日期的前一天日期
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date getPreviousDate(Date currDate) {
		Date previousDate = null;
		try {
			Calendar calendar = Calendar.getInstance(); // 得到日历
			calendar.setTime(currDate);// 把当前时间赋给日历
			calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
			previousDate = calendar.getTime();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return previousDate;
	}

	/**
	 * 判断指定日期是否是星期日
	 * 
	 * @param currDate
	 * @return
	 */
	public static boolean isSunday(Date currDate) {
		boolean state = false;
		try {

		} catch (Exception ex) {
			state = false;
			ex.printStackTrace();
		}
		return state;
	}

	/**
	 * 获得指定日期所在一周的星期一的日期
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date mondayDate(Date currDate) {
		Date mondayDate = null;
		try {

		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return mondayDate;
	}

	/**
	 * 获得指定日期所在一周的星期日的日期
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date SundayDate(Date currDate) {
		Date sunday = null;
		try {

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sunday;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param startTime
	 *            较小的时间
	 * @param endTime
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date startTime, Date endTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		startTime = sdf.parse(sdf.format(startTime));
		endTime = sdf.parse(sdf.format(endTime));
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		long time1 = cal.getTimeInMillis();
		cal.setTime(endTime);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 获得指定日期的前一月
	 * 
	 * @return
	 */
	public static String getBeforeMonth(Date currDate) {
		// 获得系统日期的前一天
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currDate);
		calendar.add(Calendar.MONTH, -1); // 设置为前一月
		Date beforeDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String beforeDateStr = sdf.format(beforeDate);
		return beforeDateStr;
	}

	/**
	 * 取得当前日期对应周的前一周
	 * 
	 * @param date
	 * @return
	 */
	public static int getBeforeWeekOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(1);
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, -1); // 设置为前一周
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 取得当前日期的年份
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setMinimalDaysInFirstWeek(1);
		Date beforeDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String beforeDateStr = sdf.format(beforeDate);
		return Integer.valueOf(beforeDateStr).intValue();
	}

	/**
	 * 获得指定日期所在一周在一年中的周数
	 * 
	 * @param currDate
	 * @return
	 */
	public static int week(Date currDate) {
		int weekNum = 0;
		try {

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return weekNum;
	}

	/**
	 * 
	 * 得到某年某周的第一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */

	public static Date getFirstDayOfWeek(int year, int week) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 设置周一
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		return calendar.getTime();
	}

	/**
	 * 得到某年某周的最后一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */

	public static Date getLastDayOfWeek(int year, int week) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); // Sunday
		return calendar.getTime();
	}

	/**
	 * 获得指定日期的前一日
	 * 
	 * @return
	 */
	public static String getBeforeDateString(Date currDate) {
		// 获得系统日期的前一天
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(currDate);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		Date beforeDate = calendar.getTime(); // 得到前一天的时间
		SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
		String beforeDateStr = sdf.format(beforeDate);
		return beforeDateStr;
	}

	public static Date getBeforeDate(Date currDate) {
		// 获得系统日期的前一天
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(currDate);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		Date beforeDate = calendar.getTime(); // 得到前一天的时间
		return beforeDate;
	}

	/**
	 * 获得指定日期的前2日
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date getBeforeDate2(Date currDate) {
		// 获得系统日期的前一天
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(currDate);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -2); // 设置为前2天
		Date beforeDate = calendar.getTime(); // 得到前一天的时间
		return beforeDate;
	}

	/**
	 * 获得指定日期的后一日
	 * 
	 * @return
	 */
	public static String getAfterDateString(Date currDate) {
		// 获得系统日期的前一天
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(currDate);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, 1); // 设置为后一天
		Date beforeDate = calendar.getTime(); // 得到后一天的时间
		SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
		String beforeDateStr = sdf.format(beforeDate);
		return beforeDateStr;
	}

	public static Date getAfterDate(Date currDate) {
		// 获得系统日期的前一天
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(currDate);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, 1); // 设置为后一天
		Date afterDate = calendar.getTime(); // 得到后一天的时间
		return afterDate;
	}

	/**
	 * 获取指定日期所在周的周一
	 * 
	 * @Methods Name getMonday
	 * @return Date
	 */
	public static Date getMonday(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		cDay.set(Calendar.DAY_OF_WEEK, 2);// 老外将周日定位第一天，周一取第二天
		return cDay.getTime();
	}

	/**
	 * 获取指定日期所在周日
	 * 
	 * @Methods Name getSunday
	 * @return Date
	 */
	public static Date getSunday(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		if (Calendar.DAY_OF_WEEK == cDay.getFirstDayOfWeek()) { // 如果刚好是周日，直接返回
			return date;
		} else {// 如果不是周日，加一周计算
			cDay.add(Calendar.DAY_OF_YEAR, 7);
			cDay.set(Calendar.DAY_OF_WEEK, 1);
			System.out.println(cDay.getTime());
			return cDay.getTime();
		}
	}

	/**
	 * 得到本月第一天的日期
	 * 
	 * @Methods Name getFirstDayOfMonth
	 * @return Date
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		cDay.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println(cDay.getTime());
		return cDay.getTime();
	}

	/**
	 * 得到本月第一天的日期 0时0分0秒0毫秒
	 * @Methods Name getFirstDayOfMonth
	 * @return Date
	 */
	public static Date getFirstDayOfMonthZero(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		cDay.set(Calendar.DAY_OF_MONTH, 1);
		cDay.set(Calendar.HOUR_OF_DAY, 0);
		cDay.set(Calendar.MINUTE, 0);
		cDay.set(Calendar.SECOND, 0);
		cDay.set(Calendar.MILLISECOND, 0);
		return cDay.getTime();
	}
	/**
	 * 获取今天的23:59:59:00
	 */
	public static Date getLastDay(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		cDay.set(Calendar.HOUR_OF_DAY, 23);
		cDay.set(Calendar.MINUTE, 59);
		cDay.set(Calendar.SECOND, 59);
		cDay.set(Calendar.MILLISECOND,0);
		return cDay.getTime();
	}

	/**
	 * 得到本月最后一天的日期
	 * 
	 * @Methods Name getLastDayOfMonth
	 * @return Date
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
		System.out.println(cDay.getTime());
		return cDay.getTime();
	}

	/**
	 * 得到本季度第一天的日期
	 * 
	 * @Methods Name getFirstDayOfQuarter
	 * @return Date
	 */
	public static Date getFirstDayOfQuarter(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		int curMonth = cDay.get(Calendar.MONTH);
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
			cDay.set(Calendar.MONTH, Calendar.JANUARY);
		}
		if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
			cDay.set(Calendar.MONTH, Calendar.APRIL);
		}
		if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
			cDay.set(Calendar.MONTH, Calendar.JULY);
		}
		if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
			cDay.set(Calendar.MONTH, Calendar.OCTOBER);
		}
		cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMinimum(Calendar.DAY_OF_MONTH));
		System.out.println(cDay.getTime());
		return cDay.getTime();
	}

	/**
	 * 得到本季度最后一天的日期
	 * 
	 * @Methods Name getLastDayOfQuarter
	 * @return Date
	 */
	public static Date getLastDayOfQuarter(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		int curMonth = cDay.get(Calendar.MONTH);
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
			cDay.set(Calendar.MONTH, Calendar.MARCH);
		}
		if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
			cDay.set(Calendar.MONTH, Calendar.JUNE);
		}
		if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
			cDay.set(Calendar.MONTH, Calendar.AUGUST);
		}
		if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
			cDay.set(Calendar.MONTH, Calendar.DECEMBER);
		}
		cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
		System.out.println(cDay.getTime());
		return cDay.getTime();
	}

	public static String StrDateToFormatStr(String dateStr, String befformat, String newformat) {
		// 原有日期格式
		SimpleDateFormat sdf = new SimpleDateFormat(befformat);
		try {
			Date date = sdf.parse(dateStr);// parse出Date类型
			sdf.applyPattern(newformat);// 设置新的日期格式
			String str2 = sdf.format(date);// 格式化Date
			return str2;
		} catch (Exception e) {
			log.error("异常:",e);
			return "";
		}
	}

	/**
	 * yyyyMMdd格式化为yyyy-MM-dd,然后加一天
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String convertDateAddOneDayString(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(YEARMONTHFORMATE);
		SimpleDateFormat longSdf = new SimpleDateFormat(DATEFORMAT);
		Date dt = sdf.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date lastDate = cal.getTime();
		return longSdf.format(lastDate);
	}

	/**
	 * 将字符串日期转成格式为（yyyy-MM-dd）
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String convertShortDateTwo(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(YEARMONTHFORMATE);
		SimpleDateFormat longSdf = new SimpleDateFormat(DATEFORMAT);
		Date dt = sdf.parse(date);
		return longSdf.format(dt);
	}

	public static Date convertShortDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(YEARMONTHFORMATE);
		Date dt = sdf.parse(date);
		return dt;
	}

	/**
	 * 字符串类型的时间减一天得到字符串类型
	 * 
	 * @param date
	 * @param num
	 * @return
	 * @throws ParseException
	 */
	public static String covertPreDateStr(String date, int num) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
		Date dtt = sdf.parse(date);
		String dtStr = subDayFormatLong(dtt, num);
		return dtStr;
	}

	/**
	 * 减num天
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static String subDayFormatLong(Date date, int num) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, -num);
		return sdf.format(startDT.getTime());
	}

	/**
	 * 减num天
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static String subDay(Date date, int num) {
		SimpleDateFormat sdf = new SimpleDateFormat(YEARMONTHFORMATE);
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, -num);
		return sdf.format(startDT.getTime());
	}
	// public static void main(String[] args) {
	// /*String time=DateUtil.getFrontMinDate(5);
	// System.out.println(time);*/
	//// System.out.println(getFormatDate("yyMMdd", new Date()));
	// }
	/** 
     * 根据日期计算某月有多少天 
     * @param date 需要计算有多少天的日期 
     * @return 返回当前日期的天数 
     */  
    public static int getDays(Date date){  
        Calendar objCalendar = new GregorianCalendar();  
        try {  
            objCalendar.setTime(date);  
            int days = objCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
            return days;  
        } catch (Exception e1) {  
            e1.printStackTrace();  
        }  
        return 0;  
    }  
    /** 
     * 根据日期计算某月有多少天 
     * @param date 需要计算有多少天的日期 
     * @return 返回当前日期的天数 
     */  
    public static int getDays(String date){  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        Calendar objCalendar = new GregorianCalendar();  
        try {  
            objCalendar.setTime(formatter.parse(date));  
            int days = objCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
            return days;  
        } catch (Exception e1) {  
            e1.printStackTrace();  
        }  
        return 0;  
    }
	/**
	 * 将当前日期转换成Unix时间戳
	 * 
	 * @return long 时间戳
	 */
	public static long dateToUnixTimestamp() {
		long timestamp = new Date().getTime();
		return timestamp;
	}
}

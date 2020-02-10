package com.ucsmy.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * DateTool 封装了一些日期常用方法
 * @author gaokx
 * @version 1.0
 */
public class DateUtils{

	public final static short DATE_YEAR = 0;
	public final static short DATE_MONTH = 1;
	public final static short DATE_DAY = 2;
	public final static short DATE_HOUR = 3;
	public final static short DATE_MINUTE = 4;
	public final static short DATE_SECOND = 5;

	private final static Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

	/**
	 * 将date转为calendar
	 * @param date 指定日期
	 * @return
	 */
	public static Calendar dateToCalerdar(Date date){
		try{
			if(date == null){
				return null;
			}
			Calendar cal = new GregorianCalendar();
			cal.clear();
			cal.setTime(date);
			return cal;
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}
	
	/**
	 * 获取指定年月日的calendar
	 * @param year 指定年
	 * @param month 指定月（0-11）
	 * @param date 指定日
	 * @return
	 */
	public static Date getDate(int year, int month, int date){
		try {
			Calendar cal = new GregorianCalendar();
			cal.clear();
			cal.set(year, month, date);
			return cal.getTime();
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}
	
	/**
	 * 获取指定年月日时分的calendar
	 * @param year 指定年
	 * @param month 指定月（0-11）
	 * @param date 指定日
	 * @param hourOfDay 指定时(24小时制)
	 * @param minute 指定分
	 * @return
	 */
	public static Date getDate(int year, int month, int date, int hourOfDay, int minute){
		try {
			Calendar cal = new GregorianCalendar();
			cal.clear();
			cal.set(year, month, date, hourOfDay, minute);
			return cal.getTime();
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}
	
	/**
	 * 获取指定年月日时分秒的calendar
	 * @param year 指定年
	 * @param month 指定月（0-11）
	 * @param date 指定日
	 * @param hourOfDay 指定时(24小时制)
	 * @param minute 指定分
	 * @param second 指定秒
	 * @return
	 */
	public static Date getDate(int year, int month, int date, int hourOfDay, int minute, int second){
		try {
			Calendar cal = new GregorianCalendar();
			cal.clear();
			cal.set(year, month, date, hourOfDay, minute, second);
			return cal.getTime();
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}
	
	/**
	 * 将日期转为一定格式的字符串
	 * @param date 日期
	 * @param format 日期格式字符串
	 * @return 转换后的日期字符串
	 */
	public static String dateToString(Date date, String format){
		try {
			if(date == null){
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}

	/**
	 * 将日期转为一定格式的字符串
	 * @param datetime 时间戳
	 * @param format 日期格式字符串
	 * @return 转换后的日期字符串
	 */
	public static String dateToString(long datetime, String format){
		try {
			if (datetime == 0){
				return "";
			}
			Date d = new Date(datetime);
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(d);
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}

	/**
	 * 根据传入的格式和值获得Date型
	 * @param value 传入日期字符串
	 * @param format 格式字符串
	 * @return 日期
	 */
	public static Date strToDate(String value, String format) throws ParseException{
		try {
			if(value == null || StringUtils.isEmpty(value.trim())){
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(value);
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}

	/**
	 * 获得指定日期当天的最小值（例如 1990-10-30 00:00:01）
	 * @param date 指定日期
	 * @return
	 */
	public static Date getTodayMin(Date date){
		try {
			if(date == null){
				return null;
			}
			GregorianCalendar gc = getGregorianCalendar(date, 0, 0, 0);
			return gc.getTime();
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}

	/**
	 * 获得指定日期当天的最大值（例如 1990-10-30 23:59:59）
	 * @param date 指定日期
	 * @return
	 */
	public static Date getTodayMax(Date date){
		try {
			if(date == null){
				return null;
			}
			GregorianCalendar gc = getGregorianCalendar(date, 23, 59, 59);
			return gc.getTime();
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}

	/**
	 * 根据小时，分，秒，获取GregorianCalendar
	 * @param date
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 * @return
	 */
	private static GregorianCalendar getGregorianCalendar(Date date, int hourOfDay, int minute, int second) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(GregorianCalendar.HOUR_OF_DAY, hourOfDay);
		gc.set(GregorianCalendar.MINUTE, minute);
		gc.set(GregorianCalendar.SECOND, second);
		return gc;
	}

	/**
	 * 指定日期加减日期
	 * @param date 指定日期
	 * @param value 要加减的数值，负数为减
	 * @param type 类型,(数值不正确时默认为加减天数)
	 * @return
	 */
	public static Date addDay(Date date, int value, short type){
		try {
			if(date == null){
				return null;
			}
			Calendar cal = new GregorianCalendar();
			cal.clear();
			cal.setTime(date);
			switch(type){
				case DateUtils.DATE_YEAR:{
					cal.add(Calendar.YEAR, value); 
					break;
				}
				case DateUtils.DATE_MONTH:{
					cal.add(Calendar.MONTH, value); 
					break;
				}
				case DateUtils.DATE_DAY:{
					cal.add(Calendar.DAY_OF_MONTH, value); 
					break;
				}
				case DateUtils.DATE_HOUR:{
					cal.add(Calendar.HOUR_OF_DAY, value); 
					break;
				}
				case DateUtils.DATE_MINUTE:{
					cal.add(Calendar.MINUTE, value); 
					break;
				}
				case DateUtils.DATE_SECOND:{
					cal.add(Calendar.SECOND, value); 
					break;
				}
				default : {
					cal.add(Calendar.DAY_OF_MONTH, value); 
					break;
				}
			}
			return cal.getTime();
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}


	/**
	 * 获得指定日期的年月日时分秒,用数组形式int[]
	 * @param date 指定日期
	 * @return
	 */
	public static int[] returnIntArray(Date date){
		try{
			if(date == null){
				return null;
			}
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			int second = cal.get(Calendar.SECOND);
			return new int[] { year, month, day, hour, minute, second};
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}

	/**
	 * 比较两个日期
	 * @param date1	指定日期1
	 * @param date2  指定日期2
	 * @return	date1等于date2，则返回 0 值；date1在date2之前，
	 * 则返回小于 0 的值；date1在date2之后，则返回大于 0 的值。 
	 */
	public static int compareTo(Date date1, Date date2){
		try {
			if(date1 == null || date2 == null){
				throw new IllegalArgumentException("date1和date2不能为空");
			}
			Calendar cal = new GregorianCalendar();
			cal.clear();
			cal.setTime(date1);
			Calendar cal2 = new GregorianCalendar();
			cal2.clear();
			cal2.setTime(date2);
			return cal.compareTo(cal2);
		}catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}

	/**
	 * 判断两个日期是否为同一天
	 * @param date1	日期1
	 * @param date2	日期2
	 * @return boolean true相同，false 不同,当有参数为null时，返回false
	 */
	public static boolean isSameDay(Date date1, Date date2){
		try {
			if(date1 == null || date2 == null){
				return false;
			}
			Calendar cal = new GregorianCalendar();
			cal.clear();
			cal.setTime(date1);
			int[] date1Val = {cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)};
			cal.clear();
			cal.setTime(date2);
			int[] date2Val = {cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)};
			return date1Val[0] == date2Val[0] && date1Val[1] == date2Val[1] && date1Val[2] == date2Val[2];
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}

	}

	/**
	 * 去除指定日期的时分秒
	 * @param date 日期
	 * @return 时分秒都为0的日期
	 */
	public static Date removeHMS(Date date){
		try {
			if(date == null){
				return null;
			}
			Calendar cal = new GregorianCalendar();
			cal.clear();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND,0);
			return cal.getTime();
		} catch (Exception e) {
			LOGGER.error("异常", e);
			throw e;
		}
	}
	
	/**
	 * 获得指定日期的当前周，上一周或下一周的指定星期几的日期
	 * @author xjw
	 * @param date	指定日期
	 * @param weekDay	星期几
	 * @param type	0：当前周，-1：上一周，1：下一周
	 * @return 日期DateTool.getTodayMax(DateTool.getWeekDay(new Date(), Calendar.SUNDAY, (short) 1))
	 */
	public static Date getWeekDay(Date date, int weekDay, short type)
	{
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.clear();
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.DAY_OF_WEEK, weekDay);
		if (type == -1)
		{
			cal.setTimeInMillis(cal.getTimeInMillis() - (7 * 3600 * 24 * 1000));
		}
		else if (type == 1)
		{
			cal.setTimeInMillis(cal.getTimeInMillis() + (7 * 3600 * 24 * 1000));
		}
		return cal.getTime();
	}

}

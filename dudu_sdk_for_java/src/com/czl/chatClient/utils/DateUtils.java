package com.czl.chatClient.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ShaoLin on 16/4/1;
 */
public class DateUtils {
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);

	/**
	 * 格式化时间到秒
	 *
	 * @param time
	 * @return
	 */
	public static final String timeFormatFull(long time) {
		return simpleDateFormat.format(new Date(time));
	}

	/**
	 * 格式化时间到天数
	 *
	 * @param time
	 * @return
	 */
	public static final String timeFormatDay(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date(time));
	}

	/**
	 * 格式化时间到秒
	 *
	 * @param time
	 * @return
	 */
	public static final String timeFormatDayDetail(long time) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd hh:mm:ss");
		return format.format(new Date(time));
	}

	/**
	 * 格式化时间到秒
	 *
	 * @param time
	 * @return
	 */
	public static final String timeFormatDayHour(long time) {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
		return format.format(new Date(time));
	}

	/**
	 * 倒计时计算
	 *
	 * @param endTime
	 * @return
	 */
	public static final String timeCalculateUtilBegin(long endTime) {
		Date now = new Date();
		long l = now.getTime() - endTime;
		String format = "";
		if (l > 0) {
			long day = l / (24 * 60 * 60 * 1000);
			if (day > 0) {
				format = day + "天前";
			} else {
				long hour = (l / (60 * 60 * 1000) - day * 24);
				long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
				long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
				if (hour > 0) {
					format = hour + "小时前";
				} else if (min > 0) {
					format = min + "分钟前";
				} else {
					if (s == 0) {
						s = 1;
					}
					format = s + "秒前";
				}
			}
		} else {
			format = 1 + "秒前";
		}
		return format;
	}

	/*
	 * 将时间转换为时间戳
	 */
	public static String dateToStamp(String s) {
		String res;
		long ts = dateToStamplong(s);
		res = String.valueOf(ts);
		return res;
	}

	public static long dateToStamplong(String s) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = simpleDateFormat.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long ts = date.getTime();
		return ts;
	}

	public static String secToTime(long time) {
		StringBuffer timeStr = new StringBuffer();
		long day = 0;
		long hour = 0;
		long minute = 0;
		long second = 0;

		if (time <= 0)
			return "00:00";
		else {
			second = time;
			minute = second / 60;
			hour = minute / 60;
			day = hour / 24;
			if (day >= 1) {
				timeStr.append(day);
				timeStr.append("天");
				hour = hour % 24;
			}
			if (hour >= 1) {
				timeStr.append(unitFormat(hour));
				timeStr.append("小时");
				minute = minute % 60;
			}
			if (minute >= 1) {
				timeStr.append(unitFormat(minute));
				timeStr.append("分钟");
				second = second % 60;
			}
			if (second >= 1) {
				timeStr.append(unitFormat(second));
				timeStr.append("秒");
			}

			return timeStr.toString();
		}
	}

	public static String secToTimeOne(long time) {
		StringBuffer timeStr = new StringBuffer();
		long day = 0;
		long hour = 0;
		long minute = 0;
		long second = 0;

		if (time <= 0)
			return "00:00";
		else {
			second = time;
			minute = second / 60;
			hour = minute / 60;
			day = hour / 24;
			if (day >= 1) {
				timeStr.append(day);
				timeStr.append(":");
				hour = hour % 24;
			}
			if (hour >= 1) {
				timeStr.append(unitFormat(hour));
				timeStr.append(":");
				minute = minute % 60;
			}
			if (minute >= 1) {
				timeStr.append(unitFormat(minute));
				timeStr.append(":");
				second = second % 60;
			} else {
				timeStr.append("00:");
			}
			if (second >= 1) {
				timeStr.append(unitFormat(second));
			}
			return timeStr.toString();
		}
	}

	public static String setVideoTime(long time) {
		StringBuffer timeStr = new StringBuffer();
		long day = 0;
		long hour = 0;
		long minute = 0;
		long second = 0;

		if (time <= 0)
			return "00:00";
		else {
			second = time;
			minute = second / 60;
			hour = minute / 60;
			day = hour / 24;
			if (day >= 1) {
				timeStr.append(day);
				// timeStr.append("天");
				hour = hour % 24;
			}
			if (hour >= 1) {
				timeStr.append(unitFormat(hour));
				timeStr.append(":");
				minute = minute % 60;
			}
			if (minute >= 1) {
				timeStr.append(unitFormat(minute));
				timeStr.append(":");
				second = second % 60;
			}
			if (second >= 1) {
				if (time < 60) {
					timeStr.append("00:");
				}
				timeStr.append(unitFormat(second));
			}
			if (second == 0) {
				timeStr.append("00");
			}

			return timeStr.toString();
		}
	}

	public static String unitFormat(long i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Long.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	public static boolean isThisYear(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		int year1 = c1.get(Calendar.YEAR);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date());
		int year2 = c2.get(Calendar.YEAR);
		if (year1 == year2) {
			return true;
		}
		return false;
	}

	public static boolean isThisWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.setTime(date);
		int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		if (paramWeek == currentWeek) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前日期是星期几<br>
	 *
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	public static boolean isSameYear(int year) {
		Calendar cal = Calendar.getInstance();
		int CurYear = cal.get(Calendar.YEAR);
		// Log.e("","CurYear="+CurYear);//2015
		return CurYear == year;
	}

	public static long getTimestampFromString(String sendtime) {
		return Long.parseLong(dateToStamp(sendtime));
	}

	public static String getFormatTimeFromTemple(long l) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date(l));
	}

	public static String getFormatTimeCountFromTemple(long l) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return simpleDateFormat.format(new Date(l));
	}
}

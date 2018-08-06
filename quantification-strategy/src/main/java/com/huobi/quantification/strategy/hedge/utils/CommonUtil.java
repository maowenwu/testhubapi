package com.huobi.quantification.strategy.hedge.utils;

import java.util.Date;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.date.Week;

public class CommonUtil {

	// 判断当前时间是否在普通对冲时间
	public static Boolean isNormalHedgingDate() {
		Date date = new Date();
		Week week = DateUtil.dayOfWeekEnum(date);
		Integer hour = DateUtil.hour(date, true);
		Integer minute = DateUtil.minute(date);
		// 周五 15:00---周五 15:55,不进行正常对冲
		if (Week.FRIDAY.getValue() == week.getValue() && hour == 15 && minute >= 0 && minute <= 55) {
			return false;
		}
		return true;
	}

}

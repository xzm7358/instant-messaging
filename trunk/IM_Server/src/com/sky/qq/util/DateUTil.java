package com.sky.qq.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUTil {
	private static SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	
	public static String format(Date date){
		return format.format(date);
	}
	
}

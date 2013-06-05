package com.sky.qq.util;

import java.text.DecimalFormat;

/**
 * 单位换算工具类
 * @author Administrator
 *
 */
public class UnitUtil {
	
	/**
	 * 换算为MB
	 * @param size
	 * @return
	 */
	public static String convertToMB(long size){
		DecimalFormat df=new DecimalFormat("#.00");
		return df.format((double)size / 1024 / 1024);
	}
}

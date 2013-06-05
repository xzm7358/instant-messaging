package com.sky.qq.biz;

import com.sky.qq.entity.Config;

public class ProgramVariable {
	/**
	 * 程序相关的配置文件
	 */
	private static Config config;

	public static Config getConfig() {
		return config;
	}

	public static void setConfig(Config config) {
		ProgramVariable.config = config;
	}
	
}

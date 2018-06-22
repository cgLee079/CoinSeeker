package com.cglee079.coinseeker.log;

import com.cglee079.coinseeker.util.TimeStamper;

public class Log {
	public static synchronized void i(String message) {
		String date = TimeStamper.getDateTime();
		System.out.println(date + "\t" + message);
	}
}

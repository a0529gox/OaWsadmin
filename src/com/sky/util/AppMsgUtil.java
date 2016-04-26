package com.sky.util;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppMsgUtil {

	public static final String propertiesName = "com.sky.properties.AppMsg";
	private static ResourceBundle bundle = null;

	private static ResourceBundle getBundle() {
		if (bundle == null) {
			bundle = ResourceBundle.getBundle(propertiesName, Locale.getDefault(), Thread.currentThread().getContextClassLoader());
		}
		return bundle;
	}
	
	public static String get(String key) {
		String msg =  getBundle().getString(key);
		try {
			msg = new String(msg.getBytes("iso8859-1"), "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return msg;
	}
	
	public static String get(String... params) {
		String key = params[0];
		if (params.length > 1) {
			Object p[] = new String[params.length - 1];
			for (int i = 1; i < params.length; i++) {
				p[i - 1] = params[i].toString();
			}
			return MessageFormat.format(get(key), p);
		} else {
			return get(key);
		}
	}
	
	public AppMsgUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		System.out.println(get("show.starting"));
		System.out.println(get("show.s"));

	}

}

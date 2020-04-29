package io.multiversum.db.executor.core.util;

import java.util.Arrays;

public class StringUtils {

	public static byte[] toBytes(String str, int length) {
		return Arrays.copyOf(str.getBytes(), length);
	}
	
	public static byte[] toBytes(String str) {
		return toBytes(str, 32);
	}
	
	public static String repeat(String what, int count) {
		String res = "";
		for (int i = 0; i < count; i++) {
			res += what;
		}
		
		return res;
	}
	
}

package com.sky.qq.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ByteUtil {

	private static CharsetDecoder decoder = Charset.forName("UTF-8")
			.newDecoder();

	/**
	 * Byte转换为Char
	 * 
	 * @param data
	 * @return
	 */
	public static CharBuffer byteToChar(ByteBuffer data) {
		CharBuffer result = null;
		try {
			result = decoder.decode(data);
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * long转字节数组
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] longToBytes(long n) {
		byte[] b = new byte[8];
		b[7] = (byte) (n & 0xff);
		b[6] = (byte) (n >> 8 & 0xff);
		b[5] = (byte) (n >> 16 & 0xff);
		b[4] = (byte) (n >> 24 & 0xff);
		b[3] = (byte) (n >> 32 & 0xff);
		b[2] = (byte) (n >> 40 & 0xff);
		b[1] = (byte) (n >> 48 & 0xff);
		b[0] = (byte) (n >> 56 & 0xff);
		return b;
	}

	/**
	 * 字节数组转long
	 * 
	 * @param array
	 * @return
	 */
	public static long bytesToLong(byte[] array) {
		return ((((long) array[0] & 0xff) << 56)
				| (((long) array[1] & 0xff) << 48)
				| (((long) array[2] & 0xff) << 40)
				| (((long) array[3] & 0xff) << 32)
				| (((long) array[4] & 0xff) << 24)
				| (((long) array[5] & 0xff) << 16)
				| (((long) array[6] & 0xff) << 8) | (((long) array[7] & 0xff) << 0));
	}

	/**
	 * int转字节数组
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] intToBytes(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}

	/**
	 * 字节数组转int
	 * 
	 * @param b
	 * @return
	 */
	public static int bytesToInt(byte b[]) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16
				| (b[0] & 0xff) << 24;
	}
	
	/**
	 * 从索引处转
	 * @param b
	 * @param i
	 * @return
	 */
	public static int bytesToInt(byte b[],int i) {
		return b[i+3] & 0xff | (b[i+2] & 0xff) << 8 | (b[i+1] & 0xff) << 16
				| (b[0] & 0xff) << 24;
	}
}

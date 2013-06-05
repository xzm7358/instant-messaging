package com.sky.qq.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64UTil {

	/**
	 * 将图片转成base64
	 * @param path
	 * @return
	 */
	public static String getImageBinaryByFile(File path){
		//将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
        InputStream in = null;  
        byte[] buffer = new byte[1024];  
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        try {  
            in = new FileInputStream(path);
            for(int i=-1;(i=in.read(buffer))!=-1;){
            	byteArrayOutputStream.write(buffer, 0, i);
            }
            byteArrayOutputStream.flush();
            in.close();
        }   
        catch (IOException e)   {  
            e.printStackTrace();  
        }  
        //对字节数组Base64编码  
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(byteArrayOutputStream.toByteArray());//返回Base64编码过的字节数组字符串  
	}
	
	/**
	 * 将base64字符串转换为byte
	 * @return
	 */
	public static byte[] convertBase64ToByte(String source){
		byte[] result=null;
		BASE64Decoder decoder = new BASE64Decoder();
		 //Base64解码  
		try {
			result = decoder.decodeBuffer(source);
			for(int i=0;i<result.length;++i)  {  
	            if(result[i]<0)  {//调整异常数据  
	            	result[i]+=256;  
	            }  
	        }  
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return result; 
	}
	
	/**
	 * 将二进制数据转成bast64
	 * @param data
	 * @return
	 */
	public static String convertByteBase64(byte[] data){
		//对字节数组Base64编码  
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(data);//返回Base64编码过的字节数组字符串  
	}
	
}

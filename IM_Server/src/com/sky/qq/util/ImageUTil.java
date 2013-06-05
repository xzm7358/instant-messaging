package com.sky.qq.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import sun.misc.BASE64Encoder;

/**
 * 图片处理工具类
 * 
 * @author Administrator
 * 
 */
public class ImageUTil {

	/**
	 * 图片缩放
	 * 
	 * @param path
	 * @param n
	 * @return
	 * @throws IOException
	 */
	public static byte[] changeImage(String path, double proportion)throws IOException {
		File file = new File(path);
		BufferedImage img = ImageIO.read(file);
		int width = (int) (img.getWidth() * proportion); // 得到源图宽
		int height = (int) (img.getHeight() * proportion); // 得到源图长
		//得到缩放后的image实例
		Image image = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		BufferedImage bufferedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB); // 缩放图像
		bufferedImage.getGraphics().drawImage(image, 0, 0, null); // 绘制后的图
		bufferedImage.getGraphics().dispose();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "JPEG", bos);
		return bos.toByteArray();
	}

	/**
	 * 将图片转成base64
	 * 
	 * @param path
	 * @return
	 */
	public static String getImageBinaryByFile(String path) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			in = new FileInputStream(path);
			for (int i = -1; (i = in.read(buffer)) != -1;) {
				byteArrayOutputStream.write(buffer, 0, i);
			}
			byteArrayOutputStream.flush();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return convertImgToBase64(byteArrayOutputStream.toByteArray());
	}

	/**
	 * 将字节数据转换为Base64
	 * 
	 * @param data
	 * @return
	 */
	public static String convertImgToBase64(byte[] data) {
		return new BASE64Encoder().encode(data);
	}
}

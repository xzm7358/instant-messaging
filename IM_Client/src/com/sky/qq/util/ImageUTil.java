package com.sky.qq.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图片处理工具类
 * @author Administrator
 *
 */
public class ImageUTil {

	/**
	 * 将图片灰度处理
	 * @param srcImg
	 * @return
	 */
    public static BufferedImage grayImage(BufferedImage srcImg) {
        int width = srcImg.getWidth();
        int height = srcImg.getHeight();
        int[] pixels = new int[width * height];
        srcImg.getRGB(0, 0, width, height, pixels, 0, width);
        int newPixels[] = new int[width * height];
        for(int i = 0; i < width * height; i++) {
            HSLColor hslColor = new HSLColor(HSLColor.fromRGB(new Color(pixels[i])));
            newPixels[i] = hslColor.adjustSaturation(0).getRGB();
        }
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0, 0, width, height, newPixels, 0, width);
        return bi;
    }

    /**
     * 讲字节数组转换为BufferedImage
     * @param data
     * @return
     */
    public static BufferedImage convertByteToBufferedImage(byte[] data){
    	BufferedImage result=null;
    	try {
    		result= ImageIO.read(new ByteArrayInputStream(data));
		} catch (IOException e) {
			e.printStackTrace();
		} 
    	return result;
    }
    
    /**
	 * 图片缩放
	 * 
	 * @param path
	 * @param n
	 * @return
	 * @throws IOException
	 */
	public static byte[] changeImage(byte[] data, double proportion)throws IOException {
		BufferedImage img = convertByteToBufferedImage(data);
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
}

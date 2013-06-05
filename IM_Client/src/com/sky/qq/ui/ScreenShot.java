package com.sky.qq.ui;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.sky.qq.handler.ICallBack;

/**
 * 屏幕截图
 * @author Administrator
 *
 */
public class ScreenShot extends JFrame{
	private static final long serialVersionUID = 7874388407069925363L;
	private ICallBack callBack;
	private JLabel shotImg=new JLabel();
	private BufferedImage screenImg;//屏幕图像
	private BufferedImage result;
	private int startX=0;
	private int startY=0;
	
	public ScreenShot(ICallBack callBack){
		this.callBack=callBack;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		registerEvent();
	}
	
	private void init() throws Exception{
		Robot robot = new Robot();
		Rectangle screeSize=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		screenImg = robot.createScreenCapture(screeSize);
		shotImg.setIcon(new ImageIcon(screenImg));
		shotImg.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		this.setUndecorated(true);
		this.getContentPane().add(shotImg);
		this.setSize(screeSize.width, screeSize.height);
		this.setVisible(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	/**
	 * 事件注册
	 */
	private void registerEvent(){
		shotImg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				startX=e.getX();
				startY=e.getY();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				int endX=e.getX();
				int endY=e.getY();
				if(endX < startX){
					int temp=endX;
					endX=startX;
					startX=temp;
				}
				if(endY < startY){
					int temp=endY;
					endY=startY;
					startY=temp;
				}
				result=screenImg.getSubimage(startX, startY, endX-startX, endY-startY);
				callBack.exece(result);
				dispose();
			}
		});
	}
}




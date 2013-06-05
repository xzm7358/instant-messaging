package com.sky.qq.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sky.qq.biz.DestoryCenter;
import com.sky.qq.entity.Resource;
import com.sky.qq.handler.ICallBack;

public class Brow extends JFrame {
	private static final long serialVersionUID = -1403704052472903815L;
	private ICallBack iCallBack;
	private Map<JLabel, Integer> browIndex=new HashMap<JLabel, Integer>();  //表情索引
	
	public Brow(ICallBack iCallBack){
		this.iCallBack=iCallBack;
		this.setTitle("选择表情");
		this.setSize(new Dimension(463, 330));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(0, 0);
		this.setUndecorated(true);
		loadUI();
	}
	
	private void loadUI(){
		this.setLayout(new GridLayout(7,15));
		for(int i=0,t=Resource.getBrowLength();i<t;i++){
			JLabel label=new JLabel(new ImageIcon(Resource.getBrowData(i)));
			browIndex.put(label, i);
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					dispose();
					try {
						iCallBack.exece(browIndex.get(e.getSource()));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					DestoryCenter.removeFrame(Brow.this);
				}
			});
			this.add(label);
		}
		setVisible(true);
	}
	
}

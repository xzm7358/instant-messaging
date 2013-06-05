package com.sky.qq.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import com.sky.qq.entity.DataBox;

public class OnlineClient extends JDialog{
	private static final long serialVersionUID = -386248326510796834L;
	private JPanel panCenter=new JPanel();
	private Vector<Vector<String>> store=new Vector<Vector<String>>();
	
	private JTable tabOnline;
	
	public OnlineClient(JFrame frame){
		//若第二个参数为true this.setVisible(true);则为阻塞的
		super(frame, true);
		this.setTitle("在线QQ");
		this.setSize(new Dimension(300,380));
		this.setLocationRelativeTo(frame);
		new Thread(){public void run() {
			OnlineClient.this.setVisible(true);
		};}.start();
		loadUI();
		
	}
	
	public void loadUI(){
		for(String key:DataBox.getOnlineQQ().keySet()){
			Vector<String> qq=new Vector<String>();
			qq.add(key);
			qq.add(DataBox.getOnlineQQ().get(key));
			store.add(qq);
		}
		Vector<String> columns=new Vector<String>();
		columns.add("QQ");
		columns.add("上线时间");
		tabOnline=new JTable(store,columns);
		
		tabOnline.setEnabled(false);
		tabOnline.setRowHeight(20);//行高为20px
		tabOnline.setPreferredScrollableViewportSize(new Dimension(550, 30));
		panCenter.setLayout(new BorderLayout());
		panCenter.add(new JScrollPane(tabOnline),BorderLayout.CENTER);
		this.add(panCenter,BorderLayout.CENTER);
	}
	
}

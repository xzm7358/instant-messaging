package com.sky.qq.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.ProgramVariable;
import com.sky.qq.socket.Server;

@Service
public class Main extends JFrame {
	private static final long serialVersionUID = 2247503797352437773L;
	private JPanel panTop=new JPanel();
	private JButton btnStart=new JButton("启动");
	private JButton btnStop=new JButton("停止");
	private static JTextArea txtShowInfor=new JTextArea();
	private JPanel panSouth=new JPanel(new FlowLayout(FlowLayout.RIGHT));
	public static JLabel labTotal=new JLabel("当前在线人数:0");
	
	@Autowired
	private Server server;
	private JScrollPane jScrollPane=new JScrollPane(txtShowInfor);
	
	public static JTextArea getTxtShowInfor() {
		return txtShowInfor;
	}

	public static void setTxtShowInfor(JTextArea txtShowInfor) {
		Main.txtShowInfor = txtShowInfor;
	}

	public void loadUI(){
		panSouth.add(labTotal);
	}
	public Main(){
		loadUI();
		panTop.setLayout(new FlowLayout(FlowLayout.LEFT));
		panTop.add(btnStart);
		panTop.add(btnStop);
		this.add(panTop,"North");
		this.add(jScrollPane,"Center");
		this.add(panSouth,"South");
		this.setSize(800, 600);
		this.setTitle("QQ Server");
		this.setLocationRelativeTo(null);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		btnStop.setEnabled(false);
		addEvent();
		//将关闭按钮默认事件改为隐藏窗体
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.this.setVisible(false);
			}
		});
		
	}
	
	public void showWindow(){
		this.setVisible(true);
	}
	
	public void addEvent(){
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("server:"+server);
				if(server.start()){
					txtShowInfor.append("服务器已启动,正在监听"+ProgramVariable.getConfig().getPort()+"端口......\r\n");
					server.pull();
					btnStart.setEnabled(false);		
					btnStop.setEnabled(true);
				}else{
					txtShowInfor.append("服务器启动失败");
				}
			}
		});
		
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(server.stop()){
					btnStart.setEnabled(true);		
					btnStop.setEnabled(false);
					txtShowInfor.append("服务器已关闭\r\n");
				}
			}
		});
		labTotal.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				new OnlineClient(Main.this);
			}
		});
	}

	
}

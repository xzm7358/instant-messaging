package com.sky.qq.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import com.google.gson.JsonObject;
import com.sky.qq.biz.QQInforBiz;
import com.sky.qq.entity.Owner;
import com.sky.qq.handler.CommandHandler;
import com.sky.qq.handler.OwnStateHanler;
import com.sky.qq.handler.TempletHandler;
import com.sky.qq.socket.Server;
import com.sky.qq.util.Base64UTil;

public class Login extends JFrame {
	private static final long serialVersionUID = 3591107245333112013L;
	private JTextField txtUserName;
	private JTextField txtPassword;
	private JButton btnLogin;
	private JButton btnSetting;
	private JLabel labRegisterQQ;
	private QQInforBiz qqInforBiz=new QQInforBiz();
	private int userName;
	
	public Login() {
		loadUI();
		registerEvent();
		registerHandler();
		Server.connect();
		
	}
	
	/**
	 * UI初始化
	 */
	public void loadUI(){
		this.setLayout(null);
		JPanel panShowImg = new JPanel();
		panShowImg.setBounds(0, 0, 376, 109);
		getContentPane().add(panShowImg);
		
		JPanel panFaceImg = new JPanel();
		panFaceImg.setBounds(10, 119, 90, 81);
		getContentPane().add(panFaceImg);
		
		txtUserName = new JTextField("254541843");
		txtUserName.setBounds(110, 119, 192, 22);
		getContentPane().add(txtUserName);
		txtUserName.setColumns(10);
		
		txtPassword = new JTextField("1");
		txtPassword.setColumns(10);
		txtPassword.setBounds(110, 151, 192, 22);
		getContentPane().add(txtPassword);
		
		labRegisterQQ = new JLabel("注册账号");
		labRegisterQQ.setBounds(312, 122, 54, 15);
		getContentPane().add(labRegisterQQ);
		
		JCheckBox checkBox = new JCheckBox("记住密码");
		checkBox.setBounds(157, 179, 80, 23);
		getContentPane().add(checkBox);
		
		JCheckBox checkBox_1 = new JCheckBox("自动登录");
		checkBox_1.setBounds(232, 179, 80, 23);
		getContentPane().add(checkBox_1);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 210, 376, 40);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		btnLogin = new JButton("登陆");
		btnLogin.setBounds(298, 10, 68, 23);
		panel.add(btnLogin);
		
		btnSetting = new JButton("设置");
		btnSetting.setBounds(10, 10, 68, 23);
		panel.add(btnSetting);
		
		this.setSize(new Dimension(385,285));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * 事件注册
	 */
	private void registerEvent(){
		labRegisterQQ.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Register(Login.this);
			}
			
		});
		
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userName=Integer.parseInt(txtUserName.getText().trim());
				qqInforBiz.login(userName, txtPassword.getText().trim());
			}
		});
	}
	
	/**
	 * 注册处理器
	 */
	private void registerHandler(){
		//注册用户登陆处理器
		CommandHandler.regsiterHandler(new TempletHandler("login") {
			@Override
			public void run(JsonObject obj) {
				login(obj);
			}
		});
	}
	
	/**
	 * 登陆结果处理
	 * @param obj
	 */
	private void login(JsonObject obj){
		obj=obj.getAsJsonObject("playload");
		System.out.println(obj.toString());
		if(obj.get("login").getAsBoolean()){
			CommandHandler.regsiterHandler(new OwnStateHanler());
			Owner.setQq(userName);
			new Main(Base64UTil.convertBase64ToByte(obj.getAsJsonObject("qqInfo").get("faceImg").getAsString()));
			Login.this.dispose();
		}else{
			JOptionPane.showMessageDialog(Login.this, "用户名或者密码错误，请重新输入!");
		}
	}
	
}

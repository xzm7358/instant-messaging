package com.sky.qq.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import com.google.gson.JsonObject;
import com.sky.qq.handler.CommandHandler;
import com.sky.qq.handler.TempletHandler;
import com.sky.qq.socket.Server;

public class Register extends JDialog{
	private static final long serialVersionUID = -7055114899628407037L;
	private JTextField txtNickName;
	private JTextField txtPwd1;
	private JTextField txtPwd2;
	private JPanel panCenter;
	private JPanel panButtom=new JPanel();
	
	private JPanel panUserInfo=new JPanel(null);
	private JPanel panQuestion=new JPanel();
	private JPanel panShowQQ=new JPanel(null);
	private CardLayout mainLayout=new CardLayout();
	private JTextField txtQuestion;
	private JTextField txtResult;
	private JButton btnNext = new JButton("注册");
	private int i=0;
	private JLabel labShowQQ;
	private JLabel labShowPwd;
	
	public Register(JFrame source) {
		super(source, true);
		setTitle("用户注册");
		this.setSize(new Dimension(310, 200));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(source);
		panCenter=new JPanel(mainLayout);
		JLabel lblNewLabel = new JLabel("用户昵称：");
		lblNewLabel.setBounds(39, 20, 70, 20);
		panUserInfo.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("用户密码：");
		lblNewLabel_1.setBounds(39, 54, 70, 15);
		panUserInfo.add(lblNewLabel_1);
		
		JLabel label = new JLabel("确认密码：");
		label.setBounds(39, 86, 70, 15);
		panUserInfo.add(label);
		
		txtNickName = new JTextField();
		txtNickName.setBounds(109, 20, 147, 21);
		panUserInfo.add(txtNickName);
		txtNickName.setColumns(10);
		
		txtPwd1 = new JTextField();
		txtPwd1.setColumns(10);
		txtPwd1.setBounds(109, 51, 147, 21);
		panUserInfo.add(txtPwd1);
		
		txtPwd2 = new JTextField();
		txtPwd2.setColumns(10);
		txtPwd2.setBounds(109, 83, 147, 21);
		panUserInfo.add(txtPwd2);
		
		
		panCenter.add(panUserInfo,"panUserInfo");
		panButtom.setBounds(0, 150, 305, 33);
		
		
		FlowLayout fLayout=new FlowLayout(FlowLayout.RIGHT);
		fLayout.setHgap(25);
		panButtom.setLayout(fLayout);
		
		
		btnNext.setBounds(159, 5, 57, 23);
		panButtom.add(btnNext);
		
		panQuestion.setLayout(null);
		
		txtQuestion = new JTextField();
		txtQuestion.setBounds(118, 19, 156, 21);
		panQuestion.add(txtQuestion);
		txtQuestion.setColumns(10);
		
		txtResult = new JTextField();
		txtResult.setColumns(10);
		txtResult.setBounds(116, 55, 156, 21);
		
		panQuestion.add(txtResult);
		panCenter.add(panShowQQ,"panShowQQ");
		panShowQQ.setLayout(null);
		
		JLabel lblqq = new JLabel("注册QQ号：");
		lblqq.setBounds(32, 22, 80, 15);
		panShowQQ.add(lblqq);
		
		JLabel lblQq = new JLabel("QQ密码：");
		lblQq.setBounds(32, 47, 80, 15);
		panShowQQ.add(lblQq);
		
		labShowQQ = new JLabel("New label");
		labShowQQ.setBounds(96, 22, 147, 15);
		panShowQQ.add(labShowQQ);
		
		labShowPwd = new JLabel("New label");
		labShowPwd.setBounds(96, 47, 147, 15);
		panShowQQ.add(labShowPwd);
		
		getContentPane().add(panCenter,BorderLayout.CENTER);
		getContentPane().add(panButtom,BorderLayout.SOUTH);
		
		registerEvent();
		this.setVisible(true); 
	}
	
	private boolean validateFormat(){
		boolean result=false;
		if(txtNickName.getText().trim().equals("")){
			JOptionPane.showMessageDialog(Register.this, "请输入QQ昵称");
			txtNickName.requestFocus();
		}else if(txtPwd1.getText().trim().equals("")){
			JOptionPane.showMessageDialog(Register.this, "请输入密码");
			txtPwd1.requestFocus();
		}else if(txtPwd2.getText().trim().equals("")){
			JOptionPane.showMessageDialog(Register.this, "请再次输入密码");
			txtPwd2.requestFocus();
		}else if(!txtPwd1.getText().trim().equals(txtPwd2.getText().trim())){
			JOptionPane.showMessageDialog(Register.this, "两次密码输入不一致");
			txtPwd2.requestFocus();
		}else{
			result=true;
		}
		return result;
	}
	
	private void registerEvent(){
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(i==0){
					if(validateFormat()){
						mainLayout.next(panCenter);
						i++;
						btnNext.setText("关闭");
						CommandHandler.regsiterHandler(new TempletHandler("register") {
							@Override
							public void run(JsonObject obj) {
								System.out.println(obj.toString());
								JsonObject playload=obj.getAsJsonObject("playload");
								if(playload.get("success").getAsBoolean()){
									labShowQQ.setText(playload.get("qqNum").getAsString());
									labShowPwd.setText(txtPwd2.getText().trim());
								}
							}
						});
						JsonObject root=new JsonObject();
						root.addProperty("type", "register");
						JsonObject playload=new JsonObject();
						playload.addProperty("nickName", txtNickName.getText().trim());
						playload.addProperty("password", txtPwd2.getText().trim());
						root.add("playload", playload);
						try {
							Server.write(root.toString().getBytes("UTF-8"));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					
				}else if(i==1){
					dispose();
				}
			}
		});
	}
	
	public static void main(String[] args) {
		new Register(null);
	}
}

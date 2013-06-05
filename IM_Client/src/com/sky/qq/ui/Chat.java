package com.sky.qq.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import com.google.gson.JsonObject;
import com.sky.qq.biz.*;
import com.sky.qq.biz.Observer;
import com.sky.qq.entity.*;
import com.sky.qq.handler.ICallBack;
import com.sky.qq.socket.FileWrite;
import com.sky.qq.socket.Server;
import com.sky.qq.util.DateUTil;
import com.sky.qq.util.MD5Hash;
import com.sky.qq.util.Path;
import com.sky.qq.util.UnitUtil;

public class Chat extends JFrame implements Observer{
	private static final long serialVersionUID = -5695041678103913473L;
	private final String formType="FRIENDMES";
	private JPanel panTop=new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel panCenter=new JPanel();
	private JPanel panEast=new JPanel();
	
	private JButton btnClose=new JButton("关闭");
	private JButton btnSend=new JButton("发送");
	private JLabel labSendFile=new JLabel(Resource.getSendFile());
	private JImageTextPane txtShowMes=new JImageTextPane();
	private JImageTextPane txtSendMes=new JImageTextPane();
	private JLabel labScreenshot=new JLabel(Resource.getScreenshot());
	private JLabel labBrows=new JLabel(Resource.getBrow());
	private Map<String, Progress> acceptProcess=new ConcurrentHashMap<String, Progress>();  //所有接受文件发送进度条
	private Map<String, Progress> sendProcess=new ConcurrentHashMap<String, Progress>();  //所有发送文件进度条 
	private QQInfor qq;
	
	public Chat(QQInfor qq){
		//btnScreenshot.setSize(20, 20);
		this.qq=qq;
		this.setTitle("您正在跟"+qq.getNickName()+"聊天");
		panTop.setPreferredSize(new Dimension(0,50));
		panTop.add(new JLabel(new ImageIcon(qq.getFaceImgData())));
		panTop.add(new JLabel(qq.getNickName()));
		panEast.setPreferredSize(new Dimension(150,0));
		panTop.setBackground(new Color(123,193,236));
		//panCenter.setBackground(Color.cyan);
		panEast.setBackground(new Color(193,219,235));
		this.setSize(540, 550);
		
		this.add(panTop,BorderLayout.NORTH);
		this.add(panCenter,BorderLayout.CENTER);
		this.add(panEast,BorderLayout.EAST);
		
		addCenter();
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		registerEvent();
		
		readMesByBox();
	}
	
	//检查消息盒子中是否有消息
	public void readMesByBox(){
		List<JsonObject> qqMessage=MessageBox.getQqMessages().get(String.valueOf(qq.getUserName()));
		System.out.println("消息盒子中的数目为:"+qqMessage.size());
		if(qqMessage.size()>0){
			for(JsonObject qMessage : qqMessage){
				String type=qMessage.get("type").getAsString();
				JsonObject message=qMessage.getAsJsonObject("playload");
				if(type.equals("qqMes")){
					StyledDocument doc=txtShowMes.getStyledDocument();
					try {
						doc.insertString(doc.getLength(),message.get("sender").getAsString()+"\t"+message.get("time").getAsString()+"\r\n", null);
						txtShowMes.setFormatText(message.get("mes").getAsString());
						doc.insertString(doc.getLength(), "\r\n\r\n", null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Progress progress=new Progress(createFileInfoByAccpet(message),progressCallback());
					acceptProcess.put(message.get("md5").getAsString(), progress);
					panEast.add(progress.getComponent());
					System.out.println("收到文件请求："+qMessage.toString());
				}
			}
			qqMessage.clear();
		}
	}

	/**
	 * 进度条关闭时回调
	 */
	private ICallBack progressCallback(){
		return new ICallBack() {
			@Override
			public void exece(Object obj) {
				Object[] data=(Object[])obj;
				EFileTransType fileTransType=(EFileTransType)data[0];
				String md5=data[1].toString();
				String showCue=data[2].toString();
				Component ProgressUI=(Component)data[3];
				disposeProgress(ProgressUI);
				if(fileTransType==EFileTransType.ACCEPT){
					acceptProcess.remove(md5);
				}else{
					sendProcess.remove(md5);
				}
				insertStr(txtShowMes,showCue);
			}
		};
	}
	
	/**
	 * 销毁进度条
	 * @param component
	 */
	public void disposeProgress(Component component){
		panEast.remove(component);
		panEast.updateUI();
	}
	
	
	private void addCenter(){
		panCenter.setLayout(new BorderLayout());
		//panCenter.add(new JScrollPane(txtShowMes),BorderLayout.CENTER);
		/////
		
		txtShowMes.setEditable(false);
		panCenter.add(new JScrollPane(txtShowMes),BorderLayout.CENTER);
		
		JPanel panOperate=new JPanel(new BorderLayout());
		JPanel panFuncMenu=new JPanel(new FlowLayout(FlowLayout.LEFT));
		panFuncMenu.setBackground(new Color(193,219,235));
		//panFuncMenu.setPreferredSize(new Dimension(0,100));
		//JLabel labScreenshot=new JLabel();
		
		panFuncMenu.add(labScreenshot);
		panFuncMenu.add(labBrows);
		panFuncMenu.add(labSendFile);
		panOperate.add(panFuncMenu,BorderLayout.NORTH);
		
		txtSendMes.setSize(100, 100);
		txtSendMes.setPreferredSize(new Dimension(0, 100));
		panOperate.add(new JScrollPane(txtSendMes),BorderLayout.CENTER);
		
		JPanel panBtnSend=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panBtnSend.setBackground(new Color(193,219,235));
		panBtnSend.add(btnClose);
		panBtnSend.add(btnSend);
		panOperate.add(panBtnSend,BorderLayout.SOUTH);
		
		panCenter.add(panOperate,BorderLayout.SOUTH);
		
		
		///////
		
		
		/*JPanel panSend=new JPanel();
		panSend.setBackground(Color.blue);
		
		JPanel panFuncMenu=new JPanel(new FlowLayout(FlowLayout.LEFT));
		panFuncMenu.add(btnScreenshot);
		panFuncMenu.add(btnBrows);
		panFuncMenu.add(btnSendFile);
		
		
		GridLayout gridLayout=new GridLayout(2,1);
		
		JPanel panAcceptMes=new JPanel(gridLayout);
		panAcceptMes.add(new JScrollPane(txtShowMes));
		txtShowMes.setEditable(false);
		panAcceptMes.add(panFuncMenu);
		panCenter.add(panAcceptMes);
		
		panCenter.add(panSend,"South");
		panSend.setLayout(new BorderLayout());
		
		//JPanel panMesAssistant=new JPanel();
		
		txtSendMes.setSize(100, 100);
		JPanel panBtnSend=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panBtnSend.add(btnClose);
		panBtnSend.add(btnSend);
		txtSendMes.setPreferredSize(new Dimension(0, 100));
		//panSend.add(panMesAssistant,"North");
		panSend.add(new JScrollPane(txtSendMes),"Center");
		panSend.add(panBtnSend,"South");
*/	
	}
	
	@Override
	public void dispose() {
		boolean isClose=true;
		if(acceptProcess.size()!=0 || sendProcess.size()!=0){
			if (JOptionPane.showConfirmDialog(null, "当前有文件正在传输，是否继续关闭？") != 0) {
				isClose=false;
			}else{
				for(String md5 : acceptProcess.keySet()){
					Socket socket=DestoryCenter.getSocket(EFileTransType.ACCEPT, String.valueOf(qq.getUserName()), md5);
					if(socket!=null){
						DestoryCenter.closeSocket(socket);
					}else{
						FileTransfersBiz.sendRefuseRequest(qq.getUserName(), md5);
					}
				}
				for(String md5 : sendProcess.keySet()){
					Socket socket=DestoryCenter.getSocket(EFileTransType.SEND, String.valueOf(qq.getUserName()), md5);
					if(socket!=null){
						DestoryCenter.closeSocket(socket);
					}else{
						FileTransfersBiz.sendCancelRequest(qq.getUserName(), md5);
					}
				}
			}
		}
		if(isClose){
			MessageBox.getInstance().dettach(String.valueOf(qq.getUserName()), this); //删除被监视对象
			DestoryCenter.removeFrame(this);
			super.dispose();
		}
	}
	
	private TransFileInfo createFileInfoByAccpet(JsonObject data){
		 TransFileInfo fileInfo=new TransFileInfo();
		 fileInfo.setFileTransType(EFileTransType.ACCEPT);
		 fileInfo.setTarget(data.get("sender").getAsInt());
		 fileInfo.setFileName(data.get("fileName").getAsString());
		 fileInfo.setFileSize(data.get("size").getAsLong());
		 fileInfo.setMd5(data.get("md5").getAsString());
		return fileInfo;
	}
	
	/**
	 * 注册事件
	 */
	private void registerEvent(){
		btnClose.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				Chat.this.dispose();
			}
		});
		labScreenshot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				screenshot();
			}
		});
		
//		(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				screenshot();
//			}
//		});
		labBrows.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showBrows();
			}
		});
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMes();
			}
		});
		labSendFile.addMouseListener(new MouseAdapter() {
			MD5Hash md5Hash=new MD5Hash();
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser chooser=new JFileChooser();
				if(chooser.showOpenDialog(Chat.this)==JFileChooser.APPROVE_OPTION){
					System.out.println(chooser.getSelectedFile().toString());
					System.out.println(chooser.getSelectedFile().getName());
					try {
						File file=chooser.getSelectedFile();
						String md5=md5Hash.getFileMD5String(file);
						if(!sendProcess.containsKey(md5)){
							System.out.println("文件MD5："+md5);
							FileTransfersBiz.sendFile(file, qq.getUserName(), md5);
							
							TransFileInfo fileInfo=new TransFileInfo();
							fileInfo.setFilePath(file.getAbsolutePath());
							fileInfo.setFileTransType(EFileTransType.SEND);
							fileInfo.setTarget(qq.getUserName());
							fileInfo.setFileName(file.getName());
							fileInfo.setMd5(md5);
							fileInfo.setFileSize(file.length());
							Progress progress=new Progress(fileInfo,progressCallback());
							panEast.add(progress.getComponent());
							sendProcess.put(md5, progress);
							panEast.updateUI();
						}else{
							JOptionPane.showMessageDialog(null, "当前文件正在发送,请勿重复发送！","提示",0);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void update(JsonObject qMessage) {
		String type=qMessage.get("type").getAsString();
		final JsonObject message=qMessage.getAsJsonObject("playload");
		if(type.equals("qqMes")){
			acceptMes(message);
		}else{
			acceptFile(message);
		}
	}

	/**
	 * 接受消息
	 */
	public void acceptMes(JsonObject message){
		StyledDocument doc=txtShowMes.getStyledDocument();
		try {
			doc.insertString(doc.getLength(),message.get("sender").getAsString()+"\t"+message.get("time").getAsString()+"\r\n", null);
			txtShowMes.setCaretPosition(doc.getLength());
			txtShowMes.setFormatText(message.get("mes").getAsString());
			doc.insertString(doc.getLength(), "\r\n\r\n", null);
			txtShowMes.setCaretPosition(doc.getLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 接受文件请求
	 */
	public void acceptFile(final JsonObject message){
		 String typee=message.get("type").getAsString();
		 final String md5=message.get("md5").getAsString();
		 if(typee.equals("request")){
			 if(message.get("action").getAsString().equals("send")){
				 Progress progress=new Progress(createFileInfoByAccpet(message),progressCallback());
				 acceptProcess.put(md5, progress);
				 panEast.add(progress.getComponent());
				 System.out.println("收到文件发送请求："+message.toString()); 
			 }else{
				 Progress progress=acceptProcess.get(md5);
				 disposeProgress(progress.getComponent());
				 acceptProcess.remove(md5);
				 insertStr(txtShowMes,message.get("sender").getAsString()+"取消了"+progress.getFileInfo().getFileName()+"发送请求！\r\n\r\n");
			 }
		 }else{
			 if(message.get("action").getAsString().equals("agree")){
				 TransFileInfo fileInfo=sendProcess.get(md5).getFileInfo();
				 final long size=fileInfo.getFileSize();
					FileWrite write=new FileWrite(new ICallBack() {
						long position=message.get("position").getAsLong();
						@Override
						public void exece(Object obj) {
							int i=Integer.parseInt(obj.toString());
							position+=i;
							double rate=(double)position / (double)size;
							sendProcess.get(md5).setProgressValue((int)(rate * 100));
						}
					});
					if(write.connect(message.get("host").getAsString(),message.get("port").getAsInt())){
						DestoryCenter.addSocket(EFileTransType.SEND, String.valueOf(qq.getUserName()), md5, write.getSocket());
						//将12位识别码发送给目标
						String key=String.valueOf(Owner.getQq());
						for(int i=1,j=12-key.length();i<=j;i++){
							key="0"+key;
						}
						Progress process=sendProcess.get(md5);
						long position=message.get("position").getAsInt();
						double rate=(double)position / (double)position;
						process.setProgressValue((int)(rate * 100));
						File file=new File(fileInfo.getFilePath());
						try {
							write.write(file, key+md5,position);
							insertStr(txtShowMes,DateUTil.format(new Date())+"\r\n文件:"+file.getAbsolutePath()+"发送文件成功!\r\n\r\n");
						} catch (Exception e) {
							e.printStackTrace();
							insertStr(txtShowMes,DateUTil.format(new Date())+"\r\n对方中断了文件\""+file.getName()+"\"("+UnitUtil.convertToMB(file.length())+" MB)的接受。\r\n\r\n");
						}
						DestoryCenter.removeSocket(EFileTransType.SEND, String.valueOf(qq.getUserName()), md5);
						panEast.remove(process.getComponent());
						//删除存储
						sendProcess.remove(md5);
					}
			 }else{
				  Progress process=sendProcess.get(md5);
				  TransFileInfo fileInfo=process.getFileInfo();
				  StyledDocument doc=txtShowMes.getStyledDocument();
					try {
						doc.insertString(doc.getLength(), DateUTil.format(new Date())+"\r\n对方拒绝接收\""+fileInfo.getFileName()+"\"("+UnitUtil.convertToMB(fileInfo.getFileSize())+" MB),文件发送失败。\r\n", null);
						panEast.remove(process.getComponent());
						panEast.updateUI();
						sendProcess.remove(md5);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
			 }
		 }
		panEast.updateUI();
	}
	
	public String getFrmType() {
		return this.formType;
	}

	/**
	 * 屏幕截图
	 */
	private void screenshot(){
		new ScreenShot(new ICallBack() {
			@Override
			public void exece(Object obj) {
				txtSendMes.insertIcon(new ImageIcon((Image)obj));
				try {
					BufferedImage img=(BufferedImage)obj;
					ByteArrayOutputStream bos=new ByteArrayOutputStream();
					ImageIO.write(img, "jpg", bos);
					txtSendMes.addResource(new ImgMapIndex(txtSendMes.getCaretPosition(),bos.toByteArray() , 1));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
	}

	/**
	 * 弹出表情
	 */
	private void showBrows(){
		DestoryCenter.addFrame(new Brow(new ICallBack() {
			@Override
			public void exece(Object obj) {
				int index=Integer.parseInt(obj.toString());
				txtSendMes.insertIcon(new ImageIcon(Resource.getBrowData(index)));
				int position=txtSendMes.getCaretPosition();
				txtSendMes.addResource(new ImgMapIndex(position,index,0));
			}
		}));
	}
	
	/**
	 * 发送消息
	 */
	private void sendMes(){
		String mes=txtSendMes.getFormatText();
		JsonObject root=new JsonObject();
		root.addProperty("type", "qqMes");
		JsonObject playload=new JsonObject();
		playload.addProperty("target", String.valueOf(qq.getUserName()));
		playload.addProperty("mes", mes);
		root.add("playload", playload);
		try {
			Server.write(root.toString().getBytes("UTF-8"));
			insertStr(txtShowMes,Owner.getQq()+"\t"+new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date())+"\r\n");
			txtSendMes.appendTo(txtShowMes);
			insertStr(txtShowMes,"\r\n\r\n");
			txtSendMes.setText("");
			txtSendMes.clearStore();
			txtSendMes.requestFocus();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 插入文本
	 * @param txtPanel
	 * @param str
	 */
	private void insertStr(JImageTextPane txtPanel,String str){
		StyledDocument doc=txtPanel.getStyledDocument();
		try {
			doc.insertString(doc.getLength(),str, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}

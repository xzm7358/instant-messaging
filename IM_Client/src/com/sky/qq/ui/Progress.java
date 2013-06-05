package com.sky.qq.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import com.sky.qq.biz.DestoryCenter;
import com.sky.qq.biz.FileTransfersBiz;
import com.sky.qq.entity.EFileTransType;
import com.sky.qq.entity.Owner;
import com.sky.qq.entity.TransFileInfo;
import com.sky.qq.handler.ICallBack;
import com.sky.qq.socket.FileRead;
import com.sky.qq.util.DateUTil;
import com.sky.qq.util.FileUtil;
import com.sky.qq.util.Path;
import com.sky.qq.util.UnitUtil;

/**
 * 进度条
 * 
 * @author Administrator
 * 
 */
public class Progress {
	private JProgressBar progressBar;
	private JPanel panMain = new JPanel();
	private JTextField txtFileName;
	private JTextField txtAccept = new JTextField("接受");
	private JTextField txtRefuse = new JTextField("拒绝");
	private JTextField txtCancel = new JTextField("取消");
	private JTextField txtBreakTransfer = new JTextField("续传");
	private JPanel panOperate = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	
	private ICallBack callBack;
	
	public JPanel getPanOperate() {
		return panOperate;
	}

	private TransFileInfo fileInfo;
	
	private File tempFile;
	
	
	public TransFileInfo getFileInfo() {
		return fileInfo;
	}

	public Progress(TransFileInfo fileInfo,ICallBack callBack){
		this.fileInfo=fileInfo;
		loadUI();
		this.callBack=callBack;
	}
	
	/**
	 * 加载UI
	 * @param fileTransType
	 * @param obj
	 */
	private void loadUI(){
		panMain.setLayout(new GridLayout(3, 1));
		txtFileName = new JTextField(fileInfo.getFileName());
		addProcess();
		panMain.add(txtFileName);
		panMain.add(progressBar);
		if(fileInfo.getFileTransType()==EFileTransType.ACCEPT){		//接受类型进度条
			validateFileExist();
			panOperate.add(txtAccept);
			panOperate.add(txtRefuse);
		}else{												//发送类型进度条
			panOperate.add(txtCancel);						
		}
		
		panMain.add(panOperate);
		regsiterEvent();
	}
	
	/**
	 * 判断文件是否存在
	 */
	private void validateFileExist(){
		tempFile = new File(Path.getProgramDirectory()+ "/file/temp/" + fileInfo.getMd5() + "_"+ fileInfo.getFileName());
		if (!tempFile.exists()) {
			File parentFile = tempFile.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
		}else{
			panOperate.add(txtBreakTransfer);
			double rate=(double)tempFile.length() / (double)fileInfo.getFileSize();
			setProgressValue((int)(rate * 100));
			txtBreakTransfer.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					fileInfo.setSaveSize(tempFile.length());
					addCancelButton();
					String key=Owner.getQq()+"_"+fileInfo.getTarget()+"_"+fileInfo.getMd5();
					fileInfo.setSavePath(FileUtil.getFilePath(key));
					FileTransfersBiz.sendAcceptRequest(fileInfo.getTarget(), fileInfo.getMd5(), fileInfo.getSaveSize());
					registerSaveCallback();
				}
			});
		}
	}
	
	/**
	 * 添加进度条
	 */
	private void addProcess(){
		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true); // 显示百分比字符
		progressBar.setIndeterminate(false); // 不确定的进度条
	}
	
	public void finish() {
		panMain.updateUI();
	}
	
	public JComponent getComponent(){
		return panMain;
	}
	/**
	 * 注册事件
	 */
	private void regsiterEvent() {
		if(fileInfo.getFileTransType()==EFileTransType.ACCEPT){
			//接受事件
			txtAccept.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					acceptEvent();
				}
			});
			//拒绝事件
			txtRefuse.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					FileTransfersBiz.sendRefuseRequest(fileInfo.getTarget(),fileInfo.getMd5());
					callBack.exece(new Object[]{fileInfo.getFileTransType(),fileInfo.getMd5(),DateUTil.format(new Date())+"\r\n您拒绝接收\""+fileInfo.getFileName()+"\"("+UnitUtil.convertToMB(fileInfo.getFileSize())+" MB),文件接受失败。\r\n\r\n",panMain});
				}
			});
		}else{
			//取消事件
			txtCancel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					registerCancelEvent();
				}
			});
		}
	}

	/**
	 * 注册取消事件
	 */
	private void registerCancelEvent(){
		if(fileInfo.getFileTransType()==EFileTransType.SEND){
			Socket socket=DestoryCenter.getSocket(fileInfo.getFileTransType(), String.valueOf(fileInfo.getTarget()),fileInfo.getMd5()) ;
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				FileTransfersBiz.sendCancelRequest(fileInfo.getTarget(),  fileInfo.getMd5());
			}
		}else{
			Socket socket=DestoryCenter.getSocket(fileInfo.getFileTransType(), String.valueOf(fileInfo.getTarget()), fileInfo.getMd5());
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		callBack.exece(new Object[]{fileInfo.getFileTransType(),fileInfo.getMd5(),DateUTil.format(new Date())+"\r\n您取消了\""+fileInfo.getFileName()+"\"("+UnitUtil.convertToMB(fileInfo.getFileSize())+" MB),文件传输失败。\r\n\r\n",panMain});
	}
	
	/**
	 * 接受事件
	 */
	private void acceptEvent(){
		setProgressValue(0);
		final int i = fileInfo.getFileName().indexOf(".");
		JFileChooser jChooser2 = new JFileChooser();
		jChooser2.setCurrentDirectory(new File("e:/"));// 设置默认打开路径
		jChooser2.setDialogType(JFileChooser.SAVE_DIALOG);// 设置保存对话框
		jChooser2.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return fileInfo.getFileName().substring(i);
			}

			@Override
			public boolean accept(File file) {
				return (file.isDirectory()) ? (true) : (false);
			}
		});
		getTextField(jChooser2).setText(fileInfo.getFileName());
		if (jChooser2.showDialog(null, "保存文件") == JFileChooser.APPROVE_OPTION) {
			File f = jChooser2.getSelectedFile();
			fileInfo.setSavePath(f.getAbsolutePath());
			addCancelButton();
			//注册保存回调
			registerSaveCallback();
			FileTransfersBiz.sendAcceptRequest(fileInfo.getTarget(), fileInfo.getMd5(), 0);
		}
	}
	
	/**
	 * 添加取消按钮
	 */
	private void addCancelButton(){
		panOperate.removeAll();
		panOperate.add(txtCancel);
		txtCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				registerCancelEvent();
			}
		});
		panOperate.updateUI();
	}
	
	public JTextField getTextField(Container c) {
		JTextField textField = null;
		for (int i = 0; i < c.getComponentCount(); i++) {
			Component cnt = c.getComponent(i);
			if (cnt instanceof JTextField) {
				return (JTextField) cnt;
			}
			if (cnt instanceof Container) {
				textField = getTextField((Container) cnt);
				if (textField != null) {
					return textField;
				}
			}
		}
		return textField;
	}

	public void setProgressValue(int n) {
		progressBar.setValue(n);
	}

	/**
	 * 注册保存回调
	 */
	private void registerSaveCallback(){
		//将十二位QQ号(不足则前置补0)加MD5组成Key
		String key=String.valueOf(fileInfo.getTarget());
		for(int i=1,j=12-key.length();i<=j;i++){
			key="0"+key;
		}
		key+=fileInfo.getMd5();
		FileRead.addCallBack(key,new ICallBack() {
			@Override
			public void exece(Object obj)  {
				readAndSaveFile(obj);
			}
		});
	}
	
	/**
	 * 读取并保存文件
	 */
	private void readAndSaveFile(Object obj){
		byte[] buffer=new byte[4096];
		Socket socket=(Socket)obj;
		DataInputStream dis;
		int i=0;
		String showCue=null;
		try {
			dis = new DataInputStream(socket.getInputStream());
			FileOutputStream fos=new FileOutputStream(tempFile,true);
			while((i=dis.read(buffer))!=-1){
				fileInfo.setSaveSize(fileInfo.getSaveSize()+i);
				fos.write(buffer, 0, i);
				double rate=(double)fileInfo.getSaveSize() / (double)fileInfo.getFileSize();
				setProgressValue((int)(rate * 100));
			}
			fos.flush();
			fos.close();
			if(fileInfo.getFileSize()==fileInfo.getSaveSize()){
				FileUtil.cutFile(tempFile.getAbsolutePath(), fileInfo.getSavePath());
				showCue=DateUTil.format(new Date())+"\r\n文件"+fileInfo.getSavePath()+"接受成功！\r\n\r\n";
				String key=Owner.getQq()+"_"+fileInfo.getTarget()+"_"+fileInfo.getMd5();
				FileUtil.removeFilePath(key);
			}else{
				String key=Owner.getQq()+"_"+fileInfo.getTarget()+"_"+fileInfo.getMd5();
				FileUtil.saveDataToFile(key, fileInfo.getSavePath());
				showCue=DateUTil.format(new Date())+"\r\r对方中断了\""+fileInfo.getFileName()+"\"传输！\r\n\r\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
			String key=Owner.getQq()+"_"+fileInfo.getTarget()+"_"+fileInfo.getMd5();
			FileUtil.saveDataToFile(key, fileInfo.getSavePath());
		}finally{
			DestoryCenter.removeSocket(fileInfo.getFileTransType(), String.valueOf(fileInfo.getTarget()), fileInfo.getMd5());
			callBack.exece(new Object[]{fileInfo.getFileTransType(),fileInfo.getMd5(),showCue,panMain});
		}
	}
}

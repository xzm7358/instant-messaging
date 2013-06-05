package com.sky.qq.ui;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import com.sky.qq.entity.Resource;
import com.sky.qq.util.Base64UTil;

public class JImageTextPane extends JTextPane{
	private static final long serialVersionUID = 2211800502221847793L;
	private TreeSet<ImgMapIndex> allImag=new TreeSet<ImgMapIndex>();
	private TreeSet<ImgMapIndex> showImag=new TreeSet<ImgMapIndex>();
	
	/**
	 * 将本对象中的内容追加到目标对象中
	 */
	public void appendTo(JImageTextPane target){
		Document targetDocument=target.getStyledDocument();
		String source=this.getText();
		int a=0;
		for(int i=0,j=showImag.size();i<j;i++){
			ImgMapIndex obj=showImag.pollFirst();
			allImag.add(obj);
			if(a!=obj.getIndex()){
				String str=source.substring(a, obj.getIndex()-1);
				try {
					targetDocument.insertString(targetDocument.getLength(), str, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(obj.getFlag()==1){
				target.insertIcon(new ImageIcon((byte[])obj.getValue()));
			}else{
				target.insertIcon(new ImageIcon(Resource.getBrowData(Integer.parseInt(obj.getValue().toString()))));
			}
			a=obj.getIndex();
		}
		if(a!=source.length()){
			try {
				targetDocument.insertString(targetDocument.getLength(),source.substring(a),null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 保存表情或者图片
	 * @param obj
	 */
	public void addResource(ImgMapIndex obj){
		allImag.add(obj);
	}
	
	/**
	 * 清空存储
	 */
	public void clearStore(){
		allImag.clear();
	}
	
	/**
	 * 将文本框中内容转换为传输格式
	 * @return
	 */
	public String getFormatText(){
		System.out.println("source:"+this.getText());
		StringBuilder result=new StringBuilder();
		String source=this.getText();
		int a=0;
		for(int i=0,j=allImag.size();i<j;i++){
			ImgMapIndex obj=allImag.pollFirst();
			showImag.add(obj);
			if(a!=obj.getIndex()){
				String str=source.substring(a, obj.getIndex()-1);
				result.append(str);
			}
			if(obj.getFlag()==1){
				result.append("#@~").append(Base64UTil.convertByteBase64((byte[])obj.getValue())).append("~@#");
			}else{
				result.append("#@~").append(obj.getValue().toString()).append("~@#");
			}
			a=obj.getIndex();
		}
		if(a!=source.length()){
			result.append(source.substring(a));
		}
		return result.toString();
	}
	
	/**
	 * 将格式化的字符串显示出来
	 * @param source
	 */
	public void setFormatTextOld(String source){
		//这个方法字符串太长无法处理
		Document document=getStyledDocument();
		Pattern pattern = Pattern.compile("(?<=#@~).*?(?=~@#)");
		Matcher matcher = pattern.matcher(source);
		int insertI=0;
		int a=0;
		int end=0;
		while (matcher.find()) {
			int start=matcher.start();
			end=matcher.end()+3;
			String resource=matcher.group();
			if(start>3){
				try {
					String str=source.substring(a,start-3);
					document.insertString(insertI, str, null);
					insertI+=str.length();
					a+=end+3;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(resource.length()>2){
				insertIcon(new ImageIcon(Base64UTil.convertBase64ToByte(resource)));
			}
			insertI++;
		}
		if(a!=source.length()){
			try {
				document.insertString(insertI, source.substring(a), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void setFormatText(String source){
		Document document=getStyledDocument();
		StringBuffer sb=new StringBuffer(source);
		String temp=sb.toString();
		int i=0;
		//截取~@#后面的字符串作为子串然后在处理
		while(i < sb.length()){
			int a=temp.indexOf("#@~");
			if(a!=-1){
				int z=temp.indexOf("~@#");
				try {
					if(a!=0){
						String insertStr=temp.substring(0,a);
						document.insertString(document.getLength(), insertStr, null);
						setCaretPosition(document.getLength());
					}
					i+=z+3;
				} catch (Exception e) {
					e.printStackTrace();
				}
				String resource=temp.substring(a+3, z);
				if(resource.length()>3){  //是否为图片
					this.insertIcon(new ImageIcon(Base64UTil.convertBase64ToByte(resource)));
				}else{  //为表情
					this.insertIcon(new ImageIcon(Resource.getBrowData(Integer.parseInt(resource))));
				}
				
				temp=temp.substring(z+3);
			}else{
				break;
			}
		}
		if(i<sb.toString().length()){
			try {
				document.insertString(document.getLength(),temp,null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class ImgMapIndex implements Comparable<ImgMapIndex>{
	private int index;	//表情或者图片的索引
	private Object value;  //表情值或者图片的base64码
	private int flag;  //表示表情或者图片
	
	public ImgMapIndex(int index, Object value, int flag) {
		super();
		this.index = index;
		this.value = value;
		this.flag = flag;
	}

	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public Object getValue() {
		return value;
	}


	public void setValue(Object value) {
		this.value = value;
	}


	public int getFlag() {
		return flag;
	}


	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public int compareTo(ImgMapIndex o) {
		int a=this.getIndex();
		int b=o.getIndex();
		return (a>b)?(1):((a<b)?(-1):(0));
	}
}

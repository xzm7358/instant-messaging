package com.sky.qq.ui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 * 自定义节点
 * 
 * @author Administrator
 * 
 */
public class Node extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 3429932954454597192L;
	private Icon icon;
	private String key;
	private Object tag="";
	private Map<String,Node> children=new HashMap<String,Node>();
	
	@Override
	public void add(MutableTreeNode child) {
		super.add(child);
		Node node=(Node)child;
		children.put(node.getKey(), node);
	}
	
	
	public String getKey() {
		return key;
	}


	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}


	// 包含文本和图片的节点构造
	public Node(String key,Icon icon, String txt, Object tag) {
		this(icon, txt);
		this.key=key;
		this.tag = tag;
	}

	// 包含文本和图片的节点构造
	public Node(Icon icon, String txt) {
		this.icon = icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public Icon getIcon() {
		return icon;
	}


	public Node getChildren(String key){
		return children.get(key);
	}

}

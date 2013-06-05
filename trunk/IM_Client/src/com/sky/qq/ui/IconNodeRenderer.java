package com.sky.qq.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import com.sky.qq.entity.Resource;

public class IconNodeRenderer extends DefaultTreeCellRenderer{
	
	private static final long serialVersionUID = 1648008280714383702L;
	private DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
	
	public IconNodeRenderer(){
		this.add(renderer, BorderLayout.CENTER);
		//设定节点间的距离
		this.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		this.setOpaque(false);
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		setBackgroundSelectionColor(new Color(240,247,251));
		setBackgroundNonSelectionColor(new Color(240,247,251));
		System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
		Node node=((Node) value);
		setText(node.getTag().toString());
		if(expanded){
			this.setIcon(Resource.getSpread());
		}else{
			setIcon(node.getIcon());
		}
		return this;
	}
} 
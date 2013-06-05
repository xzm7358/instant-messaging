package com.sky.qq.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.tree.TreePath;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sky.qq.biz.DestoryCenter;
import com.sky.qq.biz.EStateSerializer;
import com.sky.qq.biz.FriendBox;
import com.sky.qq.biz.FriendGroupBiz;
import com.sky.qq.biz.LeaveMessageBiz;
import com.sky.qq.biz.MessageBox;
import com.sky.qq.biz.DataPool;
import com.sky.qq.entity.*;
import com.sky.qq.handler.CommandHandler;
import com.sky.qq.handler.TempletHandler;
import com.sky.qq.socket.FileRead;
import com.sky.qq.util.Base64UTil;
import com.sky.qq.util.ImageUTil;
import com.sky.qq.util.JsonUtil;
import com.sky.qq.util.Path;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends JFrame {
	private static final long serialVersionUID = 4486259063264109578L;
	
	private byte[] face;
	private JPanel panTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
	//private JPanel panLeft = new JPanel();
	private JPanel panCenter = new JPanel();
	private JPanel panButtom = new JPanel();

	private CardLayout cLayout = new CardLayout();
	private JPanel panCenterCenter = new JPanel();

	private JPanel panFriends = new JPanel();
	private JPanel panGroups = new JPanel();
	private JPanel panTop50Friends = new JPanel();

	private Node root = new Node(null, null);// 定义根节点

	private AtomicInteger groupSize;
	private JTree tree = null;// 定义树
	private FriendGroupBiz friendGroupBiz=new FriendGroupBiz();
	private LeaveMessageBiz leaveMessageBiz=new LeaveMessageBiz();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main(null);
	}

	public Main(byte[] face) {
		this.face = face;
		this.setTitle(String.valueOf(Owner.getQq()));
		this.setSize(290, 580);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		this.setVisible(true);
		DestoryCenter.addFrame(this);
	}

	public void init() {
		panTop.setPreferredSize(new Dimension(0, 100));
		panTop.setBackground(new Color(111, 188, 234));
		this.add(panTop, "North");

//		panLeft.setPreferredSize(new Dimension(33, 0));
//		panLeft.setBackground(Color.CYAN);
//		this.add(panLeft, "West");

		this.add(panCenter);
		panButtom.setPreferredSize(new Dimension(0, 30));
		panButtom.setBackground(new Color(201, 224, 237));
		this.add(panButtom, "South");

		loadCenterUI();
		try {
			Resource.loadResource("./brows/"); // 加载资源文件
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileRead.start();
		FileRead.listener();

		registerFriendGroupHandler();
		
		friendGroupBiz.getGroup();
	}

	/**
	 * 注册好友分组处理器
	 */
	private void registerFriendGroupHandler() {
		CommandHandler.regsiterHandler(new TempletHandler("friendGroup") {
			@Override
			public void run(JsonObject obj) {
				loadGroupOrFriend(obj);
			}
		});
	}
	
	/**
	 * 注册其它处理器
	 */
	private void registerOtherHandler(){
		//注册好友状态处理器
		CommandHandler.regsiterHandler(new TempletHandler("state") {
			@Override
			public void run(JsonObject obj) {
				updateUserState(obj.get("playload").getAsJsonObject());
			}
		});
		// 注册好友消息处理器
		CommandHandler.regsiterHandler(new TempletHandler("qqMes") {
			@Override
			public void run(JsonObject obj) {
				MessageBox.execMes(obj);
			}
		});

		// 注册群消息处理器
		CommandHandler.regsiterHandler(new TempletHandler("groupMes") {
			@Override
			public void run(JsonObject obj) {
				MessageBox.execMes(obj);
			}
		});
		//注册文件传输处理器
		CommandHandler.regsiterHandler(new TempletHandler("fileTrans") {
			@Override
			public void run(JsonObject obj) {
				MessageBox.execMes(obj);
			}
		});
		// 离线消息处理器
		CommandHandler.regsiterHandler(new TempletHandler("leaveMes") {
			@Override
			public void run(JsonObject obj) {
				MessageBox.execMes(obj);
			}
		});
	}

	/**
	 * 注册事件
	 */
	private void registerEvent() {
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int count = tree.getRowForLocation(e.getX(), e.getY());
				if (count != -1) {
					int selRow = tree.getRowForLocation(e.getX(), e.getY());
					if (selRow != 0) {
						TreePath path = tree.getPathForLocation(e.getX(),e.getY());
						Node select = (Node) path.getLastPathComponent();
						if (e.getClickCount() == 2) {
							QQInfor qq = (QQInfor) select.getTag();
							if (!MessageBox.getInstance().observerExist(String.valueOf(qq.getUserName()),FormType.friendMes)) {
								Chat chat=new Chat(qq);
								MessageBox.getInstance().attach(String.valueOf(qq.getUserName()),chat);
								DestoryCenter.addFrame(chat);
							}
						}
					}
				}
			}
		});
	}

	/**
	 * 更新用户状态
	 */
	private void updateUserState(JsonObject obj) {
		EState state = EState.getState(obj.get("state").getAsInt());
		String userName = String.valueOf(obj.get("userName").getAsInt());
		String groupId = FriendBox.getGroupId(userName);
		FriendGroup friendGroup = (FriendGroup) root.getChildren(groupId).getTag();
		Node node = root.getChildren(String.valueOf(groupId)).getChildren(String.valueOf(userName));
		QQInfor qq = (QQInfor) node.getTag();
		if (state == EState.ONLINE) {
			node.setIcon(new ImageIcon(qq.getFaceImgData()));
			friendGroup.setOnlineNum(friendGroup.getOnlineNum() + 1);
		} else if (state == EState.OFFLINE) {
			node.setIcon(new ImageIcon(ImageUTil.grayImage(ImageUTil.convertByteToBufferedImage(qq.getFaceImgData()))));
			friendGroup.setOnlineNum(friendGroup.getOnlineNum() - 1);
		}
		tree.updateUI();
	}

	/**
	 * 加载中部UI 导航
	 */
	public void loadCenterUI() {
		JLabel labFace = new JLabel(new ImageIcon(face));
		panTop.add(labFace);

		JPanel panCenterTop = new JPanel(null);
		panCenterTop.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panCenter.setLayout(new BorderLayout());
		panCenterTop.setBackground(new Color(28, 128, 187));
		panCenterTop.setPreferredSize(new Dimension(10, 26));
		panCenter.add(panCenterTop, "North");
		panCenterCenter.setLayout(cLayout);

		panFriends.setBackground(new Color(240, 247, 251));
		panCenterCenter.add(panFriends, "friends");
		panFriends.setLayout(new BorderLayout());

		panGroups.setBackground(Color.red);
		panCenterCenter.add(panGroups, "groups");

		panTop50Friends.setBackground(new Color(28, 128, 187));
		panCenterCenter.add(panTop50Friends, "top50Friends");

		panCenter.add(panCenterCenter);

	}

	/**
	 * 加载好友分组或者好友
	 */
	private void loadGroupOrFriend(JsonObject obj) {
		obj = obj.getAsJsonObject("playload");
		if (obj.get("type").getAsString().equals("group")) {
			loadGroup(obj.get("groups"));
		}else{
			loadFriend(obj);
		}
	}

	/**
	 * 加载好友
	 * 
	 * @param jsonElement
	 */
	private void loadFriend(JsonObject jsonObject) {
		JsonArray friends = jsonObject.get("friends").getAsJsonArray();
		int groupId = jsonObject.get("groupId").getAsInt();
		FriendGroup friendGroup = (FriendGroup) root.getChildren(String.valueOf(groupId)).getTag();
		if (friends.size() > 0) {
			List<QQInfor> qqs = JsonUtil.convertToObject(new EStateSerializer(),new TypeToken<ArrayList<QQInfor>>(){}.getType(), friends.toString());
			int i = 0;
			for (QQInfor qq : qqs) {
				MessageBox.addQQKey(String.valueOf(qq.getUserName()));
				FriendBox.setGroupId(String.valueOf(qq.getUserName()), String.valueOf(groupId));
				byte[] data = Base64UTil.convertBase64ToByte(qq.getFaceImg());
				qq.setFaceImgData(data);
				ImageIcon icon = null;
				if (qq.getState() != EState.ONLINE) {
					icon = new ImageIcon(ImageUTil.grayImage(ImageUTil.convertByteToBufferedImage(data)));
				} else {
					i++;
					icon = new ImageIcon(data);
				}
				root.getChildren(String.valueOf(groupId)).add(new Node(String.valueOf(qq.getUserName()), icon, qq.getNickName(), qq));
			}
			friendGroup.setOnlineNum(i);
		} else {
			friendGroup.setOnlineNum(0);
		}
		friendGroup.setFriendSize(friends.size());
		
		if (groupSize.decrementAndGet() == 0) {
			tree.updateUI();
			registerOtherHandler();
			DataPool.pushData();
			leaveMessageBiz.getLeaveMessage();
		}
	}

	/**
	 * 加载好友分组
	 * 
	 * @param jsonElement
	 */
	private void loadGroup(JsonElement jsonElement) {
		List<FriendGroup> groups = JsonUtil.convertToObject(new TypeToken<ArrayList<FriendGroup>>() {}.getType(), jsonElement.toString());
		groupSize = new AtomicInteger(groups.size());

		for (FriendGroup fg : groups) {
			//ImageIcon icon = new ImageIcon(Path.getProgramDirectory()+ "/resource/system/shousuo.jpg");
			Node groupNode = new Node(String.valueOf(fg.getId()), Resource.getShrinkage(),fg.getName(), fg);
			root.add(groupNode);
		}
		tree = new JTree(root);// 定义树
		tree.setBackground(new Color(240, 247, 251));
		tree.putClientProperty("JTree.lineStyle", "None"); // 去掉连接线
		tree.setCellRenderer(new IconNodeRenderer()); // 设置单元格描述 树的节点的效果
		tree.setEditable(false); // 设置树是否可编辑
		tree.setRootVisible(false);// 设置树的根节点是否可视
		tree.setToggleClickCount(1);// 设置单击几次展开数节点
		panFriends.add(new JScrollPane(tree));
		registerEvent();
		tree.updateUI();
		for(FriendGroup fr : groups){
			friendGroupBiz.getFriendByGroupId(fr.getId());
		}
	}


}

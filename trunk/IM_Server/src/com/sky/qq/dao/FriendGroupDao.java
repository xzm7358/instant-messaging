package com.sky.qq.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.entity.FriendGroup;
import com.sky.qq.entity.QQInfor;
import com.sky.qq.util.ClasspathLoader;
import com.sky.qq.util.ImageUTil;

@Service
public class FriendGroupDao {
	
	@Autowired
	private BaseDao baseDao;
	
	/**
	 * 添加好友分组
	 * @param qq
	 * @param groupName
	 * @return
	 */
	public boolean addGroup(int qq,String groupName){
		return baseDao.exeProc("UP_FriendGroup_Add", qq,groupName);
	}
	
	/**
	 * 获取QQ分组
	 * @param qq
	 * @return
	 */
	public List<FriendGroup> getFriendGroups(int qq){
		List<FriendGroup> result=new ArrayList<FriendGroup>();
		ResultSet rs=baseDao.execProcReutnSet("UP_FriendGroup_Get", qq);
		try {
			while(rs.next()){
				result.add(new FriendGroup(rs.getInt("frId"),rs.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			baseDao.closeDB();
		}
		return result;
	}
	
	/**
	 * 获取分组好友列表
	 * @param qqNum
	 * @param friendGroupId
	 * @return
	 */
	public List<QQInfor> getQQFriendsByGroupId(int qqNum,int friendGroupId){
 		List<QQInfor> result=new ArrayList<QQInfor>();
		ResultSet rs=baseDao.execProcReutnSet("UP_FriendListByGroupId_Get", qqNum,friendGroupId);
		try {
			while(rs.next()){
				int userName=rs.getInt("uId");
				String faceImg=ImageUTil.getImageBinaryByFile(ClasspathLoader.getProgramDirectory()+"/Client/"+userName+"/faceImage/"+rs.getString("faceImg"));
				result.add(new QQInfor(userName,rs.getString("nickName"),faceImg));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			baseDao.closeDB();
		}
		return result;
	}
	
	/**
	 * 获取所有好友列表
	 * @param qqNum
	 * @return
	 */
	public List<QQInfor> getQQFriends(int qqNum){
		List<QQInfor> result=new ArrayList<QQInfor>();
		ResultSet rs=baseDao.execProcReutnSet("UP_FriendList_Get", qqNum);
		try {
			while(rs.next()){
				QQInfor qqInfor=new QQInfor();
				qqInfor.setUserName(rs.getInt("uId"));
				result.add(qqInfor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			baseDao.closeDB();
		}
		return result;
	}
	
	/**
	 * 添加默认分组
	 */
	public void addDefaultGroups(int qqNum){
		baseDao.execProcReutnSet("UP_FriendGroup_Add_Default", qqNum);
	}
}

package com.sky.qq.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.entity.QQInfor;

/**
 * 好友列表Dao
 * @author Administrator
 *
 */
@Service
public class FriendListDao {
	
	@Autowired
	private BaseDao baseDao;
	private int pageSize=20;
	
	/**
	 * 分页查询QQ
	 * @param qq
	 * @param page
	 * @return
	 */
	public List<QQInfor> searchQQ(int qq,int page){
		List<QQInfor> result=new ArrayList<QQInfor>();
		ResultSet rs=baseDao.execProcReutnSet("UP_FriendList_Search",qq,(page - 1) * pageSize,page * pageSize);
		try {
			while(rs.next()){
				QQInfor qqInfo=new QQInfor(rs.getInt("uId"),rs.getString("nickName"),rs.getString("faceImg"));
				result.add(qqInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			baseDao.closeDB();
		}
		return result;
	}
	
	/**
	 * 统计好友数量
	 * @param qq
	 * @return
	 */
	public int getTotalQQ(int qq){
		int result=0;
		ResultSet rs=baseDao.execProcReutnSet("UP_FriendList_Total", qq);
		try {
			while(rs.next()){
				result=rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			baseDao.closeDB();
		}
		return result;
	}
}

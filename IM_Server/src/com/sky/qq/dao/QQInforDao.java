package com.sky.qq.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.entity.QQInfor;
import com.sky.qq.util.ClasspathLoader;
import com.sky.qq.util.ImageUTil;

@Service
public class QQInforDao {
	
	@Autowired
	private BaseDao baseDao;
	
	/**
	 * 用户登陆
	 * @param qq
	 * @return
	 */
	public boolean login(QQInfor qq){
		boolean result=false;
		ResultSet rs=baseDao.execProcReutnSet("UP_Users_Login", qq.getUserName(),qq.getPassword());
		try {
			if(rs.next()){
				if(rs.getInt("total")==1){
					result=true;
				}
			}
			baseDao.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据用户名获取用户详细资料
	 * @param qq
	 * @return
	 */
	public QQInfor getQqInfor(QQInfor qq){
		ResultSet rs=baseDao.execProcReutnSet("UP_Users_GetInfo", qq.getUserName());
		try {
			if(rs.next()){
				qq.setNickName(rs.getString("nickName"));
				qq.setFaceImg(rs.getString("faceImg"));
			}
			baseDao.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//读取QQ头像Base64码
		//qq.setFaceImg(ImageUTil.getImageBinaryByFile(new File(ClasspathLoader.getProgramDirectory()+"/Client/"+qq.getUserName()+"/faceImage/"+qq.getFaceImg())));
		try {
			String data=ImageUTil.getImageBinaryByFile(ClasspathLoader.getProgramDirectory()+"/Client/"+qq.getUserName()+"/faceImage/"+qq.getFaceImg());
			//String data = ImageUTil.convertImgToBase64(ImageUTil.changeImage(ClasspathLoader.getProgramDirectory()+"/Client/"+qq.getUserName()+"/faceImage/"+qq.getFaceImg()));
			qq.setFaceImg(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return qq;
	}
	
	
	
	/**
	 * 获取QQ分组好友
	 * @param qqNum
	 * @return
	 */
//	public Map<String,List<QQInfor>>  getQQFriendsByQQ(int qqNum){
//		Map<String,List<QQInfor>> result=new HashMap<String,List<QQInfor>>();
//		ResultSet rs=baseDao.execProcReutnSet("UP_Users_GetFriends", qqNum);
//		try {
//			while(rs.next()){
//				String groupName=rs.getString("name");
//				List<QQInfor> qqInfors=null;
//				if(result.containsKey(groupName)){
//					qqInfors=result.get(groupName);
//				}else{
//					qqInfors=new ArrayList<QQInfor>();
//					result.put(groupName, qqInfors);
//				}
//				QQInfor qq=new QQInfor();
//				int userName=rs.getInt("uId");
//				if(userName!=0){
//					qq.setUserName(userName+"");
//					qq.setNickName(rs.getString("nickName"));
//					String faceIme=rs.getString("faceImg");
//					qq.setFaceImg(Base64UTil.getImageBinaryByFile(new File(ClasspathLoader.getProgramDirectory()+"/Client/"+userName+"/faceImage/"+faceIme)));
//					qqInfors.add(qq);
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/**
	 * 添加QQ号
	 * @param qqInfor
	 * @return
	 */
	public int addQQ(QQInfor qqInfor){
		int result=0;
		ResultSet rs=baseDao.execProcReutnSet("UP_Users_Add",qqInfor.getNickName(),qqInfor.getPassword());
		try {
			if(rs.next()){
				result=rs.getInt("qqNum");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {
//		NException.setLogPath(ClasspathLoader.getProgramDirectory());
//		ClasspathLoader.loadJars();
//		ProgramVariable.setConfig(new ConfigUtil().readConfigReturnT(Config.class));
//		QQInfor qInfor=new QQInfor();
//		qInfor.setNickName("xiaozhang");
//		qInfor.setPassword("123456");
//		System.out.println(new QQInforDao().addQQ(qInfor));
//		QQInfor qqInfor=new QQInfor();
//		qqInfor.setUserName("123456");
//		qqInfor.setPassword("12345");
//		qqInfor.setNickName("zhangsan");
//		qqInfor.setFaceImg("www");
//		GsonBuilder d=new GsonBuilder();
//		d=d.excludeFieldsWithoutExposeAnnotation();
//		Gson gson= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//		System.out.println(gson.toJson(qqInfor));
	}
	
}

package com.sky.qq.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.dao.QQInforDao;
import com.sky.qq.entity.QQInfor;
import com.sky.qq.util.ClasspathLoader;

@Service
public class QQInforBiz {
	
	@Autowired
	private QQInforDao qQDao; 
	
	/**
	 * 用户登录
	 * @param qq
	 * @return
	 */
	public boolean login(QQInfor qq){
		return qQDao.login(qq);
	}
	

    /**
     * 通过QQ查询QQ信息
     * @param qqNum
     * @return
     */
    public QQInfor getQQInforByQQNum(QQInfor qq){
    	return qQDao.getQqInfor(qq);
    }
    
    /**
     * 注册QQ
     * @param qqInfor
     * @return
     */
    public int registerQQ(QQInfor qqInfor){
    	return qQDao.addQQ(qqInfor);
    }
    
    /**
     * 初始化头像
     */
    public void initFaceImg(int qq){
    	File defaultFaceImg=new File(ClasspathLoader.getProgramDirectory()+"/client/share/faceImage/1.jpg");
    	File qqFile=new File(ClasspathLoader.getProgramDirectory()+"/Client/"+qq+"/faceImage/1.jpg");
    	qqFile.getParentFile().mkdirs();
    	try {
    		byte[] buffer=new byte[1024];
			FileInputStream fis=new FileInputStream(defaultFaceImg);
			FileOutputStream fos=new FileOutputStream(qqFile);
			int i=-1;
			while((i=fis.read(buffer))!=-1){
				fos.write(buffer,0,i);
			}
			fos.flush();
			fos.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}

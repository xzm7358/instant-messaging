package com.sky.qq.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.ProgramVariable;
import com.sky.qq.entity.Config;
import com.sky.qq.util.ClasspathLoader;
import com.sky.qq.util.ConfigUtil;
import com.sky.qq.util.NException;

@Service
public class BaseDao {
	private static DataSource dataSource=new DataSource();
	
	private Connection conn = null;
	private Statement st = null;
	private ResultSet rs = null;

	/**
	 * 获得连接
	 * @throws SQLException 
	 * @throws Exception 
	 */
	public void getConnection() throws SQLException  {
		conn= dataSource.getConnection();
	}
	
	/**
	 * 创建Statement
	 * @throws SQLException
	 */
	public void getStatement() throws SQLException{
		st=conn.createStatement();
	}

	/**
	 * 释放连接
	 */
	public void closeDB() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (st != null) {
				st.close();
				st = null;
			}
			if (dataSource != null) {
				dataSource.closeConnection(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 执行sql语句 返回boolean
	 * 
	 * @param sql
	 * @return
	 */
	public  boolean execSql(String sql) {
		boolean result = false;
		try {
			getConnection();
			getStatement();
			st.executeUpdate(sql);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return result;
	}

	/**
	 * 执行sql语句返回ResultSet
	 * 
	 * @param sql
	 * @return
	 */
	public ResultSet execSqlReturnSet(String sql) {
		try {
			getConnection();
			getStatement();
			rs = st.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 获得CallableStatement
	 * @param procName
	 * @param nums
	 * @return
	 * @throws Exception
	 */
	private CallableStatement getCallStatement(String procName,int nums){
		CallableStatement statement=null;
		try{
			StringBuilder sb=new StringBuilder().append("{call ").append(procName).append("(");
			for(int i=1;i<=nums;i++){
				sb.append("?,");
			}
			getConnection();
			statement=conn.prepareCall(sb.replace(sb.length()-1, sb.length(), ")").append("}").toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return statement;
		
	}
	
	/**
	 * 执行存储过程 不返回ResultSet
	 * @param statement
	 * @param proc
	 * @return
	 */
	public boolean exeProc(String procName,Object... proc){
		boolean result=true;
		try{
			CallableStatement statement=packageProc(procName,proc);
			statement.executeUpdate();
		}catch (Exception e) {
			result=false;
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 封装存储过程
	 * @param procName
	 * @param proc
	 * @throws Exception 
	 */
	private CallableStatement packageProc(String procName,Object... proc) throws Exception{
		CallableStatement statement=getCallStatement(procName,proc.length);
		for(int i=1;i<=proc.length;i++){
			statement.setObject(i, proc[i-1]);
		}
		return statement;
	}
	
	/**
	 * 执行存储过程 返回ResultSet
	 * @param procName
	 * @param proc
	 * @return
	 */
	public ResultSet execProcReutnSet(String procName,Object... proc){
		ResultSet rs=null;
		try{
			CallableStatement statement=packageProc(procName,proc);
			rs=statement.executeQuery();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static void main(String[] args) throws Exception {
		NException.setLogPath(ClasspathLoader.getProgramDirectory());
		//NException.consoleFording("qqLog");
		//ClasspathLoader.loadJars();
		ProgramVariable.setConfig(new ConfigUtil().readConfigReturnT(Config.class));
		//InfuseUtil.setBaseClassPath(ProgramVariable.getConfig().getClassPath());
		
		//SgetConnection();
		//QQInfor qq=new QQInfor();
		//qq.setPassword("2323");
		//qq.setNickName("yuyu");
//		List<String> proc=new ArrayList<String>();
//		proc.add("550451685");
//		proc.add("22s");
//		ResultSet rs=execProcReutnSet("UP_Users_Login",proc);
//		while (rs.next()) {
//			System.out.println(rs.getObject(1));
//		}
		
		//getCallStatement("addQQ",3);
	}
}

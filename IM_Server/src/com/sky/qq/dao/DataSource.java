package com.sky.qq.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;

import com.sky.qq.biz.ProgramVariable;
import com.sky.qq.entity.Config;

public class DataSource implements javax.sql.DataSource {
	/** 驱动 */
	private String driver;
	/** url */
	private String url;
	/** 用户名 */
	private String user;
	/** 密码 */
	private String password;
	/** 初始化连接数 */
	private int initCount;
	/** 连接池中最大连接数 */
	private int maxCount;
	/** 当前使用连接数 */
	private int currentCount = 0;
	/** 等待连接最长时间 */
	private int wait = 3000;
	/** 连接池 */
	private Vector<ConnectionPool> connections;

	public DataSource() {
		init();
		initConnections();
	}

	/**
	 * 初始化
	 */
	private void init() {
		Config config = ProgramVariable.getConfig();
		driver = config.getDriverName();
		url = config.getDatabaseUrl();
		user = config.getUserName();
		password = config.getPassword();
		initCount = config.getIntCount();
		maxCount = config.getMaxCount();
		connections = new Vector<ConnectionPool>();
	    try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}    
	}

	/**
	 * 初始化连接池中数据库连接个数
	 * 
	 */
	private void initConnections() {
		for (int i = 0; i < initCount; i++) {
			try {
				Connection conn = createConnection();
				if (i == 0) {
					DatabaseMetaData metaData = conn.getMetaData();
					int dataCount = metaData.getMaxConnections();
					System.out.println("数据库最大连接为:" + dataCount);
					if (dataCount > 0 && maxCount > dataCount) {
						maxCount = dataCount;
					}
				}
				ConnectionPool pool = new ConnectionPool(conn);
				connections.addElement(pool);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("初始化连接池错误");
			}
		}
	}

	/**
	 * 创建连接对象
	 * @return
	 * @throws SQLException
	 */
	private Connection createConnection() throws SQLException {
		currentCount++;
		return DriverManager.getConnection(url, user, password);
	}

	/**
	 * 当数据源中原有连接已用完时，创建链接
	 * 
	 * @return
	 * @throws SQLException
	 */
	private ConnectionPool createConnPool() throws SQLException {
		Connection conn = createConnection();
		ConnectionPool pool = new ConnectionPool(conn);
		pool.setBusy(true);
		return pool;
	}

	/**
	 * 从连接池中得到连接对象
	 */
	public synchronized Connection getConnection() throws SQLException {
		if (connections == null) {
			System.out.println();
			return null;
		}
		Connection conn = findFreeConnection();
		if (conn == null) {
			if (currentCount <= maxCount) {
				ConnectionPool conn1 = createConnPool();
				return conn1.getConn();
			} else {
				try {
					Thread.sleep(wait);
					return findFreeConnection();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return conn;
	}

	/**
	 * 查找空闲链接
	 * 
	 * @return
	 */
	private Connection findFreeConnection() {
		Connection conn = null;
		if (connections.size() > 0) {
			Enumeration<ConnectionPool> enu = connections.elements();
			while (enu.hasMoreElements()) {
				ConnectionPool pool = (ConnectionPool) enu.nextElement();
				if (!(pool.isBusy())) {
					conn = pool.getConn();
					pool.setBusy(true);
					return conn;
				}

			}
		}
		return conn;
	}

	/**
	 * 关闭连接对象
	 * 
	 * @param conn
	 */
	public synchronized void closeConnection(Connection conn) {
		currentCount--;
		if (connections.size() == 0 || connections == null) {
			return;
		}
		Enumeration<ConnectionPool> enu = connections.elements();
		while (enu.hasMoreElements()) {
			ConnectionPool pool = (ConnectionPool) enu.nextElement();
			if (pool.getConn() == conn) {
				pool.setBusy(false);
				break;
			}
		}
	}

	/**
	 * 单独创建一个连接，不从连接池中去取
	 */
	public synchronized Connection getConnection(String username,
			String password) throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	/**
	 * 如果当前没有可用连接，设置等待时间 毫秒数
	 */
	public void setLoginTimeout(int seconds) throws SQLException {
		this.wait = seconds;
	}

	/**
	 * 或得系统等待连接时间
	 */
	public int getLoginTimeout() throws SQLException {
		return wait;
	}


	/**
	 * 包装链接对象
	 * 
	 * @author Administrator
	 * 
	 */
	class ConnectionPool {
		private Connection conn;
		private boolean busy = false;

		private ConnectionPool(Connection conn) {
			this.conn = conn;
		}

		private void setBusy(boolean busy) {
			this.busy = busy;
		}

		private boolean isBusy() {
			return busy;
		}

		private Connection getConn() {
			return conn;
		}
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}

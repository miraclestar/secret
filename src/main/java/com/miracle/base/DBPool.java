package com.miracle.base;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class DBPool {
	
	private static Logger log = LoggerFactory.getLogger(DBPool.class);

    private static String configPath = System.getProperty("user.home") + "/conf/jdbc.properties";

	private static DBPool dbPool = new DBPool();

	private static ConcurrentMap<String, ComboPooledDataSource> dataSources = new ConcurrentHashMap<String, ComboPooledDataSource>();
	
	private DBPool() {
	}

	public void destory() {
		for (ComboPooledDataSource dataSource : dataSources.values()) {
			dataSource.close();
		}
		dataSources = null;
	}
	/**
	 * 
	 * @param conf
	 */
	public static void setConfig(String conf) {
		configPath = conf;
	}

	public final static DBPool getInstance() {
		return dbPool;
	}

	public final Connection getConnection(String connName) {
		try {
			Connection conn = getDataSource(connName).getConnection();
			return conn;
		} catch (SQLException e) {
			log.error("can't get the connection " + connName, e);
			throw new RuntimeException("unable to connect to the database " + connName, e);
		}
	}

	private ComboPooledDataSource getDataSource(String connName) {

		ComboPooledDataSource dataSource = dataSources.get(connName);

		if (dataSource != null) {
			return dataSource;
		}

		dataSource = initialDBpool(connName, dataSource);

		dataSources.putIfAbsent(connName, dataSource);

		return dataSource;
	}

	public ComboPooledDataSource initialDBpool(String connName, ComboPooledDataSource dataSource) {
		try {
			log.info("load db config: {}", configPath);
			File config = new File(configPath);
			
			InputStream is = new FileInputStream(config);
			
			Properties p = new Properties();
			p.load(is);
			String dbUrl = p.getProperty(connName + ".url");
			String dbUser = p.getProperty(connName + ".username");
			String dbPwd = p.getProperty(connName + ".password");
			String driver = p.getProperty("driver");
			dataSource = new ComboPooledDataSource();
			dataSource.setUser(dbUser);
			dataSource.setPassword(dbPwd);
			dataSource.setJdbcUrl(dbUrl);
			dataSource.setDriverClass(driver);
			dataSource.setInitialPoolSize(5);
			dataSource.setMinPoolSize(2);
			dataSource.setMaxPoolSize(50);
			dataSource.setMaxStatements(50);
			dataSource.setMaxIdleTime(60);
			dataSource.setAcquireIncrement(5);
			is.close();
		} catch (PropertyVetoException e) {
			log.error("jdbc.prop error " + connName, e);
			throw new RuntimeException("jdbc.prop error " + connName, e);
		} catch (Exception e) {
			log.error("datasource generate error " + connName, e);
			throw new RuntimeException("datasource generate error " + connName, e);
		}
		return dataSource;
	}

	public static void close(Statement pstmt, ResultSet rs, Connection conn) {
		try {
			if (pstmt != null)
				pstmt.close();
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.error("close conn error ", e);
		}
	}

	/**
	 * @param sql,dbname
	 * @return
	 */
	public static boolean commonExe(String sql, String dbname) throws SQLException {

		int flag = 0;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		conn = DBPool.getInstance().getConnection(dbname);
		pstmt = conn.prepareStatement(sql);
		flag = pstmt.executeUpdate();

		DBPool.close(pstmt, rs, conn);

		if (flag != 0) {
			return true;
		} else {
			return false;
		}
	}

}

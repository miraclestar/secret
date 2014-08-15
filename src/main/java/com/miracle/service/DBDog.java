package com.miracle.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.miracle.base.DBPool;

/**
 * 为什么不能叫dog，嘿嘿
 * 
 * @author hyliu
 * 
 */
public class DBDog {

	public static void main(String args[]) {
		System.out.println(new Timestamp(Long.valueOf(1367828663000l)));
		System.out.println(Calendar.getInstance(TimeZone.getTimeZone("GMT+01:00")).getTimeInMillis());
		System.out.println(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		System.out.println(new Timestamp(Calendar.getInstance().getTimeInMillis() + 28800000));
	}

	private static Logger log = Logger.getLogger(DBDog.class);

    // 随即获取一个秘密
	public static String getSecretPic(String fromUsername) {
		String ret = null;
        Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select secret,create_dt from secret WHERE own=1 and uid<>'" + fromUsername + "' ORDER BY RAND() LIMIT 1";
		log.debug("sql : " + sql);
		try {
            conn = DBPool.getInstance().getConnection("secret");
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ret = rs.getString("secret");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			log.error("~~~~~~~~~~~~~~~ query secret error ! ", e);
		} finally {
            DBPool.close(pstmt, rs, conn);
		}
		return ret;

	}

	public static String getSecretComm(String secret) {
		String ret = null;
        Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select uid,secret,create_dt from secret WHERE secret='" + secret + "'";
		log.debug("sql : " + sql);
		try {
            conn = DBPool.getInstance().getConnection("secret");
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ret = rs.getString("secret");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			log.error("~~~~~~~~~~~~~~~ query secret error ! ", e);
		} finally {
            DBPool.close(pstmt, rs, conn);
		}
		return ret;

	}

	public static boolean checkmd5(String md5) {

		boolean ret = false;
        Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * from secret WHERE md5='" + md5 + "'";
		log.debug("sql : " + sql);
		try {
            conn = DBPool.getInstance().getConnection("secret");
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ret = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			log.error("~~~~~~~~~~~~~~~ query md5 error ! ", e);
		} finally {
            DBPool.close(pstmt, rs, conn);
		}
		return ret;
	}

	public static void saveSecret(String fromUsername, String picUrl, String md5) {
        Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "insert into secret(uid,secret,own,create_dt,md5) values(?,?,?,?,?)";
		try {
            conn = DBPool.getInstance().getConnection("secret");
			Timestamp t = new Timestamp(Calendar.getInstance().getTimeInMillis());
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromUsername);
			pstmt.setString(2, picUrl);
			pstmt.setInt(3, 1);
			pstmt.setTimestamp(4, t);
			pstmt.setString(5, md5);
			pstmt.executeUpdate();
            log.info("saved uid:" + fromUsername + ",secret:" + picUrl + ",md5:" + md5);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("~~~~~~~~~~~~~~~ save error ! ", e);
		} finally {
            DBPool.close(pstmt, rs, conn);
		}
	}

	public static void saveLiuyan(String fromUsername, String content) {
        Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "insert into liuyan(uid,content,dt) values(?,?,?)";
		try {
            conn = DBPool.getInstance().getConnection("secret");
			Timestamp t = new Timestamp(Calendar.getInstance().getTimeInMillis());
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromUsername);
			pstmt.setString(2, content);
			pstmt.setTimestamp(3, t);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			log.error("~~~~~~~~~~~~~~~ save error ! ", e);
		} finally {
            DBPool.close(pstmt, rs, conn);
		}
	}

	public static String getWZSecret(String fromUsername) {
		String ret = null;
        Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select secret from wz_secret WHERE uid<>'" + fromUsername + "' ORDER BY RAND() LIMIT 1";
		log.debug("sql : " + sql);
		try {
            conn = DBPool.getInstance().getConnection("secret");
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ret = rs.getString("secret");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			log.error("~~~~~~~~~~~~~~~ query wz secret error ! ", e);
		} finally {
            DBPool.close(pstmt, rs, conn);
		}
		return ret;
	}

	public static void saveWZSecret(String fromUsername, String content) {
        Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "insert into wz_secret(uid,secret,dt) values(?,?,?)";
		try {
            conn = DBPool.getInstance().getConnection("secret");
			Timestamp t = new Timestamp(Calendar.getInstance().getTimeInMillis());
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromUsername);
			pstmt.setString(2, content);
			pstmt.setTimestamp(3, t);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			log.error("~~~~~~~~~~~~~~~ save error ! ", e);
		} finally {
            DBPool.close(pstmt, rs, conn);
		}

	}

	public static String getVoice(String fromUsername) {
		String ret = null;
        Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select secret,create_dt from secret WHERE own=1 and md5='voice' and uid<>'" + fromUsername + "' ORDER BY RAND() LIMIT 1";

		log.debug("sql : " + sql);
		try {
            conn = DBPool.getInstance().getConnection("secret");
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ret = rs.getString("secret");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			log.error("~~~~~~~~~~~~~~~ query secret error ! ", e);
		} finally {
            DBPool.close(pstmt, rs, conn);
		}
		return ret;
	}
}

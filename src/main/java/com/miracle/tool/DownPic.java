package com.miracle.tool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DownPic {

	public static String calcMD5(String urlString) throws IOException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		String md5 = "";
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 输入流
		InputStream is = con.getInputStream();
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		MessageDigest messagedigest = MessageDigest.getInstance("MD5");
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			messagedigest.update(bs, 0, len);
		}
		md5 = MD5Util.bufferToHex(messagedigest.digest());
		is.close();
		return md5;
	}

	/**
	 * 下载文件到本地
	 * 
	 * @param urlString
	 *            被下载的文件地址
	 * @param filename
	 *            本地文件名
	 * @throws Exception
	 *             各种异常
	 */
	public static String download(String urlString, String filename) throws Exception {

		String md5 = "";
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 输入流
		InputStream is = con.getInputStream();
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		OutputStream os = new FileOutputStream(filename);
		MessageDigest messagedigest = MessageDigest.getInstance("MD5");
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
			messagedigest.update(bs, 0, len);
		}
		md5 = MD5Util.bufferToHex(messagedigest.digest());
		// 完毕，关闭所有链接
		os.close();
		is.close();
		return md5;
	}

	public static void main(String[] args) throws Exception {
		String md5 = calcMD5("http://mmsns.qpic.cn/mmsns/ZlYdIP6NUnMGZfefT7pIoXib96pPEazGWA9l4LNhkzJnCdXL1wbBAHA/0");
		System.out.println(md5);
	}
}

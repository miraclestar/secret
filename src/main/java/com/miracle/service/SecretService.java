package com.miracle.service;

import info.xmark.core.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.miracle.tool.DownPic;

public class SecretService {

	private static Logger log = Logger.getLogger(SecretService.class);

	public static String reply(HttpServletRequest request) {
		String ret = "";
		// 1、分析用户请求
		String postStr = null;
		try {
			postStr = Service.readPostXML(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("received ： " + postStr);

		if (null != postStr && !postStr.isEmpty()) {
			Document document = null;
			try {
				document = DocumentHelper.parseText(postStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null == document || document.equals("")) {
				log.info(" document is empty !!!");
				return "";
			}

			Element root = document.getRootElement();
			String fromUsername = root.elementText("FromUserName");
			String toUsername = root.elementText("ToUserName");
			String msgType = root.elementText("MsgType");
			String time = root.elementText("CreateTime");
			if (msgType != null) {

				if (msgType.equals("image")) {
					String picUrl = root.elementText("PicUrl");

					String md5 = "";
					try {
						md5 = DownPic.calcMD5(picUrl);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (DBDog.checkmd5(md5)) {
						ret = BirdSing.singAsong("这个图片秘密已经上传过了，请不要重复上传，谢谢~", fromUsername, toUsername, time);
					} else {
						// 用户的秘密，需要回复给他一个秘密,保存秘密
						// 保存
						DBDog.saveSecret(fromUsername, picUrl, md5);
						// 回复一个秘密
						ret = BirdSing.tellSecret(fromUsername, toUsername, time);
					}

				} else if (msgType.equals("text")) {

					ret = textReply(root, fromUsername, toUsername, time);

				} else if (msgType.equals("event")) {
					// 新用户订阅，提醒用户使用方法
					ret = BirdSing.singAsong(fromUsername, toUsername, time);
				} else if (msgType.equals("video")) {
					// 视频，微信不支持了！
					String mediaId = root.elementText("MediaId").trim();
					String thumbMediaId = root.elementText("ThumbMediaId").trim();
					log.info("video, mediaId:" + mediaId + ",thumbMediaId:" + thumbMediaId);
					ret = BirdSing.singAsongVideo("s-K03TfkqeXJ2I3OICEAFQn5ZmqT-qdrzBoDdPa271TZXlS9DjGLlkT_vRDx4J9z", fromUsername, toUsername, time, "video");
				} else if (msgType.equals("voice")) {
					// 语音
					String mediaId = root.elementText("MediaId").trim();
					String format = root.elementText("Format").trim();
					log.info("voice, mediaId:" + mediaId + ",format:" + format);

					DBDog.saveSecret(fromUsername, mediaId, "voice");
					String reMediaId = DBDog.getVoice(fromUsername);
					ret = BirdSing.singAsongVoice(reMediaId, fromUsername, toUsername, time, "voice");

				} else {
					ret = BirdSing.singAsong("你说的神马，我听不懂~", fromUsername, toUsername, time);
				}
			}
		}
		log.info("reply： " + ret);
		try {
			ret = new String(ret.getBytes("UTF-8"), "iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		log.info("reply iso-8859-1 ： " + ret);
		return ret;
	}

	private static String textReply(Element root, String fromUsername, String toUsername, String time) {
		String ret;
		String content = root.elementText("Content").trim();

		if (content.length() < 20) {
			ret = BirdSing.singAsong("您的秘密太短，请写把秘密写详细点吧，不少于20个字~", fromUsername, toUsername, time);
		} else {
			DBDog.saveWZSecret(fromUsername, content.substring(2));
			ret = BirdSing.singAsong("作为交换，告诉你这个秘密：" + BirdSing.getRandomWZMM(fromUsername), fromUsername, toUsername, time);
		}
		/**
		 * if (content.startsWith("WD")) { // 查看我的秘密 ret =
		 * BirdSing.showSecret(fromUsername, toUsername, time); } else if
		 * (content.startsWith("CK") || content.startsWith("ck") ||
		 * content.startsWith("Ck") || content.startsWith("cK")) { // 查看我的留言 ret
		 * = BirdSing.showLiuyan(fromUsername, toUsername, time);
		 * 
		 * } else if (content.substring(0, 1).equalsIgnoreCase("LY")) { // 留言 if
		 * (content.substring(2).equals("")) { ret =
		 * BirdSing.singAsong("请加上你的留言，留言请以'LY'开头，谢谢", fromUsername, toUsername,
		 * time); } else { DBDog.saveLiuyan(fromUsername, content.substring(2));
		 * ret = BirdSing.singAsong("留言成功,你可以输入CK来查看所有人的留言.", fromUsername,
		 * toUsername, time); } } else if (content.substring(0,
		 * 1).equalsIgnoreCase("MM")) { // 文字秘密 if
		 * (content.substring(2).equals("")) { ret =
		 * BirdSing.singAsong("请加上你的秘密，秘密以'MM'开头，谢谢", fromUsername, toUsername,
		 * time); } else { DBDog.saveWZSecret(fromUsername,
		 * content.substring(2)); ret = BirdSing.singAsong("作为交换，告诉你这个秘密：" +
		 * BirdSing.getRandomWZMM(fromUsername), fromUsername, toUsername,
		 * time); } } else if (content.substring(0, 1).equalsIgnoreCase("PL")) {
		 * // 评论 DBDog.saveLiuyan(fromUsername, content.substring(2)); ret =
		 * BirdSing.singAsong("pl成功,你可以输入CP来查看自己的评论.", fromUsername, toUsername,
		 * time);
		 * 
		 * } else { // 非命令，提醒用户使用方法 ret =
		 * BirdSing.singAsong("秘密请以'MM'开头，来交换别人的秘密；如要发留言板,请输入以'LY'开头的内容。谢谢",
		 * fromUsername, toUsername, time); }
		 **/
		return ret;
	}
}

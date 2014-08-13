package com.miracle.service;

import org.apache.log4j.Logger;

/**
 * birdsing,小鸟唱歌一样，回复
 * 
 * @author hyliu
 * 
 */
public class BirdSing {
	    /**
     * 告诉你一个秘密
     * 
     * @param fromUsername
     * @param toUsername
     * @return
     */

	private static Logger log = Logger.getLogger(BirdSing.class);

	public static String DEFAULTMM = "http://myweixin.cloudfoundry.com/tmp/dt/games-animated-gif-002.gif";
    public static String DEFAULTWZ = "我有一个小秘密，小秘密~";

    // 回一个秘密
	public static String tellSecret(String fromUsername, String toUsername, String time) {
		String ret = "";
		String textPicTemplate = " <xml> <ToUserName><![CDATA[%1$s]]></ToUserName> <FromUserName><![CDATA[%2$s]]></FromUserName> <CreateTime>%3$s</CreateTime> <MsgType><![CDATA[news]]></MsgType> "
				+ "<ArticleCount>1</ArticleCount> <Articles> <item> <Title><![CDATA[%4$s]]></Title>  <Description><![CDATA[%5$s]]></Description>"
				+ " <PicUrl><![CDATA[%6$s]]></PicUrl> <Url><![CDATA[%7$s]]></Url> </item>  </Articles> <FuncFlag>1</FuncFlag> </xml> ";

		String picUrl = getRandomMM(fromUsername);

        ret = String.format(textPicTemplate, fromUsername, toUsername, time, "好吧，告诉你一个秘密", "这个秘密告诉你", picUrl, picUrl);
		return ret;
	}

    // 随即获取一个图片秘密
	public static String getRandomMM(String fromUsername) {

		String ret = DBDog.getSecretPic(fromUsername);
		if (null == ret || ret.equals("")) {
			ret = DEFAULTWZ;
		}
		return ret;
	}

    // 随即取一个文字秘密
	public static String getRandomWZMM(String fromUsername) {
		String ret = DBDog.getWZSecret(fromUsername);
		if (null == ret || ret.equals("")) {
			ret = DEFAULTMM;
		}
		return ret;
	}

	/**
	 * text
	 * 
	 * @param content
	 * @param fromUsername
	 * @param toUsername
	 * @param time
	 * @return
	 */
	public static String singAsong(String content, String fromUsername, String toUsername, String time) {
		if (content == null || content.equals("")) {
			return singAsong(fromUsername, toUsername, time);
		} else {
			String ret = "";
			String textTemplate = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$s</CreateTime><MsgType><![CDATA[%4$s]]></MsgType><Content><![CDATA[%5$s]]></Content><FuncFlag>0</FuncFlag></xml>";
			String msgType = "text";
			String contentStr = content;
			ret = String.format(textTemplate, fromUsername, toUsername, time, msgType, contentStr);
			return ret;
		}
	}

	/**
	 * voice
	 * 
	 * @param mediaId
	 * @param fromUsername
	 * @param toUsername
	 * @param time
	 * @param msgType
	 * @return
	 */
	public static String singAsongVoice(String mediaId, String fromUsername, String toUsername, String time, String msgType) {
		if (mediaId == null || mediaId.equals("")) {
			return singAsong(fromUsername, toUsername, time);
		} else {
			String ret = "";
			String voice = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$s</CreateTime><MsgType><![CDATA[%4$s]]></MsgType><Voice><MediaId><![CDATA[%5$s]]></MediaId></Voice></xml>";
			String contentStr = mediaId;
			ret = String.format(voice, fromUsername, toUsername, time, msgType, contentStr);
			return ret;
		}
	}

	/**
	 * video
	 * 
	 * @param mediaId
	 * @param fromUsername
	 * @param toUsername
	 * @param time
	 * @param msgType
	 * @return
	 */
	public static String singAsongVideo(String mediaId, String fromUsername, String toUsername, String time, String msgType) {
		if (mediaId == null || mediaId.equals("")) {
			return singAsong(fromUsername, toUsername, time);
		} else {
			String ret = "";
			String voice = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$s</CreateTime><MsgType><![CDATA[%4$s]]></MsgType><Video><MediaId><![CDATA[%5$s]]></MediaId><Title><![CDATA[%6$s]]></Title><Description><![CDATA[%7$s]]></Description></Video> </xml>";
			String contentStr = mediaId;
			String title = "my video";
			String desc = "video for you";
			ret = String.format(voice, fromUsername, toUsername, time, msgType, contentStr, title, desc);
			return ret;
		}
	}

    // 回复内容
	public static String singAsong(String fromUsername, String toUsername, String time) {

		String ret = "";
		String textTemplate = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$s</CreateTime><MsgType><![CDATA[%4$s]]></MsgType><Content><![CDATA[%5$s]]></Content><FuncFlag>0</FuncFlag></xml>";
		String msgType = "text";
        String contentStr = "欢迎来到交换秘密，你发送一个秘密给我，我也告诉你一个秘密，fair enough ~\n 特别提醒:秘密只能是图片、声音或者文字~\n";
		ret = String.format(textTemplate, fromUsername, toUsername, time, msgType, contentStr);
		return ret;
	}


	public static String showLiuyan(String fromUsername, String toUsername, String time) {
		String ret = "";
		String textPicTemplate = " <xml> <ToUserName><![CDATA[%1$s]]></ToUserName> <FromUserName><![CDATA[%2$s]]></FromUserName> <CreateTime>%3$s</CreateTime> <MsgType><![CDATA[news]]></MsgType> "
				+ "<ArticleCount>1</ArticleCount> <Articles> <item> <Title><![CDATA[%4$s]]></Title>  <Description><![CDATA[%5$s]]></Description>"
				+ " <PicUrl><![CDATA[%6$s]]></PicUrl> <Url><![CDATA[%7$s]]></Url> </item>  </Articles> <FuncFlag>1</FuncFlag> </xml> ";

		String picUrl = "http://www.xmark.info/tradesecret/image/gethead.jpg";

        ret =
                String.format(textPicTemplate, fromUsername, toUsername, time, "留言板", "留言请以字母LY开头,查看留言请回复CK。", picUrl,
				"http://www.xmark.info/tradesecret/liuyan.jsp");
		return ret;
	}

}

package com.miracle.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Service {

	public static String readPostXML(HttpServletRequest request) throws IOException {
		BufferedReader sis = request.getReader();
		char[] buf = new char[1024];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		while ((len = sis.read(buf)) != -1) {
			sb.append(buf, 0, len);
		}
		return sb.toString();
	}

	public static void responseMsg(HttpServletRequest request, PrintWriter out) {
		String postStr = null;
		try {
			postStr = Service.readPostXML(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("接收到： " + postStr);
		if (null != postStr && !postStr.isEmpty()) {
			Document document = null;
			try {
				document = DocumentHelper.parseText(postStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null == document) {
				System.out.println(" document is null !!!");
				return;
			}
			Element root = document.getRootElement();
			String fromUsername = root.elementText("FromUserName");

			String toUsername = root.elementText("ToUserName");
			String keyword = root.elementTextTrim("Content");
			String time = new Date().getTime() + "";
            // 回复信息
			try {
				out.print(reply(fromUsername, toUsername, keyword, time));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println(" warn nothing received !!!");
		}
	}

	private static String reply(String fromUsername, String toUsername, String keyword, String time) throws UnsupportedEncodingException {
		String res = "";

		if (null != keyword && !keyword.equals("")) {
			String transfer = analy(keyword);
			if (!transfer.equals("")) {
				String textPicTemplate = " <xml> <ToUserName><![CDATA[%1$s]]></ToUserName> <FromUserName><![CDATA[%2$s]]></FromUserName> <CreateTime>%3$s</CreateTime> <MsgType><![CDATA[news]]></MsgType> "
						+ "<ArticleCount>1</ArticleCount> <Articles> <item> <Title><![CDATA[%4$s]]></Title>  <Description><![CDATA[%5$s]]></Description>"
						+ " <PicUrl><![CDATA[%6$s]]></PicUrl> <Url><![CDATA[%7$s]]></Url> </item>  </Articles> <FuncFlag>1</FuncFlag> </xml> ";

                String picUrl = "http://www.xmark.info/tradesecret/image/" + transfer + "/";
                res = String.format(textPicTemplate, fromUsername, toUsername, time, "h哈哈", "嘿嘿，" + keyword, picUrl, picUrl);

			} else {
				String textTemplate = "<xml>" + "<ToUserName><![CDATA[%1$s]]></ToUserName>" + "<FromUserName><![CDATA[%2$s]]></FromUserName>"
						+ "<CreateTime>%3$s</CreateTime>" + "<MsgType><![CDATA[%4$s]]></MsgType>" + "<Content><![CDATA[%5$s]]></Content>"
						+ "<FuncFlag>0</FuncFlag>" + "</xml>";
				String msgType = "text";
                String contentStr = "想看什么样的美眉，我帮你找找看，性感？纯洁？还是美腿？或者其他\n";
				res = String.format(textTemplate, fromUsername, toUsername, time, msgType, contentStr);
			}
		} else {
            System.out.println("用户没有输入 warning~");
		}
		res = new String(res.getBytes("UTF-8"), "iso-8859-1");
		System.out.println("resultStr: " + res);
		return res;
	}

	private static String analy(String keyword) {

		String dirName = "";

		HashMap<String, String> mmlist = new HashMap<String, String>();
        mmlist.put("纯洁", "chunjie");
        mmlist.put("性感", "xinggan");
        mmlist.put("美腿", "meitui");
        mmlist.put("黑白", "heibai");
        mmlist.put("其他", "other");
        mmlist.put("都要", "other");
        mmlist.put("美女", "other");
        mmlist.put("创意", "chuangyi");
        mmlist.put("动态", "dt");
		for (Map.Entry<String, String> m : mmlist.entrySet()) {
			if (m.getKey().equals(keyword) || keyword.contains(m.getKey())) {
				dirName = m.getValue();
			}
		}
		return dirName;
	}
}

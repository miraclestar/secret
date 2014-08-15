package com.miracle.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miracle.service.SecretService;

public class Validate extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(Validate.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = request.getParameter("echostr");
        response.getWriter().print(s);
        log.debug("validate weixin token ,get echostr: " + s);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        log.info(request.getCharacterEncoding() + " : " + response.getCharacterEncoding());
        // response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String ret = SecretService.reply(request);
        out.print(ret);
        log.info(ret);
        out.flush();
        out.close();
    }

}

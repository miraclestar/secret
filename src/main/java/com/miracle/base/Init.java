package com.miracle.base;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

public class Init extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private static final String configPath = System.getProperty("user.home") + "/conf/";

    @Override
    public void init() throws ServletException {

        System.out.println("**************  sync generator initial start  *****************");

        System.out.println("config path:" + configPath);

        // log4j
        PropertyConfigurator.configure(configPath + "log4j.properties");

        System.out.println("log4j config done;");

        System.out.println("***************  sync generator initial end  ****************");
    }

}

package com.asiainfo.controller;

import com.asiainfo.until.SwitchDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AsyncAutoSwitchController extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(AsyncAutoSwitchController.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        SwitchDateUtils.IS_AUTOSWITCH = true;
        SwitchDateUtils.SWITCH_DATE = "";
        PrintWriter writer=response.getWriter();
        String ip = request.getLocalAddr();
        logger.info("服务器ip:" + ip);
        String msg = "同步自动切换设置成功";
        logger.info(msg);
        writer.append("{\"flag\":true,\"msg\":\""+msg+"\"}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }
}

package com.asiainfo.controller;

import com.asiainfo.until.HbaseUtils;
import com.asiainfo.until.SwitchDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AsyncSetDateController extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(AsyncSetDateController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String date = request.getParameter("date");
        SwitchDateUtils.SWITCH_DATE = date;
        SwitchDateUtils.IS_AUTOSWITCH = false;
        logger.info("同步切换日期为："+date);
        String ip = request.getLocalAddr();
        PrintWriter writer=response.getWriter();
        logger.info("服务器ip:" + ip);
        String msg = "同步切换日期成功";
        logger.info(msg);
        writer.append("{\"flag\":true,\"msg\":\""+msg+"\"}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }
}

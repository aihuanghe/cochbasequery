package com.asiainfo.controller;

import com.asiainfo.until.SwitchDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="setDate", urlPatterns={"/setDate"},loadOnStartup=1)
public class SetDateController  extends HttpServlet{

    private Logger logger = LoggerFactory.getLogger(SetDateController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String date = request.getParameter("date");
        SwitchDateUtils.SWITCH_DATE = date;
        SwitchDateUtils.IS_AUTOSWITCH = false;
        logger.info("手动切换日期为："+date);
        PrintWriter writer=response.getWriter();
        writer.append(SwitchDateUtils.SWITCH_DATE);
    }
}

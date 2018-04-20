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

public class AutoSwitchController extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(AutoSwitchController.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SwitchDateUtils.IS_AUTOSWITCH = true;
        SwitchDateUtils.SWITCH_DATE = "";
        PrintWriter writer=response.getWriter();
        logger.info("自动切换设置成功");
        writer.append("true");
    }
}

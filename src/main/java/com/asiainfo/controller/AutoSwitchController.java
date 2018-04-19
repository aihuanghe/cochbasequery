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

@WebServlet(name = "autoSwitch",urlPatterns = {"/autoSwitch"},loadOnStartup = 1)
public class AutoSwitchController extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(AutoSwitchController.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SwitchDateUtils.IS_AUTOSWITCH = true;
        PrintWriter writer=response.getWriter();
        logger.info("自动切换设置成功");
        writer.append("true");
    }
}

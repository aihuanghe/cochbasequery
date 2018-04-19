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

@WebServlet(name = "getDate",urlPatterns = {"/getDate"},loadOnStartup = 1)
public class GetDateController extends HttpServlet{

    private Logger logger = LoggerFactory.getLogger(GetDateController.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer=response.getWriter();
        logger.info("{\"autoSwitch\":\""+SwitchDateUtils.IS_AUTOSWITCH + "\",\"date\":\"" + SwitchDateUtils.SWITCH_DATE + "\"}");
        writer.append("{\"autoSwitch\":\""+SwitchDateUtils.IS_AUTOSWITCH + "\",\"date\":\"" + SwitchDateUtils.SWITCH_DATE + "\"}");
    }
}

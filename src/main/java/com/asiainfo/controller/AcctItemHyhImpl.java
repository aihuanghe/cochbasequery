package com.asiainfo.controller;

import com.asiainfo.busi.AcctItemHyhBusi;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class AcctItemHyhImpl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final static Log log = LogFactory.getLog(AcctItemHyhImpl.class.getSimpleName());


    private String getRequestBody(HttpServletRequest request) {
        String jsonStr = null;
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(request.getInputStream()));
            while ((jsonStr = reader.readLine()) != null) {
                result.append(jsonStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result.toString();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");


        PrintWriter out = response.getWriter();
        String jsonparam=getRequestBody(request);
        JSONObject jsparam=JSONObject.fromObject(jsonparam);
        log.info("==========请求报文："+jsonparam);

        AcctItemHyhBusi busi=new AcctItemHyhBusi();
        JSONObject jo= busi.getQueryRes(jsparam);

        try {
            out.print(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

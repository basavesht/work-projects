package com.tcs.ebw.serverside.jaas.struts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;
/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class HeaderAdditionServlet extends HttpServlet{

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.addHeader("username",request.getParameter("JDOE"));
        request.setAttribute("username",request.getParameter("JDOE"));
        request.getRequestDispatcher("/SubmitLogon.do?username=JDOE&groupid=SG01&eventid="+request.getParameter("eventid")).	forward(request,response);
        //response.sendRedirect("/ebankworkscgRJ/SubmitLogon.do?username=JDOE&groupid=SG01&eventid="+request.getParameter("eventid"));
        
        super.doGet(request,response);
    }
    /**
     * 
     */
    public HeaderAdditionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
    }
}

package com.demiglace.trainings.servlets.sm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SourceServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {

			for (int i = 0; i < cookies.length; i++) {
				System.out.println(cookies[i].getName());
				System.out.println(cookies[i].getValue());
			}
		}

		// adding cookies
		response.addCookie(new Cookie("securityToken", "12345"));

		// retrieve the user name
		String userName = request.getParameter("userName");

		// retrieve the session
		HttpSession session = request.getSession();
		session.setAttribute("user", userName);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String url = "targetServlet?sessionId=123";
		out.print("<a href='" + url + "'>Click here to get the User Name</a>");
	}
}

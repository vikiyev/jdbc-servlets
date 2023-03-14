package com.demiglace.trainings.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.GenericServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/additionServlet")
public class AdditionServlet extends GenericServlet {
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		// take the parameters from the request
		if (req.getParameter("num1")!=null && req.getParameter("num2")!=null) {			
			Long num1 = Long.parseLong(req.getParameter("num1"));
			Long num2 = Long.parseLong(req.getParameter("num2"));
		PrintWriter out = res.getWriter();
		out.println("The sum is " + (num1 + num2));
		}
	}
}

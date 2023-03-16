package com.demiglace.trainings.interservlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// retrieve parameters
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");

		try {
			// connect to database
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/mydb", "root", "1234");

			// create and execute the statement
			Statement statement = con.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select * from user where email='" + userName + "' and password=+'" + password + "'");

			RequestDispatcher requestDispatcher = request.getRequestDispatcher("homeServlet");
			// if there is a valid record found, forward the request to the homeServlet
			if (resultSet.next()) {
				request.setAttribute("message", "Login Success");
				requestDispatcher.forward(request, response);
			} else {
				requestDispatcher = request.getRequestDispatcher("login.html");
				requestDispatcher.include(request, response);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

package com.demiglace.trainings.jsp.customtags;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.Tag;
import jakarta.servlet.jsp.tagext.TagSupport;

public class ResultHandler extends TagSupport {
	Connection con;
	PreparedStatement stmt;
	
	public ResultHandler() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "1234");
			stmt = con.prepareStatement("select * from user where email=?");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public int doStartTag() throws JspException {
		// get the user id out of the request
		ServletRequest request = pageContext.getRequest();
		String email = request.getParameter("email");
		
		try {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			JspWriter out = pageContext.getOut();
			
			
			if (rs.next()) {
				out.print("User Details are: <br/>");
				out.print("First Name: <br/>");
				out.print(rs.getString(1)); 
				out.print("Last Name: <br/>");
				out.print(rs.getString(2)); 
			} else {
				out.print("Invalid email entered");
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		
		
		return Tag.SKIP_BODY;
	}
	
	@Override
	public void release() {
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

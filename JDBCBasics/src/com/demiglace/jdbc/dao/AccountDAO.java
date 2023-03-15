package com.demiglace.jdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {
	public static void main(String[] args) {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "1234");
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("select * from account");) {

			System.out.println(connection);

			int result = statement.executeUpdate("insert into account values(1, 'doge', 'doge', 1000)");
			System.out.println(result + " rows got inserted");

			int result2 = statement.executeUpdate("update account set bal=2000 where accno=1");
			System.out.println(result2 + " rows got updated");

			int result3 = statement.executeUpdate("delete from account where accno=1");
			System.out.println(result3 + " rows got deleted");

			// read from database
			while (rs.next()) {
				// print the first name last name, balance using the column index
				System.out.println(rs.getString(2));
				System.out.println(rs.getString(3));
				System.out.println(rs.getInt(4));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

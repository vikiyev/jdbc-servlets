# JDBC Servlets and JSP - Java Web Development Fundamentals

## Configuring Tomcat in Eclipse

In Eclipse, under the **Servers** tab, click on Create new server and select Tomcat under Apache.

## Web Application Basics

A web application is an application whose services can be accessed via the web through the HTTP protocol.

### Dynamic Web Application

Dynamic Web Applications can generate html on the fly. It runs on a web container, not on a web server.

### Life Cycle Methods and Phases

There are three Life Cycle Methods:

1. init() - runs only once
2. service() - where the business logic goes. Called n times
3. destroy()

The four Life Cycle Phases associated with these methods are

1. Instantiation
2. Initialization
3. Servicing
4. Destruction

### Servlets

Technology in the Java EE standard for developing dynamic web applications. Comes with an API for develoeprs and specification for providers. A **servlet** is a Java program that runs on a web container. It can capture requests from the web browser, process it, call a datbase, then take the response and prepare it a HTML response dynamically.

```html
<form action="additionServlet">
  <h2>Enter Numbers</h2>
  <br />
  Number1: <input type="text" name="num1" /><br />
  Number2: <input type="text" name="num2" /><br />
  <input type="submit" name="Submit" />
</form>
```

```java
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
```

The servlet can also be registered in web.xml, instead of using annotations:

```xml
  <servlet>
  	<servlet-name>HelloServlet</servlet-name>
  	<servlet-class>com.demiglace.trainings.servlet.AdditionServlet</servlet-class>
  </servlet>
  <servlet-mapping>
  	<servlet-name>AdditionServlet</servlet-name>
  	<url-pattern>/additionServlet</url-pattern>
  </servlet-mapping>
```

Running the servlet on eclipse can be done using Run As -> Run On Server -> Tomcat. The application will be available in `localhost:8080/ServletBasics/hello`.

## JDBC

The JDBC architecture is made up of:

1. JDBC Client
2. JDBC API
3. JDBC Driver - interfaces between client and an underlying database
4. Driver Manager - helper class that finds a driver and establishes connection to the database

Dynamic Java applications perform CRUD operations through DML and DQL in the following steps:

1. Establish connection
2. Create the statemeent object
3. Submit the SQL query to DBMS
4. Close the statemenet
5. Close the connection

When creating a JDBC project, we need to conifigure the driver jar. We need to download mysql-connector-j and install it to the local machine using MySQL installer. After installation, the jar should be available under program files, which we can copy to the working directory under `lib`. We then add the jar to the classpath by configuring it in the Java Build Path in eclipse.

### Connecting to the Database

The database schema for the following examples is:

```sql
use mydb;
create table account(accno int,lastname varchar(25),firstname varchar(25),bal int);
```

```java
public class AccountDAO {
	public static void main(String[] args) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "1234");
			System.out.println(connection);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
```

### Creating a Statement

The Statement instance will be used to execute the sql statements.

```java
			Statement statement = connection.createStatement();

			int result = statement.executeUpdate("insert into account values(1, 'doge', 'doge', 1000)");
			System.out.println(result + " rows got inserted");

			int result2 = statement.executeUpdate("update account set bal=2000 where accno=1");
			System.out.println(result2 + " rows got updated");

			int result3 = statement.executeUpdate("delete from account where accno=1");
			System.out.println(result3 + " rows got deleted");
```

### Reading Data

Using a Select statement, we can read data from the database. We can follow the following steps:

1. Establish the Connection
2. Create Statement object
3. Submit the select statement
4. Check if records exists, if none, close the ResultSet, Statement, Connection
5. If record exists, process the records, then check for more records
6. Close the ResultSet, Statement, Connection

A **ResultSet** interface in the JDBC api is used to handle the data that comes back from a select query. It is an object-oriented representation of table records. It has three areas: Zero Record Area, Record Area, No Record Area wherein a Cursor points at. The cursor can be moved using the **next()** method.

```java
			// read from database
			ResultSet rs = statement.executeQuery("select * from account");
			while (rs.next()) {
				// print the first name last name, balance using the column index
				System.out.println(rs.getString(2));
				System.out.println(rs.getString(3));
				System.out.println(rs.getInt(4));
			}
```

### Cleaning up JDBC Resources

Starting java 7, we can clean up the resources through the try resource block, wherein we do not need to explicitly invoke the **close()** method. The JRE will automatically close the **close()** method through the AutoCloseable interface.

```java
try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "1234");
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("select * from account");)
```

## Dynamic Web App

For a dynamic web app, we can add the connector jar into `/webapp/WEB-INF/lib`. This jar will be automatically added to the build path of the application. Once deployed to a web server, it will also be picked up by the server.

The database schema for the following example will be:

```sql
use mydb;
create table user (firstName varchar(20),lastName varchar(20), email varchar(20),password varchar(20));
```

addUser.html:

```html
<form method="post" action="addServlet">
  <table>
    <tr>
      <td>First Name:</td>
      <td><input name="firstName" /></td>
    </tr>
    <tr>
      <td>Last Name :</td>
      <td><input name="lastName" /></td>
    </tr>
    <tr>
      <td>Email:</td>
      <td><input name="email" /></td>
    </tr>
    <tr>
      <td>Password:</td>
      <td><input name="password" type="password" /></td>
    </tr>
    <tr>
      <td />
      <td><input type="submit" value="Add" /></td>
    </tr>
  </table>
</form>
```

### Creating the Servlet

A Servlet can be created in Eclipse through the New->Servlet option. We implement the **init()** wherein we connect to the database. **doPost()** is called for every servlet request from the web browser.

```java
@WebServlet("/addServlet")
public class CreateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	public void init() {
		try {
			// load driver for tomcat
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "1234");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		try {
			Statement statement = connection.createStatement();
			int result = statement.executeUpdate("insert into user values('"+firstName+"','"+lastName+"','"+email+"','"+password+"')");

			PrintWriter out = response.getWriter();

			// send the response if record was created in db
			if (result > 0) {
				out.print("<h1>User Created</h1>");
			} else {
				out.print("<h1>Error Creating User</h1>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
```

## Init Params

Init Params are name-value pairs that are supplied by the servlet container to a servlet during its initialization phase through the web.xml file.

```xml
	<servlet>
		<servlet-name>ReadUsersServlet</servlet-name>
		<servlet-class>com.demiglace.users.servlets.ReadUsersServlet</servlet-class>
		<init-param>
			<param-name>dbUrl</param-name>
			<param-value>jdbc:mysql://localhost:3306/mydb</param-value>
		</init-param>
		<init-param>
			<param-name>dbUser</param-name>
			<param-value>root</param-value>
		</init-param>
		<init-param>
			<param-name>dbPassword</param-name>
			<param-value>1234</param-value>
		</init-param>
	</servlet>
```

Init Params can also be configured using the **WebServlet** annotation. We can then overload the **init()** method with the ServletConfig.

```java
@WebServlet(urlPatterns = "/addServlet", initParams = {
		@WebInitParam(name = "dbUrl", value = "jdbc:mysql://localhost:3306/mydb"),
		@WebInitParam(name = "dbUser", value = "root"), @WebInitParam(name = "dbPassword", value = "1234") })
public class CreateUserServlet extends HttpServlet {

	public void init(ServletConfig config) {
		try {
			// load driver for tomcat
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(config.getInitParameter("dbUrl"),
					config.getInitParameter("dbUser"), config.getInitParameter("dbPassword"));
		}
```

## Servlet Context

The servlet container creates and injects the ServletContext as soon as the application is deployed. There is only one ServletContext for the entire application and is destroyed when the application is undeployed. The data placed in the ServletContext is accessible throughout the application.

Common uses for ServletContext are:

1. Sharing and manipulating data
2. Create a RequestDispatcher object for inter-servlet communication
3. Dealing with context params
4. store information in to the server log files using log()

### Context Parameters

Context Parameters are key-value pairs supplied in web.xml. The difference from Init Parameters is that Context Parameters can be accessed across the application, and not just to a specific servlet. These parameters are read by the container when the application is deployed and inject them to the ServletContext.

```xml
	<context-param>
		<param-name>dbUrl</param-name>
		<param-value>jdbc:mysql://localhost:3306/mydb</param-value>
	</context-param>
	<context-param>
		<param-name>dbUser</param-name>
		<param-value>root</param-value>
	</context-param>
	<context-param>
		<param-name>dbPassword</param-name>
		<param-value>1234</param-value>
	</context-param>
```

The servlet context can be retrieved using **getServletContext()**.

```java
	public void init(ServletConfig config) {
		try {
			// get the servlet context
			ServletContext context = config.getServletContext();

			// load driver for tomcat
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(context.getInitParameter("dbUrl"),
					context.getInitParameter("dbUser"), context.getInitParameter("dbPassword"));
    }
```

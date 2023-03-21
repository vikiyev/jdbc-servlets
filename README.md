# JDBC Servlets and JSP - Java Web Development Fundamentals

- [JDBC Servlets and JSP - Java Web Development Fundamentals](#jdbc-servlets-and-jsp---java-web-development-fundamentals)
  - [Configuring Tomcat in Eclipse](#configuring-tomcat-in-eclipse)
  - [Web Application Basics](#web-application-basics)
    - [Dynamic Web Application](#dynamic-web-application)
    - [Life Cycle Methods and Phases](#life-cycle-methods-and-phases)
    - [Servlets](#servlets)
  - [JDBC](#jdbc)
    - [Connecting to the Database](#connecting-to-the-database)
    - [Creating a Statement](#creating-a-statement)
    - [Reading Data](#reading-data)
    - [Cleaning up JDBC Resources](#cleaning-up-jdbc-resources)
  - [Dynamic Web App](#dynamic-web-app)
    - [Creating the Servlet](#creating-the-servlet)
  - [Init Params](#init-params)
  - [Servlet Context](#servlet-context)
    - [Context Parameters](#context-parameters)
  - [Prepared Statement](#prepared-statement)
  - [Inter-Servlet Communication](#inter-servlet-communication)
  - [Pre-Initialization](#pre-initialization)
  - [Servlet Listeners](#servlet-listeners)
  - [Servlet Filters](#servlet-filters)
  - [Session Management](#session-management)
    - [Session Tracking](#session-tracking)
    - [Using Cookies](#using-cookies)
  - [JSP](#jsp)
    - [Implicit Objects](#implicit-objects)
    - [Scripting Elements](#scripting-elements)
    - [JSP Actions](#jsp-actions)
  - [MVC Pattern](#mvc-pattern)
    - [Model](#model)
    - [View](#view)
    - [Controller](#controller)
  - [Custom Tags](#custom-tags)
    - [Tag Handler Class](#tag-handler-class)
    - [Tag Lib Descriptor](#tag-lib-descriptor)

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

## Prepared Statement

PreparedStatement is a child interface of Statement and is a pre-compiled version of a SQL statement, represented in an object-oriented fashion. The benefit is that the compilation of the sql happens only once, and is typically more performant than regular Statements. **?** are used to mark placeholders in the Prepared Statement.

For the following examples, the SQL schema is:

```sql
use mydb;
create table product (id int,name varchar(20),description varchar(20),price int);
```

We can prepare a statement using the **Connection.prepareStatement()** method. Parameters can be bound using the corresponding setter methods.

```java
public class ProductServlet extends HttpServlet {
	Connection conn;
	PreparedStatement stmt;

	public void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/mydb", "root", "1234");

			stmt = conn.prepareStatement("insert into product values(?,?,?,?)");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// get parameters
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String desc = request.getParameter("desc");
		int price = Integer.parseInt(request.getParameter("price"));

		// bind parameters
		try {
			stmt.setInt(1, id);
			stmt.setString(2, name);
			stmt.setString(3, desc);
			stmt.setInt(4, price);

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
```

## Inter-Servlet Communication

Servlets and JSP's can communicate with each other through Request Dispatching. The servlet api provides the **RequestDispatcher** interface. The example login application uses **forward** upon successful login, and **include** upon a failed login attempt.

```html
<form action="loginServlet" method="post">
  Email : <input type="text" name="userName" /><br />
  Password: <input type="password" name="password" /><br />
  <input type="submit" />
</form>
```

```java
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
```

We can then retrieve the _message_ attribute that was set in the LoginServlet

```java
@WebServlet("/homeServlet")
public class HomeServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.print(request.getAttribute("message"));
	}

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.print(request.getAttribute("message"));
	}
}
```

## Pre-Initialization

There are two ways a Servlet can be initialized:

1. Lazy Initialization - Servlet is initialized only when the first web client request comes in. By default, all servlets are lazily initialized
2. Pre-Initialization - The container initializes the Servlet even before a client request comes in

Pre-Initialization can be enabled through the **<load-on-startup>** tags in web.xml, or by annotations. Some examples of pre-initialized services are CXF or Jersey and Spring MVC.

```java
@WebServlet(urlPatterns = "/preInitServlet", loadOnStartup = 0)
public class PreInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init() {
		System.out.println("Inside init()");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().write("From the pre init servlet");;
	}
}
```

```xml
  <servlet>
    <servlet-name>PreInitServlet</servlet-name>
    <servlet-class>com.demiglace.trainings.servlets.preinit.PreInitServlet</servlet-class>
    <load-on-startup>0</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>PreInitServlet</servlet-name>
    <url-pattern>/preInitServlet</url-pattern>
  </servlet-mapping>
```

## Servlet Listeners

Servlet Listeners enable our applications to react to certain events in the web container. These events could:

1. Request
2. Session
3. Context
4. Async

To create listeners, we implement the **HttpSessionListener** and override its methods. For request-level events, we use **HttpRequestListener**. Eclipse also supports creating listener through the new->Listener wizard. The container will call the following listener whenever it creates a request and response object.

```java
@WebListener
public class RequestListener implements ServletRequestListener {
    public void requestInitialized(ServletRequestEvent event)  {
    	System.out.println("Request Created");
    }

    public void requestDestroyed(ServletRequestEvent event)  {
    	System.out.println("Request Destroyed");
    }
}
```

## Servlet Filters

A filter can intercept the request and response cycles of a servlet. An example of a filter is an injection attack filter. We can apply multiple filters through Filter Chaining. Filters have a similar lifecycle as a servlet: init(), doFilter(), destroy().

```java
@WebServlet("/filterDemoServlet")
public class FilterDemoServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.print("From the Servlet");
	}
}
```

Creating a filter can be done using the Eclipse new->Filter wizard. We write the logic that we want to happen before the FilterChain.doFilter is called.

```java
@WebFilter("/filterDemoServlet")
public class DemoFilters extends HttpFilter implements Filter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		out.print("Pre Servlet");
		chain.doFilter(request, response);
		out.print("Post Servlet");
	}
}

```

```
Pre Servlet
From the Servlet
Post Servlet
```

## Session Management

HTTP is a stateless protocol which means that the server does not maintain a continuous connection once the server handles the request and sends back the response. The socket connection is destroyed everytime the server sends a response back. When the next request comes from the client, it has to establish a new connection. The advantages of being stateless are performance and scalability.

Handling session can be done in the following steps:

1. Create the Session
2. Maintain the data using the four attribute methods on HttpSession
3. End the Session

```html
<h1>Enter User Name:</h1>
<form method="post" action="sourceServlet">
  User Name: <input name="userName" />
  <input type="submit" value="send" name="submitButton" />
</form>
```

```java
public class SourceServlet extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// retrieve the user name
		String userName = request.getParameter("userName");

		// retrieve the session
		HttpSession session = request.getSession();
		session.setAttribute("user", userName);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.print("<a href='targetServlet'>Click here to get the User Name</a>");
	}
}
```

When we invoke **getSession()**, the web container will check to see if the incoming request has a sessionId. If none, the container will create a unique session for that client. The web container will then automatically include the session id on the response back to the client. The web client has to send that id back, which will be taken by the container to check if there is an associated session for that id.

### Session Tracking

Session Tracking is used to maintain the state despite the statelessness of HTTP.

```java
public class SourceServlet extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// retrieve the user name
		String userName = request.getParameter("userName");

		// retrieve the session
		HttpSession session = request.getSession();
		session.setAttribute("user", userName);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.print("<a href='targetServlet'>Click here to get the User Name</a>");
	}
}

public class TargetServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// retrieve the session
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user");

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.print("<h1>User Name is: " + userName + "</h1>");
	}
}
```

```xml
	<servlet>
		<servlet-name>sourceServlet</servlet-name>
		<servlet-class>com.demiglace.trainings.servlets.sm.SourceServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>sourceServlet</servlet-name>
		<url-pattern>/sourceServlet</url-pattern>
	</servlet-mapping>

	<servlet>
		<servlet-name>targetServlet</servlet-name>
		<servlet-class>com.demiglace.trainings.servlets.sm.TargetServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>targetServlet</servlet-name>
		<url-pattern>/targetServlet</url-pattern>
	</servlet-mapping>
```

Ending a Session can be done using an explicit logout wherein we call the **HttpSession.invalidate()** which will tell the container to destroy the session associated in the memory. When a user is inactive Session Expiry occurs wherein the container will also implicitly destroy the session. The expiry time can be overriden through **HttpSession.setMaxInactiveInterval()** or through web.xml <session-timeout>.

### Using Cookies

HTTP cookies are name-value pairs that can be used to exchange data between the web server and client as part of the HTTP headers. Cookies are usually used to maintain sessions between the container and client. The cookie used for session maangement in Java EE is **jsessionid**.

```java
public class SourceServlet extends HttpServlet{
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
```

## JSP

Java Server Pages is a Java EE technology which runs on a JSP container. It can take a request from a client, process the request, make db calls, and send back a response to the client. JSP's can do everything that a servlet can do and it also separates Java from HTML as opposed to servlets.

JSP's are grouped into elements:

1. Scripting Elements - allows us to embed java code into a JSP page
2. Directives - translation time instructions to the JSP container
3. Actions- runtime instructions to the JSP container

JSP Life Cycle Methods:

1. jspInit()
2. jspService()
3. jspDestroy()

JSP Life Cycle Phases:

1. Translation
2. Compilation
3. Instantiation
4. Initialization
5. Servicing
6. Destruction

### Implicit Objects

These are available to every JSP page.

| **Object Name** | **Type**                      |
| --------------- | ----------------------------- |
| config          | ServletConfig                 |
| request         | HttpServletRequest            |
| response        | HttpServletResponse           |
| session         | HttpSession                   |
| application     | ServletContext                |
| page            | java.lang.Object              |
| pageContext     | javax.servlet.jsp.PageContext |
| exception       | java.lang.Throwable           |
| out             | javax.servlet.jsp.JSPWriter   |

### Scripting Elements

Scripting Elements allow us to directly embed Java code into JSP pages. There are three types:

1. Declaration - <%! %> - used to declare fields or methods which becomes members of the generated servlet
2. Expression - <%= %> - holds Java expressions that evaluates to a value, then sends the result to the web client
3. Scriptlet - <% %> - can hold any amount of logic

```java
<%
  int num1 = Integer.parseInt(request.getParameter("number1"));
	int num2 = Integer.parseInt(request.getParameter("number2"));
%>

Sum of <%=num1 %> and <%=num2 %> is <%=num1+num2 %>
</body>
```

### JSP Actions

JSP Actions or tags are runtime instructions to the JSP containers. There are predefined tags and custom tags. Examples of predefined tags are:

1. include
2. forward
3. param
4. usebean
5. setProperty
6. getProperty

```html
<form action="displayDetails.jsp" method="post">
  Product Id: <input type="text" name="id" /><br />
  Product Name: <input type="text" name="name" /><br />
  Product Description: <input type="text" name="description" /><br />
  Product price: <input type="text" name="price" /><br />
  <input type="submit" />
</form>
```

Java Beans can be used to hold data. The bean will have properties with the same names as the names in the html

```java
public class Product {
	private int id;
	private String name;
	private String description;
	private float price;
}
```

Using the \* wildcard, the setProperty tag will automatically assign the properties to the bean using reflection.

```jsp
<body>
	<jsp:useBean id="product" class="com.demiglace.trainings.jsp.Product">
		<jsp:setProperty name="product" property="*" />
	</jsp:useBean>

	Product Details<br/>
	Id:<jsp:getProperty property="id" name="product"/>
	Name:<jsp:getProperty property="name" name="product"/>
	Description:<jsp:getProperty property="description" name="product"/>
	Price:<jsp:getProperty property="price" name="product"/>
</body>
```

## MVC Pattern

MVC is a design pattern that splits the web layer into three parts:

1. Model - represents the current state of the application and does the business logic. Represented by a java class
2. View - displays the current model to the end user. Represented by JSP
3. Controller - responsible for selecting the appropriate model and view. Represented by a Servlet

MVC has the following advantages:

1. Maintenance
2. Parallel Development

### Model

```java
public class AverageCalculator {
	public int calculate(int num1, int num2) {
		return (num1 + num2)/2;
	}
}
```

### View

```html
<body>
  <h3>Enter two number:</h3>
  <form action="averageController" method="post">
    Number 1 : <input name="number1" /><br />
    Number 2: <input name="number2" /><br />
    <input type="submit" />
  </form>
</body>
```

```jsp
<body>
<%
	int result = (Integer) request.getAttribute("result");
	out.print("<b>The average is: " + result + "</b>");
%>
</body>
```

### Controller

```java
@WebServlet("/averageController")
public class AverageController extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int num1 = Integer.parseInt(request.getParameter("number1"));
		int num2 = Integer.parseInt(request.getParameter("number2"));

		AverageCalculator model = new AverageCalculator();
		int result = model.calculate(num1, num2);

		// send result to the view
		request.setAttribute("result", result);
		RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
		dispatcher.forward(request, response);
	}
}
```

## Custom Tags

We can define custom tags when the inbuilt tags that come with the JSP specifications are not enough. There are two steps in creating custom tags:

1. Create the Tag Handler Class
2. Create the Tag Lib Descriptor (TLD)

### Tag Handler Class

The tag handler extends the TagSupport class. The doStartTag() method will be invoked by the container as soon as it reaches the starting element of our tag in the JSP page. To access the implicit objects such as the request, response, etc. the **pageContext** of the parent TagSupport class is used.

```java
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
			JspWriter out = null;

			if (rs.next()) {
				out = pageContext.getOut();
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
```

### Tag Lib Descriptor

The tag lib descriptor is an xml file with the `tld` extension located under `WEB-INF`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<taglib>
	<tlibversion>2.0</tlibversion>
	<jspversion>2.0</jspversion>
	<shortname>userinformation</shortname>
	<info>This library displays the user information.</info>
	<uri>http://demiglace.com</uri>

	<tag>
		<name>displayuser</name>
		<tagclass>com.demiglace.trainings.jsp.customtags.ResultHandler</tagclass>
		<info>Displays the User's information</info>
	</tag>
</taglib>
```

We can then use the custom tag in our JSP page using the **taglib** directive.

```jsp
<%@taglib prefix="demiglace" uri="http://demiglace.com" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Display User Info</title>
</head>
<body>
	<demiglace:displayuser/>
</body>
</html>
```

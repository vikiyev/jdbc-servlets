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

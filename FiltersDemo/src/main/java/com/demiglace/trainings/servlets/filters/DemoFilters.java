package com.demiglace.trainings.servlets.filters;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;

@WebFilter("/filterDemoServlet")
public class DemoFilters extends HttpFilter implements Filter {
       
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		out.print("Pre Servlet");
		chain.doFilter(request, response);
		out.print("Post Servlet");
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
}

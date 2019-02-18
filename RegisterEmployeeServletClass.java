package com.cognizant.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterEmployeeServletClass
 */
@WebServlet(
		name = "RegisterEmployeeServlet", 
		urlPatterns = { "/registerEmployee" }, 
		initParams = { 
				@WebInitParam(name = "ConfigPath", value = "C:\\CognizantConfig\\DBInfo.properties")
		})
public class RegisterEmployeeServletClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String path;
	Properties properties;
     String driver;
     String url;
     String username;
     String password;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterEmployeeServletClass() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		this.path=config.getInitParameter("ConfigPath");
		File file=new File(path);
		try(
				InputStream is=new FileInputStream(file);
				)
		{
			Properties properties=new Properties();
			properties.load(is);
			this.driver=properties.getProperty("driver");
			this.url=properties.getProperty("url");
			this.username=properties.getProperty("username");
			this.password=properties.getProperty("password");
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int empId=Integer.parseInt(request.getParameter("empId"));
		String empName=request.getParameter("empName");
		double empSalary=Double.parseDouble(request.getParameter("empSalary"));
		String empDesignation=request.getParameter("empDesignation");
		
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try(Connection connection=DriverManager.getConnection(url,username,password);
				PrintWriter out=response.getWriter();
				)
		{
			PreparedStatement statement=connection.prepareStatement("insert into EMPLOYEES values(?,?,?,?)");
			statement.setInt(1, empId);
			statement.setString(2, empName);
			statement.setDouble(3, empSalary);
			statement.setString(4, empDesignation);
			
			int rows=statement.executeUpdate();
			if(rows>0)
			{
				out.println("Registration successful!!!");
			}
			else
			{
				out.println("Registration failed!!!");
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		
}

}

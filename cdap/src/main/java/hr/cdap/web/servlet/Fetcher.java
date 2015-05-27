package hr.cdap.web.servlet;

import hr.cdap.web.object.ElementSessionDO;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Fetcher
 */
@WebServlet("/Fetcher")
public class Fetcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Fetcher() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		fetchValuesFromSession(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
	private void fetchValuesFromSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Map map=(Map) request.getSession().getAttribute("elementMap");
		String elementId=request.getParameter("elementId");
		String objectName=request.getParameter("objectName");
		objectName = objectName.substring(0, 1).toUpperCase() + objectName.substring(1);
		String newValue=request.getParameter("newValue");
		if (map.get(elementId) != null) {
			Object object=map.get(elementId);
			try {
				Method method=ElementSessionDO.class.getMethod("get"+objectName, null);
				String value=(String) method.invoke(object, null);
				PrintWriter out = response.getWriter();
				if (newValue != null && !newValue.isEmpty()) {
					method=ElementSessionDO.class.getMethod("set"+objectName, String.class);
					method.invoke(object, newValue);
					out.print(newValue);
					System.out.println("New fetcher value: " + newValue);
				}
				else {
					out.print(value);
					System.out.println("Fetcher old value: " + value);
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
}

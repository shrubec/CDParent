package hr.cdap.web.servlet;

import hr.cdap.web.object.ElementSessionDO;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Fetcher
 */
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

		String elementId=request.getParameter("elementId");
		String objectName=request.getParameter("objectName");
		String newValue=request.getParameter("newValue");
		
		objectName = objectName.substring(0, 1).toUpperCase() + objectName.substring(1);
		
		Map map=(Map) request.getSession().getAttribute("elementMap");
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
					
					System.out.println("Postavljena nova vrijednost u objekt "+objectName+": " + newValue);
					
				}
				else {
//					if (value == null || value.equals("null")) value="";
					out.print(value);
					System.out.println("Vracena vrijednost iz objekta "+objectName+": " + value);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

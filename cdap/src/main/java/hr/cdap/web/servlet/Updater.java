package hr.cdap.web.servlet;

import hr.cdap.web.object.ElementSessionDO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Updater")
public class Updater extends HttpServlet {

	private static final long serialVersionUID = 1986921291198030074L;

	public Updater() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		saveElementIntoSession(request);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
	@SuppressWarnings({"unchecked","rawtypes"})
	private void saveElementIntoSession(HttpServletRequest request) throws UnsupportedEncodingException {
		
		String id=request.getParameter("id");
		String x=request.getParameter("x");
		String y=request.getParameter("y");
		String elementWidth=request.getParameter("elementWidth");
		String elementHeight=request.getParameter("elementHeight");
		String elementEditor=request.getParameter("elementEditor");
		String elementName=request.getParameter("elementName");
		ElementSessionDO element=null;
		Map map=(Map) request.getSession().getAttribute("elementMap");
		if (map.get(id) == null) {
			element=new ElementSessionDO();
			element.setElementId(id);
			map.put(id, element);
		}
		else {
			element=(ElementSessionDO)map.get(id);
		}
		element.setElementX(x);
		element.setElementY(y);
		
		if (!elementName.equals("null")) element.setElementName(elementName);
		if (!elementWidth.equals("null")) element.setElementWidth(elementWidth);
		if (!elementHeight.equals("null")) element.setElementHeight(elementHeight);
		if (!elementEditor.equals("null")) element.setElementEditor(new String(elementEditor.getBytes("ISO-8859-1"),"UTF-8"));
		
		request.getSession().setAttribute("selectedElementId", id);
	}
}

package hr.cdap.web.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImageSelector
 */
@WebServlet("/ImageSelector")
public class ImageSelector extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageSelector() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		saveImageElementIntoSession(request);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private void saveImageElementIntoSession(HttpServletRequest request) throws UnsupportedEncodingException {
		String imageCardTypeName=request.getParameter("cardTypeName");
		String imageElementId=request.getParameter("elementId");
		request.getSession().setAttribute("imageCardTypeName", imageCardTypeName);
		request.getSession().setAttribute("imageElementId", imageElementId);
	}

}

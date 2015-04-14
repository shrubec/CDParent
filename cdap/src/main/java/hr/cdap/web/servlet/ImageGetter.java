package hr.cdap.web.servlet;

import hr.cdap.entity.CardElement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImageGetter
 */

@WebServlet("/ImageGetter")
public class ImageGetter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImageGetter() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		getImage(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private void getImage(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String elementId = request.getParameter("elementImageObjectId");
		String cardTypeName= (String) request.getSession().getAttribute("imageCardTypeName");
		String selectedFormId= (String) request.getSession().getAttribute("imageElementId");
		if (elementId != null && !elementId.isEmpty()) {
			List<CardElement> elementList=(List<CardElement> ) request.getSession().getAttribute(cardTypeName);
			if (elementList != null) {
				for (CardElement element:elementList) {
					if (element.getFormId().equals(selectedFormId)) {
						if (element.getCardData() != null && element.getCardData().getValueBlob() != null) {
							displayImage(response, element.getCardData().getValueBlob());
						}
					}
				}
			}
		}
	}
	
	private void displayImage(HttpServletResponse response,byte[] bytes)  throws IOException{
		ByteArrayOutputStream baos = null;
		ServletOutputStream out = response.getOutputStream();
		try {
			baos = new ByteArrayOutputStream();
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (baos != null)
				baos.close();
			if (out != null)
				out.close();
		}
	}

}

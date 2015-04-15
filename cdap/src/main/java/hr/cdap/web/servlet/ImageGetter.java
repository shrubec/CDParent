package hr.cdap.web.servlet;

import hr.cdap.entity.Card;
import hr.cdap.entity.CardData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
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
		Card card=(Card)request.getSession().getAttribute("activeCard");
		if (elementId != null && !elementId.isEmpty() && card != null) {
			for (CardData data:card.getDataList()) {
				if (data != null && data.getValueBlob() != null && data.getCardElement().getFormId().equals(elementId)) {
					displayImage(response, data.getValueBlob());
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

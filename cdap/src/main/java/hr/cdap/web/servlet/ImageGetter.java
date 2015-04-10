package hr.cdap.web.servlet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImageGetter
 */
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
		// TODO Auto-generated method stub

		
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
		
		byte[] bytes = (byte[]) request.getSession().getAttribute("imageBytes");
		
		System.out.println("Image getter servlet... " + bytes);
		
		if (bytes != null) {
			String imageId = request.getParameter("imageId");
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

}

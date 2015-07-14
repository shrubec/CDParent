package hr.cdap.web.util;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.graphicimage.GraphicImage;

public class MobileImage extends GraphicImage{

	public void encodeBegin(FacesContext context) throws IOException
	  {
	   ResponseWriter writer = context.getResponseWriter();
	   writer.startElement("div", this);
	   writer.writeAttribute("id", getId(), null);
//	   writer.writeAttribute("style", "background-color:red;", null);
	   super.encodeBegin(context);
	  }
	
	public void encodeEnd(FacesContext context) throws IOException {
		super.encodeEnd(context);
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}
}

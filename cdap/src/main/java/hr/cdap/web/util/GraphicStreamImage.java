package hr.cdap.web.util;

import java.io.ByteArrayInputStream;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.model.DefaultStreamedContent;

public class GraphicStreamImage extends GraphicImage{

	private @Getter @Setter byte[] imageBytes;
	
	public GraphicStreamImage() {
		super();
	}
	
	public GraphicStreamImage(byte[] imageBytes) {
		super();
		this.imageBytes=imageBytes;
	}

	@Override
	public Object getValue() {
		
		System.out.println("Image getter: " + imageBytes);
		System.out.println("FAZA:" + FacesContext.getCurrentInstance().getCurrentPhaseId().getName());
		
		if (imageBytes == null) {
//			return super.getValue();
			return null;
		}
		
		 if (FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
	            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
	            return new DefaultStreamedContent();
	        }
		 else {
			 return new DefaultStreamedContent(new ByteArrayInputStream(imageBytes), "image/jpg","test.jpg"); 
		 }
		
	}
	
}

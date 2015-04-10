package hr.cdap.web.controller;

import hr.cdap.entity.CardElement;
import hr.cdap.web.util.WebUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
//@SessionScoped
@SuppressWarnings("unchecked")
public class ImageController {

	private byte[] bytes;
	private @Getter @Setter Part file;
	private @Setter StreamedContent imageContent;
	private List<CardElement> elementList;
	
	@ManagedProperty(value="#{acquisitionController}")
	private @Setter AcquisitionController acquisitionController;
	
//	public ImageController() {
//		WebUtil.getSession().setAttribute("imageBytes", null);
//	}
	
	@PostConstruct
    public void init() {
		System.out.println("Post construct...");
		WebUtil.getSession().setAttribute("imageBytes", null);
//		acquisitionController = (AcquisitionController) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("acquisitionController");
//		acquisitionController = (AcquisitionController) WebUtil.getSession().getAttribute("acquisitionController");
		System.out.println("Dohvacen acquisition controller: " + acquisitionController);
    }
	
	public StreamedContent getImageContent() {
		if (bytes != null) {
			imageContent = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "image/jpg");   
		}
		return imageContent;
	}
	
	public void upload() {
		try {
			bytes = IOUtils.toByteArray(file.getInputStream());
			WebUtil.getSession().setAttribute("imageBytes", bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveImage() {
		
		
		String cardTypeName= (String) WebUtil.getSession().getAttribute("imageCardTypeName");
		String selectedFormId= (String) WebUtil.getSession().getAttribute("imageElementId");
		elementList=(List<CardElement> ) WebUtil.getSession().getAttribute(cardTypeName);
		for (CardElement element:elementList) {
			if (element.getFormId().equals(selectedFormId)) {
				byte[] target = new byte[bytes.length];
			    System.arraycopy(bytes, 0, target, 0, bytes.length);
				element.getCardData().setValueBlob(target);
				bytes=null;
			}
		}
		
		
		RequestContext.getCurrentInstance().closeDialog(0);
//		acquisitionController.loadCardTemplate(false);
		
		
		acquisitionController.displayUploadedImage();
//		acquisitionController.loadCardTemplate(false);
	} 
	
	
}

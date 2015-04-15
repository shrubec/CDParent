package hr.cdap.web.controller;

import hr.cdap.entity.CardElement;
import hr.cdap.web.util.WebUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
@SuppressWarnings("unchecked")
public class ImageController {

	private byte[] bytes;
	private @Getter @Setter Part file;
	private @Setter StreamedContent imageContent;
	private List<CardElement> elementList;
	
	@PostConstruct
    public void init() {
		WebUtil.getSession().setAttribute("imageBytes", null);
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
		try {
			cardTypeName=new String(cardTypeName.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		elementList=(List<CardElement> ) WebUtil.getSession().getAttribute(cardTypeName);
		
		System.out.println("ELEMENT LIST: " + elementList + " za " + cardTypeName);
		
		for (CardElement element:elementList) {
			if (element.getFormId().equals(selectedFormId)) {
				element.getCardData().setValueBlob(bytes);
			}
		}
		RequestContext.getCurrentInstance().closeDialog(0);
	} 
	
	
}

package hr.cdap.web.controller;

import hr.cdap.entity.CardElement;
import hr.cdap.web.util.WebUtil;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@RequestScoped
//@ViewScoped
@SuppressWarnings("unchecked")
public class ImageDisplayController  {

	private StreamedContent imageContent;
	
	private String getType() {
		String cardTypeName= (String) WebUtil.getSession() .getAttribute("imageCardTypeName");
		String selectedFormId= (String) WebUtil.getSession() .getAttribute("imageElementId");
		List<CardElement> elementList=(List<CardElement> ) WebUtil.getSession() .getAttribute(cardTypeName);
		for (CardElement element:elementList) {
			if (element.getFormId().equals(selectedFormId)) {
				return element.getType();
			}
		}
		return null;
	}
	
	public StreamedContent getImageContent() {
		System.out.println("Image content...");
		byte[] bytes=(byte[])WebUtil.getSession().getAttribute("imageBytes");	
		if (bytes != null) {
			imageContent = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "image/jpg");   
		}
		return imageContent;
	}
	
	public void setImageContent(StreamedContent imageContent) {
		this.imageContent = imageContent;
	}
	
	
	public String getWidth() {
		if (getType() != null && getType().equals(CardElement.ELEMENT_TYPE_IMAGE)) return "350";
		return "550";
	}
	
	public String getHeight() {
		if (getType() != null && getType().equals(CardElement.ELEMENT_TYPE_IMAGE)) return "350";
		return "250";
	}
}

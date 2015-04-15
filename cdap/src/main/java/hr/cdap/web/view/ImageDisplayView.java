package hr.cdap.web.view;

import hr.cdap.entity.Card;
import hr.cdap.entity.CardElement;
import hr.cdap.web.util.WebUtil;

import java.io.ByteArrayInputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@RequestScoped
public class ImageDisplayView  {

	private StreamedContent imageContent;
	
	private String getType() {
		String selectedFormId= (String) WebUtil.getSession() .getAttribute("imageElementId");
		Card activeCard=(Card)WebUtil.getSession().getAttribute("activeCard");
		for (CardElement element:activeCard.getCardType().getElementList()) {
			if (element.getFormId().equals(selectedFormId)) {
				return element.getType();
			}
		}
		return null;
	}
	
	public StreamedContent getImageContent() {
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
		if (getType() != null && getType().equals(CardElement.ELEMENT_TYPE_IMAGE)) 
			return "350";
		else
			return "550";
	}
	
	public String getHeight() {
		if (getType() != null && getType().equals(CardElement.ELEMENT_TYPE_IMAGE)) 
			return "350";
		else
			return "250";
	}
}

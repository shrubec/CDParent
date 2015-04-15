package hr.cdap.web.view;

import hr.cdap.entity.Card;
import hr.cdap.entity.CardData;
import hr.cdap.entity.CardElement;
import hr.cdap.web.util.WebUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
public class ImageUploadView {

	private byte[] bytes;
	private @Getter @Setter Part file;
	private @Setter StreamedContent imageContent;
	
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
		Card activeCard=(Card)WebUtil.getSession().getAttribute("activeCard");
		String selectedFormId= (String) WebUtil.getSession().getAttribute("imageElementId");
		for (CardElement element:activeCard.getCardType().getElementList()) {
			if (element.getFormId().equals(selectedFormId)) {
				CardData data=WebUtil.fetchDataForElement(element, activeCard);
				data.setValueBlob(bytes);
			}
		}
		RequestContext.getCurrentInstance().closeDialog(0);
	} 
	
	
}

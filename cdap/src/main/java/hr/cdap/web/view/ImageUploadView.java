package hr.cdap.web.view;

import hr.cdap.entity.Card;
import hr.cdap.entity.CardData;
import hr.cdap.entity.CardElement;
import hr.cdap.web.util.WebUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class ImageUploadView {

	private byte[] bytes;
	private @Getter @Setter Part file;
	private @Setter StreamedContent imageContent;
	
	@PostConstruct
    public void init() {
		WebUtil.getSession().removeAttribute("imageBytes");
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
		WebUtil.getSession().removeAttribute("imageBytes");	
	} 
	
	public void handleFileUpload(FileUploadEvent event) {
		bytes = event.getFile().getContents();
		WebUtil.getSession().setAttribute("imageBytes", bytes);
		
	}
	
}

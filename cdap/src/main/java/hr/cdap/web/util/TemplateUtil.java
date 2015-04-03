package hr.cdap.web.util;

import hr.cdap.entity.CardElement;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.component.dnd.Draggable;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.component.resizable.Resizable;
import org.primefaces.context.RequestContext;



public class TemplateUtil {

	public static  OutputPanel resolveSide(String side) {
		UIViewRoot cardForm = FacesContext.getCurrentInstance().getViewRoot();
		String selectedSide="frontSide";
		if (side.equals("2")) {
			selectedSide="backSide";
		}
		return (OutputPanel) fetchElement(cardForm,selectedSide);
	}
	
	
	public static UIComponent fetchElement(UIComponent parent,String id) {
		for (UIComponent child:parent.getChildren()) {
			if (child.getId().equals(id)) {
				return child;
			}
			else {
				UIComponent grandChild= fetchElement(child,id);
				if (grandChild != null) return grandChild;
			}
		}
		return null;
	}
	
	public static void executeJS_setElementStyle(CardElement element) {
		
		if (element.getWidth() != null && !element.getWidth().isEmpty()) {
			BigDecimal width=new BigDecimal(element.getWidth());
			BigDecimal height=new BigDecimal(element.getHeight());
			width=width.divide(new BigDecimal("0.264583333"),0, RoundingMode.HALF_UP);
			height=height.divide(new BigDecimal("0.264583333"),0, RoundingMode.HALF_UP);
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.width='"+width+"px'");
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.height='"+height+"px'");
		}
		
		RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.display='inline-block'");
	}
	
	public static OutputPanel createImagePanel(CardElement element) {
		OutputPanel imagePanel = new OutputPanel();
		BigDecimal width = new BigDecimal(element.getWidth());
		BigDecimal height = new BigDecimal(element.getHeight());
		width = width.divide(new BigDecimal("0.264583333"), 0,RoundingMode.HALF_UP);
		height = height.divide(new BigDecimal("0.264583333"), 0,RoundingMode.HALF_UP);
		imagePanel.setId(element.getFormId());
		imagePanel.setStyle("width:" + width + "px;height:" + height+ "px;");
		return imagePanel;
	}
	
	public static GraphicImage createImage(CardElement element) {
		GraphicImage graphicImage = new GraphicImage();
		if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE)) {
			graphicImage.setValue("images/slika.jpg");
		}
		if (element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
			graphicImage.setValue("images/potpis.jpg");
		}
		graphicImage.setId(element.getFormId() + "_image");
		graphicImage.setWidth("100%");
		graphicImage.setHeight("100%");
		return graphicImage;
	}
	
	public static Resizable createResizable(CardElement element) {
		Resizable resizableElement = new Resizable();
		resizableElement.setId("resizableElement_"+ element.getFormId());
		resizableElement.setFor(element.getFormId());
		resizableElement.setAspectRatio(true);
		resizableElement.setOnResize("resizeImage('mainForm:"+ element.getFormId() + "');");
		return resizableElement;
	}
	
	public static OutputLabel createLabel(CardElement element) {
		OutputLabel formLabel = new OutputLabel();
		formLabel.setValue(element.getValue());
		formLabel.setId(element.getFormId());
		return formLabel;
	}
	
	public static Draggable createDraggable(CardElement element) {
		Draggable draggableElement = new Draggable();
		draggableElement.setId("draggableElement" + element.getFormId());
		draggableElement.setFor(element.getFormId());
		draggableElement.setContainment("parent");
		draggableElement.setOpacity(0.3);
		return draggableElement;
	}
	
	public static InputText createField(CardElement element) {
		InputText formField = new InputText();
		formField.setId(element.getFormId());
		
		if (element.getMaxCharLength() != null) {
			formField.setMaxlength(Integer.valueOf(element.getMaxCharLength()));
		}
//		if (element.getRequired()) {
//			formField.setRequired(true);
//			formField.setRequiredMessage("Polje je obavezno");
//		}
		return formField;
	}
	
	public static Calendar createCalendar(CardElement element) {
		Calendar calField=new Calendar();
		calField.setId(element.getFormId());
		calField.setPattern(element.getDateFormat());
		calField.setSize(8);
		return calField;
	}
	
	
	public static CommandLink createImagBtn(CardElement element) {
		CommandLink imageLink=new CommandLink();
		GraphicImage image=new GraphicImage();
		image.setValue("images/slika.jpg");
		image.setId(element.getFormId());
		imageLink.getChildren().add(image);
		return imageLink;
	}
	
	public static CommandLink createSignatureBtn(CardElement element) {
		CommandLink imageLink=new CommandLink();
		GraphicImage image=new GraphicImage();
		image.setValue("images/potpis.jpg");
		image.setId(element.getFormId());
		imageLink.getChildren().add(image);
		return imageLink;
	}
}

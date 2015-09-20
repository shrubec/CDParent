package hr.cdap.web.util;

import hr.cdap.entity.Card;
import hr.cdap.entity.CardData;
import hr.cdap.entity.CardElement;
import hr.cdap.entity.Client;
import hr.cdap.entity.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.component.dnd.Draggable;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.component.resizable.Resizable;
import org.primefaces.context.RequestContext;



public class WebUtil {
	
	public static String getParameter(String param) {
		 Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		 return (String)params.get(param);
	}
	
	public static void infoMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",message));
	}

	public static void warnMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning",message));
	}

	public static void errorMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",message));
	}

	public static void fatalMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal",message));
	}

	public static HttpSession getSession() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session=(HttpSession)fc.getExternalContext().getSession(false);
		if (session == null) {
			session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		}
		return session;
	}
	
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
	
	public static GraphicImage createImage(CardElement element,CardData data) {
		GraphicImage graphicImage = new GraphicImage();
		if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE)) {
			if (data == null || data.getValueBlob() == null) {
				graphicImage.setValue("resources/images/person.png");
			}
			else {
				graphicImage.setValue("ImageGetter?elementImageObjectId="+element.getFormId()+"&random="+String.valueOf(Math.random()));
			}
		}
		else if (element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
			if (data == null || data.getValueBlob() == null) {
				graphicImage.setValue("resources/images/signature.png");
			}
			else {
				graphicImage.setValue("ImageGetter?elementImageObjectId="+element.getFormId()+"&random="+String.valueOf(Math.random()));
			}
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
		formLabel.setValue(element.getStyleValue());
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
		
		if (element.getMaxCharLength() != null && !element.getMaxCharLength().isEmpty()) {
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
	
	
	public static CommandLink createImagBtn(CardElement element,CardData data) {
		CommandLink imageLink=new CommandLink();
		GraphicImage image = new GraphicImage();
		image.setId(element.getFormId());
		if (data == null || data.getValueBlob() == null) {
			image.setValue("resources/images/person.png");
		}
		else {
			image.setValue("ImageGetter?elementImageObjectId="+element.getFormId()+"&random="+String.valueOf(Math.random()));
		}
		imageLink.setOnclick("selectImageForUpload('"+element.getCardType().getName()+"','"+element.getFormId()+"')");
		imageLink.getChildren().add(image);
		imageLink.setId(element.getFormId()+"_link");
		return imageLink;
	}
	
	public static CommandLink createSignatureBtn(CardElement element,CardData data) {
		CommandLink imageLink=new CommandLink();
		GraphicImage image=new GraphicImage();
		image.setId(element.getFormId());
		if (data == null || data.getValueBlob() == null) {
			image.setValue("resources/images/signature.png");
		}
		else {
			image.setValue("ImageGetter?elementImageObjectId="+element.getFormId()+"&random="+String.valueOf(Math.random()));
		}
		imageLink.setOnclick("selectImageForUpload('"+element.getCardType().getName()+"','"+element.getFormId()+"')");
		imageLink.getChildren().add(image);
		return imageLink;
	}
	
	
	public static CardData fetchDataForElement(CardElement element,Card card) {
		if (card != null) {
			for (CardData data:card.getDataList()) {
				if (data.getCardElement().equals(element))
					return data;
			}
		}
		return null;
	}
	
	public static List<Client> initClientList(List<Client> clientList,User user) {
		List<Client> filteredClientList=new ArrayList<Client>();
		if (user != null && !user.getClient().getName().equals("AKD")) {
			for (Client client:clientList) {
				if (user.getClient().equals(client)) {
					filteredClientList.add(client);
				}
			}
		}
		else {
			filteredClientList.addAll(clientList);
		}
		return filteredClientList;
	}
	
}

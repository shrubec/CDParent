package hr.cdap.web.controller;

import hr.cdap.entity.CardElement;
import hr.cdap.entity.CardType;
import hr.cdap.web.util.TemplateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
@SuppressWarnings({"rawtypes","unchecked"})
public class AcquisitionController {
	
	private @Getter @Setter String selectedCardTypeName;
	private @Getter @Setter CardType selectedCardType;
	private @Getter @Setter List<CardType> cardTypes=new ArrayList<CardType>();
	
	private @Getter @Setter Boolean editPhase=false;
	
	public AcquisitionController() {
		loadTypesFromSession();
	}
	
	public void newCard() {
		editPhase=true;
		loadCardTemplate();
		System.out.println("nova kartica...");
	}
	
	public void cardTypeSelected() {
		for (CardType cardType:cardTypes) {
			if (cardType.getName() != null && selectedCardTypeName.equals(cardType.getName())) {
				selectedCardType=cardType;
			}
		}
		System.out.println("Odabrani: " + selectedCardType.getName());
		loadCardTemplate();
	}
	
	
	public List<SelectItem> getCardTypeItems() {
		List<SelectItem> list=new ArrayList<SelectItem> ();
		for (CardType cardType:cardTypes) {
			list.add(new SelectItem(cardType,cardType.getName()));
		}
		return list;
	}
	
	
	
	private void loadTypesFromSession() {
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session == null) {
			session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		}
		
		List<CardType> list=(List<CardType>)session.getAttribute("cardTypeList");
		if (list == null) {
			cardTypes=new ArrayList<CardType>();
			System.out.println("Nema liste u sessionu");
		}
		else {
			cardTypes=list;
			System.out.println("Ucitana lista iz sessiona: " + cardTypes.size());
		}
		
		
	}
	
	
	
	public void addElementOnForm(CardElement element) {

		OutputPanel panel = TemplateUtil.resolveSide(element.getSide());
		createFormElements(panel, element);
		RequestContext.getCurrentInstance().execute("moveToPositionInitial('mainForm:" + panel.getId()+ "','mainForm:" + element.getFormId() + "',"+ element.getPositionX() + "," + element.getPositionY()+ ");");
		TemplateUtil.executeJS_setElementStyle(element);
	}
	
	
	private void createFormElements(OutputPanel panel,CardElement element) {
		if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE)|| element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
			if (!editPhase) {
				OutputPanel imagePanel=TemplateUtil.createImagePanel(element);
				GraphicImage graphicImage = TemplateUtil.createImage(element);
				imagePanel.getChildren().add(graphicImage);
				panel.getChildren().add(imagePanel);
			}
			else {
				
				if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE)) {
					CommandLink imageBtn=TemplateUtil.createImagBtn(element); 
					panel.getChildren().add(imageBtn);
				}
				else if (element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
					CommandLink signatureBtn=TemplateUtil.createSignatureBtn(element); 
					panel.getChildren().add(signatureBtn);
				}
				
				
			}
		} else {
			
			if (!editPhase) {
				OutputLabel formLabel = TemplateUtil.createLabel(element);
				panel.getChildren().add(formLabel);
			}
			else {
				if (element.getType().equals(CardElement.ELEMENT_TYPE_FIELD)) {
					
					if (element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_STRING) || element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_NUMBER)) {
						InputText formField=TemplateUtil.createField(element);
						panel.getChildren().add(formField);
					}
					else {
						Calendar calField=TemplateUtil.createCalendar(element);
						panel.getChildren().add(calField);
						System.out.println("Dodan kalendar...");
					}
					
				}
				else {
					OutputLabel formLabel = TemplateUtil.createLabel(element);
					panel.getChildren().add(formLabel);
				}
			}
			
		}

	}
	
	
	
	
	public void saveCard() {
		
		System.out.println("Spremam karticu...");
		
		
		editPhase=false;
		loadCardTemplate();
	}
	
	
	public void loadCardTemplate() {
		
		OutputPanel front=TemplateUtil.resolveSide("1");
		OutputPanel back=TemplateUtil.resolveSide("2");
		front.getChildren().clear();
		back.getChildren().clear();
		
		System.out.println("Load attempt...");
		if (selectedCardType != null) {
			HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			List<CardElement> elementList=(List<CardElement> ) session.getAttribute(selectedCardType.getName());
			if (elementList == null) {
				elementList=new ArrayList<CardElement>();
			}
			
			System.out.println("Loadam template, element list: " +elementList.size() + ", " +  selectedCardType.getName());
			
			
			for (CardElement element:elementList) {
				addElementOnForm(element);
			}
			
		}
		
		
	}
	
}

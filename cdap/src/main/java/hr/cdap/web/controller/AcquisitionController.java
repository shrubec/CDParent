package hr.cdap.web.controller;

import hr.cdap.entity.Card;
import hr.cdap.entity.CardData;
import hr.cdap.entity.CardElement;
import hr.cdap.entity.CardType;
import hr.cdap.service.AcquisitionService;
import hr.cdap.web.util.TemplateUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	List<CardElement> elementList;
	private @Getter @Setter Boolean editPhase=false;
	
	public AcquisitionController() {
		loadTypesFromSession();
	}
	
	public void newCard() {
		System.out.println("kreiram novu karticu...");
		editPhase=true;
		loadCardTemplate(true);
		System.out.println("nova kartica...");
	}
	
	public void cardTypeSelected() {
		for (CardType cardType:cardTypes) {
			if (cardType.getName() != null && selectedCardTypeName.equals(cardType.getName())) {
				selectedCardType=cardType;
			}
		}
		System.out.println("Odabrani: " + selectedCardType.getName());
		loadCardTemplate(false);
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
		
		if (element.getType().equals(CardElement.ELEMENT_TYPE_LABEL)) {
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+element.getStyleValue()+"'");
		}
		else if (element.getType().equals(CardElement.ELEMENT_TYPE_FIELD)) {
			
			System.out.println("Test vrijednosti: " + element.getCardData());
			System.out.println("Specimen vrijednost: " + element.getValue());
			System.out.println("Specimen vrijednost style: " + element.getStyleValue());
			
			if (element.getCardData() != null) {
				String value="";
				
				if (element.getCardData().getValueString() != null) {
					value=element.getCardData().getValueString();
				}
				else if (element.getCardData().getValueInt() != null) {
					value=String.valueOf(element.getCardData().getValueInt());
				}
				else if (element.getCardData().getValueDec() != null) {
					value=String.valueOf(element.getCardData().getValueDec());
				}
				else if (element.getCardData().getValueDate() != null) {
					SimpleDateFormat sdf=new SimpleDateFormat(element.getDateFormat());
					value=sdf.format(element.getCardData().getValueDate());
				}
				
				
				if (value != null && !value.isEmpty()) {
					String styleValue=element.getStyleValue().replace(element.getValue(), value);
					
					System.out.println("Postavljena inner html vrijednost polja: " + styleValue);
					
					RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+styleValue+"'");
					
				}
				
				
			}
			else
				RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+element.getStyleValue()+"'");
		}
	}
	
	
	private void createFormElements(OutputPanel panel,CardElement element) {
		if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE) || element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
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
				if (element.getCardData() != null) {
					
					if (element.getCardData().getValueString() != null) {
						formLabel.setValue(element.getCardData().getValueString());
					}
					else if (element.getCardData().getValueInt() != null) {
						formLabel.setValue(element.getCardData().getValueInt());
					}
					else if (element.getCardData().getValueDec() != null) {
						formLabel.setValue(element.getCardData().getValueDec());
					}
					else if (element.getCardData().getValueDate() != null) {
						SimpleDateFormat sdf=new SimpleDateFormat(element.getDateFormat());
						formLabel.setValue(sdf.format(element.getCardData().getValueDate()));
					}
					
				}
				panel.getChildren().add(formLabel);
			}
			else {
				if (element.getType().equals(CardElement.ELEMENT_TYPE_FIELD)) {
					
					if (element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_STRING) || element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_NUMBER)) {
						InputText formField=TemplateUtil.createField(element);
						
						if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_STRING)) {
							formField.setValue(element.getCardData().getValueString());
							
							System.out.println("postavljen form value: " + formField.getValue());
							
						}
						if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_NUMBER))
							formField.setValue(element.getCardData().getValueInt());
						
						panel.getChildren().add(formField);
					}
					else {
						Calendar calField=TemplateUtil.createCalendar(element);
						if (element.getCardData().getValueDate() != null) {
							calField.setValue(element.getCardData().getValueDate());
						}
						panel.getChildren().add(calField);
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
		
		for (CardElement element:elementList) {
			Object obj=TemplateUtil.fetchElement(TemplateUtil.resolveSide(element.getSide()), element.getFormId());
			System.out.println("Pronadjen objekt na formi: " + obj);
			Object value=null;
			if (obj instanceof InputText) {
				InputText text=(InputText) obj;
				value=text.getSubmittedValue();
				System.out.println("Value text: "+value);
			}
			else if (obj instanceof Calendar) {
				Calendar cal=(Calendar) obj;
				value=cal.getSubmittedValue();
				System.out.println("Value cal: "+value);
			}
			else if (obj instanceof GraphicImage) {
				GraphicImage image=(GraphicImage) obj;
				value=image.getValue();
				System.out.println("Value image: "+value);
			}
			
			if (!element.getType().equals(CardElement.ELEMENT_TYPE_LABEL)) {
				applyFormDataToCard(element, value);
			}
		}
		
		
		editPhase=false;
		loadCardTemplate(false);
	}
	
	
	
	private void applyFormDataToCard(CardElement element,Object value) {
		if (element.getDataType() != null) {
			if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_STRING))
				element.getCardData().setValueString((String)value);
			if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_NUMBER))
				element.getCardData().setValueInt(Integer.valueOf((String)value));
			if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_DATE)) {
				SimpleDateFormat sdf=new SimpleDateFormat(element.getDateFormat());
				try {
					element.getCardData().setValueDate(sdf.parse((String)value));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		//slika!!!!
		
	}
	
	
	private void mapCardDataToCardElement(List<CardData> dataList) {
		for (CardElement element:elementList) {
			for (CardData data:dataList) {
				if (data.getCardElement().equals(element)) {
					element.setCardData(data);
					
					System.out.println("POSTAVLJEN TRANS CARD DATA NA ELEMENT!!!!");
					
				}
			}
		}
		
		System.out.println("Kraj mapiranja...");
	}
	
	
	
	public void loadCardTemplate(boolean isNewCard) {
		
		OutputPanel front=TemplateUtil.resolveSide("1");
		OutputPanel back=TemplateUtil.resolveSide("2");
		front.getChildren().clear();
		back.getChildren().clear();
		
		System.out.println("Load attempt...");
		if (selectedCardType != null) {
			HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			elementList=(List<CardElement> ) session.getAttribute(selectedCardType.getName());
			if (elementList == null) {
				elementList=new ArrayList<CardElement>();
			}
			
			System.out.println("Loadam template, element list: " +elementList.size() + ", " +  selectedCardType.getName());
			if (isNewCard) {
				mapCardDataToCardElement(AcquisitionService.prepareNewCardData(elementList));
			}
			for (CardElement element:elementList) {
				System.out.println("Inicijalno dohvacanje vrijednosti: " + element.getValue());
				System.out.println("Inicijalno dohvacanje style vrijednosti: " + element.getStyleValue());
				addElementOnForm(element);
			}
			
		}
		
		
	}
	
}

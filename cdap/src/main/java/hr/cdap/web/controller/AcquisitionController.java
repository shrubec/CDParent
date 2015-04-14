package hr.cdap.web.controller;

import hr.cdap.entity.CardData;
import hr.cdap.entity.CardElement;
import hr.cdap.entity.CardType;
import hr.cdap.service.AcquisitionService;
import hr.cdap.web.util.WebUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

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
	
	 
	@PostConstruct
	private void loadTypesFromSession() {
		List<CardType> list=(List<CardType>)WebUtil.getSession().getAttribute("cardTypeList");
		if (list == null) 
			cardTypes=new ArrayList<CardType>();
		else 
			cardTypes=list;
	}
	
	public void showImageDialog() {
		RequestContext.getCurrentInstance().openDialog("uploadImage");
	}
	
	public void returnImageDialog() {
		RequestContext.getCurrentInstance().update("mainForm");
		String selectedFormId= (String) WebUtil.getSession().getAttribute("imageElementId");
		for (CardElement element:elementList) {
			OutputPanel panel = WebUtil.resolveSide(element.getSide());
			if (element.getFormId().equals(selectedFormId)) {
				GraphicImage image=(GraphicImage)WebUtil.fetchElement(panel, element.getFormId());
				image.setValue("ImageGetter?elementImageObjectId="+element.getFormId());
			}
			RequestContext.getCurrentInstance().execute("moveToPositionInitial('mainForm:" + panel.getId()+ "','mainForm:" + element.getFormId() + "',"+ element.getPositionX() + "," + element.getPositionY()+ ");");
			WebUtil.executeJS_setElementStyle(element);
		}
	}
	
	public void newCard() {
		editPhase=true;
		loadCardTemplate(true);
	}
	
	public void cardTypeSelected() {
		for (CardType cardType:cardTypes) {
			if (cardType.getName() != null && selectedCardTypeName.equals(cardType.getName())) 
				selectedCardType=cardType;
		}
		loadCardTemplate(false);
	}
	
	public List<SelectItem> getCardTypeItems() {
		List<SelectItem> list=new ArrayList<SelectItem> ();
		for (CardType cardType:cardTypes) {
			list.add(new SelectItem(cardType,cardType.getName()));
		}
		return list;
	}
	
	
	public void addElementOnForm(CardElement element) {

		OutputPanel panel = WebUtil.resolveSide(element.getSide());
		createFormElements(panel, element);
		RequestContext.getCurrentInstance().execute("moveToPositionInitial('mainForm:" + panel.getId()+ "','mainForm:" + element.getFormId() + "',"+ element.getPositionX() + "," + element.getPositionY()+ ");");
		WebUtil.executeJS_setElementStyle(element);
		
		if (element.getType().equals(CardElement.ELEMENT_TYPE_LABEL)) 
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+element.getStyleValue()+"'");
		else if (element.getType().equals(CardElement.ELEMENT_TYPE_FIELD)) {
			if (element.getCardData() != null) {
				String value="";
				if (element.getCardData().getValueString() != null) 
					value=element.getCardData().getValueString();
				else if (element.getCardData().getValueInt() != null) 
					value=String.valueOf(element.getCardData().getValueInt());
				else if (element.getCardData().getValueDec() != null) 
					value=String.valueOf(element.getCardData().getValueDec());
				else if (element.getCardData().getValueDate() != null) {
					SimpleDateFormat sdf=new SimpleDateFormat(element.getDateFormat());
					value=sdf.format(element.getCardData().getValueDate());
				}
				if (value != null && !value.isEmpty()) {
					String styleValue=element.getStyleValue().replace(element.getValue(), value);
					RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+styleValue+"'");
				}
			}
			else 
				RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+element.getStyleValue()+"'");
		}
	}
	
	private void createFormElements(OutputPanel panel,CardElement element) {
		if (editPhase) 
			createElementForEditPhase(panel, element);
		else 
			createElementForDisplayPhase(panel, element);
	}
	
	private void createElementForEditPhase(OutputPanel panel,CardElement element) {
		if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE)) {
			CommandLink imageBtn=WebUtil.createImagBtn(element); 
			panel.getChildren().add(imageBtn);
		}
		else if (element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
			CommandLink signatureBtn=WebUtil.createSignatureBtn(element); 
			panel.getChildren().add(signatureBtn);
		}
		else if (element.getType().equals(CardElement.ELEMENT_TYPE_FIELD)) {
			if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_STRING)) {
				InputText formField=WebUtil.createField(element);
				formField.setValue(element.getCardData().getValueString());
				panel.getChildren().add(formField);
			}
			else if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_NUMBER)) {
				InputText formField=WebUtil.createField(element);
				formField.setValue(element.getCardData().getValueInt());
				panel.getChildren().add(formField);
			}
			else if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_DATE)) {
				Calendar calField=WebUtil.createCalendar(element);
				if (element.getCardData().getValueDate() != null) 
					calField.setValue(element.getCardData().getValueDate());
				panel.getChildren().add(calField);
			}
		}
		else if (element.getType().equals(CardElement.ELEMENT_TYPE_LABEL)) {
			OutputLabel formLabel = WebUtil.createLabel(element);
			panel.getChildren().add(formLabel);
		}
	}
	
	private void createElementForDisplayPhase(OutputPanel panel,CardElement element) {
		
		if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE) || element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
			OutputPanel imagePanel=WebUtil.createImagePanel(element);
			GraphicImage graphicImage = WebUtil.createImage(element);
			imagePanel.getChildren().add(graphicImage);
			panel.getChildren().add(imagePanel);
		}
		else {
			OutputLabel formLabel = WebUtil.createLabel(element);
			if (element.getCardData() != null) {
				if (element.getCardData().getValueString() != null) 
					formLabel.setValue(element.getCardData().getValueString());
				else if (element.getCardData().getValueInt() != null) 
					formLabel.setValue(element.getCardData().getValueInt());
				else if (element.getCardData().getValueDec() != null) 
					formLabel.setValue(element.getCardData().getValueDec());
				else if (element.getCardData().getValueDate() != null) {
					SimpleDateFormat sdf=new SimpleDateFormat(element.getDateFormat());
					formLabel.setValue(sdf.format(element.getCardData().getValueDate()));
				}
			}
			panel.getChildren().add(formLabel);
		}
	}
	
	
	public void saveCard() {
		
		System.out.println("Spremam karticu...");
		
		for (CardElement element:elementList) {
			Object obj=WebUtil.fetchElement(WebUtil.resolveSide(element.getSide()), element.getFormId());
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
			if (!element.getType().equals(CardElement.ELEMENT_TYPE_LABEL)) 
				applyFormDataToCard(element, value);
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
	}
	
	private void mapCardDataToCardElement(List<CardData> dataList) {
		for (CardElement element:elementList) {
			for (CardData data:dataList) {
				if (data.getCardElement().equals(element)) 
					element.setCardData(data);
			}
		}
	}
	
	
	public void loadCardTemplate(boolean isNewCard) {
		
		OutputPanel front=WebUtil.resolveSide("1");
		OutputPanel back=WebUtil.resolveSide("2");
		front.getChildren().clear();
		back.getChildren().clear();
		
		if (selectedCardType != null) {
			elementList=(List<CardElement> ) WebUtil.getSession().getAttribute(selectedCardType.getName());
			if (elementList == null) {
				elementList=new ArrayList<CardElement>();
			}
			if (isNewCard) {
				mapCardDataToCardElement(AcquisitionService.prepareNewCardData(elementList));
			}
			for (CardElement element:elementList) {
				addElementOnForm(element);
			}
		}
	}
	
	
}

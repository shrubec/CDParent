package hr.cdap.web.view;

import hr.cdap.entity.Card;
import hr.cdap.entity.CardData;
import hr.cdap.entity.CardElement;
import hr.cdap.entity.CardType;
import hr.cdap.service.AcquisitionService;
import hr.cdap.service.DesignService;
import hr.cdap.web.util.AbstractView;
import hr.cdap.web.util.WebUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class AcquisitionView extends AbstractView {
	
	private @Getter @Setter String selectedCardTypeName;
	private @Getter @Setter List<CardType> cardTypes=new ArrayList<CardType>();
	private @Getter @Setter List<Card> cardList=new ArrayList<Card>();
	private @Getter @Setter Card selectedCard; //ovo je samo za prikaz
	private @Getter @Setter Card activeCard;
	private @Getter @Setter Boolean editPhase=false;
	
	
	@PostConstruct
	private void loadCardTypes() {
		WebUtil.getSession().removeAttribute("activeCard");
		cardTypes=DesignService.fetchCardTypes();
		oneSideActive=(Boolean)WebUtil.getSession().getAttribute("oneSideActive");
	}
	
	
	public void changeActiveSide() {
		if (editPhase) mapFormDataToObject(); 
		if (activeBack) activeBack=false;
		else activeBack=true;
		loadCardTemplate();
		if (activeBack) activeBack=false;
		else activeBack=true;
	}
	
	public void showImageDialog() {
		RequestContext.getCurrentInstance().openDialog("templates/dialogs/uploadImage");
	}
	
	public void returnImageDialog() {
		RequestContext.getCurrentInstance().update("mainForm");
		for (CardElement element:selectedCardType.getElementList()) {
			OutputPanel panel = WebUtil.resolveSide(element.getSide());
			if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE) || element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
				CardData data=WebUtil.fetchDataForElement(element, activeCard);
				if (data != null && data.getValueBlob() != null) {
					GraphicImage image=(GraphicImage)WebUtil.fetchElement(panel, element.getFormId());
					image.setValue("ImageGetter?elementImageObjectId="+element.getFormId()+"&random="+String.valueOf(Math.random()));
				}
			}
			RequestContext.getCurrentInstance().execute("moveToPositionInitial('mainForm:" + panel.getId()+ "','mainForm:" + element.getFormId() + "',"+ element.getPositionX() + "," + element.getPositionY()+ ");");
			WebUtil.executeJS_setElementStyle(element);
			if (element.getType().equals(CardElement.ELEMENT_TYPE_LABEL)) 
				RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+element.getStyleValue()+"'");
		}
	}
	
	public void newCard() {
		editPhase=true; 
		activeCard=new Card();
		activeCard.setDateCreated(new Date());
		activeCard.setCardType(selectedCardType);
		selectedCardType.getElementList().clear();
		selectedCardType.getElementList().addAll(DesignService.fetchElementsForCardType(selectedCardType));
		activeCard.setDataList(AcquisitionService.prepareNewCardData(activeCard,selectedCardType.getElementList()));
		WebUtil.getSession().setAttribute("activeCard", activeCard);
		loadCardTemplate();
	}
	
	public void editCard() {
		if (activeCard == null) 
			newCard();
		else {
			editPhase=true;
			loadCardTemplate();
		}
	}
	
	public void cardTypeSelected() {
		activeCard=null;
		WebUtil.getSession().removeAttribute("activeCard");
		for (CardType cardType:cardTypes) {
			if (cardType.getName() != null && selectedCardTypeName.equals(cardType.getName())) 
				selectedCardType=cardType;
		}
		loadCardTemplate();
		cardList.clear();
		cardList.addAll(AcquisitionService.fetchCardByType(selectedCardType));
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
		createFormElement(panel, element);
		RequestContext.getCurrentInstance().execute("moveToPositionInitial('mainForm:" + panel.getId()+ "','mainForm:" + element.getFormId() + "',"+ element.getPositionX() + "," + element.getPositionY()+ ");");
		WebUtil.executeJS_setElementStyle(element);
		
		if (element.getType().equals(CardElement.ELEMENT_TYPE_LABEL)) 
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+element.getStyleValue()+"'");
		else if (element.getType().equals(CardElement.ELEMENT_TYPE_FIELD)) {
			CardData data=WebUtil.fetchDataForElement(element, activeCard);
			if (data != null) {
				String value="";
				if (data.getValueString() != null) 
					value=data.getValueString();
				else if (data.getValueInt() != null) 
					value=String.valueOf(data.getValueInt());
				else if (data.getValueDec() != null) 
					value=String.valueOf(data.getValueDec());
				else if (data.getValueDate() != null) {
					SimpleDateFormat sdf=new SimpleDateFormat(element.getDateFormat());
					value=sdf.format(data.getValueDate()); 
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
	
	private void createFormElement(OutputPanel panel,CardElement element) {
		if (editPhase) 
			createElementForEditPhase(panel, element);
		else 
			createElementForDisplayPhase(panel, element);
	}
	
	private void createElementForEditPhase(OutputPanel panel,CardElement element) {
		if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE)) {
			CardData data=WebUtil.fetchDataForElement(element, activeCard);
			CommandLink imageBtn=WebUtil.createImagBtn(element,data); 
			panel.getChildren().add(imageBtn);
		}
		else if (element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
			CardData data=WebUtil.fetchDataForElement(element, activeCard);
			CommandLink signatureBtn=WebUtil.createSignatureBtn(element,data); 
			panel.getChildren().add(signatureBtn);
		}
		else if (element.getType().equals(CardElement.ELEMENT_TYPE_FIELD)) {
			CardData data=WebUtil.fetchDataForElement(element, activeCard);
			if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_STRING)) {
				InputText formField=WebUtil.createField(element);
				formField.setValue(data.getValueString());
				panel.getChildren().add(formField);
			}
			else if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_NUMBER)) {
				InputText formField=WebUtil.createField(element);
				formField.setValue(data.getValueInt());
				panel.getChildren().add(formField);
			}
			else if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_DATE)) {
				Calendar calField=WebUtil.createCalendar(element);
				if (data.getValueDate() != null) 
					calField.setValue(data.getValueDate());
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
			CardData data=WebUtil.fetchDataForElement(element, activeCard);
			OutputPanel imagePanel=WebUtil.createImagePanel(element);
			GraphicImage graphicImage = WebUtil.createImage(element,data);
			imagePanel.getChildren().add(graphicImage);
			panel.getChildren().add(imagePanel);
		}
		else {
			OutputLabel formLabel = WebUtil.createLabel(element);
			CardData data=WebUtil.fetchDataForElement(element, activeCard);
			if (data != null) {
				if (data.getValueString() != null) 
					formLabel.setValue(data.getValueString());
				else if (data.getValueInt() != null) 
					formLabel.setValue(data.getValueInt());
				else if (data.getValueDec() != null) 
					formLabel.setValue(data.getValueDec());
				else if (data.getValueDate() != null) {
					SimpleDateFormat sdf=new SimpleDateFormat(element.getDateFormat());
					formLabel.setValue(sdf.format(data.getValueDate()));
				}
			}
			panel.getChildren().add(formLabel);
		}
	}
	
	public void selectCard() {
		editPhase=false;
		activeCard=selectedCard;
		WebUtil.getSession().setAttribute("activeCard", activeCard); 
		activeCard.setDataList(AcquisitionService.fetchDataForCard(activeCard));
		loadExistingCard(activeCard);
		RequestContext.getCurrentInstance().update("mainForm");
	}
	
	public void saveCard() {
		mapFormDataToObject(); 
		AcquisitionService.saveCard(activeCard);
		cardList.clear();
		cardList.addAll(AcquisitionService.fetchCardByType(selectedCardType));
		editPhase=false;
		loadCardTemplate();
		WebUtil.infoMessage("Kartica " + activeCard.getCardNumber() + " uspješno spremljena");
	}
	

	private void mapFormDataToObject() {
		for (CardElement element:selectedCardType.getElementList()) {
			Object obj=WebUtil.fetchElement(WebUtil.resolveSide(element.getSide()), element.getFormId());
			Object value=null;
			if (obj instanceof InputText) 
				value=((InputText)obj).getSubmittedValue();
			else if (obj instanceof Calendar) 
				value=((Calendar)obj).getSubmittedValue();
			else if (obj instanceof GraphicImage) 
				value=((GraphicImage) obj).getValue();
			
			if (!element.getType().equals(CardElement.ELEMENT_TYPE_LABEL)) 
				applyFormDataToCard(element, value);
		} 
	}
	
	
	private void applyFormDataToCard(CardElement element,Object value) {
		if (element.getDataType() != null && value != null) {
			CardData data=WebUtil.fetchDataForElement(element, activeCard);
			if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_STRING)) {
				data.setValueString((String)value);
			}
			if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_NUMBER))
				data.setValueInt(Integer.valueOf((String)value));
			if(element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_DATE)) {
				SimpleDateFormat sdf=new SimpleDateFormat(element.getDateFormat());
				try {
					data.setValueDate(sdf.parse((String)value));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void loadCardTemplate() {
		OutputPanel front=WebUtil.resolveSide("1");
		OutputPanel back=WebUtil.resolveSide("2");
		front.getChildren().clear();
		back.getChildren().clear();
		String activeSide=getActiveSide();
		if (selectedCardType != null) {
			for (CardElement element:selectedCardType.getElementList()) {
				if (activeSide == null || activeSide.equals(element.getSide())) {
					addElementOnForm(element);
				}
			}
		}
	}
	
	public void loadExistingCard(Card card) {
		OutputPanel front=WebUtil.resolveSide("1");
		OutputPanel back=WebUtil.resolveSide("2");
		front.getChildren().clear();
		back.getChildren().clear();
		String activeSide=getActiveSide();
		for (CardElement element:card.getCardType().getElementList()) {
			if (activeSide == null || activeSide.equals(element.getSide())) {
				addElementOnForm(element);
			}
		}
	}
	
}

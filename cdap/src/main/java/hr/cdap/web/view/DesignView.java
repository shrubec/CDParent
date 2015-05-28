package hr.cdap.web.view;


import hr.cdap.entity.CardElement;
import hr.cdap.entity.CardType;
import hr.cdap.service.DesignService;
import hr.cdap.web.object.ElementSessionDO;
import hr.cdap.web.util.AbstractView;
import hr.cdap.web.util.WebUtil;
import hr.cdap.web.validator.CardDesignValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.model.SelectItem;

import lombok.Getter;
import lombok.Setter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.primefaces.component.dnd.Draggable;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.component.resizable.Resizable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
@SuppressWarnings({"rawtypes","unchecked"})
public class DesignView extends AbstractView {
	
	private @Getter @Setter String selectedId;
	private @Getter @Setter String selectedCardTypeName;
	private @Getter @Setter List<CardType> cardTypes;
	
	private @Getter @Setter String cardWidth="480px;";
	private @Getter @Setter String cardHeight="280px;";
	private @Getter @Setter String fontSize="90%;";
	private String fieldWidth="40";
	private String fieldHeight="8";
	private String imageWidth="25";
	private String imageHeight="25";
	private String signatureWidth="40";
	private String signatureHeight="10";
	 
	@PostConstruct
	public void init() {
		createElementMap();
		cardTypes=DesignService.fetchCardTypes();
		oneSideActive=(Boolean)WebUtil.getSession().getAttribute("oneSideActive");
	}
	
	
	public void changeActiveSide() {

		//zato jer se sprema template od prethodno odabrane strane
		if (activeBack) activeBack=false;
		else activeBack=true;
		
		saveTemplateElements();
		
		if (activeBack) activeBack=false;
		else activeBack=true;
		
		setSelectedId(null);
		loadCardTemplate();
	}
	
	public void showSelectBackgroundDialog() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("contentHeight", 450);
		options.put("contentWidth", 1300);
		RequestContext.getCurrentInstance().openDialog("templates/dialogs/selectBackground",options, null);
	}
	
	public void returnBackgroundDialogFront(SelectEvent event) {
		saveTemplateElements();
		selectedCardType.setImageFront((String)event.getObject());
		RequestContext.getCurrentInstance().update("mainForm:frontSide");
		loadCardTemplate();
	}
	
	public void returnBackgroundDialogBack(SelectEvent event) {
		saveTemplateElements();
		selectedCardType.setImageBack((String)event.getObject());
		RequestContext.getCurrentInstance().update("mainForm:backSide");
		loadCardTemplate();
	}
	
	
	public void newCardType() {
		selectedCardTypeName=null;
		selectedCardType=new CardType();
	}
	
	public void saveCardType() {
		selectedCardTypeName=selectedCardType.getName();
		cardTypes.add(selectedCardType);
		DesignService.saveCardType(selectedCardType);
		loadCardTemplate();
	}
	
	public void cardTypeSelected() {
		for (CardType cardType:cardTypes) {
			if (cardType.getName() != null && selectedCardTypeName.equals(cardType.getName())) {
				selectedCardType=cardType;
				selectedCardType.getElementList().clear();
				selectedCardType.getElementList().addAll(DesignService.fetchElementsForCardType(selectedCardType));
			}
		}
		loadCardTemplate();
	}
	
	
	public List<SelectItem> getCardTypeItems() {
		List<SelectItem> list=new ArrayList<SelectItem> ();
		for (CardType cardType:cardTypes) {
			list.add(new SelectItem(cardType,cardType.getName()));
		}
		return list;
	}
	
	
	public Boolean getRenderCardPanel() {
		return selectedCardType != null ? true : false;
	}
	
	private void createElementMap() {
		WebUtil.getSession().setAttribute("elementMap", new HashMap<String,String>());
	}
	
	public CardElement getSelectedElement() {
		for (CardElement element:selectedCardType.getElementList()) {
			if (("mainForm:"+element.getFormId()).equals(selectedId)) {
				return element;
			}
		}
		return null;
	}
	
	private Integer fetchNumberOfElements(String type) {
		int count=0;
		for (CardElement element:selectedCardType.getElementList()) {
			if (element.getType().equals(type)) {
				count++;
			}
		}
		return count;
	}
	
	public void addNewCardElement(String side,String type) {
		CardElement element=createNewElement(side, type);
		selectedCardType.getElementList().add(element);
		selectedId=("mainForm:"+element.getFormId());
		Map map=fetchElementMap();
		ElementSessionDO elementSessionDO=createSessionElementObject(element);
		map.put(elementSessionDO.getElementId(), elementSessionDO);
		String activeSide=getActiveSide();
		for (CardElement e:selectedCardType.getElementList()) {
			if (activeSide == null || activeSide.equals(e.getSide())) {
				addElementOnForm(e);
			}
		}
		executeJS_markElementSelected(element);
	}
	
	
	private CardElement createNewElement(String side,String type) {
		
		CardElement element=new CardElement(selectedCardType,type.equals(CardElement.ELEMENT_TYPE_LABEL) ? false : true);
		element.setFormId("cardElement"+String.valueOf(selectedCardType.getElementList().size()));
		element.setValue("");
		element.setType(type);
		element.setSide(side);
		element.setPositionX("5");
		element.setPositionY("5"); 
		
		if (type.equals(CardElement.ELEMENT_TYPE_LABEL)) {
			element.setName("Labela "+String.valueOf(fetchNumberOfElements(type)));
		}
		else if (type.equals(CardElement.ELEMENT_TYPE_FIELD)) {
			element.setName("Polje "+String.valueOf(fetchNumberOfElements(type)));
		}
		else if (type.equals(CardElement.ELEMENT_TYPE_IMAGE)) {
			element.setName("Slika "+String.valueOf(fetchNumberOfElements(type)));
		}
		else if (type.equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
			element.setName("Potpis "+String.valueOf(fetchNumberOfElements(type)));
		}
		
		if (type.equals(CardElement.ELEMENT_TYPE_IMAGE)) {
			element.setWidth(imageHeight);
			element.setHeight(imageWidth);
			element.setRequired(true);
		}
		else if (type.equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
			element.setWidth(signatureWidth);
			element.setHeight(signatureHeight);
			element.setRequired(true);
		}
		else if (type.equals(CardElement.ELEMENT_TYPE_FIELD)) {
			element.setValue("SPECIMEN");
			element.setStyleValue("SPECIMEN");
			element.setWidth(fieldWidth);
			element.setHeight(fieldHeight);
			element.setDataType(CardElement.ELEMENT_DATA_TYPE_STRING);
			element.setMinCharLength("3");
			element.setMaxCharLength("20");
			element.setRequired(true);
		}
		else {
			element.setValue("SPECIMEN");
			element.setStyleValue("SPECIMEN");
			element.setWidth(fieldWidth);
			element.setHeight(fieldHeight);
		}
		return element;
	}
	
	
	private ElementSessionDO createSessionElementObject(CardElement element) {
		
		ElementSessionDO elementSessionDO=new ElementSessionDO();
		elementSessionDO.setElementId("mainForm:"+element.getFormId());
		elementSessionDO.setElementType(element.getType());
		elementSessionDO.setElementEditor(element.getStyleValue());
		elementSessionDO.setElementHeight(element.getHeight());
		elementSessionDO.setElementWidth(element.getWidth());
		elementSessionDO.setElementX(element.getPositionX());
		elementSessionDO.setElementY(element.getPositionY());
		elementSessionDO.setElementName(element.getName());
		
		if (element.getRequired() != null)
			elementSessionDO.setElementRequired(element.getRequired()?"DA":"NE");
		
		if (element.getDataType() != null) {
			if (element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_STRING)) {
				elementSessionDO.setElementDataType("Text");
				elementSessionDO.setElementMinChar(element.getMinCharLength());
				elementSessionDO.setElementMaxChar(element.getMaxCharLength());
			}
			else if (element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_NUMBER))
				elementSessionDO.setElementDataType("Broj");
			else if (element.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_DATE)) {
				elementSessionDO.setElementDataType("Datum");
				elementSessionDO.setElementDateFormat(element.getDateFormat());
			}
		}
		return elementSessionDO;
	}
	
	
	private void executeJS_markElementSelected(CardElement element) {
		String selectedSide="frontSide";
		if (element.getSide().equals("2")) {
			selectedSide="backSide";
		}
		RequestContext.getCurrentInstance().execute("selectElement(document.getElementById('mainForm:"+element.getFormId()+"'),'mainForm:"+selectedSide+"')");
	}
	
	private void executeJS_setElementStyleAndValue(CardElement element) {
		
		WebUtil.executeJS_setElementStyle(element);
		if (element.getType().equals(CardElement.ELEMENT_TYPE_LABEL) || element.getType().equals(CardElement.ELEMENT_TYPE_FIELD)) {
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+element.getStyleValue()+"'");
		}
	}
	
	
	public void refreshAdditionalForm() {
		RequestContext.getCurrentInstance().update("mainForm:mainTab:elementDataType");
		RequestContext.getCurrentInstance().update("mainForm:mainTab:elementMinimumLength");
		RequestContext.getCurrentInstance().update("mainForm:mainTab:elementMaximumLength");
		RequestContext.getCurrentInstance().update("mainForm:mainTab:dateFormat");
		RequestContext.getCurrentInstance().update("mainForm:mainTab:elementRequired");
	}
	
	public void addElementOnForm(CardElement element) {

		OutputPanel panel = WebUtil.resolveSide(element.getSide());
		if (!element.getAddedOnForm()) {
			createFormElements(panel, element, createOnClickScript(element));
		}
		else {
			UIComponent formElement = WebUtil.fetchElement(panel, element.getFormId());
			if (formElement instanceof OutputLabel) {
				((OutputLabel) formElement).setValue(element.getStyleValue());
			}
		}

		ElementSessionDO elementSessionDO = fetchElementMapValues(element.getFormId());
		if (elementSessionDO != null) {
			element.setName(elementSessionDO.getElementName());
			element.setPositionX(elementSessionDO.getElementX());
			element.setPositionY(elementSessionDO.getElementY());
			element.setWidth(elementSessionDO.getElementWidth());
			element.setHeight(elementSessionDO.getElementHeight());
			if (elementSessionDO.getElementEditor() != null) {
				element.setValue(getPlainTextValue(elementSessionDO.getElementEditor()).trim());
			}
			element.setStyleValue(elementSessionDO.getElementEditor()); 
		}

		RequestContext.getCurrentInstance().execute("moveToPositionInitial('mainForm:" + panel.getId()+ "','mainForm:" + element.getFormId() + "',"+ element.getPositionX() + "," + element.getPositionY()+ ");");
		executeJS_setElementStyleAndValue(element);
	}
	
	private String createOnClickScript(CardElement element) {
		String coordinatesTarget = "this";
		if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE) || element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
			coordinatesTarget = "this.parentNode";
		}
		String oncClickScript;
		if (element.getSide().equals("1")) {
			oncClickScript = "selectElementFront(" + coordinatesTarget + ");";
		} else {
			oncClickScript = "selectElementBack(" + coordinatesTarget + ");";
		}
		return oncClickScript;
	}
	
	
	private void createFormElements(OutputPanel panel,CardElement element,String oncClickScript) {
		UIComponent component = null;
		if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE)|| element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
			OutputPanel imagePanel=WebUtil.createImagePanel(element);
			GraphicImage graphicImage = WebUtil.createImage(element,null);
			graphicImage.setOnclick(oncClickScript);
			imagePanel.getChildren().add(graphicImage);
			panel.getChildren().add(imagePanel);
			component = imagePanel;
			Resizable resizableElement = WebUtil.createResizable(element);
			component.getChildren().add(resizableElement);
		} else {
			OutputLabel formLabel = WebUtil.createLabel(element);
			formLabel.setOnclick(oncClickScript);
			component = formLabel;
			panel.getChildren().add(formLabel);
		}

		Draggable draggableElement = WebUtil.createDraggable(element);
		component.getChildren().add(draggableElement);
		element.setAddedOnForm(true);
	}
	
	
	private ElementSessionDO fetchElementMapValues(String formId) {
		Map map=fetchElementMap();
		return (ElementSessionDO)map.get("mainForm:"+formId);
	}
	
	private Map fetchElementMap() {
		Map map=(Map) WebUtil.getSession().getAttribute("elementMap");
		return map;
	}
	
	
	private String getPlainTextValue(String styleValue) {
		if (styleValue != null) {
			Document doc=Jsoup.parse(styleValue);
			Elements elements=doc.getAllElements();
			for (Element element:elements) {
				if (element.nodeName().equals("body")) {
					return element.text();
				}
			}
		}
		return styleValue;
	}
	
	public void changeDataType() {
		CardElement selectedElement=getSelectedElement();
		selectedElement.setMinCharLength(null);
		selectedElement.setMaxCharLength(null);
		selectedElement.setDateFormat(null);
		selectedElement.setRequired(true);
		if (selectedElement.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_STRING)) {
			selectedElement.setMinCharLength("3");
			selectedElement.setMaxCharLength("20");
		}
		else if (selectedElement.getDataType().equals(CardElement.ELEMENT_DATA_TYPE_DATE)) {
			selectedElement.setDateFormat("dd.MM.yyyy");
		}
		executeJS_markElementSelected(selectedElement);
	}
	
	
	private void saveTemplateElements() {

		String activeSide=getActiveSide();
		for (CardElement element:selectedCardType.getElementList()) {
			
			
			if (activeSide == null || activeSide.equals(element.getSide())) {
				
			
				System.out.println("Spremam element " + element.getSide()+", " + element.getFormId() + ", " + activeSide + ", " +oneSideActive+", " + activeBack);
				
				if (element.getStyleValue() != null) {
					element.setValue(getPlainTextValue(element.getStyleValue()).trim());
				}
				ElementSessionDO elementSessionDO=fetchElementMapValues(element.getFormId());
				element.setName(elementSessionDO.getElementName());
				element.setPositionX(elementSessionDO.getElementX());
				element.setPositionY(elementSessionDO.getElementY());
				element.setWidth(elementSessionDO.getElementWidth());
				element.setHeight(elementSessionDO.getElementHeight());
				element.setStyleValue(elementSessionDO.getElementEditor());
				if (elementSessionDO.getElementDataType() == null) {
					element.setDataType(null);
					element.setDateFormat(null);
					element.setMinCharLength(null);
					element.setMaxCharLength(null);
					element.setRequired(null);
				}

			
			}
			
			
		}
	}
	
	public void saveCardTemplate() {
		saveTemplateElements();
//		validateCardTemplate();
		DesignService.saveCardType(selectedCardType);
		OutputPanel front=WebUtil.resolveSide("1");
		OutputPanel back=WebUtil.resolveSide("2");
		front.getChildren().clear();
		back.getChildren().clear();
		WebUtil.getSession().setAttribute("elementMap", new HashMap<String,String>());
		RequestContext.getCurrentInstance().execute("clearForm()");
		loadCardTemplate();
		WebUtil.infoMessage("Tip kartice uspješno spremljen.");
	}
	
	
	public void loadCardTemplate() {
		
		OutputPanel front=WebUtil.resolveSide("1");
		OutputPanel back=WebUtil.resolveSide("2");
		front.getChildren().clear();
		back.getChildren().clear();
		Map map=(Map) WebUtil.getSession().getAttribute("elementMap");
		map.clear();
		String activeSide=getActiveSide();
		for (CardElement element:selectedCardType.getElementList()) {
			if (activeSide == null || activeSide.equals(element.getSide())) {
				element.setAddedOnForm(false);
				ElementSessionDO elementSessionDO=createSessionElementObject(element);
				map.put(elementSessionDO.getElementId(), elementSessionDO);
				addElementOnForm(element);
			}
		}
		RequestContext.getCurrentInstance().execute("clearForm()");
	}
	
	public void validateCardTemplate() {
		
		List<String> messages=new ArrayList<String>();
		for (CardElement element:selectedCardType.getElementList()) {
			CardDesignValidator.validateCardElement(messages, element);
		}
		
		for (String s:messages) {
			System.out.println("Validacija: " + s);
		}
		
	}
	
	public void deleteElement() {
		saveTemplateElements();
		if (selectedId != null && !selectedId.isEmpty()) {
			System.out.println("Brisem element: " +selectedId);
			for (CardElement element:selectedCardType.getElementList()) {
				if (("mainForm:"+element.getFormId()).equals(selectedId)) {
					selectedCardType.getElementList().remove(element);
					Map map=fetchElementMap();
					map.remove("mainForm:"+element.getFormId());
					break;
				}
			}
			setSelectedId(null);
			loadCardTemplate();
		}
	}
	

}

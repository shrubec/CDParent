package hr.cdap.web.controller;


import hr.cdap.entity.TemplateElement;
import hr.cdap.web.object.ElementSessionDO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

@ManagedBean
@ViewScoped
@SuppressWarnings({"rawtypes","unchecked"})
public class CardDesignerController {
	
	private @Getter @Setter List<TemplateElement> elementList=new ArrayList<TemplateElement>();
	private @Getter @Setter String selectedId;
	private @Getter @Setter String ime="";
	
	public CardDesignerController() {
		System.out.println("DesigrController constructor...");
		createElementMap();
		elementList=new ArrayList<TemplateElement>();
	}
	
	private void createElementMap() {
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session == null) {
			session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		}
		session.setAttribute("elementMap", new HashMap<String,String>());
	}
	
	public TemplateElement getSelectedElement() {
		for (TemplateElement element:elementList) {
			if (("mainForm:"+element.getFormId()).equals(selectedId)) {
				return element;
			}
		}
		return null;
	}
	
	private Integer fetchNumberOfElements(String type) {
		int count=0;
		for (TemplateElement element:elementList) {
			if (element.getType().equals(type)) {
				count++;
			}
		}
		return count;
	}
	
	public void addNewCardElement(String side,String type) {
		TemplateElement element=createNewElement(side, type);
		elementList.add(element);
		selectedId=("mainForm:"+element.getFormId());
		Map map=fetchElementMap();
		ElementSessionDO elementSessionDO=createSessionElementObject(element);
		map.put(elementSessionDO.getElementId(), elementSessionDO);
		for (TemplateElement e:elementList) {
			addElementOnForm(e);
		}
		executeJS_markElementSelected(element);
	}
	
	
	private TemplateElement createNewElement(String side,String type) {
		
		TemplateElement element=new TemplateElement();
		element.setFormId("cardElement"+String.valueOf(elementList.size()));
		element.setValue("");
		element.setType(type);
		element.setSide(side);
		element.setPositionX("5");
		element.setPositionY("5"); 
		
		if (type.equals(TemplateElement.ELEMENT_TYPE_LABEL)) {
			element.setName("Labela "+String.valueOf(fetchNumberOfElements(type)));
		}
		else if (type.equals(TemplateElement.ELEMENT_TYPE_FIELD)) {
			element.setName("Polje "+String.valueOf(fetchNumberOfElements(type)));
		}
		else if (type.equals(TemplateElement.ELEMENT_TYPE_IMAGE)) {
			element.setName("Slika "+String.valueOf(fetchNumberOfElements(type)));
		}
		else if (type.equals(TemplateElement.ELEMENT_TYPE_SIGNATURE)) {
			element.setName("Potpis "+String.valueOf(fetchNumberOfElements(type)));
		}
		
		if (type.equals(TemplateElement.ELEMENT_TYPE_IMAGE)) {
			element.setWidth("25");
			element.setHeight("25");
			element.setRequired(true);
		}
		else if (type.equals(TemplateElement.ELEMENT_TYPE_SIGNATURE)) {
			element.setWidth("40");
			element.setHeight("10");
			element.setRequired(true);
		}
		else if (type.equals(TemplateElement.ELEMENT_TYPE_FIELD)) {
			element.setValue("SPECIMEN "+String.valueOf(elementList.size()));
			element.setStyleValue("SPECIMEN "+String.valueOf(elementList.size()));
			element.setWidth("40");
			element.setHeight("8");
			element.setDataType(TemplateElement.ELEMENT_DATA_TYPE_STRING);
			element.setMinCharLength("3");
			element.setMaxCharLength("20");
			element.setRequired(true);
		}
		else {
			element.setValue("SPECIMEN "+String.valueOf(elementList.size()));
			element.setStyleValue("SPECIMEN "+String.valueOf(elementList.size()));
			element.setWidth("40");
			element.setHeight("8");
		}
		return element;
	}
	
	
	private ElementSessionDO createSessionElementObject(TemplateElement element) {
		
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
			if (element.getDataType().equals(TemplateElement.ELEMENT_DATA_TYPE_STRING)) {
				elementSessionDO.setElementDataType("Text");
				elementSessionDO.setElementMinChar(element.getMinCharLength());
				elementSessionDO.setElementMaxChar(element.getMaxCharLength());
			}
			else if (element.getDataType().equals(TemplateElement.ELEMENT_DATA_TYPE_NUMBER))
				elementSessionDO.setElementDataType("Broj");
			else if (element.getDataType().equals(TemplateElement.ELEMENT_DATA_TYPE_DATE)) {
				elementSessionDO.setElementDataType("Datum");
				elementSessionDO.setElementDateFormat(element.getDateFormat());
			}
			else if (element.getDataType().equals(TemplateElement.ELEMENT_DATA_TYPE_SERIAL)) {
				elementSessionDO.setElementDataType("Serijski broj");
				elementSessionDO.setElementCardNumber(element.getStartSerialNumber());
			}
		}
		return elementSessionDO;
	}
	
	
	private void executeJS_markElementSelected(TemplateElement element) {
		String selectedSide="frontSide";
		if (element.getSide().equals("2")) {
			selectedSide="backSide";
		}
		RequestContext.getCurrentInstance().execute("selectElement(document.getElementById('mainForm:"+element.getFormId()+"'),'mainForm:"+selectedSide+"')");
	}
	
	private void executeJS_setElementStyleAndValue(TemplateElement element) {
		
		if (element.getWidth() != null && !element.getWidth().isEmpty()) {
			BigDecimal width=new BigDecimal(element.getWidth());
			BigDecimal height=new BigDecimal(element.getHeight());
			width=width.divide(new BigDecimal("0.264583333"),0, RoundingMode.HALF_UP);
			height=height.divide(new BigDecimal("0.264583333"),0, RoundingMode.HALF_UP);
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.width='"+width+"px'");
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.height='"+height+"px'");
		}
		
		RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.display='inline-block'");
		if (element.getType().equals(TemplateElement.ELEMENT_TYPE_LABEL) || element.getType().equals(TemplateElement.ELEMENT_TYPE_FIELD)) {
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+element.getValue()+"'");
		}
	}
	
	private OutputPanel resolveSide(String side) {
		UIViewRoot cardForm = FacesContext.getCurrentInstance().getViewRoot();
		String selectedSide="frontSide";
		if (side.equals("2")) {
			selectedSide="backSide";
		}
		return (OutputPanel)fetchElement(cardForm,selectedSide);
	}
	
	public void refreshAdditionalForm() {
		RequestContext.getCurrentInstance().update("mainForm:elementDataPanel3");
		RequestContext.getCurrentInstance().update("mainForm:elementDataPanel4");
	}
	
	public void addElementOnForm(TemplateElement element) {

		OutputPanel panel = resolveSide(element.getSide());
		if (!element.getAddedOnForm()) {
			createFormElements(panel, element, createOnClickScript(element));
		}
		else {
			UIComponent formElement = fetchElement(panel, element.getFormId());
			if (formElement instanceof OutputLabel) {
				((OutputLabel) formElement).setValue(element.getValue());
			}
		}

		ElementSessionDO elementSessionDO = fetchElementMapValues(element.getFormId());
		if (elementSessionDO != null) {
			element.setName(elementSessionDO.getElementName());
			element.setPositionX(elementSessionDO.getElementX());
			element.setPositionY(elementSessionDO.getElementY());
			element.setWidth(elementSessionDO.getElementWidth());
			element.setHeight(elementSessionDO.getElementHeight());
			element.setValue(elementSessionDO.getElementEditor());
			element.setStyleValue(elementSessionDO.getElementEditor());
		}

		RequestContext.getCurrentInstance().execute("moveToPositionInitial('mainForm:" + panel.getId()+ "','mainForm:" + element.getFormId() + "',"+ element.getPositionX() + "," + element.getPositionY()+ ");");
		executeJS_setElementStyleAndValue(element);
	}
	
	private String createOnClickScript(TemplateElement element) {
		String coordinatesTarget = "this";
		if (element.getType().equals(TemplateElement.ELEMENT_TYPE_IMAGE) || element.getType().equals(TemplateElement.ELEMENT_TYPE_SIGNATURE)) {
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
	
	
	private void createFormElements(OutputPanel panel,TemplateElement element,String oncClickScript) {
		UIComponent component = null;
		if (element.getType().equals(TemplateElement.ELEMENT_TYPE_IMAGE)|| element.getType().equals(TemplateElement.ELEMENT_TYPE_SIGNATURE)) {
			OutputPanel imagePanel=createImagePanel(element);
			GraphicImage graphicImage = createImage(element);
			graphicImage.setOnclick(oncClickScript);
			imagePanel.getChildren().add(graphicImage);
			panel.getChildren().add(imagePanel);
			component = imagePanel;
			Resizable resizableElement = createResizable(element);
			component.getChildren().add(resizableElement);
		} else {
			OutputLabel formLabel = createLabel(element);
			formLabel.setOnclick(oncClickScript);
			component = formLabel;
			panel.getChildren().add(formLabel);
		}

		Draggable draggableElement = createDraggable(element);
		component.getChildren().add(draggableElement);
		element.setAddedOnForm(true);
	}
	
	private OutputPanel createImagePanel(TemplateElement element) {
		OutputPanel imagePanel = new OutputPanel();
		BigDecimal width = new BigDecimal(element.getWidth());
		BigDecimal height = new BigDecimal(element.getHeight());
		width = width.divide(new BigDecimal("0.264583333"), 0,RoundingMode.HALF_UP);
		height = height.divide(new BigDecimal("0.264583333"), 0,RoundingMode.HALF_UP);
		imagePanel.setId(element.getFormId());
		imagePanel.setStyle("width:" + width + "px;height:" + height+ "px;");
		return imagePanel;
	}
	
	private GraphicImage createImage(TemplateElement element) {
		GraphicImage graphicImage = new GraphicImage();
		if (element.getType().equals(TemplateElement.ELEMENT_TYPE_IMAGE)) {
			graphicImage.setValue("images/slika.jpg");
		}
		if (element.getType().equals(TemplateElement.ELEMENT_TYPE_SIGNATURE)) {
			graphicImage.setValue("images/potpis.jpg");
		}
		graphicImage.setId(element.getFormId() + "_image");
		graphicImage.setWidth("100%");
		graphicImage.setHeight("100%");
		return graphicImage;
	}
	
	private Resizable createResizable(TemplateElement element) {
		Resizable resizableElement = new Resizable();
		resizableElement.setId("resizableElement_"+ element.getFormId());
		resizableElement.setFor(element.getFormId());
		resizableElement.setAspectRatio(true);
		resizableElement.setOnResize("resizeImage('mainForm:"+ element.getFormId() + "');");
		return resizableElement;
	}
	
	private OutputLabel createLabel(TemplateElement element) {
		OutputLabel formLabel = new OutputLabel();
		formLabel.setValue(element.getValue());
		formLabel.setId(element.getFormId());
		return formLabel;
	}
	
	private Draggable createDraggable(TemplateElement element) {
		Draggable draggableElement = new Draggable();
		draggableElement.setId("draggableElement" + element.getFormId());
		draggableElement.setFor(element.getFormId());
		draggableElement.setContainment("parent");
		draggableElement.setOpacity(0.3);
		return draggableElement;
	}
	
	private ElementSessionDO fetchElementMapValues(String formId) {
		Map map=fetchElementMap();
		return (ElementSessionDO)map.get("mainForm:"+formId);
	}
	
	private Map fetchElementMap() {
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		Map map=(Map) session.getAttribute("elementMap");
		return map;
	}
	
	
	private UIComponent fetchElement(UIComponent parent,String id) {
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
		
		TemplateElement selectedElement=getSelectedElement();
		selectedElement.setMinCharLength(null);
		selectedElement.setMaxCharLength(null);
		selectedElement.setDateFormat(null);
		selectedElement.setStartSerialNumber(null);
		selectedElement.setRequired(true);
		
		if (selectedElement.getDataType().equals(TemplateElement.ELEMENT_DATA_TYPE_STRING)) {
			selectedElement.setMinCharLength("3");
			selectedElement.setMaxCharLength("20");
		}
		else if (selectedElement.getDataType().equals(TemplateElement.ELEMENT_DATA_TYPE_DATE)) {
			selectedElement.setDateFormat("dd.MM.yyyy");
		}
		else if (selectedElement.getDataType().equals(TemplateElement.ELEMENT_DATA_TYPE_SERIAL)) {
			selectedElement.setStartSerialNumber("0000000001");
			selectedElement.setRequired(null);
		}
		executeJS_markElementSelected(selectedElement);
	}
	
	
	public void saveCardTemplate() {
		
		for (TemplateElement element:elementList) {
			if (element.getStyleValue() != null) {
				element.setValue(getPlainTextValue(element.getStyleValue()).trim());
			}
			ElementSessionDO elementSessionDO=fetchElementMapValues(element.getFormId());
			element.setName(elementSessionDO.getElementName());
			element.setPositionX(elementSessionDO.getElementX());
			element.setPositionY(elementSessionDO.getElementY());
			element.setWidth(elementSessionDO.getElementWidth());
			element.setHeight(elementSessionDO.getElementHeight());
			element.setValue(elementSessionDO.getElementEditor());
			element.setStyleValue(elementSessionDO.getElementEditor());
			if (elementSessionDO.getElementDataType() == null) {
				element.setDataType(null);
				element.setDateFormat(null);
				element.setStartSerialNumber(null);
				element.setMinCharLength(null);
				element.setMaxCharLength(null);
				element.setRequired(null);
			}
		}
		
		validateCardTemplate();
		
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		List<TemplateElement> savedElementList=new ArrayList<TemplateElement>();
		savedElementList.addAll(elementList);
		session.setAttribute(ime, savedElementList);
		elementList=new ArrayList<TemplateElement>();
		OutputPanel front=resolveSide("1");
		OutputPanel back=resolveSide("2");
		front.getChildren().clear();
		back.getChildren().clear();
		session.setAttribute("elementMap", new HashMap<String,String>());
		RequestContext.getCurrentInstance().execute("clearForm()");
	}
	
	
	public void loadCardTemplate() {
		
		OutputPanel front=resolveSide("1");
		OutputPanel back=resolveSide("2");
		front.getChildren().clear();
		back.getChildren().clear();
		
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		elementList=(List<TemplateElement> ) session.getAttribute(ime);
		if (elementList == null) {
			elementList=new ArrayList<TemplateElement>();
		}
		
		HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Map map=(Map) request.getSession().getAttribute("elementMap");
		map.clear();
		
		for (TemplateElement element:elementList) {

			element.setAddedOnForm(false);
			ElementSessionDO elementSessionDO=createSessionElementObject(element);
			map.put(elementSessionDO.getElementId(), elementSessionDO);
			addElementOnForm(element);
		}
		RequestContext.getCurrentInstance().execute("clearForm()");
	}
	
	public void validateCardTemplate() {
		
//		List<String> messages=new ArrayList<String>();
//		for (TemplateElement element:elementList) {
//			CardDesignValidator.validateCardElement(messages, element);
//		}
//		
//		for (String s:messages) {
//			System.out.println("Validacija: " + s);
//		}
		
	}
	

}

package hr.cdap.web.controller;


import hr.cdap.web.object.ElementSessionDO;
import hr.cdap.web.validator.CardDesignValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
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
@SuppressWarnings("rawtypes")
public class CardDesignerController {
	
	public static final String ELEMENT_TYPE_LABEL="1";
	public static final String ELEMENT_TYPE_FIELD="2";
	public static final String ELEMENT_TYPE_IMAGE="3";
	public static final String ELEMENT_TYPE_SIGNATURE="4";
	
	public static final String ELEMENT_DATA_TYPE_STRING="1";
	public static final String ELEMENT_DATA_TYPE_NUMBER="2";
	public static final String ELEMENT_DATA_TYPE_DATE="3";
	public static final String ELEMENT_DATA_TYPE_SERIAL="4";
	
	private @Getter @Setter TempCardElement selectedCardElement;
	private @Getter @Setter List<TempCardElement> elementList=new ArrayList<TempCardElement>();
	private @Getter @Setter TempCardElement selectedElement;
	private @Getter @Setter String selectedId;
	
	private @Getter @Setter String ime="";
	
	
	public CardDesignerController() {
		System.out.println("CardDesignerController...");
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session == null) {
			session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		}
		
		session.setAttribute("elementMap", new HashMap<String,String>());
		selectedElement=new TempCardElement();
		elementList=new ArrayList<TempCardElement>();
	}
	
	
//	@PostConstruct
//	public void fetchCardElements() {
//		HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
//		System.out.println("Post construct... " + request.getParameter("template"));
//		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//		
//		elementList=(List<TempCardElement> ) session.getAttribute(request.getParameter("template"));
//		
//		System.out.println("Dohvacen element list: " +elementList );
//		
//		if (elementList == null) elementList=new ArrayList<TempCardElement>();
//		for (TempCardElement element:elementList) {
//			element.setAddedOnForm(false);
//			
//			selectedId=element.getFormId(); //ovo vjerojatno ne treba
//			
//			executeJS_markSelectedElement(element);
//			executeJS_setElementStyleAndValue(element);
//			addElementsOnForm();
//			
//		}
//		
//	}
//	
//	
	
	
	private Integer fetchNumberOfElements(String type) {
		int count=0;
		for (TempCardElement element:elementList) {
			if (element.getType().equals(type)) {
				count++;
			}
		}
		return count;
	}
	
	
	public void addNewCardElement(String side,String type) {
		
		TempCardElement element=new TempCardElement();
		element.setFormId("cardElement"+String.valueOf(elementList.size()));
		element.setValue("");
		if (type.equals(ELEMENT_TYPE_LABEL)) {
			element.setName("Labela "+String.valueOf(fetchNumberOfElements(type)));
		}
		else if (type.equals(ELEMENT_TYPE_FIELD)) {
			element.setName("Polje "+String.valueOf(fetchNumberOfElements(type)));
		}
		else if (type.equals(ELEMENT_TYPE_IMAGE)) {
			element.setName("Slika "+String.valueOf(fetchNumberOfElements(type)));
		}
		else if (type.equals(ELEMENT_TYPE_SIGNATURE)) {
			element.setName("Potpis "+String.valueOf(fetchNumberOfElements(type)));
		}
		element.setType(type);
		element.setPositionX("5");
		element.setPositionY("5"); 
		element.setSide(side);
		element.setRequired(true);
		
		if (type.equals(ELEMENT_TYPE_IMAGE)) {
			element.setWidth("25");
			element.setHeight("25");
		}
		else if (type.equals(ELEMENT_TYPE_SIGNATURE)) {
			element.setWidth("40");
			element.setHeight("10");
		}
		else {
			element.setValue("SPECIMEN");
			element.setStyleValue("SPECIMEN");
			element.setWidth("40");
			element.setHeight("8");
			element.setDataType(ELEMENT_DATA_TYPE_STRING);
			element.setMinCharLength("3");
			element.setMaxCharLength("20");
		}
		
		elementList.add(element);
		selectedId=("mainForm:"+element.getFormId());
		selectedElement=element;

		
		
		executeJS_markSelectedElement(element);
		//executeJS_setElementStyleAndValue(element);
		
		addElementsOnForm(false);
		
		
	}
	
	
	private void executeJS_markSelectedElement(TempCardElement element) {
		String selectedSide="frontSide";
		if (element.getSide().equals("2")) {
			selectedSide="backSide";
		}
		
		RequestContext.getCurrentInstance().execute("selectElement(document.getElementById('mainForm:"+element.getFormId()+"'),'mainForm:"+selectedSide+"')");
		
	}
	
	
	private void executeJS_setElementStyleAndValue(TempCardElement element) {
		
		System.out.println("WIDTH elementa " + element.getFormId()+": " + element.getWidth());
		
		if (element.getWidth() != null && !element.getWidth().isEmpty()) {
			BigDecimal width=new BigDecimal(element.getWidth());
			BigDecimal height=new BigDecimal(element.getHeight());
			width=width.divide(new BigDecimal("0.264583333"),0, RoundingMode.HALF_UP);
			height=height.divide(new BigDecimal("0.264583333"),0, RoundingMode.HALF_UP);
			
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.width='"+width+"px'");
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.height='"+height+"px'");
			
		}
		
		RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.display='inline-block'");
		
		if (element.getType().equals(ELEMENT_TYPE_LABEL) || element.getType().equals(ELEMENT_TYPE_FIELD)) {
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+element.getValue()+"'");
		}
		
		RequestContext.getCurrentInstance().execute("moveToPosition()");
	}
	
	
	private OutputPanel resolveSide(String side) {
		UIViewRoot cardForm = FacesContext.getCurrentInstance().getViewRoot();
		String selectedSide="frontSide";
		if (side.equals("2")) {
			selectedSide="backSide";
		}
		return (OutputPanel)fetchElement(cardForm,selectedSide);
	}
	
	
	public void saveFormValuesIntoObject() {
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		selectedId=(String)session.getAttribute("selectedElementId");
		for (TempCardElement element:elementList) {
			if (selectedId.equals("mainForm:"+element.getFormId())) {
				ElementSessionDO elementSessionDO=fetchElementMapValues(element.getFormId());
				element.setWidth(elementSessionDO.getElementWidth());
				element.setHeight(elementSessionDO.getElementHeight());
				element.setName(elementSessionDO.getElementName()); 
				element.setValue(elementSessionDO.getElementEditor());
				element.setStyleValue(elementSessionDO.getElementEditor());
				selectedElement=element;
				executeJS_setElementStyleAndValue(element);
				executeJS_markSelectedElement(element);
			}
		}
	}
	
	public void saveImageSizeIntoObject() {
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		selectedId=(String)session.getAttribute("selectedElementId");
		for (TempCardElement element:elementList) {
			if (selectedId.equals("mainForm:"+element.getFormId())) {
				ElementSessionDO elementSessionDO=fetchElementMapValues(element.getFormId());
				element.setWidth(elementSessionDO.getElementWidth());
				element.setHeight(elementSessionDO.getElementHeight());
			}
		}
	}
	
	public void saveFormValuesIntoObjectWithoutRefresh() {
		for (TempCardElement element:elementList) {
			if (selectedId.equals("mainForm:"+element.getFormId())) {
				ElementSessionDO elementSessionDO=fetchElementMapValues(element.getFormId());
				element.setWidth(elementSessionDO.getElementWidth());
				element.setHeight(elementSessionDO.getElementHeight());
				element.setName(elementSessionDO.getElementName()); 
				element.setValue(elementSessionDO.getElementEditor());
				element.setStyleValue(elementSessionDO.getElementEditor());
			}
		}
	}
	
	
	public void displayFormValuesFromObject() {
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		selectedId=(String)session.getAttribute("selectedElementId");
		for (TempCardElement element:elementList) {
			if (selectedId.equals("mainForm:"+element.getFormId())) {
				RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:elementEditor_input').value='"+element.getValue()+"'");
				RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:elementWidth').value="+element.getWidth());
				RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:elementHeight').value="+element.getHeight());
				RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:elementName').value='"+element.getName()+"'");
			}
		}
	}
	
	//metoda sluzi da se refresha vrijednost editora - ta komponenta se ne zeli automatski refresati kad mu se sa javascriptom promijeni vrijednost
	public void refreshEditorValueOnly() {
		for (TempCardElement element:elementList) {
			if (selectedId.equals("mainForm:"+element.getFormId())) {
				selectedElement=element;
			}
		}
		RequestContext.getCurrentInstance().update("mainForm:elementEditor");
		RequestContext.getCurrentInstance().update("mainForm:elementDataPanel1");
		RequestContext.getCurrentInstance().update("mainForm:elementDataPanel2");
		RequestContext.getCurrentInstance().update("mainForm:elementDataPanel3");
		RequestContext.getCurrentInstance().update("mainForm:elementDataPanel4");
	}
	
	
	public void addElementsOnForm(boolean savedTemplate) {
		
		for (TempCardElement element:elementList) {
			
			String coordinatesTarget="this";
			if (element.getType().equals(ELEMENT_TYPE_IMAGE)|| element.getType().equals(ELEMENT_TYPE_SIGNATURE)) {
				coordinatesTarget="this.parentNode";
			}
			String findCoordinates;
			OutputPanel panel=resolveSide(element.getSide());
			if (element.getSide().equals("1")) {
				findCoordinates="findCoordinatesFront("+coordinatesTarget+");refreshEditorValueOnly()";
			}
			else {
				findCoordinates="findCoordinatesBack("+coordinatesTarget+");refreshEditorValueOnly()";
			}
			
			if (!element.addedOnForm) {
				
				UIComponent component=null;
				
				if (element.getType().equals(ELEMENT_TYPE_IMAGE)|| element.getType().equals(ELEMENT_TYPE_SIGNATURE)) {
					
//					findCoordinates=findCoordinates+";updateImageSize("+coordinatesTarget+",'mainForm:"+element.getFormId()+"_image"+"')";
					
					OutputPanel imagePanel=new OutputPanel();
					imagePanel.setId(element.getFormId());
					
					GraphicImage graphicImage=new GraphicImage();
					if (element.getType().equals(ELEMENT_TYPE_IMAGE)) graphicImage.setValue("images/slika.jpg");
					if (element.getType().equals(ELEMENT_TYPE_SIGNATURE)) graphicImage.setValue("images/potpis.jpg");
					graphicImage.setOnclick(findCoordinates);
					graphicImage.setId(element.getFormId()+"_image");
					graphicImage.setWidth("100%");
					graphicImage.setHeight("100%");
					imagePanel.getChildren().add(graphicImage);
					panel.getChildren().add(imagePanel);
					component=imagePanel;
					
					Resizable resizableElement=new Resizable();
					resizableElement.setId("resizableElement_"+element.getFormId());
					resizableElement.setFor(element.getFormId());
					resizableElement.setAspectRatio(true);
					resizableElement.setOnResize("resizeImage('mainForm:"+element.getFormId()+"');");
					component.getChildren().add(resizableElement);
					
					
					
				}
				else {
					OutputLabel formLabel=new OutputLabel();
					formLabel.setValue(element.getValue());
					formLabel.setOnclick(findCoordinates);
					formLabel.setId(element.getFormId());
					component=formLabel;
					panel.getChildren().add(formLabel);
				}
				
				Draggable draggableElement=new Draggable();
				draggableElement.setId("draggableElement"+element.getFormId());
				draggableElement.setFor(element.getFormId());
				draggableElement.setContainment("parent");
				draggableElement.setOpacity(0.3);
				component.getChildren().add(draggableElement);
				
				element.setAddedOnForm(true);
				
			}   
			
			else {
				
				UIComponent formElement=fetchElement(panel, element.getFormId());
				if (formElement instanceof OutputLabel) {
					((OutputLabel) formElement).setValue(element.getValue());
				}

			}
			
			
//			
//			if (savedTemplate) {
//				
//				UIComponent formElement=fetchElement(panel, element.getFormId());
//				if (formElement instanceof OutputLabel) {
//					((OutputLabel) formElement).setValue(element.getValue());
//					System.out.println("Postavljena nova vrijednost... " + element.getValue());
//					
//				}
//				executeJS_setElementStyleAndValue(element);
//				
//			}
//			else {
				ElementSessionDO elementSessionDO=fetchElementMapValues(element.getFormId());
				
				if (elementSessionDO != null) {
					element.setName(elementSessionDO.getElementName());
					element.setPositionX(elementSessionDO.getElementX());
					element.setPositionY(elementSessionDO.getElementY());
					element.setWidth(elementSessionDO.getElementWidth());
					element.setHeight(elementSessionDO.getElementHeight());
					element.setValue(elementSessionDO.getElementEditor());
					element.setStyleValue(elementSessionDO.getElementEditor());
					
					UIComponent formElement=fetchElement(panel, element.getFormId());
					if (formElement instanceof OutputLabel) {
						((OutputLabel) formElement).setValue(element.getValue());
						System.out.println("Postavljena nova vrijednost... " + element.getValue());
						
					}
					executeJS_setElementStyleAndValue(element);
					
				}
				
//			}
			
			
			
			RequestContext.getCurrentInstance().execute("moveToPositionInitial('mainForm:"+panel.getId()+"','mainForm:"+element.getFormId()+"',"+element.getPositionX()+","+element.getPositionY()+");");
			
			
			if (selectedId != null && selectedId.equals("mainForm:"+element.getFormId())) {
				executeJS_markSelectedElement(element);
			}
			
			
			
		}
		
	}
	
	
	
	
	private ElementSessionDO fetchElementMapValues(String formId) {
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		Map map=(Map) session.getAttribute("elementMap");
		return (ElementSessionDO)map.get("mainForm:"+formId);
	}
	
	
	public void elementChanged(ValueChangeEvent evt) {
		System.out.println("Element changed... " + evt.getComponent().getId());
		addElementsOnForm(false);
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
	
	
	
	
	@Getter @Setter
	public class TempCardElement {
		
		private String formId;
		private String name;
		private String side;
//		private Boolean staticText; //odabir sa gumbicom na meniju
		
		private String value;
		private String styleValue;
		
		private String positionX;
		private String positionY;
		private String width;
		private String height;
		private String maxLength;
		private String type; //buduca referenca
		
		private String dataType; //buduca referenca
		private String startCardNumber; //ovo bude nekakav izbornik
		private Boolean required;
		
		private String minCharLength;
		private String maxCharLength;
		
		private String startSerialNumber;
		
		private String dateFormat;
		
		//transient
		private boolean addedOnForm=false;
		
	}
	
	
	
	public void changeDataType() {
		
		selectedElement.setMinCharLength(null);
		selectedElement.setMaxCharLength(null);
		selectedElement.setDateFormat(null);
		selectedElement.setStartCardNumber(null);
		selectedElement.setRequired(true);
		
		if (selectedElement.getDataType().equals(ELEMENT_DATA_TYPE_STRING)) {
			selectedElement.setMinCharLength("3");
			selectedElement.setMaxCharLength("20");
		}
		else if (selectedElement.getDataType().equals(ELEMENT_DATA_TYPE_DATE)) {
			selectedElement.setDateFormat("dd.MM.yyyy");
		}
		else if (selectedElement.getDataType().equals(ELEMENT_DATA_TYPE_SERIAL)) {
			selectedElement.setStartCardNumber("0000000001");
			selectedElement.setRequired(null);
		}
		
	}

	
	
	public void saveCardTemplate() {
		for (TempCardElement element:elementList) {
			if (element.getStyleValue() != null) {
				element.setValue(getPlainTextValue(element.getStyleValue()).trim());
			}
		}
		validateCardTemplate();
		
		
		//privremeni save templeta
//		saveFormValuesIntoObjectWithoutRefresh();
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		selectedElement=new TempCardElement();
		List<TempCardElement> savedElementList=new ArrayList<TempCardElement>();
		savedElementList.addAll(elementList);
		session.setAttribute(ime, savedElementList);
		System.out.println("Spremljen template " + ime);
		elementList=new ArrayList<TempCardElement>();
		OutputPanel front=resolveSide("1");
		OutputPanel back=resolveSide("2");
		front.getChildren().clear();
		back.getChildren().clear();
		session.setAttribute("elementMap", new HashMap<String,String>());
		
		
		for (TempCardElement saved:savedElementList) {
			System.out.println("SAVED ELEMENT: " + saved.getFormId()+", " + saved.getPositionX()+", " + saved.getPositionY() + ", " + saved.getWidth() + " / " + saved.getHeight());
		}
	}
	
	public void loadCardTemplate() {
		
		OutputPanel front=resolveSide("1");
		OutputPanel back=resolveSide("2");
		front.getChildren().clear();
		back.getChildren().clear();
		
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		elementList=(List<TempCardElement> ) session.getAttribute(ime);
		
		System.out.println("Dohvacen element list " +ime+": " + elementList );
		
		if (elementList == null) {
			elementList=new ArrayList<TempCardElement>();
		}
		
		
		
		for (TempCardElement saved:elementList) {
			System.out.println("LOADED ELEMENT: " + saved.getFormId()+", " + saved.getPositionX()+", " + saved.getPositionY() + ", " + saved.getWidth() + " / " + saved.getHeight());
		}
		
		
		
		for (TempCardElement element:elementList) {
			element.setAddedOnForm(false);
			selectedElement=element;
			executeJS_markSelectedElement(element);
			executeJS_setElementStyleAndValue(element);
			addElementsOnForm(true);
			
			
			/*
			ElementSessionDO elementSessionDO=new ElementSessionDO();
			elementSessionDO.setElementId(element.getFormId());
			elementSessionDO.setElementWidth(element.getWidth());
			elementSessionDO.setElementHeight(element.getHeight());
			elementSessionDO.setElementName(element.getName()); 
			elementSessionDO.setElementX(element.getPositionX());
			elementSessionDO.setElementY(element.getPositionY());
			elementSessionDO.setElementEditor(element.getStyleValue());
			
			Map map=(Map) session.getAttribute("elementMap");
			map.put(element.getFormId(), elementSessionDO);
			
			*/
			
		}
		
		
		
	}
	
	public void validateCardTemplate() {
		
		List<String> messages=new ArrayList<String>();
		for (TempCardElement element:elementList) {
			CardDesignValidator.validateCardElement(messages, element);
		}
		
		for (String s:messages) {
			System.out.println("Validacija: " + s);
		}
		
	}
	
	
}

package hr.cdap.web.view;

import hr.cdap.entity.CardElement;
import hr.cdap.entity.CardType;
import hr.cdap.web.object.ElementSessionDO;
import hr.cdap.web.util.AbstractView;
import hr.cdap.web.util.MobileImage;
import hr.cdap.web.util.MobileLabel;
import hr.cdap.web.util.WebUtil;

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

import lombok.Getter;
import lombok.Setter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
@SuppressWarnings({"rawtypes","unchecked"})
public class MobileDesignView extends AbstractView{

	private @Getter @Setter List<String> images=new ArrayList<String>();
	private @Getter @Setter String activeSide;
	
	//identicno
	private @Getter @Setter String selectedId;
	
	private @Getter @Setter Boolean editPhase=false;
	
//	private  @Getter @Setter ElementSessionDO selectedElement;
	
	private  @Getter @Setter CardElement selectedElement;
	
	private @Getter @Setter String elementText;
	
	@PostConstruct
	public void init() {
		activeSide="1";
		createElementMap();
		selectedCardType=new CardType();
		images.add("template_1_front.jpg");
    	images.add("template_1_back.jpg");
    	images.add("template_2_front.jpg");
    	images.add("template_2_back.jpg");
    	images.add("template_3_front.jpg");
    	images.add("template_3_back.jpg");
    	images.add("template_4_front.jpg");
    	images.add("template_4_back.jpg");
    	images.add("template_5_front.jpg");
    	images.add("template_5_back.jpg");
    	images.add("template_6_front.jpg");
    	images.add("template_6_back.jpg");
    	images.add("template_7_front.jpg");
    	images.add("template_7_back.jpg");
    	images.add("template_8_front.jpg");
    	images.add("template_8_back.jpg");
    	images.add("template_9_front.jpg");
    	images.add("template_9_back.jpg");
    	images.add("template_10_front.jpg");
    	images.add("template_10_back.jpg");
	}
	
	public void showDetails() {
		System.out.println("Detalji za " + selectedId);
		
		for (CardElement element:selectedCardType.getElementList()) {
			if (("mainForm:"+element.getFormId()).equals(selectedId)) {
				selectedElement=element;
				elementText=selectedElement.getStyleValue();
				
				System.out.println("Postavljen style value: " + selectedElement.getStyleValue()+", " + selectedElement.getValue());
				
			}
		}
		
//		selectedElement=(ElementSessionDO)fetchElementMap().get(selectedId);
//		elementText=selectedElement.getElementEditor();
		
		
		editPhase=true;
//		saveCardTemplate();
	}
	
	public void saveDetails() {
		editPhase=false;
		
		System.out.println("Selected element id: " + selectedId+", postavljen tekst: " + elementText );
		
//		System.out.println("Selected element pozicija: " + selectedElement.getElementX() + " - " + selectedElement.getElementY());
		
		selectedElement.setStyleValue(elementText);
		
//		saveCardTemplate();
		loadCardTemplate();
		
//		addNewCardElement("1", "1");
		
	}

	public void showMain() {
		editPhase=false;
		loadCardTemplate();
	}
	
	private void createElementMap() {
		WebUtil.getSession().setAttribute("elementMap", new HashMap<String,String>());
	}
	
	
	public String selectBackgroundImage() {
		String selectedBackground=WebUtil.getParameter("selectedImage");
		if (activeSide.equals("1"))
			selectedCardType.setImageFront(selectedBackground);
		else
			selectedCardType.setImageBack(selectedBackground);
		return "pm:first";
	}
	
	public void changeSide() {
		System.out.println("Active side: " + activeSide);
//		Tab activeTab = event.getTab();
//		if(activeTab.getId().equals("tabFront"))
//			 activeSide="1";
//		else
//			 activeSide="2";
	}
	
	
	
	//element selected zakomentiran
	public void addNewCardElement(String side,String type) {
		
//		if (!selectedCardType.getElementList().isEmpty())
//			saveCardTemplate();
		
		System.out.println("Novi element na formu...");
		
		CardElement element=createNewElement(side, type);
		selectedCardType.getElementList().add(element);
		selectedId=("mainForm:"+element.getFormId());
		Map map=fetchElementMap();
		ElementSessionDO elementSessionDO=createSessionElementObject(element);
		map.put(elementSessionDO.getElementId(), elementSessionDO);
		
		System.out.println("Session objekt stavljenu mapu: " + elementSessionDO.getElementId()+", " + elementSessionDO);
		
		for (CardElement e:selectedCardType.getElementList()) {
			addElementOnForm(e);
		}

		executeJS_markElementSelected(element);
		
	}
	
	
	
	//identicna
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
			element.setWidth("25");
			element.setHeight("25");
			element.setRequired(true);
		}
		else if (type.equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
			element.setWidth("40");
			element.setHeight("10");
			element.setRequired(true);
		}
		else if (type.equals(CardElement.ELEMENT_TYPE_FIELD)) {
			element.setValue("SPECIMEN "+String.valueOf(selectedCardType.getElementList().size()));
			element.setStyleValue("SPECIMEN "+String.valueOf(selectedCardType.getElementList().size()));
			element.setWidth("40");
			element.setHeight("8");
			element.setDataType(CardElement.ELEMENT_DATA_TYPE_STRING);
			element.setMinCharLength("3");
			element.setMaxCharLength("20");
			element.setRequired(true);
		}
		else {
			element.setValue("SPECIMEN "+String.valueOf(selectedCardType.getElementList().size()));
			element.setStyleValue("SPECIMEN "+String.valueOf(selectedCardType.getElementList().size()));
			element.setWidth("40");
			element.setHeight("8");
		}
		return element;
	} 

	//identicna
	private Integer fetchNumberOfElements(String type) {
		int count=0;
		for (CardElement element:selectedCardType.getElementList()) {
			if (element.getType().equals(type)) {
				count++;
			}
		}
		return count;
	}
	
	//identicna
	private Map fetchElementMap() {
		Map map=(Map) WebUtil.getSession().getAttribute("elementMap");
		return map;
	}
	
	//razlika u IDu
	private ElementSessionDO fetchElementMapValues(String formId) {
		Map map=fetchElementMap();
		return (ElementSessionDO)map.get(formId);
	}
	
	//razlika u IDu
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
	
	//mobile label
	private void createFormElements(OutputPanel panel,CardElement element,String oncClickScript) {
		UIComponent component = null;
		if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE)|| element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
		
			/*
			OutputPanel imagePanel=WebUtil.createImagePanel(element);
			GraphicImage graphicImage = WebUtil.createImage(element,null);
//			graphicImage.setOnclick(oncClickScript);
			imagePanel.getChildren().add(graphicImage);
			panel.getChildren().add(imagePanel);
			component = imagePanel;
			Resizable resizableElement = WebUtil.createResizable(element);
			component.getChildren().add(resizableElement);
			*/
			
//			
//			GraphicImage graphicImage = WebUtil.createImage(element,null);
//			graphicImage.setOnclick(oncClickScript);
//			panel.getChildren().add(graphicImage);
//			
//			Resizable resizableElement = WebUtil.createResizable(element);
//			graphicImage.getChildren().add(resizableElement);
//			
			
//			MobileImage
			
			MobileImage graphicImage = new MobileImage(); 
			if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE)) {
				graphicImage.setValue("resources/images/person.png");
			}
			else if (element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
				graphicImage.setValue("resources/images/signature.png");
			}
			graphicImage.setId(element.getFormId());
			graphicImage.setWidth("100%");
			graphicImage.setHeight("100%");
			graphicImage.setOnclick(oncClickScript);
			panel.getChildren().add(graphicImage);
			
		} else {
//			OutputLabel formLabel = WebUtil.createLabel(element);
			
			MobileLabel formLabel = new MobileLabel();
			formLabel.setValue(element.getStyleValue());
			formLabel.setId(element.getFormId());
			
			
			formLabel.setOnclick(oncClickScript);
			
			component = formLabel;
			panel.getChildren().add(formLabel);
		}

//		Draggable draggableElement = WebUtil.createDraggable(element);
//		component.getChildren().add(draggableElement);
		element.setAddedOnForm(true);
	}
	
	//identicna
	public void addElementOnForm(CardElement element) {

		OutputPanel panel = WebUtil.resolveSide(element.getSide());
		
		System.out.println("Parent panel: " + panel);
		
		if (!element.getAddedOnForm()) {
			createFormElements(panel, element, createOnClickScript(element));
		}
		else {
			UIComponent formElement = WebUtil.fetchElement(panel, element.getFormId());
			if (formElement instanceof OutputLabel) {
				((OutputLabel) formElement).setValue(element.getStyleValue());
			}
		}

		ElementSessionDO elementSessionDO = fetchElementMapValues("mainForm:"+element.getFormId());
		
		System.out.println("Session objekt se vadi iz forme " + "mainForm:"+element.getFormId()+": " + elementSessionDO);
		
		if (elementSessionDO != null) {
			element.setName(elementSessionDO.getElementName());
			element.setPositionX(elementSessionDO.getElementX());
			element.setPositionY(elementSessionDO.getElementY());
			element.setWidth(elementSessionDO.getElementWidth());
			element.setHeight(elementSessionDO.getElementHeight());
//			if (elementSessionDO.getElementEditor() != null) {
//				element.setValue(getPlainTextValue(elementSessionDO.getElementEditor()).trim());
//			}
//			element.setStyleValue(elementSessionDO.getElementEditor()); 
		}

		//kao pocetak se uzima pozicija taba a ne panela unutar taba
		RequestContext.getCurrentInstance().execute(" $(function() { $( '#"+element.getFormId()+"' ).draggable();  });");
		
		
		RequestContext.getCurrentInstance().execute("var theElement = document.getElementById('"+element.getFormId()+"'); theElement.addEventListener('touchend', handlerFunction, false); function handlerFunction(event) {"+createOnClickScript(element)+" }");
		
		//element za pomak je bez prefiksa - cilja se njegov div element koji ima fiksno postavljen id
		
		RequestContext.getCurrentInstance().execute("moveToPositionInitial('mainForm:frontSide','" +element.getFormId() + "',"+ element.getPositionX() + "," + element.getPositionY()+ ");");
		executeJS_setElementStyleAndValue(element);
		
	}
	
	//coordinatesTarget = "this.parentNode"; !!
	private String createOnClickScript(CardElement element) {
		String coordinatesTarget = "this";
//		if (element.getType().equals(CardElement.ELEMENT_TYPE_IMAGE) || element.getType().equals(CardElement.ELEMENT_TYPE_SIGNATURE)) {
//			coordinatesTarget = "this.parentNode";
//		}
		
//		coordinatesTarget = "this.parentNode";
		
		String oncClickScript;
		if (element.getSide().equals("1")) {
			oncClickScript = "selectElementFront(" + coordinatesTarget + ");";
		} else {
			oncClickScript = "selectElementBack(" + coordinatesTarget + ");";
		}
		return oncClickScript;
	}
	
	//dodano first:mainForm
	private void executeJS_markElementSelected(CardElement element) {
		String selectedSide="frontSide";
		if (element.getSide().equals("2")) {
			selectedSide="backSide";
		}
		RequestContext.getCurrentInstance().execute("selectElement(document.getElementById('mainForm:"+element.getFormId()+"'),'mainForm:"+selectedSide+"')");
	}
	
	//identicna
	private void executeJS_setElementStyleAndValue(CardElement element) {
		
//		WebUtil.executeJS_setElementStyle(element);
		
		if (element.getWidth() != null && !element.getWidth().isEmpty()) {
			BigDecimal width=new BigDecimal(element.getWidth());
			BigDecimal height=new BigDecimal(element.getHeight());
			width=width.divide(new BigDecimal("0.264583333"),0, RoundingMode.HALF_UP);
			height=height.divide(new BigDecimal("0.264583333"),0, RoundingMode.HALF_UP);
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.width='"+width+"px'");
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.height='"+height+"px'");
		}
		
		RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').style.display='inline-block'");
		
		
		if (element.getType().equals(CardElement.ELEMENT_TYPE_LABEL) || element.getType().equals(CardElement.ELEMENT_TYPE_FIELD)) {
			RequestContext.getCurrentInstance().execute("document.getElementById('mainForm:"+element.getFormId()+"').innerHTML='"+element.getStyleValue()+"'");
		}
	}

	//identicna
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
	
	//modificirana
	public void saveCardTemplate() {
		
		
		
		for (CardElement element:selectedCardType.getElementList()) {
			if (element.getStyleValue() != null) {
				element.setValue(getPlainTextValue(element.getStyleValue()).trim());
			}
			
			
			ElementSessionDO elementSessionDO=fetchElementMapValues("mainForm:"+element.getFormId());
			
			System.out.println("Dohvacam element iz mape " + "mainForm:"+element.getFormId()+": " + elementSessionDO);

			System.out.println("XY: " + elementSessionDO.getElementX()+" - "+elementSessionDO.getElementY());
			
			element.setName(elementSessionDO.getElementName());
			element.setPositionX(elementSessionDO.getElementX());
			element.setPositionY(elementSessionDO.getElementY());
			element.setWidth(elementSessionDO.getElementWidth());
			element.setHeight(elementSessionDO.getElementHeight());
			element.setStyleValue(elementSessionDO.getElementEditor());
			
			System.out.println("New editor value: " + element.getStyleValue());
			
		}
//		validateCardTemplate();
		
//		WebUtil.getSession().setAttribute("elementMap", new HashMap<String,String>());
//		loadCardTemplate();
	}
	
	
	
	
	//modificirana
	public void loadCardTemplate() {
		
		OutputPanel panel=WebUtil.resolveSide(activeSide);
		panel.getChildren().clear();
//		Map map=(Map) WebUtil.getSession().getAttribute("elementMap");
//		map.clear();
		for (CardElement element:selectedCardType.getElementList()) {
			element.setAddedOnForm(false);
			
//			ElementSessionDO elementSessionDO=createSessionElementObject(element);
//			map.put(elementSessionDO.getElementId(), elementSessionDO);
			
			addElementOnForm(element);
		}
	}
}


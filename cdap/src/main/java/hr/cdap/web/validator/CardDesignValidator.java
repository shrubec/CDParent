package hr.cdap.web.validator;

import hr.cdap.entity.TemplateElement;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class CardDesignValidator {

	public static void validateCardElement(List<String> messages,TemplateElement element) {
		
		validateFieldRequired(messages, element.getFormId(), element.getName(), "Naziv elementa",  element.getName());
		
		validateFieldRequired(messages, element.getFormId(), element.getName(), "Pozicija X",  element.getPositionX());
		validateFieldDecimalNumber(messages, element.getFormId(), element.getName(), "Pozicija X",  element.getPositionX());
		
		validateFieldRequired(messages, element.getFormId(), element.getName(), "Pozicija Y",  element.getPositionY());
		validateFieldDecimalNumber(messages, element.getFormId(), element.getName(), "Pozicija Y",  element.getPositionY());
		
		validateFieldRequired(messages, element.getFormId(), element.getName(), "Širina polja",  element.getWidth());
		validateFieldWholeNumber(messages, element.getFormId(), element.getName(), "Širina polja",  element.getWidth());
		
		validateFieldRequired(messages, element.getFormId(), element.getName(), "Visina polja",  element.getHeight());
		validateFieldWholeNumber(messages, element.getFormId(), element.getName(), "Visina polja",  element.getHeight());
		
		if (element.getType().equals(TemplateElement.ELEMENT_TYPE_LABEL) || element.getType().equals(TemplateElement.ELEMENT_TYPE_FIELD)) {
			validateFieldRequired(messages, element.getFormId(), element.getValue(), "Statièki / inicijalni tekst",  element.getValue());
		}
		if (element.getType().equals(TemplateElement.ELEMENT_TYPE_FIELD)) {
			if (element.getDataType().equals(TemplateElement.ELEMENT_DATA_TYPE_STRING)) {
				validateFieldRequired(messages, element.getFormId(), element.getName(), "Minimalni broj znakova",  element.getMinCharLength());
				validateFieldWholeNumber(messages, element.getFormId(), element.getName(), "Minimalni broj znakova",  element.getMinCharLength());
				validateFieldRequired(messages, element.getFormId(), element.getName(), "Maksimalni broj znakova",  element.getMaxCharLength());
				validateFieldWholeNumber(messages, element.getFormId(), element.getName(), "Maksimalni broj znakova",  element.getMaxCharLength());
			}
			else if (element.getDataType().equals(TemplateElement.ELEMENT_DATA_TYPE_DATE)) {
				validateFieldRequired(messages, element.getFormId(), element.getName(), "Format datuma",  element.getDateFormat());
				validateDateFormat(messages, element.getFormId(), element.getName(), "Format datuma",  element.getDateFormat());
			}
			else if (element.getDataType().equals(TemplateElement.ELEMENT_DATA_TYPE_SERIAL)) {
				validateFieldRequired(messages, element.getFormId(), element.getName(), "Poèetni serijski broj",  element.getStartSerialNumber());
				validateDateFormat(messages, element.getFormId(), element.getName(), "Poèetni serijski broj",  element.getStartSerialNumber());
			}
		}
	}
	
	
	private static void validateFieldRequired(List<String> messages,String formId, String elementName,String fieldName,String value) {
		if (value.isEmpty()) messages.add("Element " + formId+"("+elementName+"): " +fieldName +" je obavezno polje");
	}
	
	private static void validateFieldWholeNumber(List<String> messages,String formId, String elementName,String fieldName,String value) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			messages.add("Element " + formId+"("+elementName+"): " +fieldName +" je neispravno ("+value+"), treba biti cijeli broj");
		}
	}
	
	private static void validateFieldDecimalNumber(List<String> messages,String formId, String elementName,String fieldName,String value) {
		try {
			new BigDecimal(value);
		} catch (Exception e) {
			messages.add("Element " + formId+"("+elementName+"): " +fieldName +" je neispravno ("+value+"), treba biti broj");
		}
		
	}
	
	private static void validateDateFormat(List<String> messages,String formId, String elementName,String fieldName,String value) {
		try {
			new SimpleDateFormat(value);
		} catch (Exception e) {
			messages.add("Element " + formId+"("+elementName+"): " +fieldName +" je neispravan ("+value+"), primjer ispravne vrijednost: dd.MM.yyyy");
		}
		
	}
}

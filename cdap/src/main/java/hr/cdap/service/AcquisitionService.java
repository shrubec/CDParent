package hr.cdap.service;

import hr.cdap.entity.CardData;
import hr.cdap.entity.CardElement;

import java.util.ArrayList;
import java.util.List;


public class AcquisitionService {

	
	
	public static List<CardData> prepareNewCardData(List<CardElement> elementList) {
		
		List<CardData> list=new ArrayList<CardData>();
		for (CardElement element:elementList) {
			if (!element.getType().equals(CardElement.ELEMENT_TYPE_LABEL)) {
				CardData data=new CardData();
				data.setCardElement(element);
				list.add(data);
			}
		}
		return list;
	}
	
	
}

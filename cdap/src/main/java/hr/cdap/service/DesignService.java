package hr.cdap.service;

import hr.cdap.entity.CardElement;
import hr.cdap.entity.CardType;
import hr.cdap.web.util.WebUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class DesignService {

	
	public static List<CardType> fetchCardTypes() {
		List<CardType> list= (List<CardType>) WebUtil.getSession().getAttribute("cardTypeList");
		if (list==null) {
			list=new ArrayList<CardType>();
			WebUtil.getSession().setAttribute("cardTypeList",list);
		}
		return list;
	}
	
	public static void saveCardType(CardType cardType) {
		cardType.setId(fetchCardTypes().size()+1);
		List<CardElement> elementList= (List<CardElement>) WebUtil.getSession().getAttribute("elementList");
		if (elementList==null) {
			elementList=new ArrayList<CardElement>();
			WebUtil.getSession().setAttribute("elementList",elementList);
		}
		for (CardElement element:cardType.getElementList()) {
			element.setId(elementList.size()+1);
			elementList.add(element);
		}
		
	}
	
	public static List<CardElement> fetchElementsForCardType(CardType type) {
		List<CardElement> list= (List<CardElement>) WebUtil.getSession().getAttribute("elementList");
		if (list==null) {
			list=new ArrayList<CardElement>();
			WebUtil.getSession().setAttribute("elementList",list);
		}
		else {
			List<CardElement> newList=new ArrayList<CardElement>();
			for (CardElement element:list) {
				if (element.getCardType().equals(type)) {
					newList.add(element);
				}
			}
			return newList;
		}
		return list;
	}
}

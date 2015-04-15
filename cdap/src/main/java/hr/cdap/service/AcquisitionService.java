package hr.cdap.service;

import hr.cdap.entity.Card;
import hr.cdap.entity.CardData;
import hr.cdap.entity.CardElement;
import hr.cdap.entity.CardType;
import hr.cdap.web.util.WebUtil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;


public class AcquisitionService {

	
	
	public static void prepareNewCardData(Card card,List<CardElement> elementList) {
		
		List<CardData> dataList=(List<CardData>)WebUtil.getSession().getAttribute("dataList");
		if (dataList == null) {
			dataList=new ArrayList<CardData>();
			WebUtil.getSession().setAttribute("dataList",dataList);
		}
		
		List<CardData> list=new ArrayList<CardData>();
		for (CardElement element:elementList) {
			if (!element.getType().equals(CardElement.ELEMENT_TYPE_LABEL)) {
				CardData data=new CardData();
				data.setId(dataList.size()+1);
				data.setCardElement(element);
				data.setCard(card);
				list.add(data);
				dataList.add(data);
			}
		}
		
		
	}
	
	public static List<CardData> fetchDataForCard(Card card) {
		List<CardData> dataList=(List<CardData>)WebUtil.getSession().getAttribute("dataList");
		return fetchDataForCard(dataList, card);
	}
	
	public static List<CardData> fetchDataForCard(HttpSession session,Card card) {
		List<CardData> dataList=(List<CardData>)session.getAttribute("dataList");
		return fetchDataForCard(dataList, card);
	}
	
	private static List<CardData> fetchDataForCard(List<CardData> dataList,Card card) {
		List<CardData> newList=new ArrayList<CardData>();
		if (dataList != null) {
			for (CardData data:dataList) {
				if (data.getCard().equals(card)) {
					newList.add(data);
				}
			}
		}
		return newList;
	}
	 
	public static List<Card> fetchCardByType(CardType cardType) {
		List<Card> list=(List<Card>)WebUtil.getSession().getAttribute("cardList");
		List<Card> newList=new ArrayList<Card>();
		if (list != null) {
			for (Card card:list) {
				if (card.getCardType().equals(cardType)) {
					newList.add(card);
				}
			}
		}
		return newList;
	}

	public static void saveCard(Card card) {
		List<Card> list=(List<Card>)WebUtil.getSession().getAttribute("cardList");
		if (list == null) {
			list=new ArrayList<Card>();
			WebUtil.getSession().setAttribute("cardList", list);
		}
		card.setId(list.size()+1);
		card.setCardNumber("000000"+String.valueOf(card.getId()));
		list.add(card);
	}
	
	
}

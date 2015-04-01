package hr.cdap.web.controller;

import hr.cdap.entity.CardType;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
@SuppressWarnings({"rawtypes","unchecked"})
public class AcquisitionController {
	
	private @Getter @Setter String selectedCardTypeName;
	private @Getter @Setter CardType selectedCardType;
	private @Getter @Setter List<CardType> cardTypes=new ArrayList<CardType>();
	
	
	public AcquisitionController() {
		loadTypesFromSession();
	}
	
	
	public void cardTypeSelected() {
		for (CardType cardType:cardTypes) {
			if (cardType.getName() != null && selectedCardTypeName.equals(cardType.getName())) {
				selectedCardType=cardType;
			}
		}
//		loadCardTemplate();
		
		System.out.println("Odabrani: " + selectedCardType.getName());
		
	}
	
	
	public List<SelectItem> getCardTypeItems() {
		List<SelectItem> list=new ArrayList<SelectItem> ();
		for (CardType cardType:cardTypes) {
			list.add(new SelectItem(cardType,cardType.getName()));
		}
		return list;
	}
	
	
	
	private void loadTypesFromSession() {
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session == null) {
			session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		}
		
		List<CardType> list=(List<CardType>)session.getAttribute("cardTypeList");
		if (list == null) {
			cardTypes=new ArrayList<CardType>();
		}
		else {
			cardTypes=list;
		}
	}
	
	
}

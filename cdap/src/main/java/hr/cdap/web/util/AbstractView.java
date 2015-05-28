package hr.cdap.web.util;

import lombok.Getter;
import lombok.Setter;
import hr.cdap.entity.CardType;

public class AbstractView {

	
	protected @Getter @Setter CardType selectedCardType;
	protected  @Getter @Setter Boolean oneSideActive=false;
	protected  @Getter @Setter boolean activeBack=false;
	
	public String getBackgroundImageFront() {
		if (selectedCardType == null) 
			return "";
		else {
			return "url('resources/images/background/"+selectedCardType.getImageFront()+"')";
		}
	}
	
	public String getBackgroundImageBack() {
		if (selectedCardType == null) 
			return "";
		else {
			return "url('resources/images/background/"+selectedCardType.getImageBack()+"')";
		}
	}
	
	protected String getActiveSide() {
		String activeSide=null;
		if (oneSideActive ==null) oneSideActive=false;
		if (oneSideActive == true && !activeBack) activeSide="1";
		if (oneSideActive == true && activeBack) activeSide="2";
		return activeSide;
	}
}

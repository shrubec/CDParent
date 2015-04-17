package hr.cdap.web.util;

import lombok.Getter;
import lombok.Setter;
import hr.cdap.entity.CardType;

public class AbstractView {

	
	protected @Getter @Setter CardType selectedCardType;
	
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
}

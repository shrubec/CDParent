package hr.cdap.web.object;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ElementSessionDO {

	private String elementId;
	private String elementType;
	private String elementName;
	private String elementX;
	private String elementY;
	private String elementWidth;
	private String elementHeight;
	private String elementEditor;
	
	//za dodatne podatke polja
	private String elementDataType;
	private String elementMinChar;
	private String elementMaxChar;
	private String elementDateFormat;
	private String elementRequired;
	
}

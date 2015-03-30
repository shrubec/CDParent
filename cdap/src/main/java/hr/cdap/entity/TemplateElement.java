package hr.cdap.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TemplateElement {

	public static final String ELEMENT_TYPE_LABEL="1";
	public static final String ELEMENT_TYPE_FIELD="2";
	public static final String ELEMENT_TYPE_IMAGE="3";
	public static final String ELEMENT_TYPE_SIGNATURE="4";
	
	public static final String ELEMENT_DATA_TYPE_STRING="1";
	public static final String ELEMENT_DATA_TYPE_NUMBER="2";
	public static final String ELEMENT_DATA_TYPE_DATE="3";
	public static final String ELEMENT_DATA_TYPE_SERIAL="4";
	
	
	private String formId;
	private String name;
	private String side;
	private String value;
	private String styleValue;
	private String positionX;
	private String positionY;
	private String width;
	private String height;
	private String type; 
	private String dataType; 
	private String minCharLength;
	private String maxCharLength;
	private String startSerialNumber;
	private String dateFormat;
	private Boolean required;
	
	//transient
	private Boolean addedOnForm=false;
}

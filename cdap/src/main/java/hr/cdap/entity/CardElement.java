package hr.cdap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Entity
@Table(name = "card_element")
public class CardElement {

	public static final String ELEMENT_TYPE_LABEL="1";
	public static final String ELEMENT_TYPE_FIELD="2";
	public static final String ELEMENT_TYPE_IMAGE="3";
	public static final String ELEMENT_TYPE_SIGNATURE="4";
	
	public static final String ELEMENT_DATA_TYPE_STRING="1";
	public static final String ELEMENT_DATA_TYPE_NUMBER="2";
	public static final String ELEMENT_DATA_TYPE_DATE="3";
	public static final String ELEMENT_DATA_TYPE_SERIAL="4";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="card_type")
	private CardType cardType;
	
	@Column(name = "form_id")
	private String formId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "side")
	private String side;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "style_value")
	private String styleValue;
	
	@Column(name = "position_x")
	private String positionX;
	
	@Column(name = "position_y")
	private String positionY;
	
	@Column(name = "width")
	private String width;
	
	@Column(name = "height")
	private String height;
	
	@Column(name = "type")
	private String type; 
	
	@Column(name = "data_type")
	private String dataType;
	
	@Column(name = "min_char_length")
	private String minCharLength;
	
	@Column(name = "max_char_length")
	private String maxCharLength;
	
	@Column(name = "start_serial_number")
	private String startSerialNumber;
	
	@Column(name = "date_format")
	private String dateFormat;
	
	@Column(name = "required")
	private Boolean required;
	
	@Column(name = "dynamic")
	private Boolean dynamic;
	
	
	@Transient
	private Boolean addedOnForm=false;
	
	@Transient
	private CardData cardData;
	
	
	public CardElement(CardType cardType,Boolean dynamic) {
		this.cardType=cardType;
		this.dynamic=dynamic;
	}
	
}

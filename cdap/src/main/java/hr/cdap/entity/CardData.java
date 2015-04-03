package hr.cdap.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "card_data")
public class CardData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="card_id")
	private Card card;
	
	@ManyToOne
	@JoinColumn(name="card_element_id")
	private CardElement cardElement;
	
	@Column(name="value_string")
	private String valueString;
	
	@Column(name="value_int")
	private Integer valueInt;
	
	@Column(name="value_dec")
	private BigDecimal valueDec;
	
	@Column(name="value_date")
	@Temporal(TemporalType.DATE)
	private Date valueDate;
	
	@Lob
	@Column(name="value_blob")
	private byte[] valueBlob;
	
	
}

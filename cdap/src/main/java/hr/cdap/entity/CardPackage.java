package hr.cdap.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "package")
public class CardPackage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="card_type_id")
	private CardType cardType;
	
	@ManyToOne
	@JoinColumn(name="delivery_location_id")
	private DeliveryLocation deliveryLocation;
	
	@ManyToOne
	@JoinColumn(name="user_created_id")
	private User userCreated;
	
	@Column(name = "card_number")
	private Integer cardNumber;
	
	@Column(name = "date_created")
	@Temporal(TemporalType.DATE)
	private Date dateCreated;
}

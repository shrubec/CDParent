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
@Table(name = "log")
public class Log {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="type_id")
	private LogType logType;
	
	@Column(name = "time_created")
	@Temporal(TemporalType.DATE)
	private Date timeCreated;
	
	@Column(name = "info")
	private String info;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="card_type_id")
	private CardType cardType;
	 
	@ManyToOne
	@JoinColumn(name="package_id")
	private CardPackage cardPackage;
	
	@ManyToOne
	@JoinColumn(name="card_id")
	private Card card;
	
	@ManyToOne
	@JoinColumn(name="machine_id")
	private PersoMachine machine;
}

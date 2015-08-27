package hr.cdap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "log_event")
public class LogType {

	
	public static final Integer LOG_TYPE_LOGIN=1;
	public static final Integer LOG_TYPE_LOGOUT=2;
	public static final Integer LOG_TYPE_CARD_TYPE_UPDATE=3;
	public static final Integer LOG_TYPE_CARD_UPDATE=4;
	public static final Integer LOG_TYPE_PACKAGE_UPDATE=5;
	public static final Integer LOG_TYPE_PACKAGE_PERSO=6;
	public static final Integer LOG_TYPE_PACKGAGE_PT=7;
	public static final Integer LOG_TYPE_CARD_PT=8;
	public static final Integer LOG_TYPE_LOGIN_ERROR=9;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "active")
	private Boolean active;
}

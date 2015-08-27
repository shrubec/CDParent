package hr.cdap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="client_id")
	private Client client;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "lastName")
	private String lastName;
	
	@Column(name = "active")
	private Boolean active;
	
	@Column(name = "role_design")
	private Boolean roleDesign;
	
	@Column(name = "role_acquisition")
	private Boolean roleAcquisition;
	
	@Column(name = "role_perso")
	private Boolean rolePerso;
	
	@Column(name = "role_sales")
	private Boolean roleSales;
	
	@Column(name = "role_storage")
	private Boolean roleStorage;
	
	@Column(name = "role_admin")
	private Boolean roleAdmin;
}

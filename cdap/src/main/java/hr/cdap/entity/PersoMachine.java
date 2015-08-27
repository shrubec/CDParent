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
@Table(name = "perso_machine")
public class PersoMachine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "desr")
	private String descr;
	
	@Column(name = "xml_folder")
	private String xmlFolder;
	
	@Column(name = "image_folder")
	private String imageFolder;
	
	@Column(name = "signature_folder")
	private String signatureFolder;
	
	@Column(name = "log_folder")
	private String logFolder;
	
	@Column(name = "xml_header")
	private String xmlHeader;
	
	
}

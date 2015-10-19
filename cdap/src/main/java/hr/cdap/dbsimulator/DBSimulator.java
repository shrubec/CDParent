package hr.cdap.dbsimulator;

import hr.cdap.entity.Card;
import hr.cdap.entity.CardPackage;
import hr.cdap.entity.CardType;
import hr.cdap.entity.Client;
import hr.cdap.entity.DeliveryLocation;
import hr.cdap.entity.Log;
import hr.cdap.entity.LogType;
import hr.cdap.entity.PersoMachine;
import hr.cdap.entity.User;
import hr.cdap.web.util.WebUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import lombok.Getter;
import lombok.Setter;

@Startup
@Singleton
public class DBSimulator {

	private @Getter @Setter List<Client> clientList=new ArrayList<Client>();
	private @Getter @Setter List<User> userList=new ArrayList<User>();
	private @Getter @Setter List<PersoMachine> machineList=new ArrayList<PersoMachine>();
	private @Getter @Setter List<Log> logList=new ArrayList<Log>();
	private @Getter @Setter List<CardPackage> packageList=new ArrayList<CardPackage>();
	private @Getter @Setter List<DeliveryLocation> locationList=new ArrayList<DeliveryLocation>();
	private @Getter @Setter Map<Integer,LogType> logTypeMap=new HashMap<Integer,LogType>();
	

	@PostConstruct
	public void init() {
		initializeClient();
		initializeUser();
		initializeMachine();
		initializeLogEvent();
		initializeLocationList();
	}

	public void logAction(Integer logType,User user,Card card,PersoMachine machine,CardPackage cardPackage,CardType cardType,String additionalInfo) {
		if (logTypeMap.get(logType).getActive()) {
			Log log=new Log();
			log.setId(logList.size()+1);
			log.setTimeCreated(new Date());
			log.setUser(user);
			log.setCard(card);
			log.setMachine(machine);
			log.setCardType(cardType);
			log.setCardPackage(cardPackage);
			log.setLogType(logTypeMap.get(logType));
			log.setInfo(additionalInfo);
			logList.add(log);
		}
	}
	
	public Client findClientrByName(String name) {
		for (Client client:clientList) {
			if (client.getName().equals(name)) {
				return client;
			}
		}
		return null;
	}
	
	public DeliveryLocation findLocationByName(String name) {
		for (DeliveryLocation location:locationList) {
			if (location.getName().equals(name)) {
				return location;
			}
		}
		return null;
	}
	
	public CardType findTypeByName(String name) {
		List<CardType> typeList= (List<CardType>) WebUtil.getSession().getAttribute("cardTypeList");
		for (CardType cardType:typeList) {
			if (cardType.getName().equals(name)) {
				return cardType;
			}
		}
		return null;
	}
	
	private void initializeClient() {
		
		Client c0=new Client();
		c0.setId(0);
		c0.setName("AKD");
		c0.setAddress("Savska Cesta 100, 1000 Zagreb");
		c0.setComment("Agencija za komercijalnu djelatnost");
		c0.setLogoLink("akd.jpg");
		clientList.add(c0);
		
		Client c1=new Client();
		c1.setId(1);
		c1.setName("PBZ");
		c1.setAddress("Zagrebacka 101, 1000 Zagreb");
		c1.setComment("VIP client");
		c1.setLogoLink("pbz2.jpg");
		clientList.add(c1);
		
		Client c2=new Client();
		c2.setId(2);
		c2.setName("ZABA");
		c2.setAddress("Varazdinska 5b, 1000 Zagreb");
		c2.setComment("VIP client");
		c2.setLogoLink("zaba.jpg");
		clientList.add(c2);
		
		Client c3=new Client();
		c3.setId(3);
		c3.setName("MORH");
		c3.setAddress("Zagrebacka 101, 1000 Zagreb");
		c3.setComment("");
		c3.setLogoLink("morh.png");
		clientList.add(c3);
		
		Client c4=new Client();
		c4.setId(4);
		c4.setName("Varteks");
		c4.setAddress("Zagrebacka 101, 1000 Zagreb");
		c4.setComment("Nevazni mali klijent");
		c4.setLogoLink("varteks.jpg");
		clientList.add(c4);
		
		Client c5=new Client();
		c5.setId(5);
		c5.setName("MUP");
		c5.setAddress("");
		c5.setComment("VIP client");
		c5.setLogoLink("mup.png");
		clientList.add(c5);
	}
	
	private void initializeUser() {
		User u1=new User();
		u1.setId(1);
		u1.setName("Sasa");
		u1.setLastName("Hrubec");
		u1.setUsername("admin");
		u1.setPassword("admin");
		u1.setClient(clientList.get(0));
		u1.setActive(true);
		u1.setRoleAdmin(true);
		userList.add(u1);
	
		User u2=new User();
		u2.setId(2);
		u2.setName("Marko");
		u2.setLastName("Markiæ");
		u2.setUsername("mmarkic");
		u2.setPassword("mmarkic");
		u2.setClient(clientList.get(1));
		u2.setActive(false);
		userList.add(u2);
	
		User u3=new User();
		u3.setId(3);
		u3.setName("Pero");
		u3.setLastName("Periæ");
		u3.setUsername("pperic");
		u3.setPassword("pperic");
		u3.setClient(clientList.get(2));
		u3.setActive(false);
		userList.add(u3);
		
		User u4=new User();
		u4.setId(4);
		u4.setName("Ivo");
		u4.setLastName("Ivic");
		u4.setUsername("iivic");
		u4.setPassword("iivic");
		u4.setClient(clientList.get(0));
		u4.setActive(false);
		userList.add(u4);
	}
	
	private void initializeMachine() {
		PersoMachine m1=new PersoMachine();
		m1.setName("SCP 5600");
		m1.setDescr("Veliki stroj za personalizaciju");
		m1.setImageFolder("D:\\slike");
		m1.setSignatureFolder("D:\\potpis");
		m1.setXmlFolder("D:\\xml");
		m1.setId(1);
		m1.setLogFolder("D:\\logs");
		machineList.add(m1);
		
		PersoMachine m2=new PersoMachine();
		m2.setName("SCP 60");
		m2.setDescr("Mali stroj za personalizaciju");
		m2.setImageFolder("D:\\slike");
		m2.setSignatureFolder("D:\\potpis");
		m2.setXmlFolder("D:\\xml");
		m2.setId(2);
		m2.setLogFolder("D:\\logs");
		machineList.add(m2);
		
	}
	
	private void initializeLocationList() {
		DeliveryLocation l1=new DeliveryLocation();
		l1.setId(1);
		l1.setName("Savska");
		l1.setStreet("Savska 101");
		l1.setPostal("10000");
		l1.setCity("Zagreb");
		l1.setClient(clientList.get(0));
		locationList.add(l1);
		
		DeliveryLocation l2=new DeliveryLocation();
		l2.setId(2);
		l2.setName("Radnièka");
		l2.setStreet("Radnièka 5");
		l2.setPostal("10000");
		l2.setCity("Zagreb");
		l2.setClient(clientList.get(1));
		locationList.add(l2);
		
		DeliveryLocation l3=new DeliveryLocation();
		l3.setId(3);
		l3.setName("Varaždin");
		l3.setStreet("Ludbreška 5");
		l3.setPostal("42000");
		l3.setCity("Varaždin");
		l3.setClient(clientList.get(1));
		locationList.add(l3);
	}

	private void initializeLogEvent() {
		LogType le1=new LogType();
		le1.setId(1);
		le1.setName("Prijava korisnika u sustav");
		le1.setActive(true);
		logTypeMap.put(1, le1);
		
		LogType le2=new LogType();
		le2.setId(2);
		le2.setName("Odjava korisnika iz sustava");
		le2.setActive(true);
		logTypeMap.put(2, le2);
		
		LogType le3=new LogType();
		le3.setId(3);
		le3.setName("Kreiranje / ažuriranje tipa kartice");
		le3.setActive(false);
		logTypeMap.put(3, le3);
		
		LogType le4=new LogType();
		le4.setId(4);
		le4.setName("Kreiranje / ažuriranje paketa");
		le4.setActive(false);
		logTypeMap.put(4, le4);
		
		LogType le5=new LogType();
		le5.setId(5);
		le5.setName("Kreiranje / ažuriranje kartice");
		le5.setActive(false);
		logTypeMap.put(5, le5);
		
		LogType le6=new LogType();
		le6.setId(6);
		le6.setName("Personalizacija paketa");
		le6.setActive(false);
		logTypeMap.put(6, le6);
		
		LogType le7=new LogType();
		le7.setId(7);
		le7.setName("PT paketa");
		le7.setActive(true);
		logTypeMap.put(7, le7);
		
		LogType le8=new LogType();
		le8.setId(8);
		le8.setName("PT kartice");
		le8.setActive(false);
		logTypeMap.put(8, le8);
		
		LogType le9=new LogType();
		le9.setId(9);
		le9.setName("Neuspješna prijava u sustav");
		le9.setActive(true);
		logTypeMap.put(9, le9);
	}
}

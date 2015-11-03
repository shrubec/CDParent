package hr.cdap.web.view.acquisition;

import hr.cdap.dbsimulator.DBSimulator;
import hr.cdap.entity.CardPackage;
import hr.cdap.entity.CardType;
import hr.cdap.entity.DeliveryLocation;
import hr.cdap.entity.Status;
import hr.cdap.service.LoginService;
import hr.cdap.web.util.WebUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.context.RequestContext;


@ManagedBean
@ViewScoped
public class PackageView {

	private @Getter @Setter CardPackage cardPackage;
	private @Getter @Setter List<CardPackage> packageList;
	private @Getter @Setter List<CardType> typeList;
	private @Getter @Setter List<DeliveryLocation> locationList;
	private @Getter @Setter CardType selectedType;

	private @EJB DBSimulator dbSimulator;
	private @EJB LoginService loginService;
	
	
	@PostConstruct
	private void init() {
		typeList= (List<CardType>) WebUtil.getSession().getAttribute("cardTypeList");
		packageList=dbSimulator.getPackageList();
	}
	
	public void newPackage() {
		refreshLocationList();
		cardPackage=new CardPackage();
		cardPackage.setCardNumber(0);
		cardPackage.setDateCreated(new Date());
		cardPackage.setUserCreated(loginService.getLoggedUser(WebUtil.getSession()));
		cardPackage.setStatus(dbSimulator.findStatusById(Status.PRIPREMA));
	}
	
	public void editPackage() {
		refreshLocationList();
	}
	
	public void sendToPerso() {
		cardPackage.setStatus(dbSimulator.findStatusById(Status.SPREMNO));
		WebUtil.infoMessage("Paket uspješno poslan na personalizaciju. ");
		RequestContext.getCurrentInstance().update("mainForm");
		RequestContext.getCurrentInstance().execute("PF('newPackageDialog').hide()");
	}
	
	public void savePackage() {
		cardPackage.setCardType(selectedType);
		if (cardPackage.getId()== null) {
			cardPackage.setId(dbSimulator.getPackageList().size()+1);
			dbSimulator.getPackageList().add(cardPackage);
			packageList=dbSimulator.getPackageList();
		}
		WebUtil.infoMessage("Paket uspješno spremljen. ");
		RequestContext.getCurrentInstance().update("mainForm");
		RequestContext.getCurrentInstance().execute("PF('newPackageDialog').hide()");
	}
	
	public void refreshLocationList() {
		List<DeliveryLocation> fullLocationList= dbSimulator.getLocationList();
		locationList=new ArrayList<DeliveryLocation>();
		if (selectedType == null) 
			selectedType=typeList.get(0);
		for (DeliveryLocation location:fullLocationList) {
			if (location.getClient().getId().equals(selectedType.getClient().getId())) {
				locationList.add(location);
			}
		}
	}
}

package hr.cdap.web.view.acquisition;

import hr.cdap.dbsimulator.DBSimulator;
import hr.cdap.entity.Client;
import hr.cdap.entity.DeliveryLocation;
import hr.cdap.web.util.WebUtil;

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
public class LocationView {

	private @Getter @Setter DeliveryLocation location;
	private @Getter @Setter List<Client> clientList;
	private @Getter @Setter List<DeliveryLocation> locationList;
	private @EJB DBSimulator dbSimulator;
	
	
	@PostConstruct
	private void init() {
		locationList=dbSimulator.getLocationList();
		clientList=dbSimulator.getClientList();
	}
	
	public void newLocation() {
		location=new DeliveryLocation();
	}
	
	public void saveLocation() {
		if (location.getId()== null) {
			location.setId(dbSimulator.getLocationList().size()+1);
			dbSimulator.getLocationList().add(location);
			locationList=dbSimulator.getLocationList();
		}
		WebUtil.infoMessage("Lokacija uspješno spremljena. ");
		RequestContext.getCurrentInstance().update("mainForm");
		RequestContext.getCurrentInstance().execute("PF('newLocationDialog').hide()");
	}
}

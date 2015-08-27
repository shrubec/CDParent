package hr.cdap.web.view.administration;

import hr.cdap.dbsimulator.DBSimulator;
import hr.cdap.entity.Client;
import hr.cdap.web.util.WebUtil;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class ClientView {

	private @Getter @Setter List<Client> clientList;
	private @Getter @Setter Client client;
	private @EJB DBSimulator dbSimulator;
	
	@PostConstruct
	private void init() {
		clientList=dbSimulator.getClientList();
	}
	 
	public void newClient() {
		client=new Client();
	}
	
	public void selectClient() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		for (Client c:dbSimulator.getClientList()) {
			if (c.getId().toString().equals(params.get("clientId"))) {
				client=c;
			}
		}
	}
	
	public void saveClient() {
		if (client.getId()== null) {
			client.setId(dbSimulator.getClientList().size()+1);
			dbSimulator.getClientList().add(client);
		}
		WebUtil.infoMessage("Klijent uspješno spremljen. ");
		RequestContext.getCurrentInstance().update("mainForm");
		RequestContext.getCurrentInstance().execute("PF('newClientDialog').hide()");
	}
}

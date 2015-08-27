package hr.cdap.web.view.administration;

import hr.cdap.dbsimulator.DBSimulator;
import hr.cdap.entity.Client;
import hr.cdap.entity.PersoMachine;
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
public class MachineView {

	private @Getter @Setter List<PersoMachine> machineList;
	private @Getter @Setter PersoMachine machine;
	private @EJB DBSimulator dbSimulator;
	
	@PostConstruct
	private void init() {
		machineList=dbSimulator.getMachineList();
	}
	 
	public void newMachine() {
		machine=new PersoMachine();
	}
	
	public void selectMachine() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		for (PersoMachine m:dbSimulator.getMachineList()) {
			if (m.getId().toString().equals(params.get("machineId"))) {
				machine=m;
			}
		}
	}
	
	public void saveMachine() {
		if (machine.getId()== null) {
			machine.setId(dbSimulator.getClientList().size()+1);
			dbSimulator.getMachineList().add(machine);
		}
		WebUtil.infoMessage("Perso stroj uspješno spremljen. ");
		RequestContext.getCurrentInstance().update("mainForm");
		RequestContext.getCurrentInstance().execute("PF('newMachineDialog').hide()");
	}
}

package hr.cdap.web.view;

import hr.cdap.dbsimulator.DBSimulator;
import hr.cdap.entity.Client;
import hr.cdap.service.LoginService;
import hr.cdap.web.util.WebUtil;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@ManagedBean
@ViewScoped
@SuppressWarnings({"rawtypes","unchecked"})
public class DesignMenuView {

	private @Getter @Setter List<Client> filteredClientList;
	private @Getter @Setter Client client;
	private @Getter @Setter MenuModel model;
	private @EJB DBSimulator dbSimulator;
	private @EJB LoginService loginService;
	
	@PostConstruct
	private void init() {
		filteredClientList=WebUtil.initClientList(dbSimulator.getClientList(), loginService.getLoggedUser(WebUtil.getSession()));
		createMenu();
	}
	
	
	private void createMenu() {
		model = new DefaultMenuModel();
		DefaultSubMenu firstSubmenu = new DefaultSubMenu("Vanjski klijenti");
		for (Client client : filteredClientList) {
			DefaultMenuItem item = new DefaultMenuItem(client.getName());
			item.setIcon("ui-icon-home");
			item.setUpdate("mainGrid");
			item.setCommand("#{designMenuView.setActiveClient("+client.getId().toString()+")}");
			firstSubmenu.addElement(item);
		}
		model.addElement(firstSubmenu);
	}
	
	public void setActiveClient(String id) {
		for (Client client : filteredClientList) {
			if (client.getId().toString().equals(id)) {
				WebUtil.getSession().setAttribute("activeClient", client);
			}
		}
		
	}
	
}

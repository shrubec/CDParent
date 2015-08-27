package hr.cdap.web.view.administration;

import hr.cdap.dbsimulator.DBSimulator;
import hr.cdap.entity.Client;
import hr.cdap.entity.User;
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
public class UserView {
	
	private @Getter @Setter List<Client> clientList;
	private @Getter @Setter List<User> userList;
	private @Getter @Setter User user;
	private @Getter @Setter User tableSelectUser;
	private @Getter @Setter Client client;
	private @EJB DBSimulator dbSimulator;
	
	
	
	@PostConstruct
	private void init() {
		userList=dbSimulator.getUserList();
		clientList=dbSimulator.getClientList();
	}
	
	public void newUser() {
		user=new User();
		user.setActive(true);
	}
	
	public void saveUser() {
		if (user.getId()== null) {
			user.setId(dbSimulator.getUserList().size()+1);
			dbSimulator.getUserList().add(user);
			userList=dbSimulator.getUserList();
		}
		WebUtil.infoMessage("Korisnik uspješno spremljen. ");
		RequestContext.getCurrentInstance().update("mainForm");
		RequestContext.getCurrentInstance().execute("PF('newUserDialog').hide()");
	}
	
}

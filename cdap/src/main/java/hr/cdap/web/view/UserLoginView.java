package hr.cdap.web.view;

import hr.cdap.entity.User;
import hr.cdap.service.LoginService;
import hr.cdap.web.util.WebUtil;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class UserLoginView {

	private @Getter @Setter String username;
	private @Getter @Setter String password;
	
	@EJB private LoginService loginService;

	
	public void login(ActionEvent event) {
		
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;
		boolean loggedIn = loginService.loginUser(WebUtil.getSession(),username, password);
		if (loggedIn) {
			loggedIn = true;
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Dobrodošli", username);
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Greška", "Neispravno korisnièko ime ili lozinka");
		}

		FacesContext.getCurrentInstance().addMessage(null, message);
		context.addCallbackParam("loggedIn", loggedIn);
		if (loggedIn) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.jsf");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getLogoutLink() {
		User user=loginService.getLoggedUser(WebUtil.getSession());
		if (user == null) return "";
		else return "Odjava ("+user.getName()+" "+user.getLastName()+")";
	}
	
	public void logout() {
		loginService.logoutUser(WebUtil.getSession());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

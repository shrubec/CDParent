package hr.cdap.service;

import hr.cdap.dbsimulator.DBSimulator;
import hr.cdap.entity.LogType;
import hr.cdap.entity.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpSession;

@Stateless
public class LoginService {

	private @EJB DBSimulator dbSimulator;
	
	public boolean loginUser(HttpSession session,String username,String password) {
		
		for (User user:dbSimulator.getUserList()) {
			if (username != null && username.equals(user.getUsername()) && password != null && password.equals(user.getPassword())) {
				session.setAttribute("loggedUser", user);
				dbSimulator.logAction(LogType.LOG_TYPE_LOGIN, getLoggedUser(session), null, null, null, null,null);
				return true;
			}
		}
		
		dbSimulator.logAction(LogType.LOG_TYPE_LOGIN_ERROR, null, null, null, null, null,"Korisnièko ime: "+username);
		return false;
	}
	
	public void logoutUser(HttpSession session) {
		dbSimulator.logAction(LogType.LOG_TYPE_LOGOUT, getLoggedUser(session), null, null, null, null,null);
		session.removeAttribute("loggedUser");
	}
	
	public User getLoggedUser(HttpSession session) {
		User user = (User) session.getAttribute("loggedUser");
		return user;
	}
	
}

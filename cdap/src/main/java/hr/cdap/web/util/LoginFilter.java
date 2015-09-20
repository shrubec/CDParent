package hr.cdap.web.util;

import hr.cdap.service.LoginService;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//@WebFilter(filterName = "LoginFilter", urlPatterns = { "*.xhtml", "*.jsf" })
public class LoginFilter implements Filter {

	@EJB
	LoginService loginService;

//	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

//	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) arg0;
		HttpServletResponse res = (HttpServletResponse) arg1;
		HttpSession session = req.getSession();
		String reqURI = req.getRequestURI();
		if (reqURI.indexOf("/login.jsf") >= 0 || (session != null && loginService.getLoggedUser(session) != null) || reqURI.contains("javax.faces.resource")) {
			arg2.doFilter(arg0, arg1);
		} else {
			res.sendRedirect(req.getContextPath() + "/login.jsf");
		}
	}

//	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}

package hr.cdap.web.view;

import hr.cdap.web.util.WebUtil;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class MainView {
	
	@PostConstruct
	private void init() {
		WebUtil.getSession().setAttribute("fontSize", "90%");
		WebUtil.getSession().setAttribute("oneSideActive", false);
	}

	public void setCardDimensions() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		Integer height=null;
		try {
			height = Integer.valueOf(params.get("height"));
			if (height <= 800) {
				WebUtil.getSession().setAttribute("fontSize", "70%");
				WebUtil.getSession().setAttribute("oneSideActive", true);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
}

package hr.cdap.web.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
public class AdministrationMainView {

	private @Getter @Setter Boolean renderClients=true;
	private @Getter @Setter Boolean renderUsers=false;
	private @Getter @Setter Boolean renderMachines=false;
	private @Getter @Setter Boolean renderLogTypes=false;
	private @Getter @Setter Boolean renderLogs=false;

	public void renderPage(int page) {
		renderClients=false;
		renderUsers=false;
		renderMachines=false;
		renderLogTypes=false;
		renderLogs=false;
		switch (page) {
		case 1:
			renderClients=true;
			break;
		case 2:
			renderUsers=true;
			break;
		case 3:
			renderMachines=true;
			break;
		case 4:
			renderLogTypes=true;
			break;
		case 5:
			renderLogs=true;
			break;
		default:
			break;
		}
	}
}

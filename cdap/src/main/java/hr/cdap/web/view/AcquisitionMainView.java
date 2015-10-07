package hr.cdap.web.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
public class AcquisitionMainView {

	private @Getter @Setter Boolean renderPackages=true;
	private @Getter @Setter Boolean renderLocations=false;
	private @Getter @Setter Boolean renderCards=false;
	
	public void renderPage(int page) {
		renderPackages=false;
		renderCards=false;
		renderLocations=false;
		switch (page) {
		case 1:
			renderPackages=true;
			break;
		case 2:
			renderLocations=true;
			break;
		case 3:
			renderCards=true;
			break;
		default:
			break;
		}
	}
}

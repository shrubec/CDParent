package hr.cdap.web.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class SelectBackgroundView {

	private @Getter @Setter List<String> images=new ArrayList<String>();
    
    @PostConstruct
    public void init() {
    	images.add("template_1_front.jpg");
    	images.add("template_1_back.jpg");
    	images.add("template_2_front.jpg");
    	images.add("template_2_back.jpg");
    	images.add("template_3_front.jpg");
    	images.add("template_3_back.jpg");
    	images.add("template_4_front.jpg");
    	images.add("template_4_back.jpg");
    	images.add("template_5_front.jpg");
    	images.add("template_5_back.jpg");
    	images.add("template_6_front.jpg");
    	images.add("template_6_back.jpg");
    	images.add("template_7_front.jpg");
    	images.add("template_7_back.jpg");
    	images.add("template_8_front.jpg");
    	images.add("template_8_back.jpg");
    	images.add("template_9_front.jpg");
    	images.add("template_9_back.jpg");
    	images.add("template_10_front.jpg");
    	images.add("template_10_back.jpg");
    
    }
	
	
	public void selectBackground() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String image = params.get("image");
		RequestContext.getCurrentInstance().closeDialog(image);
	}
	
}

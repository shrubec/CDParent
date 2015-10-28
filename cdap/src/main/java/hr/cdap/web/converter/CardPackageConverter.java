package hr.cdap.web.converter;

import hr.cdap.dbsimulator.DBSimulator;
import hr.cdap.entity.CardPackage;

import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@FacesConverter(value = "cardPackageConverter", forClass = CardPackage.class)
@ApplicationScoped
public class CardPackageConverter implements Converter {

	private DBSimulator dbSimulator;
	
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try {
			InitialContext ic = new InitialContext();
			dbSimulator = (DBSimulator) ic.lookup("java:module/DBSimulator");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		Object obj= dbSimulator.findPackageById(Integer.valueOf(arg2));
		return obj;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		CardPackage cardPackage = (CardPackage) arg2;
		return cardPackage.getId().toString();
	}
}

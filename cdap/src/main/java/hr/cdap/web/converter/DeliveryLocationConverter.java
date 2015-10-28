package hr.cdap.web.converter;

import hr.cdap.dbsimulator.DBSimulator;
import hr.cdap.entity.DeliveryLocation;

import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@FacesConverter(value = "deliveryLocationConverter", forClass = DeliveryLocation.class)
@ApplicationScoped
public class DeliveryLocationConverter implements Converter {

	private DBSimulator dbSimulator;

//	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try {
			InitialContext ic = new InitialContext();
			dbSimulator = (DBSimulator) ic.lookup("java:module/DBSimulator");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		Object obj= dbSimulator.findLocationByName(arg2);
		System.out.println("Convertiani objekt: " + obj);
		return obj;
	}

//	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		DeliveryLocation location = (DeliveryLocation) arg2;
		return location.getName();
	}

}

package hr.cdap.web.view.administration;

import hr.cdap.dbsimulator.DBSimulator;
import hr.cdap.entity.LogType;
import hr.cdap.entity.User;
import hr.cdap.web.util.WebUtil;

import java.util.ArrayList;
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
public class LogTypeView {

	private @Getter @Setter List<LogType> logTypeList=new ArrayList<LogType>();
	private @Getter @Setter User tableSelectLogType;
	private @EJB DBSimulator dbSimulator;
	
	
	@PostConstruct
	private void init() {
		logTypeList.addAll(dbSimulator.getLogTypeMap().values());
	}
	
	
	public void saveLogType() {
		WebUtil.infoMessage("Tip loga uspješno spremljen. ");
		RequestContext.getCurrentInstance().update("mainForm");
	}
}

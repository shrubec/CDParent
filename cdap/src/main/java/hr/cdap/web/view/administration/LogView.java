package hr.cdap.web.view.administration;

import hr.cdap.dbsimulator.DBSimulator;
import hr.cdap.entity.Log;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
public class LogView {

	private @Getter @Setter List<Log> logList;
	private @EJB DBSimulator dbSimulator;
	
	
	
	@PostConstruct
	private void init() {
		logList=dbSimulator.getLogList();
	}
}

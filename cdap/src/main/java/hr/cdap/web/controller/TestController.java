package hr.cdap.web.controller;

import hr.cdap.entity.CardType;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
public class TestController {

	
	private @Getter @Setter List<CardType> list=new ArrayList<CardType>();
	private @Getter @Setter Integer selectedId;
	
	
	public TestController() {
		CardType c1=new CardType();
		c1.setId(1);
		c1.setName("Test 1");
		
		CardType c2=new CardType();
		c2.setId(2);
		c2.setName("Test 2");
		
		CardType c3=new CardType();
		c3.setId(3);
		c3.setName("Test 3");
		
		list.add(c1);
		list.add(c2);
		list.add(c3);
		
		System.out.println("Konstruktor...");
		
	}
	
	
	
	public void promjena() {
		System.out.println("Promjena: " + selectedId);
	}
	
}

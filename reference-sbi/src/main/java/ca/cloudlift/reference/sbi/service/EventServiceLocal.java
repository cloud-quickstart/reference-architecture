package ca.cloudlift.reference.sbi.service;

import java.util.List;

import ca.cloudlift.reference.sbi.model.jpa.Event;


public interface EventServiceLocal {

	List<Event> events();
	
	Event saveUpdateEvent(Event event);
	
	void populate();

}

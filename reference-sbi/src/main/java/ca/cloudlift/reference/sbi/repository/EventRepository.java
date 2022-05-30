package ca.cloudlift.reference.sbi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import ca.cloudlift.reference.sbi.model.jpa.Event;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(collectionResourceRel = "event", path = "event")
//public interface EventRepository extends PagingAndSortingRepository<Event, Long> {//CrudRepository<Event, Long> {
public interface EventRepository extends CrudRepository<Event, Long> {

	  //List<Event> findByCategory(@Param("category") String category);
	  List<Event> findByState(String state);
	  Event findById(long id);
	  List<Event> findAll();
	}


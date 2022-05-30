package ca.cloudlift.reference.sbi.model.jpa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;

@Entity
public class Event {

  @Id
  @Column(name="ID")
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  
  private Long timestamp;
  
  private String state;
  
  //@ElementCollection
  //private Map<String, String> map = new HashMap<>();
  
  //@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
  //@JoinTable(name="EVENT_SEL",
//		  joinColumns=@JoinColumn(name="EVENT_ID", referencedColumnName="ID"),
//		  inverseJoinColumns=@JoinColumn(name="SELECT_ID", referencedColumnName="ID"))
//  private Set<Selector> selectors = new HashSet<>();
  

  protected Event() {}

  public Event(Long timestamp, String state) {
	  this.timestamp = timestamp;
	  this.state = state;

  }
  
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

/*public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public Set<Selector> getSelectors() {
		return selectors;
	}

	public void setSelectors(Set<Selector> selectors) {
		this.selectors = selectors;
	}
*/
	@Override
	public String toString() {
		return "Event [id=" + id + ", timestamp=" + timestamp + ", state=" + state //+ ", map=" + map + ", selectors="m+ selectors 
				+ "]";
	}



}

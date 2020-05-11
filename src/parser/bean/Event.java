package parser.bean;

import java.util.Date;

public class Event {

	private String id;
	private String type;
	private Actor actor;
	private Repo repo;
	private Payload payload;
	private Date created_at;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Actor getActor() {
		return actor;
	}
	public void setActor(Actor actor) {
		this.actor = actor;
	}
	public Repo getRepo() {
		return repo;
	}
	public void setRepo(Repo repo) {
		this.repo = repo;
	}
	public Payload getPayload() {
		return payload;
	}
	public void setPayload(Payload payload) {
		this.payload = payload;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	@Override
	public String toString(){
		return this.getId();
	}
	
}

package parser.bean;

public class Payload {

	private String push_id;
	private int size;
	private String ref;
	private String head;
	private String before;
	private Commit[] commits;
	
	public String getPush_id() {
		return push_id;
	}
	public void setPush_id(String push_id) {
		this.push_id = push_id;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getBefore() {
		return before;
	}
	public void setBefore(String before) {
		this.before = before;
	}
	public Commit[] getCommits() {
		return commits;
	}
	public void setCommits(Commit[] commits) {
		this.commits = commits;
	}

}

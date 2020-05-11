package parser.bean;

public class Repo {

	private String id;
	private String name;
	private String url;
	private String html_url;
	private String language;
	private boolean has_issues;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getHtml_url() {
		return html_url;
	}
	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public boolean isHas_issues() {
		return has_issues;
	}
	public void setHas_issues(boolean has_issues) {
		this.has_issues = has_issues;
	}
	
}

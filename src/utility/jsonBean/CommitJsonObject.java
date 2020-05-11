package utility.jsonBean;

public class CommitJsonObject {

	private String url;
	private FileJsonBean[] files;

	public FileJsonBean[] getFiles() {
		return files;
	}

	public void setFiles(FileJsonBean[] files) {
		this.files = files;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}

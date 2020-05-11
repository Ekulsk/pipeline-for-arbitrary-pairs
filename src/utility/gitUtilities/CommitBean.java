package utility.gitUtilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class CommitBean {

	private String commitId;
	private String authorEmail;
	private Date date;
	private String commitMessage;
	private ArrayList<String> modifiedFiles;
	private ArrayList<String> addedFiles;
	private ArrayList<String> deletedFiles;
	
	
	public String getAuthor() {
		return authorEmail;
	}
	public void setAuthor(String author) {
		this.authorEmail = author;
	}
	public ArrayList<String> getModifiedFiles() {
		return modifiedFiles;
	}
	public void setModifiedFiles(ArrayList<String> modifiedFiles) {
		this.modifiedFiles = modifiedFiles;
	}
	public ArrayList<String> getAddedFiles() {
		return addedFiles;
	}
	public void setAddedFiles(ArrayList<String> addedFiles) {
		this.addedFiles = addedFiles;
	}
	public ArrayList<String> getDeletedFiles() {
		return deletedFiles;
	}
	public void setDeletedFiles(ArrayList<String> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	public String getCommitMessage() {
		return commitMessage;
	}
	public void setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString(){
		String toReturn = this.getCommitId() + "\n" + 
			"Made by: " + this.getAuthor() + " | on: " + new SimpleDateFormat("yyyy-MM-dd").format(this.getDate()) + "\n" +
			"Message: " + this.getCommitMessage() + "\n" + 
			"Modified(" + this.getModifiedFiles().size() + ")" + 
			" | Added(" + this.getAddedFiles().size() + ") | Deleted(" + this.getDeletedFiles().size() + ")\n";
		
		return toReturn;
	}
}

package extractor;

import java.io.File;

public class FilePair {

	private File fileBefore;
	private File fileAfter;

	public FilePair(File fileBefore, File fileAfter) {
		this.fileBefore = fileBefore;
		this.fileAfter = fileAfter;
	}

	public File getFileBefore() {
		return fileBefore;
	}

	public void setFileBefore(File fileBefore) {
		this.fileBefore = fileBefore;
	}

	public File getFileAfter() {
		return fileAfter;
	}

	public void setFileAfter(File fileAfter) {
		this.fileAfter = fileAfter;
	}

}

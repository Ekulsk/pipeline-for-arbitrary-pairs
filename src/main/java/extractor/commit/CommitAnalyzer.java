package extractor.commit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import extractor.ChangeExtractor;
import extractor.FilePair;
import extractor.MethodPair;
import extractor.export.ChangeExporter;
import extractor.utility.FileUtility;
import gumtree.spoon.diff.operations.Operation;

public class CommitAnalyzer {

	
	
	
	public void analyzeCommits(String root, String out) {
		
		List<Path> commitFolders = null;
		try {
			commitFolders = Files.list(Paths.get(root))
			.filter(Files::isDirectory)
			.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		for(Path commitFolder : commitFolders) {
			System.out.println("Commit: " + commitFolder.getFileName() );
			
			String preFolder = commitFolder.toString()+"/P_dir/";
			String afterFolder = commitFolder.toString()+"/F_dir/";
			
			List<File> preJavaFiles = FileUtility.listJavaFiles(preFolder);	
			List<File> afterJavaFiles = FileUtility.listJavaFiles(afterFolder);
		
			System.out.println("Files Pre: "+preJavaFiles.size());
			System.out.println("Files After: "+afterJavaFiles.size());
			if(preJavaFiles.size() != afterJavaFiles.size()) {
				System.out.println("FILES DIFFERENT SIZE!");
			}
			List<FilePair> filePairs = generateFilePairs(preJavaFiles, afterJavaFiles);
			System.out.println("File Pairs: "+filePairs.size());
			
			if(filePairs.size() != Math.min(preJavaFiles.size(), afterJavaFiles.size())) {
				System.out.println("PAIRS DIFFERENT SIZE!");
			}
			
			int fileID = 0;
			for(FilePair fp : filePairs) {
				System.out.println("FilePair: "+fileID);
				System.out.println("FileBefore: "+fp.getFileBefore().getAbsolutePath());
				System.out.println("FileAfter: "+fp.getFileAfter().getAbsolutePath());
				System.out.println("Change Extraction...");
				ChangeExtractor extractor = new ChangeExtractor();
				Map<MethodPair, List<Operation>> changedMethods = extractor.extractChanges(fp.getFileBefore().getAbsolutePath(), fp.getFileAfter().getAbsolutePath());
				
				if(changedMethods != null && !changedMethods.isEmpty()) {
					System.out.println("Change Export...");
					ChangeExporter exporter = new ChangeExporter(changedMethods);
					String outDir = out + "/" + commitFolder.getFileName() +"/"+ fileID+"/";
					exporter.exportChanges(outDir);
					fileID++;
				}

			}
		}
		
	}
	
	
	
	public List<FilePair> generateFilePairs(List<File> preJavaFiles, List<File> afterJavaFiles) {
		
		Set<File> selected = new HashSet<>();
		List<FilePair> filePairs = new ArrayList<>();
		
		for(File p : preJavaFiles) {
			for(File f : afterJavaFiles) {
				if(f.getName().equals(p.getName()) && !selected.contains(f)) {
					FilePair fp = new FilePair(p, f);
					selected.add(f);
					filePairs.add(fp);
					break;
				}
			}
		}
		
		
		return filePairs;
	}
	
	
}

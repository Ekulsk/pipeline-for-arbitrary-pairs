package extractor.main;

import extractor.commit.CommitAnalyzer;

public class Main {

	public static void main(String[] args) {

		System.out.println("Change Extractor");
		System.out.println("Argument List:");
		System.out.println("1. Root folder");
		System.out.println("2. Out folder");
		
		String root = args[0];
		String out = args[1];
		
		System.out.println("Root Folder: "+root);
		System.out.println("Out Folder: "+out);

		CommitAnalyzer analyzer = new CommitAnalyzer();
		
		analyzer.analyzeCommits(root, out);
	}

}

package extractor.main;

import extractor.commit.CommitAnalyzer;

public class Main {

	public static void main(String[] args) {

		System.out.println("Change Extractor");
		System.out.println("Argument List:");
		System.out.println("1. Root folder");
		System.out.println("2. Out folder");
		System.out.println("3. (optional) Output csv");
		
		String root = args[0];
		String out = args[1];
		String csv = "";
		
		
		System.out.println("Root Folder: "+root);
		System.out.println("Out Folder: "+out);

		if (args.length > 2)
		{
			csv=args[2];
			System.out.println("Out csv: "+csv);
		}

		CommitAnalyzer analyzer = new CommitAnalyzer();
		
		analyzer.analyzeCommits(root, out, csv);
	}

}

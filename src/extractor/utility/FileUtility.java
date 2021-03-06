package extractor.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileUtility {


	public static void writeLines(String outPath, List<String> lines) {
		try {
			Files.write(Paths.get(outPath), lines);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}


	public static List<String> readLines(String filepath){
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(filepath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

	public static List<File> listJavaFiles(String dirPath) {
		//Path Matcher for java files (recursive) 
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.java");

		List<File> files = null;
		try {
			files = Files.walk(Paths.get(dirPath))
					.filter(Files::isRegularFile)
					.filter(p -> matcher.matches(p))
					.map(Path::toFile)
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return files;
	}
	
	public static List<File> listJavaFiles(Path dirPath) {
		//Path Matcher for java files (recursive) 
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.java");

		List<File> files = null;
		try {
			files = Files.walk(dirPath)
					.filter(Files::isRegularFile)
					.filter(p -> matcher.matches(p))
					.map(Path::toFile)
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return files;
	}


	public static List<File> listJARFiles(String dirPath) {
		//Path Matcher for java files (recursive) 
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.jar");

		List<File> files = null;
		try {
			files = Files.walk(Paths.get(dirPath))
					.filter(Files::isRegularFile)
					.filter(p -> matcher.matches(p))
					.map(Path::toFile)
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return files;
	}



	public static List<File> listClassFiles(String dirPath) {
		//Path Matcher for java files (recursive) 
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.class");

		List<File> files = null;
		try {
			files = Files.walk(Paths.get(dirPath))
					.filter(Files::isRegularFile)
					.filter(p -> matcher.matches(p))
					.map(Path::toFile)
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return files;
	}





	public static String[] readDirectories(String rootDirectory) {
		File file = new File(rootDirectory);
		String[] directories = file.list(new FilenameFilter() {
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});


		Arrays.sort(directories, Collator.getInstance());
		return directories;
	}


	public static String readFile(String nomeFile) throws IOException {
		InputStream is = null;
		InputStreamReader isr = null;

		StringBuffer sb = new StringBuffer();
		char[] buf = new char[1024];
		int len;

		try {
			is = new FileInputStream(nomeFile);
			isr = new InputStreamReader(is);

			while ((len = isr.read(buf)) > 0)
				sb.append(buf, 0, len);

			return sb.toString();
		} finally {
			if (isr != null)
				isr.close();
		}
	}

	public static String[] getLines(String filePath){
		String content = "";

		try {
			content = readFile(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String lines[] = content.split("\\r?\\n");

		return lines;
	}

	public static String getClassFromSrcMLstring(String srcMLstring, String start, String end){

		int countClass = 0;

		String toReturn = "";

		Pattern newLine = Pattern.compile("\n");
		String[] lines = newLine.split(srcMLstring);

		for(int i=0; i<lines.length; i++){
			if(lines[i].contains(start)){
				countClass++;
				toReturn+=lines[i];
			}
			if(lines[i].contains(end)){
				countClass--;
				toReturn+=lines[i];
				if(countClass == 0)
					return toReturn;

			} else {
				toReturn+=lines[i];
			}
		}

		return null;

	}


	public void copyDirectory(File srcPath, File dstPath) throws IOException {

		if (srcPath.isDirectory()) {

			if (!dstPath.exists()) {

				dstPath.mkdir();

			}

			String files[] = srcPath.list();

			for (int i = 0; i < files.length; i++) {
				copyDirectory(new File(srcPath, files[i]), new File(dstPath,
						files[i]));

			}

		}

		else {

			if (!srcPath.exists()) {

				System.out.println("File or directory does not exist.");

				System.exit(0);

			}

			else

			{

				InputStream in = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath);
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];

				int len;

				while ((len = in.read(buf)) > 0) {

					out.write(buf, 0, len);

				}

				in.close();

				out.close();

			}

		}

		System.out.println("Directory copied.");

	}

	public static boolean DelDir(File dir)
	{
		if (dir.isDirectory())
		{
			String[] contenuto = dir.list();
			for (int i=0; i<contenuto.length; i++)
			{
				boolean success = DelDir(new File(dir, contenuto[i]));
				if (!success) { return false; }
			}
		}
		return dir.delete();
	}

	public static void writeFile(String pContent, String pPath){
		File file=new File(pPath);
		FileWriter fstream;
		try {
			fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(pContent);
			out.close();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}


	public static Vector<File> listRepositoryDataFiles(File pDirectory) {
		Vector<File> gitRepoDataFiles=new Vector<File>(); 
		File[] fList = pDirectory.listFiles();

		if(fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					if(file.getName().contains(".data")) {
						gitRepoDataFiles.add(file);
					}
				} else if (file.isDirectory()) {
					File directory = new File(file.getAbsolutePath());
					gitRepoDataFiles.addAll(listRepositoryDataFiles(directory));
				}
			}
		}
		return gitRepoDataFiles;
	}

	public static Vector<File> listIssueFiles(File pDirectory) {
		Vector<File> gitRepoDataFiles=new Vector<File>(); 
		File[] fList = pDirectory.listFiles();

		if(fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					if(file.getName().contains("_issues")) {
						gitRepoDataFiles.add(file);
					}
				} else if (file.isDirectory()) {
					File directory = new File(file.getAbsolutePath());
					gitRepoDataFiles.addAll(listIssueFiles(directory));
				}
			}
		}
		return gitRepoDataFiles;
	}

	@SuppressWarnings("resource")
	public static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if (!sourceFile.exists()) {
			return;
		}
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		FileChannel source = null;
		FileChannel destination = null;
		source = new FileInputStream(sourceFile).getChannel();
		destination = new FileOutputStream(destFile).getChannel();
		if (destination != null && source != null) {
			destination.transferFrom(source, 0, source.size());
		}
		if (source != null) {
			source.close();
		}
		if (destination != null) {
			destination.close();
		}

	}

	public static String convertMouthFromString(String pMounth) {

		if(pMounth.equals("Jan"))
			return "01";
		else if(pMounth.equals("Feb"))
			return "02";
		else if(pMounth.equals("Mar"))
			return "03";
		else if(pMounth.equals("Apr"))
			return "04";
		else if(pMounth.equals("May"))
			return "05";
		else if(pMounth.equals("Jun"))
			return "06";
		else if(pMounth.equals("Jul"))
			return "07";
		else if(pMounth.equals("Aug"))
			return "08";
		else if(pMounth.equals("Sep"))
			return "09";
		else if(pMounth.equals("Oct"))
			return "10";
		else if(pMounth.equals("Nov"))
			return "11";
		else return "12";

	}

	public static String readFileToString(String filePath) throws IOException {

		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		reader.close();

		return  fileData.toString();	
	}


	public static void appendToFile(File pFile, String pContent) {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(pFile, true);
			PrintWriter writer = new PrintWriter(fileWriter);

			writer.println(pContent);
			fileWriter.close();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

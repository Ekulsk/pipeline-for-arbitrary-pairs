package extractor.export;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;

import extractor.MethodPair;
import gumtree.spoon.builder.SpoonGumTreeBuilder;
import gumtree.spoon.diff.operations.Operation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;

public class ChangeExporter {
	
	private static final String SIGNATURE = "signature.txt";
	private static final String METHOD_BEFORE = "before.java";
	private static final String METHOD_AFTER = "after.java";
	private static final String OPERATIONS = "operations.txt";
	
	
	private Map<MethodPair, List<Operation>> changedMethods;
	
	public ChangeExporter(Map<MethodPair, List<Operation>> changedMethods) {
		this.changedMethods = changedMethods;
	}
	
	public void filterMethods() {
		//TODO Implement some filtering techniques
	}
	
	public void exportChanges(String outDir, String csv) {
		
		int id = 0;
		for(Entry<MethodPair, List<Operation>> e : changedMethods.entrySet()) {
			//Create directory
			String out = outDir + "/" + id + "/";
			createDir(out);
			id++;
			
			exportMethodPair(e.getKey(), out);
			exportOperations(e.getValue(), out);	
			
			if (!csv.isEmpty())
			{
				try {//The commented out lines are single-line versions of the same concept.
					Files.write(Paths.get(csv), (outDir+",!beforeMethodStart<").getBytes(), StandardOpenOption.APPEND);
					CtMethod beforeMethod = e.getKey().getMethodBefore();
					CtMethod afterMethod = e.getKey().getMethodAfter();
					/*	List<String> linesBefore = Files.readAllLines(Paths.get(out+METHOD_BEFORE));
					 *for (String b : linesBefore) {
					 *Files.write(Paths.get(csv), b.getBytes(), SandardOpenOption.APPEND);
					 *}
					 ************************************************************************/
					Files.write(Paths.get(csv), afterMethod.toString().getBytes(), StandardOpenOption.APPEND);
					Files.write(Paths.get(csv), ">,afterMethodStart<".getBytes(), StandardOpenOption.APPEND);
					Files.write(Paths.get(csv), beforeMethod.toString().getBytes(), StandardOpenOption.APPEND);
					/*List<String> linesAfter = Files.readAllLines(Paths.get(out+METHOD_AFTER));
					 * for (String a : linesAfter) {
					 *  Files.write(Paths.get(csv), a.getBytes(), StandardOpenOption.APPEND);
					 *}
					 ***********************************************************************/

					Files.write(Paths.get(csv), ">bothend!\n".getBytes(), StandardOpenOption.APPEND);
									
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}

	private void createDir(String out) {
		try {
			Files.createDirectories(Paths.get(out));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	private static void exportMethodPair(MethodPair methodPair, String out) {
		exportSignature(methodPair, out+SIGNATURE);
		exportMethod(methodPair.getMethodBefore(), out+METHOD_BEFORE);
		exportMethod(methodPair.getMethodAfter(), out+METHOD_AFTER);
	}
	
	private static void exportSignature(MethodPair methodPair, String out) {
		
		String signature = methodPair.getMethodBefore().getSignature()+"\n";
		signature += methodPair.getMethodAfter().getSignature();
		
		try {
			Files.write(Paths.get(out), signature.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void exportMethod(CtMethod method, String out) {
		try {
			Files.write(Paths.get(out), method.toString().getBytes());
		} catch (Exception e) {
			System.err.println("ERROR! Cennot print method: "+method.getSignature());
			//e.printStackTrace();
		}
	}
	
	
	private static void exportOperations(List<Operation> operations, String out) {
		
		out = out + OPERATIONS;
		List<String> printedOperations = new ArrayList<>();
		
		for(Operation op : operations) {
			printedOperations.add(toStringAction(op.getAction()));
		}
		
		try {
			Files.write(Paths.get(out), printedOperations);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String toStringAction(Action action) {
		StringBuilder stringBuilder = new StringBuilder();
		
		//Modified element
		CtElement element = (CtElement) action.getNode().getMetadata(SpoonGumTreeBuilder.SPOON_OBJECT);
		
		//Action performed
		stringBuilder.append(action.getClass().getSimpleName());

		//Element node type
		String nodeType = element.getClass().getSimpleName();
		nodeType = nodeType.substring(2, nodeType.length() - 4);
		stringBuilder.append(" ").append(nodeType);

		//Action position
		String parentType = element.getParent().getClass().getSimpleName();
		parentType = parentType.substring(2, parentType.length() - 4);
		String position = "";
		
		if (action instanceof Move) {
			CtElement elementDest = (CtElement) action.getNode().getMetadata(SpoonGumTreeBuilder.SPOON_OBJECT_DEST);
			position = " from " + parentType;
			position += " to " + elementDest.getClass().getSimpleName();
		} else {
			position = " at " + parentType;
		}
		stringBuilder.append(position);
		
		
		return stringBuilder.toString();
	}
	
	public static String toStringActionOriginal(Action action) {
		String newline = System.getProperty("line.separator");
		StringBuilder stringBuilder = new StringBuilder();

		CtElement element = (CtElement) action.getNode().getMetadata(SpoonGumTreeBuilder.SPOON_OBJECT);
		// action name
		stringBuilder.append(action.getClass().getSimpleName());

		// node type
		String nodeType = element.getClass().getSimpleName();
		nodeType = nodeType.substring(2, nodeType.length() - 4);
		stringBuilder.append(" ").append(nodeType);

		// action position
		CtElement parent = element;
		while (parent.getParent() != null && !(parent.getParent() instanceof CtPackage)) {
			parent = parent.getParent();
		}
		String position = " at ";
		if (parent instanceof CtType) {
			position += ((CtType) parent).getQualifiedName();
		}
		if (element.getPosition() != null) {
			position += ":" + element.getPosition().getLine();
		}
		if (action instanceof Move) {
			CtElement elementDest = (CtElement) action.getNode().getMetadata(SpoonGumTreeBuilder.SPOON_OBJECT_DEST);
			position = " from " + element.getParent(CtClass.class).getQualifiedName() + ":" + element.getPosition().getLine();
			position += " to " + elementDest.getParent(CtClass.class).getQualifiedName() + ":" + elementDest.getPosition().getLine();
		}
		stringBuilder.append(position).append(newline);

		// code change
		String label = element.toString();
		if (action instanceof Update) {
			CtElement elementDest = (CtElement) action.getNode().getMetadata(SpoonGumTreeBuilder.SPOON_OBJECT_DEST);
			label += " to " + elementDest.toString();
		}
		String[] split = label.split(newline);
		for (String s : split) {
			stringBuilder.append("\t").append(s).append(newline);
		}
		return stringBuilder.toString();
	}

	
}

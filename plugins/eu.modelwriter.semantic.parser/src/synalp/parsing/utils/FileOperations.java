package synalp.parsing.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import static java.nio.file.FileVisitResult.*;


public class FileOperations {
	
	public static ArrayList<String> readUncommentedLines(File file, char beginCommentChar) throws IOException {
		ArrayList<String> ret = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        while((str = in.readLine()) != null){
        	String line = str.trim();
        	if (!line.equalsIgnoreCase("")) { // Skip empty lines
        		if (line.charAt(0)!=beginCommentChar) { // Skip the commented out lines
        			ret.add(str);
        		}
        	}
        }
        in.close();
		return ret;
	}
	
	public static void writeToFile(File file, String text) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		createNewFolder(file.getParent());
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream(file), "utf-8"))) {
					writer.write(text);
					writer.close();
				  }
	}
	
	/**
	 * Creates new folder if doesn't exists. If the same foldername already exists, does nothing!!
	 * @param folderName
	 */
	public static void createNewFolder(String folderName) {
		new File(folderName).mkdirs();
	}

	
	public static void deleteFileOrFolder(File directoryName) throws IOException {
		  final Path path = directoryName.toPath();
		  Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
		    @Override public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
		      throws IOException {
		      Files.delete(file);
		      return CONTINUE;
		    }

		    @Override public FileVisitResult visitFileFailed(final Path file, final IOException e) {
		      return handleException(e);
		    }

		    private FileVisitResult handleException(final IOException e) {
		      e.printStackTrace(); // replace with more robust error handling
		      return TERMINATE;
		    }

		    @Override public FileVisitResult postVisitDirectory(final Path dir, final IOException e)
		      throws IOException {
		      if(e!=null)return handleException(e);
		      Files.delete(dir);
		      return CONTINUE;
		    }
		  });
		};
		
}

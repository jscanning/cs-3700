package project.proj1;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HuffmanInterface {
	final static Logger logger = Logger.getLogger(HuffmanInterface.class.getName());
	
	Integer maxTasksCount;
	
	String filepath;
	
	Boolean isQuiet;
	static final Integer MAX_DIFFERENT_CHARACTERS = 256;
	
	Tree huffmanTree;
	
	public static String decompressedFileExtension = ".part%d.data.decompressed";
	public static String treeFileExtension = ".part%d.tree.txt";
	public static String compressedFileExtension = ".part%d.data.compressed"; 
	
	public HuffmanInterface(String path, Integer maxTasks, Boolean isQuiet){
		this.filepath = path;
		this.maxTasksCount = maxTasks;
		this.isQuiet = isQuiet;
		if(this.isQuiet){
			logger.setLevel(Level.OFF);
		}
	}
}

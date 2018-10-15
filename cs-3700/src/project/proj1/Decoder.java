package project.proj1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.logging.Logger;

public class Decoder implements Runnable {
	private Node huffmanTree;
	private Node currentNode;
	private String filePath;
	private BitSet dataSet;
	private Integer partIndex;
	final static Logger logger = Logger.getLogger(Decoder.class.getName());
	
	public Decoder(String filePath, Integer partIndex){
		this.filePath = filePath;
		this.partIndex = partIndex;
	}
	
	private Character compute(Boolean dir){
		if(dir == Tree.leftDirection){
			currentNode = currentNode.getLeft();
		}else if(dir == Tree.rightDirection){
			currentNode = currentNode.getRight();
		}
		
		if(currentNode.isLeaf()){
			Character decodedChar = currentNode.getCharacter();
			refresh();
			return decodedChar;
		}
		return null;
	}
	
	private void refresh() {
		this.currentNode = huffmanTree;
	}
	
	public String decode(){
		logger.info(Thread.currentThread().getName() + " started decompressing.");
		StringBuilder decodedStr = new StringBuilder();
		
		for (int i = 0; i < dataSet.size(); i++) {
			Character decoded = compute(dataSet.get(i));
			if(decoded != null)
				decodedStr.append(decoded);
		}
		logger.info(Thread.currentThread().getName() + " finishing decompressing.");
		return decodedStr.toString();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		loadData();
		String decoded = decode();
		writeToFile(decoded);

	}

	private void writeToFile(String decoded) {
		logger.info(Thread.currentThread().getName() + " will flush.");
		PrintWriter out = null;
		try{
			out = new PrintWriter(new BufferedWriter(new FileWriter(String.format(filePath + HuffmanInterface.decompressedFileExtension, this.partIndex))));
			out.println(decoded);
			logger.info(Thread.currentThread().getName() + " finished flushing.");
			
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			out.close();
		}
	}

	private void loadData() {
		// TODO Auto-generated method stub
		logger.info(Thread.currentThread().getName() + " started collecting tree and file data.");
		try{
			BufferedReader reader = new BufferedReader(new FileReader(String.format(filePath + HuffmanInterface.treeFileExtension, this.partIndex)));
			
			Tree tree = new Tree();
			tree.deserialize(reader.readLine());
			huffmanTree = tree.getInnerTree();
			this.currentNode = huffmanTree;
			File file = new File(String.format(filePath + HuffmanInterface.compressedFileExtension, this.partIndex));
			
			String fileName = String.format(filePath + HuffmanInterface.compressedFileExtension, this.partIndex);
			byte[] fileData = read(fileName);
			dataSet = ByteConverter.fromByteArray(fileData);
		} catch(IOException e){
			e.printStackTrace();
		}
		logger.info(Thread.currentThread().getName() + " finished collecting data.");
		
	}

	private byte[] read(String fileName) {
		File file = new File(fileName);
		byte[] result = new byte[(int)file.length()];
		try{
			InputStream input = null;
			try{
				int totalBytesRead = 0;
				input = new BufferedInputStream(new FileInputStream(file));
				while(totalBytesRead < result.length){
					int bytesRemaining = result.length - totalBytesRead;
					int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
					if(bytesRead > 0){
						totalBytesRead += bytesRead;
					}
				}
			}
			finally{
				input.close();
			}
		}catch(IOException e){
			
		}
		return result;
	}

}
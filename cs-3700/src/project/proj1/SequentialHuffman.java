package project.proj1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

class HuffmanNode{
	final int freq;
	final char c;
	final HuffmanNode left, right;
	
	HuffmanNode(char c, int freq, HuffmanNode left, HuffmanNode right){
		this.c = c;
		this.freq = freq;
		this.left = left;
		this.right = right;
	}
	
	private boolean isLeaf(){
		assert ((left == null) && (right == null)) || ((left != null) && (right != null));
		return (left == null) && (right == null);
	}
	
	public int compareTo(HuffmanNode that){
		return this.freq - that.freq;
	}
}

class HuffmanComparator implements Comparator<HuffmanNode>{

	@Override
	public int compare(HuffmanNode o1, HuffmanNode o2) {
		return o1.freq - o2.freq;
	}
	
}

/**
 * 
 * @author Jeremy Canning
 *	Initial bytes of US Constitution Text: 45,704  bytes
 */
public class SequentialHuffman {
	
	static final String pathname = "/cs-3700/src/project/proj1/USConstitution";
	private static final int alpha = 256;
	
	public static void printCode(HuffmanNode root, String s){
		if(root.left == null && root.right == null && Character.isLetter(root.c)){
			System.out.println(root.c + ":" + s);
			return;
		}
		
		printCode(root.left, s + "0");
		printCode(root.right, s + "1");
	}
	
	public static void compress() throws IOException{
		String str = new String(Files.readAllBytes(Paths.get(pathname)), StandardCharsets.UTF_8);
		char[] input = str.toCharArray();
		
		int[] freq = new int[alpha];
		for (int i = 0; i < freq.length; i++) {
			freq[input[i]]++;
		}
		
		HuffmanNode root = buildHuffTree(freq);
		
		String[] strArr = new String[alpha];
		buildCode(strArr, root, "");
		
		writeHuffTree(root);
		
		for (int i = 0; i < input.length; i++) {
			String code = strArr[input[i]];
			for(int j = 0; j < code.length(); j++){
				if(code.charAt(j) == '0'){
					
				}
				else if(code.charAt(j) == '1'){
					
				}
				else throw new IllegalStateException("Illegal State");
			}
		}
		
		
	}
	
	private static HuffmanNode buildHuffTree(int[] freq) {
		HuffmanComparator hufComp = new HuffmanComparator();
		PriorityBlockingQueue<HuffmanNode> pq = new PriorityBlockingQueue<HuffmanNode>(alpha, hufComp);
		for(char i = 0; i < alpha; i++)
			if(freq[i] > 0)
				pq.add(new HuffmanNode(i, freq[i], null, null));
		
		if(pq.size() == 1){
			if(freq['\0'] == 0) pq.offer(new HuffmanNode('\0', 0, null, null));
			else 				pq.offer(new HuffmanNode('\1', 0, null, null));
		}
		
		while(pq.size() > 1){
			
		}
		return null;
	}

	private static void buildCode(String[] strArr, HuffmanNode root, String string) {
		// TODO Auto-generated method stub
		
	}

	private static void writeHuffTree(HuffmanNode root) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args){
		
	}
	
}

package rockPaperScissors;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

public class Test {
	public static String programName = "/cs-3700/src/rockPaperScissors/Player.java";
	public static String arg0 = "228.5.6.7";
	public static String arg1 = "6789";
	
	public static void main(String[] args) {
		
		String workingDir = System.getProperty("user.dir");
		System.out.println("Working dir: " + System.getProperty("user.dir"));
		Scanner kb = new Scanner(System.in);
		System.out.println("Enter number of games to play: ");
		String arg2 = String.valueOf(kb.nextInt());
		String[] exec_cmd = {"java", "-cp", "bin", "Player", arg0, arg1, arg2};

		try {
			String[] cmd = {"javac.exe", "Player.java"};
			ProcessBuilder compile = new ProcessBuilder(cmd);
			compile.directory(new File(workingDir));
			//System.out.println(compile.directory().getAbsolutePath());
			Process p = compile.start();
			p.waitFor();
			p.destroy();
		} catch (IOException | InterruptedException e) {
			System.out.println("Error");
			e.printStackTrace();
		}finally{
			try{
			ProcessBuilder pb = new ProcessBuilder(exec_cmd);
			pb.directory(new File(workingDir));
			pb.inheritIO();
			System.out.println(Arrays.toString(pb.command().toArray()));
			//System.out.println(pb.directory().getAbsolutePath());
			Process[] players = {pb.start(), pb.start(), pb.start()};
			for(Process player : players) {
				player.waitFor();
			}
			}catch(IOException | InterruptedException e){
				e.printStackTrace();
			}
		}
		kb.close();
	}

}

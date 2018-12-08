package rockPaperScissors;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		try {
			Runtime.getRuntime().exec("javac C:/Users/Jeremy Canning/git/cs-3700/cs-3700/src/rockPaperScissors/Player.java");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String programName = "/cs-3700/src/rockPaperScissors/Player.java";
		String arg0 = "228.5.6.7";
		String arg1 = "6789";
		Scanner kb = new Scanner(System.in);
		System.out.println("Enter number of games to play: ");
		String arg2 = String.valueOf(kb.nextInt());
		String[] command = {"java.exe", "-cp", "bin", "rockPaperScissors.Player"};
		ProcessBuilder pb = new ProcessBuilder(command);
		try {
				Process[] players = {pb.inheritIO().start(), pb.start(), pb.start()};
				for(Process player : players) {
					player.waitFor();
				}
		} catch (IOException | InterruptedException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
	}

}

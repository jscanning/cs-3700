package rockPaperScissors;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Player implements PlayerInterface {
	
	private final static String DEFAULTGROUPNAME = "228.5.6.7";
	private final static int DEFAULTPORT = 6789;
	private InetAddress group;
	private MulticastSocket mySocket;
	private int port, myScore;
	private int[] otherScores = {0, 0};
	private boolean connected = false;
	
	Player(InetAddress group, int port){
		connect(group, port);
	}
	
	Player() throws UnknownHostException{
		this(InetAddress.getByName(DEFAULTGROUPNAME), DEFAULTPORT);
	}
	
	@Override
	public Hand randomRps() {
		Random rnd = new Random(System.currentTimeMillis());
		int value = rnd.nextInt(3);
		switch (value) {
			case 0: return Hand.ROCK;
			case 1: return Hand.PAPER;
			case 2: return Hand.SCISSORS;
		}
		return null;
	}

	@Override
	public boolean connect(InetAddress group, int port) {
		this.port = port;
		this.group = group;
		try {
			mySocket = new MulticastSocket(port);
			mySocket.setLoopbackMode(true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void send(Hand myMove) {
		try {
			DatagramPacket msg = new DatagramPacket(myMove.toString().getBytes(StandardCharsets.UTF_8), myMove.toString().length(), group, port);
			mySocket.send(msg);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public Hand receive() {
		byte[] buf = new byte[1000];
		DatagramPacket recv = new DatagramPacket(buf, buf.length);
		try {
			mySocket.receive(recv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String str = new String(recv.getData(), StandardCharsets.UTF_8);
		return Hand.valueOf(str);
	}

	@Override
	public void sendReady() {
		String msg = "ready";
		DatagramPacket snd = new DatagramPacket(msg.getBytes(StandardCharsets.UTF_8), msg.length(), group, port);
		try {
			mySocket.send(snd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean waitForReady() {
		int readiesReceived = 0;
		int messagesReceived = 0;
		byte buf[] = new byte[1000];
		DatagramPacket recv = new DatagramPacket(buf, buf.length);
		while(messagesReceived < 2 || readiesReceived < 2){
			try {
				mySocket.receive(recv);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String str = new String(recv.getData(), StandardCharsets.UTF_8);
			if(str.equalsIgnoreCase("ready")) {readiesReceived += 1; }
			messagesReceived += 1;
		}	
		if(readiesReceived == 2) return true;
		else return false;
	}
	
	public void play(){
		assert connected == true;
		Hand myMove = randomRps();
		send(myMove);
		Hand moveA = receive();
		Hand moveB = receive();
		myScore += evaluateMoves(myMove, moveA, moveB);
		otherScores = shareScores(myScore);
	}
	
	public void multipleGames(int numGames){
		int gamesPlayed = 0;
		while(gamesPlayed < numGames){
			sendReady();
			waitForReady();
			play();
		}
		printScores();
	}
	
	private void printScores() {
		System.out.printf("My score was: %d \n", myScore);
		System.out.printf("The other scores were: %d and %d", otherScores[0], otherScores[1]);
	}

	private int[] shareScores(int msg) {
		String strMsg = String.valueOf(msg);
		DatagramPacket pack = new DatagramPacket(strMsg.getBytes(), strMsg.length(), group, port);
		byte[] buf = new byte[256];
		DatagramPacket recv = new DatagramPacket(buf, buf.length);
		int output[] = {0, 0};
		try {
			mySocket.send(pack);
			mySocket.receive(recv);
			output[0] = Integer.parseInt(new String(recv.getData()));
			mySocket.receive(recv);
			output[1] = Integer.parseInt(new String(recv.getData()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return output;
	}

	private int evaluateMoves(Hand myMove, Hand moveA, Hand moveB){
		int pointsWon = 0;
		if(myMove.evaluate(moveA) == Result.LOSE || myMove.evaluate(moveB) == Result.LOSE)
			return pointsWon;
		if(myMove.evaluate(moveA) == Result.WIN)
			pointsWon += 1;
		if(myMove.evaluate(moveB) == Result.WIN)
			pointsWon += 1;
		return pointsWon;
	}
	
	public static void main(String[] args) {
		if(args.length == 3){
			int port = Integer.parseInt(args[1]);
			int numGames = Integer.parseInt(args[2]);
			Player player = null;
			try {
				InetAddress group = InetAddress.getByName(args[0]);
				player = new Player(group, port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			player.multipleGames(numGames);
		}else{System.out.println("No arguments");}
	}

}

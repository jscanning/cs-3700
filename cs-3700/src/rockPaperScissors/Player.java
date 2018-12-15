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
		connected = connect(group, port);
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
			System.out.println("Attempting to connect");
			mySocket = new MulticastSocket(port);
			mySocket.setInterface(group);
			mySocket.setLoopbackMode(true);
			mySocket.joinGroup(group);
			mySocket.setTimeToLive(50);
			System.out.println("Connected to port");
			//System.out.println(mySocket.getTimeToLive());
			DatagramPacket hi = new DatagramPacket("Hello".getBytes(), "Hello".length(), group, port);
			mySocket.send(hi);
			
			byte[] buf = new byte[200];
			DatagramPacket recv = new DatagramPacket(buf, buf.length);
			mySocket.receive(recv);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void sendMove(Hand myMove) {
		try {
			byte[] buf = myMove.toString().getBytes(StandardCharsets.UTF_8);
			DatagramPacket msg = new DatagramPacket(buf, buf.length, group, port);
			mySocket.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Hand receiveMove() {
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
		byte[] buf = msg.getBytes(StandardCharsets.UTF_8);
		DatagramPacket snd = new DatagramPacket(buf, buf.length, group, port);
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
		byte buf[] = new byte[200];
		DatagramPacket recv = new DatagramPacket(buf, buf.length);
		System.out.println("waitForReady: entering while loop");
		while(messagesReceived < 2){
			try {
				mySocket.receive(recv);
				System.out.println("Received: " + recv.getData().toString());
				messagesReceived += 1;
			} catch (IOException e) {
				e.printStackTrace();
			}
			String str = new String(recv.getData(), StandardCharsets.UTF_8);
			if(str.equalsIgnoreCase("ready")) {readiesReceived += 1; }
		}	
		if(readiesReceived == 2) return true;
		else return false;
	}
	
	public void play(){
		assert connected == true;
		Hand myMove = randomRps();
		System.out.println("Sending my move");
		sendMove(myMove);
		System.out.println("Waiting for other player's moves ...");
		Hand moveA = receiveMove();
		System.out.println("Received one");
		Hand moveB = receiveMove();
		System.out.println("Received both other players moves! /nEvaluating outcome ...");
		myScore += evaluateMoves(myMove, moveA, moveB);
		System.out.printf("My new score is: %d", myScore);
		otherScores = shareScores(myScore);
	}
	
	public void multipleGames(int numGames){
		assert connected = true;
		int gamesPlayed = 0;
		while(gamesPlayed < numGames){
			System.out.println("I'm ready!");
			sendReady();
			System.out.println("Waiting for other players to ready .....");
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

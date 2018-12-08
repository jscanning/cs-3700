package rockPaperScissors;

import java.net.DatagramPacket;
import java.net.InetAddress;

public interface PlayerInterface {
	
		/**
		 * Randomly picks from Rock, Paper, or Scissors
		 * @return
		 */
		Hand randomRps();
		
		/**
		 * Establishes a communication link between the other two players in the game.
		 * @return true if link was established, false otherwise
		 */
		boolean connect(InetAddress group, int port);
		
		/**
		 * Sends the choice of move to all players.
		 * @param myMove
		 */
		void send(Hand myMove);
		
		/**
		 * Receives the move of the target player.
		 * @param sender
		 * @return
		 */
		Hand receive();
		
		void sendReady();
		
		boolean waitForReady();
}

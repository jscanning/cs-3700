package rockPaperScissors;

public interface Player {
		
		/**
		 * Randomly picks from Rock, Paper, or Scissors
		 * @return
		 */
		Hand randomRps();
		
		/**
		 * Establishes a communication link between the other two players in the game.
		 * @param p1
		 * @param p2
		 * @return true if link was established, false otherwise
		 */
		boolean connectToOtherPlayers(Player p1, Player p2);
		
		/**
		 * Sends the choice of move to target player.
		 * @param myMove
		 * @param p
		 */
		void send(Hand myMove, Player target);
		
		/**
		 * Sends the choice of move to all players.
		 * @param myMove
		 */
		void broadcast(Hand myMove);
		
		/**
		 * Receives the move of the target player.
		 * @param sender
		 * @return
		 */
		Hand receive(Player sender);
		
		void nextRound();
}

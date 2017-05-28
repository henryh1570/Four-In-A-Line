package game;

import java.util.ArrayList;

public class App {

	public static void main(String[] args) {
		ArrayList<Piece> list = new ArrayList<Piece>();
		list.add(new Piece(1, 2, 1));
		list.add(new Piece(2, 2, 2));
		list.add(new Piece(3, 2, 1));
		list.add(new Piece(4, 2, 1));
		
		Gameboard game = new Gameboard(30, list);
		System.out.println(game.toString());
		int gameStatus = game.isGameOver(3, 2);
		if (gameStatus > 0) {
			System.out.println("Player wins.");
		} else if (gameStatus < 0) {
			System.out.println("Opponent wins.");
		} else {
			System.out.println("Game is not over.");
		}
		
		// Who goes first?
		
		// Max time limit 0 to 30 seconds for computer move.
	}
}

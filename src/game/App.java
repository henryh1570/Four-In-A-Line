package game;

import java.util.ArrayList;

public class App {

	public static void main(String[] args) {
		ArrayList<Piece> list = new ArrayList<Piece>();
		list.add(new Piece(1, 2, -1));
		list.add(new Piece(2, 2, -1));
		list.add(new Piece(3, 2, -1));
		list.add(new Piece(4, 2, -1));
		Gameboard game = new Gameboard(list);

		System.out.println(game.toString());
		System.out.println(game.isGameOver());
	}
}

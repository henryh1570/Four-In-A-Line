package game;

import java.util.ArrayList;
import java.util.Scanner;

public class App {

	static Scanner k = new Scanner(System.in);

	public static void main(String[] args) {
		
		System.out.println("Who goes first?\n1. Player\n2. Opponent");
		int firstTurn = -1;
		do {
			firstTurn = getNumberInput();
		} while (firstTurn > 2 || firstTurn < 1);

		System.out.println("Enter seconds allowed per CPU Move (5 to 30)");
		int timeLimit = -1;
		do {
			timeLimit = getNumberInput();
		} while (timeLimit > 30 || timeLimit < 5);		
		timeLimit *= 1000;

		Gameboard game = new Gameboard(timeLimit);
		Evaluator evaluator = new Evaluator();

		// AI's first turn?
		if (firstTurn == 2) {
			game = game.alphaBetaDecision(game, 4);
			System.out.println(game.toString());
			System.out.println(evaluator.evaluateOpponentBoard(game.getBoard()));
		}

		boolean isGameOver = false;
		while (!isGameOver) {
			Gameboard prev = new Gameboard(timeLimit);
			prev.copyBoards(game, prev);
			game = playerMove(game);
			System.out.println(game.toString());
			Piece lastMove = getLastMove(prev, game, 1);
			
			if (lastMove != null && game.isGameOver(lastMove.getX(), lastMove.getY()) > 0) {
				System.out.println("PLAYER WINS");
				isGameOver = true;
			} else {
				prev.copyBoards(game, prev);
				System.out.println("computer moves now");
				game = game.alphaBetaDecision(game, 4);
				System.out.println(game.toString());
				lastMove = getLastMove(prev, game, -1);
				if (lastMove != null && game.isGameOver(lastMove.getX(), lastMove.getY()) < 0) {
					System.out.println("CPU WINS");
					isGameOver = true;
				}
			}
		}
	}

	// Computer will evaluate via Alpha-beta Pruning w/ heuristic and cutoff.
	public static Gameboard computerMove(Gameboard game, Evaluator evaluator) {
		ArrayList<Gameboard> states = game.getNextStates(-1);
		int value = Integer.MIN_VALUE;

		for (int i = 0; i < states.size(); i++) {
			Gameboard next = states.get(i);
			int score = evaluator.evaluateOpponentBoard(next.getBoard());
			if (score > value) {
				game = next;
				value = score;
			}
		}
		return game;
	}

	// Get input and check validity. Convert char to valid move.
	public static Gameboard playerMove(Gameboard game) {
		Gameboard next = game;
		System.out.println("Enter Move: (A - H)(1 - 8)");
		Piece piece = new Piece(0,0,0);
		char row = 'X';
		int col = -1;
		do {
			String coordinates = getCoordinateInput();
			row = coordinates.charAt(0);
			col = Integer.parseInt("" + coordinates.charAt(1));
			if (!(row > 'h' || row < 'a') && !(col > 8 || col < 1)) {
				piece = new Piece(row - 97, col - 1, 1);
			}
		} while (!next.isMoveValid(piece));
		next.makeMove(piece);
		return next;
	}
	
	// Compare the previous and current state of gameboard to find what move was made.
	public static Piece getLastMove(Gameboard previous, Gameboard current, int owner) {
		int[][] prev = previous.getBoard();
		int[][] cur = current.getBoard();
		
		for (int i = 0; i < prev.length; i++) {
			for (int k = 0; k < prev.length; k++) {
				if (prev[i][k] != cur[i][k]) {
					return new Piece(i, k, owner);
				}
			}
		}
		
		return null;
	}
	
	// Makes sure player inputs valid coordinates
	public static String getCoordinateInput() {
		String str = k.nextLine();
		if (str.length() != 2) {
			return "X9";
		}
		
		try {
			Integer.parseInt("" + str.charAt(1));
			return str;
		} catch (Exception e) {
			return "X9";
		}
	}

	public static int getNumberInput() {
		try {
			return Integer.parseInt(k.nextLine());
		} catch (Exception e) {
			return -1;
		}
	}
}

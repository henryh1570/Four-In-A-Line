package game;

import java.util.ArrayList;

public class Gameboard {
	private final int[][] BOARD = new int[8][8];

	public Gameboard() {
	}

	public Gameboard(ArrayList<Piece> list) {
		for (Piece p : list) {
			BOARD[p.getX()][p.getY()] = p.getOwner();
		}
	}

	// Given a point, check the same row and column for a 4 line.
	public boolean isGameDead(int x, int y) {
		int horizontalCounter = 0;
		int verticalCounter = 0;
		for (int i = 0; i < BOARD.length; i++) {

			// Consecutive 4 returns true. Non-consecutive will reset counter.
			if (BOARD[i][y] <= 0) {
				horizontalCounter = 0;
			} else {
				horizontalCounter++;
			}

			if (horizontalCounter == 4) {
				return true;
			}

			if (BOARD[x][i] <= 0) {
				verticalCounter = 0;
			} else {
				verticalCounter++;
			}

			if (verticalCounter == 4) {
				return true;
			}
		}

		return false;
	}

	// Checks if the current state has a 4 in a row for the player.
	public boolean isGameOver() {
		for (int i = 0; i < BOARD.length; i++) {
			for (int k = 0; k < BOARD.length; k++) {

				// If within boundaries, check vertical/horizontal victory.
				if (((i + 4) < BOARD.length) && ((k + 4) < BOARD.length)) {
					if (horizontalCheck(i, k)) {
						return true;
					} else if (verticalCheck(i, k)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// Checks for 4 vertical adjacent pieces downward.
	// Only use within 4 spaces of board boundaries.
	public boolean verticalCheck(int i, int k) {
		for (int y = k; y < (k + 4); y++) {
			if (BOARD[i][y] <= 0) {
				return false;
			}
		}
		return true;
	}

	// Checks for 4 horizontal adjacent pieces to the right.
	// Only use within 4 spaces of board boundaries.
	public boolean horizontalCheck(int i, int k) {
		for (int x = i; x < (i + 4); x++) {
			if (BOARD[x][k] <= 0) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		String str = "  1 2 3 4 5 6 7 8";
		char c = 'A';
		for (int i = 0; i < BOARD.length; i++) {
			str += ("\n" + c);
			c++;
			for (int k = 0; k < BOARD.length; k++) {
				if (BOARD[i][k] == 0) {
					str += " -";
				} else if (BOARD[i][k] > 0) {
					str += " X";
				} else {
					str += " O";
				}
			}
		}
		return str;
	}
}

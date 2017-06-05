package game;

import java.util.ArrayList;

public class Gameboard {

	private final int[][] BOARD = new int[8][8];
	private final int TIME_LIMIT;

	public Gameboard(int time) {
		TIME_LIMIT = time;
	}

	public Gameboard(int time, ArrayList<Piece> list) {
		TIME_LIMIT = time;
		for (Piece p : list) {
			BOARD[p.getX()][p.getY()] = p.getOwner();
		}
	}
	
	public int[][] getBoard() {
		return BOARD;
	}

	// Given a point, check the same row and column for a 4 line.
	// Should be called right after a player makes a move.
	public int isGameOver(int x, int y) {
		int playerHorizontalCounter = 0;
		int playerVerticalCounter = 0;
		int opponentHorizontalCounter = 0;
		int opponentVerticalCounter = 0;

		// Consecutive 4 pieces returns true.
		for (int i = 0; i < BOARD.length; i++) {

			if (BOARD[i][y] != 0) {
				if (BOARD[i][y] < 0) {
					playerHorizontalCounter = 0;
					opponentHorizontalCounter++;
				} else {
					playerHorizontalCounter++;
					opponentHorizontalCounter = 0;
				}
				// Check if done.
				if (playerHorizontalCounter == 4) {
					return 1;
				} else if (opponentHorizontalCounter == 4) {
					return -1;
				}
			} else {
				// Reset counters due to break in middle.
				playerHorizontalCounter = 0;
				opponentHorizontalCounter = 0;
			}

			if (BOARD[x][i] != 0) {
				if (BOARD[x][i] < 0) {
					playerVerticalCounter = 0;
					opponentVerticalCounter++;
				} else {
					playerVerticalCounter++;
					opponentVerticalCounter = 0;
				}
				// Check if done.
				if (playerVerticalCounter == 4) {
					return 1;
				} else if (opponentVerticalCounter == 4) {
					return -1;
				}
			} else {
				// Reset counters due to break in middle.
				playerVerticalCounter = 0;
				opponentVerticalCounter = 0;
			}
		}
		return 0; // No one wins.
	}

	public boolean isMoveValid(Piece p) {
		int x = p.getX();
		int y = p.getY();
		int owner = p.getOwner();
		
		if (x > 8 || x < 0) {
			// Check X boundaries
			return false;
		} else if (y > 8 || y < 0) {
			// Check Y boundaries
			return false;
		} else if (owner == 0) {
			// Check move affiliation
			return false;
		} else if (BOARD[x][y] != 0) {
			// Check if space is open
			return false;
		} else {
			return true;
		}
	}
	
	public Gameboard makeMove(Piece p) {
		int x = p.getX();
		int y = p.getY();
		int owner = p.getOwner();
		BOARD[x][y] = owner;
		return this;
	}
	
	// Return all possible next states of a user's turn.
	public ArrayList<Gameboard> getNextStates(int owner) {
		ArrayList<Gameboard> states = new ArrayList<Gameboard>();
		for (int i = 0; i < BOARD.length; i++) {
			for (int k = 0; k < BOARD.length; k++) {
				// If there is a valid spot for next move
				if (BOARD[i][k] == 0) {
					// Copy current board and fill the move in
					Gameboard next = new Gameboard(TIME_LIMIT);
					copyBoards(this, next);
					next.BOARD[i][k] = owner;
					states.add(next);
				}
			}
		}
		return states;
	}

	public void copyBoards(Gameboard source, Gameboard target) {
		for (int i = 0; i < source.BOARD.length; i++) {
			for (int k = 0; k < source.BOARD.length; k++) {
				target.BOARD[i][k] = source.BOARD[i][k];
			}
		}
	}
	
	public int min(Gameboard game) {
		int best = Integer.MAX_VALUE;
		int index = 0;
		ArrayList<Gameboard> nextStates = game.getNextStates(1);
		Evaluator evaluator = new Evaluator();
		// Look at every next state and choose the lowest.
		for (int i = 0; i < nextStates.size(); i++) {
			Gameboard next = nextStates.get(i);
			int score = evaluator.evaluatePlayerBoard(next.getBoard());
			if (best < score) {
				index = i;
				best = score;
			}
		}
		//TODO : return the index?
		return best;
	}
	
	public int max(Gameboard game) {
		int best = Integer.MIN_VALUE;
		int index = 0;
		ArrayList<Gameboard> nextStates = game.getNextStates(-1);
		Evaluator evaluator = new Evaluator();
		// Look at every next state and choose the highest.
		for (int i = 0; i < nextStates.size(); i++) {
			Gameboard next = nextStates.get(i);
			int score = evaluator.evaluateOpponentBoard(next.getBoard());
			if (best < score) {
				index = i;
				best = score;
			}
		}
		//TODO : return the index?
		return best;
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
					str += " O";
				} else {
					str += " X";
				}
			}
		}
		return str;
	}
}

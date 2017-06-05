package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Gameboard {

	private final int[][] BOARD = new int[8][8];
	private final long TIME_LIMIT;
	private ArrayList<Gameboard> children = new ArrayList<Gameboard>();
	private Gameboard parent;
	private int hiddenValue;

	// Time in Millis
	public Gameboard(long time) {
		TIME_LIMIT = time;
	}

	public Gameboard(long time, Gameboard parent, int val) {
		this.parent = parent;
		this.TIME_LIMIT = time;
		this.hiddenValue = val;
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
					Gameboard next = new Gameboard(TIME_LIMIT, this, 0);
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

	public Gameboard alphaBetaDecision(Gameboard game, int piles) {
		long time = game.TIME_LIMIT + System.currentTimeMillis();
		// Perform the alphaBeta pruning to get desired value
		int value = maxValue(game, null, piles, Integer.MIN_VALUE, Integer.MAX_VALUE, time);

		if (System.currentTimeMillis() > time) {
			// If time ran out, sort for highest bottom node.
			Collections.sort(children, new Comparator<Gameboard>() {
				public int compare(Gameboard c1, Gameboard c2) {
					if (c1.hiddenValue > c2.hiddenValue) {
						return -1;
					} else if (c1.hiddenValue < c2.hiddenValue) {
						return 1;
					} else {
						return 0;
					}
				}
			});
			
			// Get bottom most node and trace to parent.
			Gameboard decision = children.get(0);
			while (decision.parent != null) {
				if (decision.parent == game) {
					return decision;
				} else {
					decision = decision.parent;
				}
			}
		} else {
			// Otherwise, find the child of the alphabeta value and trace to parent.
			for (Gameboard state : children) {
				if (state.hiddenValue == value) {
					Gameboard decision = state;
					while (decision.parent != null) {
						if (decision.parent == game) {
							return decision;
						} else {
							decision = decision.parent;
						}
					}
				}
			}
		}

		throw new RuntimeException("Didn't find suitable move.");
	}

	private int maxValue(Gameboard game, Gameboard parent, int piles, int alpha, int beta, long time) {
		if (System.currentTimeMillis() > time) {
			return alpha;
		}

		Evaluator evaluator = new Evaluator();
		if (piles == 0) {
			int score = evaluator.evaluateOpponentBoard(game.BOARD);
			children.add(new Gameboard(game.TIME_LIMIT, parent, score));
			return score;
		}
		int value = Integer.MIN_VALUE;
		ArrayList<Gameboard> successors = game.getNextStates(-1);
		for (int i = 0; i < successors.size(); i++) {
			value = Math.max(value, minValue(successors.get(i), game, piles - 1, alpha, beta, time));
			// Min pruning
			if (value >= beta) {
				return value;
			}
			alpha = Math.max(alpha, value);
		}
		return value;
	}

	private int minValue(Gameboard game, Gameboard parent, int piles, int alpha, int beta, long time) {
		if (System.currentTimeMillis() > time) {
			return alpha;
		}

		Evaluator evaluator = new Evaluator();
		if (piles == 0) {
			int score = evaluator.evaluatePlayerBoard(game.BOARD);
			children.add(new Gameboard(game.TIME_LIMIT, parent, score));
			return score;
		}
		int value = Integer.MAX_VALUE;
		ArrayList<Gameboard> successors = game.getNextStates(1);
		for (int i = 0; i < successors.size(); i++) {
			value = Math.min(value, maxValue(successors.get(i), game, piles - 1, alpha, beta, time));
			// Max pruning
			if (value <= alpha) {
				return value;
			}
			beta = Math.min(beta, value);
		}
		return value;
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

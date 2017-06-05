package game;

import java.util.ArrayList;

public class Gameboard {

	private final int[][] BOARD = new int[8][8];
	private final long TIME_LIMIT;
	private ArrayList<Gameboard> children;
	private Gameboard parent;

	// Time in Millis
	public Gameboard(long time) {
		TIME_LIMIT = time;
	}
	
	public Gameboard(long time, Gameboard parent) {
		this.parent = parent;
		TIME_LIMIT = time;
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
	
	public Gameboard alphaBetaDecision(Gameboard game, int piles) {
		int value = maxValue(game, piles, Integer.MIN_VALUE, Integer.MAX_VALUE, game.TIME_LIMIT + System.currentTimeMillis());
		System.out.println("VALUE : " + value);
		ArrayList<Gameboard> successors = game.getNextStates(-1);
		Evaluator evaluator = new Evaluator();
		for (int i = 0; i < successors.size(); i++) {
			int score = evaluator.evaluateOpponentBoard(successors.get(i).BOARD);
			System.out.println("Score["+i+"] :" + score);
			if (score == value) {
				return successors.get(i);
			}
		}
		throw new RuntimeException("Didn't find suitable move.");
	}
	
	private int maxValue(Gameboard game, int piles, int alpha, int beta, long time) {
		if (System.currentTimeMillis() > time) {
			return alpha;
		}
		
		Evaluator evaluator = new Evaluator();
		if (piles == 0) {
			return evaluator.evaluateOpponentBoard(game.BOARD);
		}
		int value = Integer.MIN_VALUE;
		ArrayList<Gameboard> successors = game.getNextStates(1);
		for (int i = 0; i < successors.size(); i++) {
			value = Math.max(value, minValue(successors.get(i), piles - 1, alpha, beta, time));
			// Min pruning
			if (value >= beta) {
				return value;
			}
			alpha = Math.max(alpha, value);
		}
		return value;
	}

	private int minValue(Gameboard game, int piles, int alpha, int beta, long time) {
		if (System.currentTimeMillis() > time) {
			return alpha;
		}
		
		Evaluator evaluator = new Evaluator();
		if (piles == 0) {
			return evaluator.evaluatePlayerBoard(game.BOARD);
		}
		int value = Integer.MAX_VALUE;
		ArrayList<Gameboard> successors = game.getNextStates(-1);
		for (int i = 0; i < successors.size(); i++) {
			value = Math.min(value, maxValue(successors.get(i), piles - 1, alpha, beta, time));
			// Max pruning
			if (value <= alpha) {
				return value;
			}
			beta = Math.min(beta, value);
		}
		return value;
	}
/*
	public int alphaBeta(Gameboard game, int piles, int alpha, int beta, int player, long time, String path) {
		
		
		Evaluator evaluator = new Evaluator();
		if (piles == 0) {
			// At bottom
			if (player == -1) {
				return evaluator.evaluateOpponentBoard(game.BOARD);
			}  else {
				return evaluator.evaluatePlayerBoard(game.BOARD);				
			}
		}
		
		int value;
		
		if (player == 1) {
			value = Integer.MIN_VALUE;
			ArrayList<Gameboard> states = game.getNextStates(1);
			for (int i = 0; i < states.size(); i++) {
				value = Math.max(value, alphaBeta(states.get(i), piles - 1, alpha, beta, -1, time, path + i));
				alpha = Math.max(alpha, value);
				// Min Pruning
				if (beta <= alpha) {
					break;
				}
			}
			return value;
		} else {
			value = Integer.MAX_VALUE;
			ArrayList<Gameboard> states = game.getNextStates(-1);
			for (int i = 0; i < states.size(); i++) {
				value = Math.min(value, alphaBeta(states.get(i), piles - 1, alpha, beta, 1, time, path + i));
				beta = Math.min(beta, value);
				// Max Pruning
				if (beta <= alpha) {
					break;
				}
			}
			return value;
		}
	}
*/
/*	
	public Gameboard alphaBeta(Gameboard game, int piles, int alpha, int beta, int player) {
		Evaluator evaluator = new Evaluator();
		if (piles == 0) {
			if ((player == -1 && evaluator.evaluateOpponentBoard(game.BOARD) == evaluator.VICTORY)) {
				return evaluator.evaluateOpponentBoard(game.BOARD);
			}  else if (player == 1 && evaluator.evaluatePlayerBoard(game.BOARD) == evaluator.VICTORY) {
				return evaluator.evaluatePlayerBoard(game.BOARD);				
			}
		}
		
		int value;
		Gameboard selected = new Gameboard(game.TIME_LIMIT);
		
		if (player == 1) {
			selected = game.getNextStates(-1).get(0);
			value = evaluator.evaluateOpponentBoard(selected.BOARD);
			for (Gameboard child: game.getNextStates(1)) {
				value = Math.max(value, evaluator.evaluateOpponentBoard(alphaBeta(child, piles - 1, alpha, beta, -1).BOARD));
				alpha = Math.max(alpha, value);
				if (beta <= alpha) {
					break;
				}
			}
			return selected;
		} else {
			value = Integer.MAX_VALUE;
			for (Gameboard child: game.getNextStates(-1)) {
				value = Math.min(value, alphaBeta(child, piles - 1, alpha, beta, 1));
				beta = Math.min(beta, value);
				if (beta <= alpha) {
					break;
				}
			}
			return value;
		}
	}
*/
	/*
	 * public Gameboard getOpponentNextMove(Gameboard game) {
	 * ArrayList<Gameboard> currentStates = new ArrayList<Gameboard>(); long
	 * timeLimit = System.currentTimeMillis() + game.TIME_LIMIT; game.children =
	 * game.getNextStates(-1); Evaluator evaluator = new Evaluator(); int[]
	 * choices = {0, 0, 0, 0, 0}; int piles = 5; int alpha = Integer.MIN_VALUE;
	 * int beta = Integer.MAX_VALUE;
	 * 
	 * // Generate the tree. for (int i = 0; i < piles; i++) { currentStates =
	 * currentStates.get(0).getNextStates(-1); }
	 * 
	 * while(System.currentTimeMillis() < timeLimit) { for (int i = 0; i <
	 * currentStates.size(); i++) { if () } }
	 * 
	 * game.children = null; return game.getNextStates(-1).get(choices[0]); }
	 */
/*
	public int min(Gameboard game, int threshold) {
		int best = Integer.MAX_VALUE;
		int index = 0;
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
		// TODO : return the index?
		return best;
	}

	public int max(Gameboard game, int threshold) {
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
		// TODO : return the index?
		return best;
	}
*/
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

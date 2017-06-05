package game;

public class Evaluator {
	
	private final int VICTORY = 1000000;

	// Evaluate all rows of the board then the columns.
	// Score based on consecutive adjacents and preventing loss.
	public int evaluatePlayerBoard(int[][] board) {
		int score = 0;
		for (int i = 0; i < board.length; i++) {
			int rowValue = getHorizontalPlayerAdjacents(board[i]);
			if (rowValue == VICTORY) {
				return VICTORY;
			} else {
				score += rowValue;
			}
		}
		
		for (int k = 0; k < board.length; k++) {
			int colValue = getVerticalPlayerAdjacents(k, board);
			if (colValue == VICTORY) {
				return VICTORY;
			} else {
				score += colValue;
			}
		}		
		return score;
	}
	
	// Evaluate all rows of the board then the columns.
	// Score based on consecutive adjacents and preventing loss.
	public int evaluateOpponentBoard(int[][] board) {
		int score = 0;
		for (int i = 0; i < board.length; i++) {
			int rowValue = getHorizontalOpponentAdjacents(board[i]);
			if (rowValue == VICTORY) {
				return VICTORY;
			} else {
				score += rowValue;
			}
		}
		
		for (int k = 0; k < board.length; k++) {
			int colValue = getVerticalOpponentAdjacents(k, board);
			if (colValue == VICTORY) {
				return VICTORY;
			} else {
				score += colValue;
			}
		}		
		return score;		
	}
	
	// Score the Player's adjacent pieces.
	// Does not account for gaps directly.
	private int getVerticalOpponentAdjacents(int col, int[][] arr) {
		int score = 0;
		int playerAdjacents = 0;
		int enemyAdjacents = 0;
		
		for (int i = 0; i < arr.length; i++) {
			
			// Player has a piece adjacent to previous space
			if (arr[i][col] < 0) {
				enemyAdjacents = 0;
				playerAdjacents++;
				score += (i % 4); // Prioritize the middle

				// Check win condition
				if (playerAdjacents == 4) {
					return VICTORY;
					// Penalty if piece is on border
				}
			} else if (arr[i][col] > 0) {
				// Enemy cutting off
				playerAdjacents = 0;
				enemyAdjacents++;
				
				// adjacent == 1 gives no points
				if (enemyAdjacents == 2) {
					// Enemy has 2 adjacent on a border and player blocks it
					if ((i == arr.length && arr[i - 2][col] < 0) || (i == 2 && arr[3][col] < 0)) {
						score += 125;
						// Player completely blocked off enemy adjacent 2
					} else if ((i != 1) && (arr[i - 2][col] < 0) && (arr[i + 1][col] < 0)) {
						score += 250;
						// Player is blocking one of the sides of enemy adjacent 3
					} else if ((i != 1) && (arr[i - 2][col] < 0) || (arr[i + 1][col] < 0)) {
						score += 250;
					}					
				} else if (enemyAdjacents == 3) {
					// Enemy has 3 adjacent on border and player blocks it
					if ((i == arr.length && arr[i - 3][col] < 0) || (i == 2 && arr[3][col] < 0)) {
						score += 1000;
						// Player completely blocked off enemy adjacent 3
					} else if ((i != 2) && (arr[i - 3][col] < 0) && (arr[i + 1][col] < 0)) {
						score += 1000;
						// Player is blocking one of the sides of enemy adjacent 3
					} else if ((i != 2) && (arr[i - 3][col] < 0) || (arr[i + 1][col] < 0)) {
						score += 500;
					}
				}
			} else {
				// There is an empty cutoff
				
				// adjacent == 1 gives no points
				if (playerAdjacents == 2) {
					score += 2;
				} else if (playerAdjacents == 3) {
					// There's a piece on border: Easily blockable
					if (i == arr.length || i == 2) {
						score += 2;
					} else { // Otherwise it could mean a win
						score += 100;
					}
				}
				playerAdjacents = 0;
				enemyAdjacents = 0;
			}
		}
		return score;
	}
	
	// Score the Player's adjacent pieces.
	// Does not account for gaps directly.
	private int getVerticalPlayerAdjacents(int col, int[][] arr) {
		int score = 0;
		int playerAdjacents = 0;
		int enemyAdjacents = 0;
		
		for (int i = 0; i < arr.length; i++) {			
			// Player has a piece adjacent to previous space
			if (arr[i][col] > 0) {
				enemyAdjacents = 0;
				playerAdjacents++;
				score += (i % 4); // Prioritize the middle
				
				// Check win condition
				if (playerAdjacents == 4) {
					return VICTORY;
					// Penalty if piece is on border
				}
			} else if (arr[i][col] < 0) {
				// Enemy cutting off
				playerAdjacents = 0;
				enemyAdjacents++;
				
				// adjacent == 1 gives no points
				if (enemyAdjacents == 2) {
					// Enemy has 2 adjacent on a border and player blocks it
					if ((i == arr.length && arr[i - 2][col] > 0) || (i == 2 && arr[3][col] > 0)) {
						score += 125;
						// Player completely blocked off enemy adjacent 2
					} else if ((i != 1) && (i != 7) && (arr[i - 2][col] > 0) && (arr[i + 1][col] > 0)) {
						score += 250;
						// Player is blocking one of the sides of enemy adjacent 3
					} else if ((i != 1) && (i != 7) && (arr[i - 2][col] > 0) || (arr[i + 1][col] > 0)) {
						score += 250;
					}					
				} else if (enemyAdjacents == 3) {
					// Enemy has 3 adjacent on border and player blocks it
					if ((i == arr.length && arr[i - 3][col] > 0) || (i == 2 && arr[3][col] > 0)) {
						score += 1000;
						// Player completely blocked off enemy adjacent 3
					} else if ((i != 2) && (i != 7) && (arr[i - 3][col] > 0) && (arr[i + 1][col] > 0)) {
						score += 1000;
						// Player is blocking one of the sides of enemy adjacent 3
					} else if ((i != 2) && (i != 7) && (arr[i - 3][col] > 0) || (arr[i + 1][col] > 0)) {
						score += 500;
					}
				}
			} else {
				// There is an empty cutoff
				
				// adjacent == 1 gives no points
				if (playerAdjacents == 2) {
					score += 2;
				} else if (playerAdjacents == 3) {
					// There's a piece on border: Easily blockable
					if (i == arr.length || i == 2) {
						score += 2;
					} else { // Otherwise it could mean a win
						score += 100;
					}
				}
				playerAdjacents = 0;
				enemyAdjacents = 0;
			}
		}
		return score;
	}
	
	// Score the Player's adjacent pieces.
	// Does not account for gaps directly.
	private int getHorizontalOpponentAdjacents(int[] arr) {
		int score = 0;
		int playerAdjacents = 0;
		int enemyAdjacents = 0;
		
		for (int i = 0; i < arr.length; i++) {			
			// Player has a piece adjacent to previous space
			if (arr[i] < 0) {
				enemyAdjacents = 0;
				playerAdjacents++;
				score += (i % 4); // Prioritize the middle
				
				// Check win condition
				if (playerAdjacents == 4) {
					return VICTORY;
					// Penalty if piece is on border
				}
			} else if (arr[i] > 0) {
				// Enemy cutting off
				playerAdjacents = 0;
				enemyAdjacents++;
				
				// adjacent == 1 gives no points
				if (enemyAdjacents == 2) {
					// Enemy has 2 adjacent on a border and player blocks it
					if ((i == arr.length && arr[i - 2] < 0) || (i == 2 && arr[3]< 0)) {
						score += 125;
						// Player completely blocked off enemy adjacent 2
					} else if ((i != 1) && (i != 7) && (i != 7) && (arr[i - 2] < 0) && (arr[i + 1] < 0)) {
						score += 250;
						// Player is blocking one of the sides of enemy adjacent 3
					} else if ((i != 1) && (i != 7) && (i != 7) && (arr[i - 2] < 0) || (arr[i + 1] < 0)) {
						score += 250;
					}					
				} else if (enemyAdjacents == 3) {
					// Enemy has 3 adjacent on border and player blocks it
					if ((i == arr.length && arr[i - 3] < 0) || (i == 2 && arr[3] < 0)) {
						score += 1000;
						// Player completely blocked off enemy adjacent 3
					} else if ((i != 2) && (i != 7) && (arr[i - 3] < 0) && (arr[i + 1] < 0)) {
						score += 1000;
						// Player is blocking one of the sides of enemy adjacent 3
					} else if ((i != 2) && (i != 7) && (arr[i - 3] < 0) || (arr[i + 1] < 0)) {
						score += 500;
					}
				}
			} else {
				// There is an empty cutoff
				
				// adjacent == 1 gives no points
				if (playerAdjacents == 2) {
					score += 2;
				} else if (playerAdjacents == 3) {
					// There's a piece on border: Easily blockable
					if (i == arr.length || i == 2) {
						score += 2;
					} else { // Otherwise it could mean a win
						score += 100;
					}
				}
				playerAdjacents = 0;
				enemyAdjacents = 0;
			}
		}
		return score;
	}
	
	// Score the Player's adjacent pieces.
	// Does not account for gaps directly.
	private int getHorizontalPlayerAdjacents(int[] arr) {
		int score = 0;
		int playerAdjacents = 0;
		int enemyAdjacents = 0;
		
		for (int i = 0; i < arr.length; i++) {			
			// Player has a piece adjacent to previous space
			if (arr[i] > 0) {
				enemyAdjacents = 0;
				playerAdjacents++;
				score += (i % 4); // Prioritize the middle
				
				// Check win condition
				if (playerAdjacents == 4) {
					return VICTORY;
					// Penalty if piece is on border
				}
			} else if (arr[i] < 0) {
				// Enemy cutting off
				playerAdjacents = 0;
				enemyAdjacents++;
				
				// adjacent == 1 gives no points
				if (enemyAdjacents == 2) {
					// Enemy has 2 adjacent on a border and player blocks it
					if ((i == arr.length && arr[i - 2] > 0) || (i == 2 && arr[3] > 0)) {
						score += 125;
						// Player completely blocked off enemy adjacent 2
					} else if ((i != 1) && (i != 7) && (arr[i - 2] > 0) && (arr[i + 1] > 0)) {
						score += 250;
						// Player is blocking one of the sides of enemy adjacent 3
					} else if ((i != 1) && (i != 7) && (arr[i - 2] > 0) || (arr[i + 1] > 0)) {
						score += 250;
					}					
				} else if (enemyAdjacents == 3) {
					// Enemy has 3 adjacent on border and player blocks it
					if ((i == arr.length && arr[i - 3] > 0) || (i == 2 && arr[3] > 0)) {
						score += 1000;
						// Player completely blocked off enemy adjacent 3
					} else if ((i != 2) && (i != 7) && (arr[i - 3] > 0) && (arr[i + 1] > 0)) {
						score += 1000;
						// Player is blocking one of the sides of enemy adjacent 3
					} else if ((i != 2) && (i != 7) && (arr[i - 3] > 0) || (arr[i + 1] > 0)) {
						score += 500;
					}
				}
			} else {
				// There is an empty cutoff
				
				// adjacent == 1 gives no points
				if (playerAdjacents == 2) {
					score += 2;
				} else if (playerAdjacents == 3) {
					// There's a piece on border: Easily blockable
					if (i == arr.length || i == 2) {
						score += 2;
					} else { // Otherwise it could mean a win
						score += 100;
					}
				}
				playerAdjacents = 0;
				enemyAdjacents = 0;
			}
		}
		return score;
	}
}

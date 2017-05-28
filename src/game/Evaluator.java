package game;

public class Evaluator {
	
	private final int DEFEAT = -10000;
	private final int VICTORY = 1000000;

	// Score the Opponent's adjacent pieces.
	public int getHorizontalEnemyAdjacents(int[] arr) {
		int score = 0;
		int adjacents = 0;
		for (int i = 0; i < arr.length; i++) {
			
			// Enemy has an adjacent
			if (arr[i] < 0) {
				adjacents++;
			} else if (arr[i] > 0) { 
				// Player cutting off
				adjacents = 0;
			} else {
				// There is an empty cutoff
				adjacents = 0;
				if (adjacents == 2) {
					score -= 15;
				} else if (adjacents == 3) {
					score -= 30;
				} else if (adjacents == 4) {
					return DEFEAT;
				}
			}
			
			if (adjacents == 4) {
				return DEFEAT;
			}
		}
		return score;
	}
	
	// Scenarios: For next move
	//
	// | |X| | | | | 1 Piece no wall = 0
	// |X| | | | | | 1 Piece on wall = -1
	// |X|X| | | | | 2 Adjacent on wall = 1
	// | |X|X| | | | 2 Adjacent no wall = 3
	// | |X|X|O| | | 2 Adjacent, 1 Free, no wall block = -10
	// | | |X|X|O| | 2 Adjacent, 2 Free, no wall block = 2
	// | | |X| |X| | 2 Gap = 2
	// |X| |X| |X| | 2 Gap Multi = 2
	// |X| |X| | | | 2 Gap wall = 1
	// |X|X|X| | | | 3 Adjacent on wall = 4
	// |X|X|X|O| | | 3 Adjacent on wall block = -1
	// | |X|X|X| | | 3 Adjacent no wall no block = 100
	// | |X|X|X|O| | 3 Adjacent no wall block = 4
	// | |X| |X|X| | 3 Gap = 3
	// |X| |X|X| | | 3 Gap wall = 2
	// |X|X|X|X| | | 4 Adjacent = VICTORY
	//
	// |X|O|O| | | | Block 2 = 1
	// |X|O|O|X| | | Block 2 complete = 5
	// |X|O|O|O| | | Block 3 = -100
	// |X|O|O|O|X| | Block 3 complete = 1000
	// |O|X|O|O| | | Block 3 complete2 = 1000
	// Score the Player's adjacent pieces.
	public int getHorizontalPlayerAdjacents(int[] arr) {
		int score = 0;
		int adjacents = 0;
		
		for (int i = 0; i < arr.length; i++) {			
			// Player has a piece adjacent to previous space
			if (arr[i] > 0) {
				adjacents++;
				// Check win condition
				if (adjacents == 4) {
					return VICTORY;
					// Penalty if piece is on border
				} else if (i == 0 || (i == arr.length - 1)){
					score -= 1;
				}
			} else if (arr[i] > 0) {
				// Enemy cutting off
				adjacents = 0;
			} else {
				// There is an empty cutoff
				if (adjacents == 2) {
					score += 2;
				} else if (adjacents == 3) {
					// There's a piece on border
					if (i == arr.length || i == 2) {
						score += 2;
					} else {
						score += 100;
					}
				}
				adjacents = 0;
			}
		}
		return score;
	}
}

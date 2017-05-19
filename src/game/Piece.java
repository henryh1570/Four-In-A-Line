package game;

public class Piece {
	
	private int x;
	private int y;
	private int owner;
	
	public Piece(int row, int col, int player) {
		x = row;
		y = col;
		owner = player;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getOwner() {
		return owner;
	}
}

package ai;

import java.util.ArrayList;
import java.util.List;

import ai.Cinanto;

public final class HexifenceMinimaxTree extends MinimaxTree {

	public HexifenceMinimaxTree(boolean isMin, char[][] board, Move move) {
		super(isMin, board, move);
	}
	
	@Override
	protected int ScoringFunction(Move move, char[][] board, boolean isMin) {
		int outcome = Cinanto.getWinner(board);
		if (outcome == 3) {
			return 0;
		}
		// minimum
		if (isMin && outcome == 1) {
			return 1;
		}
		if (isMin && outcome == 2) {
			return -1;
		}
		// maximum
		if (outcome == 1) {
			return -1;
		}
		if (outcome == 2) {
			return 1;
		}

		return 0;
	}

	// gets a list of possible moves
	private List<Move> getMoves() {
		return new ArrayList<Move>();
	}
	
	// returns the board result of a move
	private char[][] moveResult(Move move) {
		return null;
	}
	
	@Override
	protected int HeuristicFunction(Move move, char[][] board, boolean isMin) {
		return 0;
	}

	@Override
	protected void BuildChildNodes() {
		
		// get the valid moves
		for (Move i : getMoves()) {
			this.nodes.add(new HexifenceMinimaxTree(!isMin, moveResult(i), i));
		}
	}	
}

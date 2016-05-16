package ai;

import java.util.ArrayList;
import java.util.List;

import ai.*;

public final class HexifenceMinimaxTree extends MinimaxTree {

	private final int p;
	
	public HexifenceMinimaxTree(boolean isMin, char[][] board, Move move, int p) {
		super(isMin, board, move);
		this.p = p;
	}
	
	@Override
	protected int ScoringFunction(Move move, char[][] board, boolean isMin) {
		int outcome = Cinanto.getWinner(board);
		if (outcome == 3) {
			return 0;
		}
		// blue 
		if (p == 1 && outcome == 1) {
			return 1;
		}
		if (p == 1 && outcome == 2) {
			return -1;
		}
		// red
		if (outcome == 1) {
			return -1;
		}
		if (outcome == 2) {
			return 1;
		}

		return 0;
	}

	// gets a list of possible moves
	private static List<Move> getMoves(char[][] board) {
		
		List<Move> 	temp = new ArrayList<Move> ();
		Move		tempMove;
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				
				tempMove = new Move();
				tempMove.Col = i;
				tempMove.Row = j;
				
				temp.add(tempMove);
			}
		}
		
		return new ArrayList<Move>();
	}
	
	// returns the board result of a move
	private static char[][] moveResult(Move move, char[][] board, int p) {
		board[move.Col][move.Row] = (p == Piece.RED ? 'R' : 'P');
			// return move
		return board;
	}
	
	private static boolean captureMove(Move move, char[][] board, int p) {
		return false;
	}
	
	// greedy heuristic
	@Override
	protected int HeuristicFunction(Move move, char[][] board, boolean isMin) {
		
		int rCnt = 0;
		int bCnt = 0;
		for (int i = 1; i < board.length; i+=2) {
			for (int j = 1; j < board.length; j +=2) {
				if (board[i][j] == 'r') rCnt++;
				if (board[i][j] == 'b') bCnt++;
			}
		}
		
		if (p == Piece.BLUE) return bCnt - rCnt;
		return rCnt - bCnt;
	}

	@Override
	protected void BuildChildNodes() {
		// get the valid moves
		for (Move i : getMoves(board)) {
			// if it is our turn to move again
			if (captureMove(i, board, p)) 
				this.nodes.add(new HexifenceMinimaxTree(isMin, moveResult
						(i, board, p), i, p));
			// decide the move for the other player
			else this.nodes.add(new HexifenceMinimaxTree(!isMin, moveResult
					(i, board, p), i, ((0-p)+3) ));
		}
	}	
}

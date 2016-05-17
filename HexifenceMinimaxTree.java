package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ai.*;

public final class HexifenceMinimaxTree extends MinimaxTree {

	// piece code for the player
	public static int playerP;
	
	// piece code for this current move
	private final int p;
	private final static int maxDepth = 12;
	
	public static void PrintMove(Move move) {
		System.out.println(move.Col + ", " + move.Row + " for " + move.P);
	}
	
	/**
	 * Function which returns the best move to make, using the minimax
	 * tree
	 * @param board
	 * @param p
	 * @return the best move to make
	 */
	public static Move HexifenceMinimaxMove(char[][] board, int p) {
		
		HexifenceMinimaxTree temp;
		int bestScore = minScore;
		int tempScore;
		Move bestMove = null;
		
		// get the list of moves
		List<Move> moves = Cinanto.getMoves(board, p);
		Collections.shuffle(moves);
		
		// iterate through each possible move
		for (Move i : moves) {
			temp = new HexifenceMinimaxTree(true, board, i, p);
			if ((tempScore = temp.GetScoreABP(0,maxDepth,null,null)) > bestScore) {
				bestScore = tempScore;
				bestMove = i;
			}
		}
		
		return bestMove;
		
	}
	
	public HexifenceMinimaxTree(boolean isMin, char[][] board, Move move, int p) {
		super(isMin, board, move);
		this.p = p;
	}
	
	@Override
	protected int ScoringFunction(Move move, char[][] board, boolean isMin) {
		int outcome = Cinanto.getWinner(board);
		if (outcome == Piece.INVALID) {
			return 0;
		}
		
		// check who has won
		
		// blue 
		if (playerP == Piece.BLUE && outcome == Piece.BLUE) {
			return 1;
		}
		if (playerP == Piece.BLUE && outcome == Piece.RED) {
			return -1;
		}
		// red
		if (outcome == Piece.BLUE) {
			return -1;
		}
		if (outcome == Piece.RED) {
			return 1;
		}
		return 0;
	}
	
	@Override
	protected int HeuristicFunction(Move move, char[][] board, boolean isMin) {
		
		int rCnt = 0;
		int bCnt = 0;	
		
		// check the number of squares captured
		for (int i = 1; i < board.length; i+=2) {
			for (int j = 1; j < board.length; j +=2) {
				if (board[i][j] == 'r') rCnt++;
				if (board[i][j] == 'b') bCnt++;
			}
		}
		
		// return the difference
		if (playerP == Piece.BLUE) return bCnt - rCnt;
		return rCnt - bCnt;
	}

	@Override
	protected void BuildChildNodes() {

		nodes = new ArrayList<MinimaxTree>();
		
		for (Move i : Cinanto.getMoves(board, p))
			if (Cinanto.isCaptureMove(board, i)) nodes.add(new 
					HexifenceMinimaxTree(isMin, board, i, p));
			else nodes.add(new HexifenceMinimaxTree(!isMin,
					board, i, p));
	}

	@Override
	protected void UpdateBoard() {
		
		board = Cinanto.moveResultNew(GetMove(), board);
		Cinanto.getCapturedPositions(board,  p);
		
		
	}
}


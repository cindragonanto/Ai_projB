package ai;

import java.util.ArrayList;
import java.util.List;

/**
 * MinimaxTree class
 * @author campbellw
 *
 */
public abstract class MinimaxTree  {
	
	protected char[][] 			board;
	private Move				move;
	
	private MinimaxTree			bestMove;
	
	protected static final int	maxScore = 999;
	protected static final int	minScore = -999;	
	
	protected final boolean 	isMin;
	
	protected final List<MinimaxTree>	nodes = new ArrayList<MinimaxTree>();
	
	// 
	/**
	 * scoring function
	 * @param move
	 * @param board
	 * @param isMin
	 * @return score
	 */
	protected abstract int  ScoringFunction(Move move, char[][] board,
			boolean isMin);
	// 
	/** 
	 * heuristic to be called for depth limited minimax
	 * @param move
	 * @param board
	 * @param isMin
	 * @return score
	 */
	protected abstract int  HeuristicFunction(Move move, char[][] board,
			boolean isMin);
	// 
	/**
	 * builds child nodes
	 */
	protected abstract void BuildChildNodes();
	
	/**
	 * update the board given the move
	 */
	protected abstract void UpdateBoard();
	
	public MinimaxTree(boolean isMin, char[][] board, Move move) {
		this.isMin = isMin;
		this.board = board;
		this.move  =  move;
	}
	
	/**
	 * returns score
	 * @return
	 */
	public int GetScore() {
		return this.GetScore(null, null);
	}
	
	/**
	 * returns score with depth limiting
	 * @param depth
	 * @param maxDepth
	 * @return
	 */
	public int GetScore(Integer depth, Integer maxDepth) {
		
		UpdateBoard();
		
		// check if the depth limit is reached
		if (depth != null && maxDepth != null && (depth >= maxDepth)) {
			return HeuristicFunction(move, board, isMin);
		}
				
		BuildChildNodes();
		
		// check if this is has no children
		if (nodes.isEmpty()) {
			return ScoringFunction(move, board, isMin);
		}
		
		// for the maximum case
		if (!isMin) {
			int max = minScore;
			int temp = 0;
			
			for (MinimaxTree i : nodes) {
				// call minimax on all children
				if (((temp = (depth != null || maxDepth != null) ? 
						(i.GetScore(++depth, maxDepth)) :
							(i.GetScore()))) > max) {
					max = temp;
					bestMove = i;
				}
			}
			return max;
		}
		
		// for minimum case
		int min = maxScore;
		int temp = 0;
		
		for (MinimaxTree i : nodes) {
			// call minimax on all children
			if (((temp = (depth != null || maxDepth != null) ? 
					(i.GetScore(++depth, maxDepth)) :
						(i.GetScore()))) < min) {
				min = temp;
				bestMove = i;
			}
		}
		
		return min;
	}
	
	/**
	 * returns score with alpha-beta pruning
	 * @param depth
	 * @param maxDepth
	 * @param alpha
	 * @param beta
	 * @return
	 */
	public int GetScoreABP(Integer depth, Integer maxDepth, Integer alpha, Integer beta) {

		UpdateBoard();
		
		// check if the depth limit is reached
		if (depth != null && maxDepth != null && (depth >= maxDepth)) {
			return HeuristicFunction(move, board, isMin);
		}
				
		BuildChildNodes();
		
		// check if this is has no children
		if (nodes.isEmpty()) {
			return ScoringFunction(move, board, isMin);
		}
		
		if (alpha == null || beta == null) {
			alpha = minScore;
			beta = maxScore;
		}
		
		// for the maximum case
		if (!isMin) {
			int max = minScore;
			int maxA = minScore;
			int temp = 0;
			
			for (MinimaxTree i : nodes) {
				// call minimax on all children
				if (((temp = (depth != null || maxDepth != null) ? 
						(i.GetScoreABP(++depth, maxDepth, maxA, beta)) :
							(i.GetScoreABP(null, null, maxA, beta)))) > max) {
					if (temp >= maxA) maxA = temp;
					max = temp;
					bestMove = i;
					if (beta <= maxA) break;
				}
			}
			return maxA;
		}
		// for minimum case
		int min = maxScore;
		int maxB = maxScore;
		int temp = 0;		
		for (MinimaxTree i : nodes) {
			// call minimax on all children
			if (((temp = (depth != null || maxDepth != null) ? 
					(i.GetScoreABP(++depth, maxDepth, alpha, maxB)) :
						(i.GetScoreABP(null, null, alpha, maxB)))) < min) {
				if (temp <= maxB) maxB = temp;
				min = temp;
				bestMove = i;
				if (maxB <= alpha) break;
			}
		}
		return maxB;
	}
	
	/** 
	 * returns the best move found amongst children
	 * @return
	 */
	public Move BestMove() {
		return bestMove.GetMove();
	}
	
	/**
	 * returns the move
	 * @return
	 */
	public Move GetMove() {
		return this.move;
	}	
}


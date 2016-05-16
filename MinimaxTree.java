package ai;

import java.util.List;

public abstract class MinimaxTree  {
	
	protected char[][] 			board;
	private Move				move;
	
	private MinimaxTree			bestMove;
	private static final int	maxScore = 999;
	private static final int	minScore = -999;	
	
	protected final boolean 	isMin;
	protected List<MinimaxTree>	nodes;
	
	// scoring function
	protected abstract int  ScoringFunction(Move move, char[][] board,
			boolean isMin);
	// heuristic to be called for depth limited minimax
	protected abstract int  HeuristicFunction(Move move, char[][] board,
			boolean isMin);
	// builds child nodes
	protected abstract void BuildChildNodes();
	
	public MinimaxTree(boolean isMin, char[][] board, Move move) {
		this.isMin = isMin;
		this.board = board;
		this.move  =  move;
	}
	
	public int GetScore() {
		return this.GetScore(null, null);
	}
			
	public int GetScore(Integer depth, Integer maxDepth) {
		// check if this is has no children
		if (nodes.isEmpty()) {
			return ScoringFunction(move, board, isMin);
		}
		
		// check if the depth limit is reached
		if (depth != null && maxDepth != null && (depth >= maxDepth)) {
			return HeuristicFunction(move, board, isMin);
		}
		
		BuildChildNodes();
		
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
	
	public int GetScoreABP(Integer depth, Integer maxDepth, Integer alpha, Integer beta) {
		// check if this is has no children
		if (nodes.isEmpty()) {
			return ScoringFunction(move, board, isMin);
		}
		
		// check if the depth limit is reached
		if (depth != null && maxDepth != null || (depth >= maxDepth)) {
			return HeuristicFunction(move, board, isMin);
		}
		
		if (alpha == null || beta == null) {
			alpha = minScore;
			beta = maxScore;
		}
		
		BuildChildNodes();
		
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
	
	public Move BestMove() {
		return bestMove.GetMove();
	}
	
	public Move GetMove() {
		return this.move;
	}	
}


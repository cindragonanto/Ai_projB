package ai;

import java.util.List;

public abstract class MinimaxTree  {
	
	private char[][] 			board;
	private Move				move;
	
	private MinimaxTree			bestMove;
	private static final int	maxScore = 999;
	private static final int	minScore = -999;	
	
	protected boolean 			isMin;
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
				if (((temp = (depth != null && maxDepth != null) ? 
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
			if (((temp = (depth != null && maxDepth != null) ? 
					(i.GetScore(++depth, maxDepth)) :
						(i.GetScore()))) < min) {
				min = temp;
				bestMove = i;
			}
		}
		
		return min;
	}
	
	public Move BestMove() {
		return bestMove.GetMove();
	}
	
	public Move GetMove() {
		return this.move;
	}	
}


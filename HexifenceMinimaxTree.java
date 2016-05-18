package aiproj.hexifence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ai.*;

/**
 * Hexifence MinimaxTree implementation
 * @author campbellw
 *
 */
public final class HexifenceMinimaxTree extends MinimaxTree {

	// piece code for the player
	public static int playerP;
	
	// piece code for this current move
	private final int p;
	
	// minimum depth for minimax
	private static final int minDepth = 4;
	
	// weight for chain average heuristic (blue)
	private static final double chainAvgConst1 = 0.3;
	// weight for chain average heuristic (red)
	private static final double chainAvgConst2 = 5;
	// weight for greedy heuristic
	private static final double greedyConst = 2;
	// weight for chain heuristic
	private static final double chainConst = 3;
	// weight for chain mod (odd/even)
	private static final double chainModWeight = 2;
	// minimum depth for minimax
	private static final double depthFactor = 0.3;
	
	// number of moves below which minimax will look deeper than depthFactor
	private static final int maxDepthMovesConst = 40;
	// maximum depth for minimax
	private static final int maxDepth = 11;
	
	private static final int winScore = 100;
	
	public static void PrintMove(Move move) {
		System.out.println(move.Col + ", " + move.Row + " for " + move.P);
	}
	
	public class Chain {
		List<Integer> x = new ArrayList<Integer>();
		List<Integer> y = new ArrayList<Integer>();
		
		public Chain() {
		}
		
		public void Add(int x, int y) {
			this.x.add((Integer)x);
			this.y.add((Integer)y);
		}

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
		
		int moveSize = moves.size();
		int depth = minDepth + (int) (depthFactor * (maxDepthMovesConst - moves.size()));
		
		if (depth < minDepth) depth = minDepth;
		if (depth > maxDepth) depth = maxDepth;
		
		// iterate through each possible move
		for (Move i : moves) {
			temp = new HexifenceMinimaxTree(true, board, i, p);
			if ((tempScore = temp.GetScoreABP(0,minDepth,null,null)) > 
			bestScore) {
				bestScore = tempScore;
				bestMove = i;
			}
		}
		
		return bestMove;
		
	}
	
	public HexifenceMinimaxTree(boolean isMin, char[][] board, Move move, 
			int p) {
		super(isMin, board, move);
		this.p = p;
	}
	
	@Override
	protected int ScoringFunction(Move move, char[][] board,
			boolean isMin) {
		int outcome = Cinanto.getWinner(board);
		if (outcome == Piece.INVALID) {
			return MinimaxTree.minScore;
		}
		
		// check who has won
		
		// blue 
		if (playerP == Piece.BLUE && outcome == Piece.BLUE) {
			return winScore;
		}
		if (playerP == Piece.BLUE && outcome == Piece.RED) {
			return -winScore;
		}
		// red
		if (outcome == Piece.BLUE) {
			return -winScore;
		}
		if (outcome == Piece.RED) {
			return winScore;
		}
		return 0;
	}
	
	/**
	 * greedy heuristic that seeks to maximise the number of captured cells
	 * @param move
	 * @param board
	 * @param isMin
	 * @return
	 */
	private int GreedyHeuristic(Move move, char[][] board, boolean isMin) {
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
	protected int HeuristicFunction(Move move, char[][] board, 
			boolean isMin) {
		
		double score = greedyConst * GreedyHeuristic(move, board, isMin)+
				chainConst * ChainHeuristicFunction(move,board,isMin);
		System.out.println("Win score: " + score);
		
		if (score >= winScore-1) return winScore - 1;
		if (score <= -winScore+1) return -winScore +1;
		
		return (int) score;
	}

	/**
	 * Returns the number of chains
	 * @return
	 */
	private List<Chain> getChains() {
		// find any cells that are 1 piece from capture
		List<Chain> temp = new ArrayList<Chain>();
		LinkedList<Integer[]> queue = new LinkedList<Integer[]>();
		List<Integer[]> checkedCells = new ArrayList<Integer[]>();
		
		Chain	  tempChain;
		Integer[] tempInt;
		int[]     tempEdge;
		
		for (int i = 1; i < board.length; i += 2) {
			for (int j = 1; j < board.length; j += 2) {
				if ((board[i][j] != 'r') && (board[i][j] != 'b') && 
						CellEdges(board,i,j) == 4) {
					// iterate through neighbouring cells to find chains
					// which exist where the neighbouring cell shares an
					// empty edge and has 4 taken edges
					tempChain = new Chain();
					checkedCells.add(new Integer[]{i,j});
					for (Integer[] k : neighbouringCells(i,j)) {
						// check if the value is valid
						if (!valueInArray(k[0],k[1],checkedCells) && 
								Cinanto.validPos(k[0], k[1], 
										(board.length+1)/4)) {
							// check if the number of edges taken is 4
							if (CellEdges(board, k[0],k[1]) == 4) {
								tempEdge = sharedEdge(k[0],k[1],i,j);
								// check if the shared edge is empty
								if (board[tempEdge[0]][tempEdge[1]] == '+') {
									tempChain.Add(k[0], k[1]);
									// add in neighbouring chains, if they
									// haven't already been checked
									for (Integer[] q : neighbouringCells
											(k[0],k[1]))
										if (!valueInArray
												(q[0],q[1],checkedCells))
											queue.add(q);
								}
							}
							// this cell isn't valid, so don't check it again
							checkedCells.add(new Integer[]{k[0],k[1]});
						}
					}
				// add our value to the return chain
				temp.add(tempChain);
				}
			}
		}
		return temp;
	}
	
	/**
	 * returns if the value is in the array
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private static boolean valueInArray(int x, int y, List<Integer[]> z) {
		for (Integer[] i : z) if (i[0] == x && i[1] == y) return true;
		return false;
	}
	
	/**
	 * returns neighbouring cells
	 * @param i
	 * @param j
	 * @return
	 */
	private List<Integer[]> neighbouringCells(int i, int j) {
		List<Integer[]> temp = new ArrayList<Integer[]>();
		temp.add(new Integer[]{i-2, j-2});
		temp.add(new Integer[]{i  , j-2});
		temp.add(new Integer[]{i-2, j  });
		temp.add(new Integer[]{i+2, j+2});
		temp.add(new Integer[]{i+2, j  });
		temp.add(new Integer[]{i  , j+2});
		return temp;
	}
	
	/**
	 * returns the coordinates of the shared edge between two cells
	 * @param i
	 * @param j
	 * @param i2
	 * @param j2
	 * @return int[2] coordinates
	 */
	private int[] sharedEdge(int i, int j, int i2, int j2) {
		if (i == i2) return new int[]{i,(j2 > j) ? j2-1 : j2+1};
		if (j == j2) return new int[]{(i2 > i) ? i2-1 : i2+1,j};
		if (i > i2)  return new int[]{i-1, j-1};
		else		 return new int[]{i+1, j+1};
	}
	
	/**
	 * returns the number of edges that are taken
	 * @param board
	 * @param i
	 * @param j
	 * @return number of edges taken
	 */
	public static int CellEdges(char[][] board, int i, int j) {
		int k = 0;
		if ((board[i-1][j-1] == 'R' || board[i-1][j-1] == 'B')) k++;
		if ((board[i  ][j-1] == 'R' || board[i  ][j-1] == 'B')) k++;
		if ((board[i-1][j  ] == 'R' || board[i-1][j  ] == 'B')) k++;
		if ((board[i+1][j+1] == 'R' || board[i+1][j+1] == 'B')) k++;
		if ((board[i  ][j+1] == 'R' || board[i  ][j+1] == 'B')) k++;
		if ((board[i+1][j  ] == 'R' || board[i+1][j  ] == 'B')) k++;
		return k;
	}
	
	/**
	 * heuristic function that looks for chains
	 * @param move
	 * @param board
	 * @param isMin
	 * @return
	 */
	private int ChainHeuristicFunction(Move move, char[][] board, 
			boolean isMin) {
		
		List<Chain> temp = getChains();
		int chainCount = temp.size();
		int chainSize = 0;
		for (Chain i : temp) {
			chainSize+=i.x.size();
		}
		
		double chainAvgSize = (double) chainSize /  (double) chainCount;
		if (chainAvgSize == 0) chainAvgSize = 1;
		
		// blue moves first
		if (playerP == Piece.BLUE) {
			chainAvgSize = (chainAvgSize * chainAvgConst1);
		} else {
			chainAvgSize = (chainAvgConst2/chainAvgSize);
		}
		
		// adjust these weights
		if (isMin) return (int) (chainCount%2 == 1 ? -chainModWeight :
			chainModWeight) + (int) chainAvgSize ;
		return (int) (chainCount%2 == 1 ? chainModWeight : -chainModWeight)
				+ (int) chainAvgSize; 
	}
	
	@Override
	protected void BuildChildNodes() {
		
		for (Move i : Cinanto.getMoves(board, p)) {
			//System.out.println("Checking move: " + i.Col + ", " + i.Row);
			if (Cinanto.isCaptureMove(board, i)) nodes.add(new 
					HexifenceMinimaxTree(isMin, board, i, p));
			else nodes.add(new HexifenceMinimaxTree(!isMin,
					board, i, p));
		}
			
	}

	@Override
	protected void UpdateBoard() {
		
		board = Cinanto.moveResultNew(GetMove(), board);
		Cinanto.getCapturedPositions(board,  p);
		
		
	}
}


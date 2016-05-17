package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
		
		// iterate through each possible move
		for (Move i : moves) {
			temp = new HexifenceMinimaxTree(true, board, i, p);
			if ((tempScore = temp.GetScoreABP(0,maxDepth,null,null)) > 
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
		
		return GreedyHeuristic(move, board, isMin)+ChainHeuristicFunction(move,board,isMin);
	}

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
						CellEdges(board,i,j) == 5) {
					// iterate through neighbouring cells to find chains
					// which exist where the neighbouring cell shares an
					// empty edge and has 4 taken edges
					tempChain = new Chain();
					checkedCells.add(new Integer[]{i,j});
					for (Integer[] k : neighbouringCells(i,j)) {
						if (!valueInArray(k[0],k[1],checkedCells)) {
							if (CellEdges(board, k[0],k[1]) == 4) {
								tempEdge = sharedEdge(k[0],k[1],i,j);
								if (board[tempEdge[0]][tempEdge[1]] == '+') {
									tempChain.Add(k[0], k[1]);
									for (Integer[] q : neighbouringCells
											(k[0],k[1]))
										if (!valueInArray
												(q[0],q[1],checkedCells))
											queue.add(q);
								}
							}
							checkedCells.add(new Integer[]{k[0],k[1]});
						}
					}
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
	 * returns whether or not a cell is chained with a neighbour
	 * @param i
	 * @param j
	 * @param i2
	 * @param j2
	 * @return the level of chaining
	 */
	private int chainedWith(int i, int j, int i2, int j2) {
		return 0;
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
		
		int score = getChains().size();
		// IF EVEN OR ODD OR WHATEVER DO THAT HERE
			
		return score;
	}
	
	@Override
	protected void BuildChildNodes() {
		
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


package aiproj.hexifence.cinanto;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ai.*;
/** CINANTO AGENT
 * 
 * @author Cinanto
 * @author campbellw
638739 *
 */
public class Cinanto implements Player{

	//n is the size of the board
	//p is the piece that the player uses, RED or BLUE
	//should initialize the needed parts of the ai
		
	public char  board[][];
	private int  size; 
	private int  boundBoardSize;
	private int  p;
	
	private static final int maxCap2 = 7;
	private static final int maxCap3 = 19;
	
	// the number of positions captured
	public int captureCount = 0;
	
	/**
	 * returns if the position is valid for the game board
	 * @param x
	 * @param y
	 * @param n size of the game
	 * @return boolean
	 */
	private static boolean canAccept(int x, int y, int n)
	{
		// return if the coordinate falls within the bounds of the board
		return (Math.abs(x-y) <= 2*n - 1);
	}
	
	/**
	 * returns if the position is valid for an edge
	 * @param x
	 * @param y
	 * @return boolean
	 */
	private boolean boardValidPos(int x, int y) {
		return validPos(x,y,boundBoardSize) && board[x][y] == '+';
	}
	
	/**
	 * returns if the position is valid for an edge
	 * @param x
	 * @param y
	 * @param n size of the game
	 * @return boolean
	 */
	static boolean validPos(int x, int y, int n) {
		// check through to ensure the positions are valid
		return (Math.abs(x-y) <= 2*n - 1) && 
				(x >= 0) && (y >= 0) &&
				(x < (4*n-1)) && (y < (4*n-1)) &&
				!(x%2 == 1 && y%2 == 1);
	}

	/**
	 * initialises the board and player
	 * @param n the size of the board
	 * @param p the player
	 * @return int code reflecting the success of init
	 */
	public int init(int n, int p) {
		int i = 0, j = 0;
		size = 4*n -1;
		this.boundBoardSize = n;
		board = new char[size][size];
		// initialise the board positions
		for (i = 0; i< size; i++){
			for (j = 0; j<size ; j++){			
				if (canAccept(i,j,n)){
					if (i%2 == 0){
						board[i][j] = '+';
					}
					else if (i%2!= 0 && j%2 ==0){
						board[i][j]= '+';
					}
					else {
						board[i][j] = '-';
					}
				}
				else{
					board[i][j] = '-';
				}
			}
		}
		this.p = p;
		
		return 0;
	}
	
	/**
	 * function called to generate and act out a move
	 * @return move to make
	 */
	public Move makeMove() {
		
		
		int x = (int)(Math.random() * size);
		int y = (int)(Math.random() * size);
		
		while (!boardValidPos(y,x)) {
			x = (int)(Math.random() * size);
			y = (int)(Math.random() * size);
		}
		
		Move temp = new Move();
		temp.Col = x;
		temp.Row = y;
		temp.P = p;
		
		moveResult(temp);
				
		HexifenceMinimaxTree.playerP = p;
		Move out = HexifenceMinimaxTree.HexifenceMinimaxMove(board, p);
		moveResult(out);
		return out;	
	}

	/**
	 * function called to reflect the opponent's move
	 * @param m the opponent's move
	 * @return int code reflecting the move state
	 */
	public int opponentMove(Move m) {
		
		// check if the position is valid
		if (!validPos(m.Row, m.Col, boundBoardSize))
			return -1;
		// check if any captures are made
		if (moveResult(m) > 0) return 1;
		return 0;	
		
	}
	
	/**
	 * updates the player's internal state given a move
	 * @param move Move
	 * @return the number of positions captured
	 */
	public int moveResult(Move move) {
		
		int pos = captureCount;
		board = moveResult(move, board);
		
		int cap = getCapturedPositions(move.P);
		captureCount = cap;
		
		// return the number of captures
		return cap-pos;
	}
	
	public static int getCapturedPositions(char[][] board) {
		return getCapturedPositions(board, null);
	}
	
	/**
	 * updates the player's internal state given a move
	 * @param move Move
	 * @param p player ID
	 * @return the number of positions captured
	 */
	public static int getCapturedPositions(char[][] board, Integer p) {
		int temp = 0;
		int size = board.length;
		// iterate through each capturable position and check if it has 
		// been captured
		for (int i = 1; i < size; i += 2) {
			for (int j = 1; j < size; j += 2) {
					if ((board[i-1][j-1] == 'R' || board[i-1][j-1] == 'B') &&
						(board[i  ][j-1] == 'R' || board[i  ][j-1] == 'B') &&
						(board[i-1][j  ] == 'R' || board[i-1][j  ] == 'B') &&
						(board[i+1][j+1] == 'R' || board[i+1][j+1] == 'B') &&
						(board[i  ][j+1] == 'R' || board[i  ][j+1] == 'B') &&
						(board[i+1][j  ] == 'R' || board[i+1][j  ] == 'B')) {
						temp++;
						if (p != null) {
							board[i][j] = (p == Piece.BLUE) ? 'b' : 'r';
						}
					}
				}
			}
		
		return temp;
	}
	
	/**
	 * updates the player's internal state given a move
	 * @param move Move
	 * @return the number of positions captured
	 */
	private int getCapturedPositions(int p) {
		int temp = 0;
		
		for (int i = 1; i < size; i += 2) {
			for (int j = 1; j < size; j += 2) {
					if ((board[i-1][j-1] == 'R' || board[i-1][j-1] == 'B') &&
						(board[i  ][j-1] == 'R' || board[i  ][j-1] == 'B') &&
						(board[i-1][j  ] == 'R' || board[i-1][j  ] == 'B') &&
						(board[i+1][j+1] == 'R' || board[i+1][j+1] == 'B') &&
						(board[i  ][j+1] == 'R' || board[i  ][j+1] == 'B') &&
						(board[i+1][j  ] == 'R' || board[i+1][j  ] == 'B')) {
						temp++;
						board[i][j] = (p == Piece.BLUE) ? 'b' : 'r';
					}
					
				}
			}
		
		return temp;
	}
	
	/**
	 * generates a list of moves for a tree
	 * @param board
	 * @param p the player
	 * @return moves
	 */
	public static List<Move> getMoves(char[][] board, int p) {
		
		List<Move> 	temp = new ArrayList<Move> ();
		Move		tempMove;
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == '+') {
					tempMove = new Move();
					tempMove.Col = j;
					tempMove.Row = i;
					tempMove.P = p;
					
					temp.add(tempMove);
				}
			}
		}
		
		return temp;
	}
	
	/**
	 * specifies whether the move results in a capture (so we know which
	 * player's turn it is to take)
	 * @param board
	 * @param p the player
	 * @return moves
	 */
	public static boolean isCaptureMove(char[][] board, Move move) {

		int count = getCapturedPositions(board);

		char[][] newBoard = moveResultNew(move, board);

		return (count != getCapturedPositions(newBoard));
		
	}
	
	/**
	 * returns whether the player has won
	 * @return the winning code
	 */
	public int getWinner() {
		
		int blue = 0;
		int red = 0;
		
		// check through and find the number of captures
		for (int i = 1; i < size; i += 2) {
			for (int j = 1; j < size; j += 2) {
				if (board[i-1][j] != '-') {
				if (board[i][j] == 'r') red++;
				if (board[i][j] == 'b') blue++;
				}
			}
		}
		
		if (boundBoardSize == 3 && (blue + red) < maxCap3) 
			return Piece.EMPTY;
		if (boundBoardSize == 2 && (blue + red) < maxCap2) 
			return Piece.EMPTY;
		
		if (blue > red) return Piece.BLUE;
		else			return Piece.RED;
	}
	
	/**
	 * returns whether the player has won
	 * @param board game board
	 * @return the winning code
	 */
	public static int getWinner(char[][] board) {

		return 0;
	}
	
	/**
	 * updates the board given a move
	 * @param move Move
	 * @param char[][]
	 * @return updated char[][]
	 */
	public static char[][] moveResult(Move move, char[][] board) {
		board[move.Row][move.Col] = (move.P == Piece.RED ? 'R' : 'B');
			// return move
		return board;
	}
	
	/**
	 * returns a new game board reflecting the move made
	 * @param board Board
	 * @return a new board
	 */
	public static char[][] moveResultNew(Move move, char[][] board) {
		char[][] out = cloneBoard(board);
		
		return moveResult(move, out);
	}
	
	/**
	 * returns a clone of the board
	 * @param board
	 * @return new cloned board
	 */
	public static char[][] cloneBoard(char[][] board) {
	    int length = board.length;
	    char[][] newBoard = new char[length][board[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(board[i], 0, newBoard[i], 0, board[i].length);
	    }
	    return newBoard;
	}

	
	//print a representation of your board to stdout
	public void printBoard(PrintStream output) {
		
		for (char[] i : board) {
			for (char j : i) output.print(j);
			output.print("\n");
		}
	}
}

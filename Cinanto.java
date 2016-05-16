package ai;

import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

import aiproj.hexifence.*;

public class Cinanto implements Player{

	//n is the size of the board
	//p is the piece that the player uses, RED or BLUE
	//should initialize the needed parts of the ai
	
	class MinimaxTree  {
		
		private char[][] 			board;
		private Move				move;
		private List<MinimaxTree>	nodes;
		private MinimaxTree			bestMove;
		private boolean 			isMin;
		private static final int		maxScore = 999;
		private static final int		minScore = -999;	
		
		public MinimaxTree() {
			
		}
		
		public int GetScore() {
			if (nodes.isEmpty()) {
				// get the score
				return 0;
			}
			// must return something pls!!!
			if (!isMin) {
				int max = minScore;
				int temp = 0;
				for(MinimaxTree i : nodes) {
					if ((temp = i.GetScore()) > max) {
						max = temp;
						bestMove = i;
					}
				}
				return max;
			}
			
			// for minimum case
			int min = maxScore;
			int temp = 0;
			for(MinimaxTree i : nodes) {
				if ((temp = i.GetScore()) < min) {
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
	
	private char board[][];
	private int  size; 
	private int boundBoardSize;
	private boolean canAccept(int x, int y, int n)
	{
		// return if the coordinate falls within the bounds of the board
		return (Math.abs(x-y) <= 2*n - 1);
	}

	public int init(int n, int p) {
		int i = 0, j = 0;
		size = 4*n -1;
		this.boundBoardSize = n;
		board = new char[size][size];
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
		return 0;
	}

	//should return the move that your ai wants to do
	private char[][] makeBoardState(int p, int x, int y){
		char[][] boardState;
		size = 4*boundBoardSize - 1;
		boardState = cloneBoard(board);
		if (p == 1){
			boardState[x][y] = 'B';
		}
		else{
			boardState[x][y] = 'R';
		}
		
		return boardState;
	}
	
	public Move makeMove() {
		//if (getWinner ==0){			
		//}
		//board[][]
		int i,j;
		for (i = 0; i< size; i++){
			for (j = 0; j<size ; j++){			
				if (canAccept(i,j,boundBoardSize)){
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
		
		return null;
	}

	//m is the move that the opponent made
	public int opponentMove(Move m) {
		return 0;
	}
	public static char[][] cloneBoard(char[][] board) {
	    int length = board.length;
	    char[][] newBoard = new char[length][board[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(board[i], 0, newBoard[i], 0, board[i].length);
	    }
	    return newBoard;
	}

	
	//should check if the board shows that a player won or if an illegal move has been made.
	public int getWinner() {
		int blueCount = 0, redCount= 0;
		// iterate through position on the board
		for (int x = 1; x < size; x += 2){
			for (int y = 1; y < size; y += 2){
				if (canAccept(x,y,boundBoardSize)){
					if (board[x][y]=='R'){
						redCount++;
					}
					else if (board[x][y]=='B'){
						blueCount++;
					}
					else{
						return 0;
					}
				}
			}		
		}
		if (redCount < blueCount){
			return 1; // blue wins
		}
		else if (redCount > blueCount){
			return 2; //red wins
		}
		else {
			return 3; // draw
		}
	}

	//print a representation of your board to stdout
	public void printBoard(PrintStream output) {
		char [][] nBoard;
		nBoard = cloneArray(board);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++){
				System.out.print(newBoard[i][j]);
			}
			System.out.print("\n");
		}		
	}
	
	//m is the move that the opponent made
	public int opponentMove(Move m) {
		if (canCapture(m.Row, m.Col) || isShared(m.Row, m.Col)){
			return 1;
		}
		else if (!(canCapture(m.Row, m.Col))){
			return 0;
		}
		else{
			return -1;
		}
	}
	private boolean canCapture (int x, int y){
		if (board[x][y] == '+'){
		
			if (x%2 == 0 && y%2 == 0)
			{
				return (canGet(x-1,y-1) || canGet(x+1,y+1));
			}
			if (x%2 == 0 && y%2 != 0)
			{
				return ( canGet(x-1,y) || canGet(x+1,y));
			}
			if (x%2 != 0 && y%2 == 0)
			{	
				return (canGet(x,y-1) || canGet(x,y+1));
			}
		}
		return false;
	}
	private boolean isShared(int x, int y)
	{   
		// for each position case, check if the adjacent cells can be captured
		if (x%2 == 0 && y%2 == 0)
		{
			return (board[x][y] == '+' && canGet(x-1,y-1) && canGet(x+1,y+1));
		}
		if (x%2 == 0 && y%2 != 0)
		{
			return (board[x][y] == '+' && canGet(x-1,y) && canGet(x+1,y));
		}
		if (x%2 != 0 && y%2 == 0)
		{
			return (board[x][y] == '+' && canGet(x,y-1) && canGet(x,y+1));
		}
		return true;
	}
}

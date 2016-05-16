package ai;

import java.io.PrintStream;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import aiproj.hexifence.*;

public class Cinanto implements Player{

	//n is the size of the board
	//p is the piece that the player uses, RED or BLUE
	//should initialize the needed parts of the ai
	
/*	class MinimaxTree  {
		
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
*/	
	private char board[][];
	private int  size; 
	private int boundBoardSize;
	private int player;
	private boolean canAccept(int x, int y, int n)
	{
		// return if the coordinate falls within the bounds of the board
		return (Math.abs(x-y) <= 2*n - 1);
	}
	//initialises the board
	public int init(int n, int p) {
		int i = 0, j = 0;
		this.player = p;
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
		
		Random random = new Random();
		Move move = new Move();
		//generate random numbers as columns and rows
		int col = random.nextInt(4*boundBoardSize - 1);
		int row = random.nextInt(4*boundBoardSize - 1);
		
		while (canAccept(row, col, boundBoardSize) == false || board[row][col]=='-') {
			col = random.nextInt(4*boundBoardSize - 1);
			row = random.nextInt(4*boundBoardSize - 1);
		}
		
		move.Col = col;
		move.Row = row;
		move.P = this.player;
		//if cell is capturable capture and update board
		captureCell(player, row, col);
		updateBoard(player, row, col);
		return move;
		
		/*for (i = 0; i< size; i++){
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
		}*/
		
	//	return null;
	}
	// get coordiante of the centre given an edge
	private int getCent(int x, int y){
		int xCent =0, yCent = 0;

		if (yCent<0 || xCent<0 ||yCent > 4*boundBoardSize - 2||xCent>4*boundBoardSize - 2 ){
			if (x%2 == 0 && y%2 == 0){
				xCent = x-1;
				yCent = y-1;
				if (yCent<0 || xCent<0){ 
					xCent = x+1;
					yCent = y+1;
				}
			}
			if (x%2 == 0 && y%2 != 0)
			{	xCent = x-1;
				yCent = y;
				if (yCent<0 || xCent<0){ 
					xCent = x+1; 
					yCent = y;
				}
			}
			if (x%2 != 0 && y%2 == 0)
			{	xCent = x;
				yCent = y-1;
				if (yCent<0 || xCent<0){ 
					xCent = x; 
					yCent = y+1;
				}
			}
		}
		return (x*10 + y);
	}
	// change the '-' into 'r' ot 'b' depending on which player captured the cell
	private void captureCell(int p, int x, int y){
		int xCent = 0, yCent = 0;
		System.out.println("uptohere");
		if (canCapture(x,y)&& canAccept(x,y, boundBoardSize)){
			xCent = getCent(x,y)/10;
			yCent = getCent(x,y)%10;
			System.out.print(xCent +" "+ yCent);
			if (p == 1){
			board[xCent][yCent] = 'b';
			}
			else{
			board[xCent][yCent] = 'r';
			}
		}
	}
	//update baord after a move
	private void updateBoard(int p, int x, int y){
		if (p == 1){
			board[x][y] = 'B';
		}
		else{
			board[x][y] = 'R';
		}
		
	}
	//check if edge is shared between two cells
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
	//check if cell can be captured
	private boolean canGet(int xCent, int yCent)
	{
		if (yCent<0 || xCent<0 ||yCent > 4*boundBoardSize - 2||xCent>4*boundBoardSize - 2 ){
			return false;
		}
		int emptyCount = 0;
		// iterate through the surrounding positions
        for (int i = -1; i <= 1; i++)
        {
        	for (int j = -1; j <= 1; j++)
        	{
        		// for each edge, if it is empty (signified by +), if so add to the count of empty edges
        		if (i+j!=0 && board[xCent+i][yCent+j] == ('+'))
        		{	
        			emptyCount += 1;
        			// return false if there is more than one empty edge
        			if (emptyCount > 1)
        			{    
        				return false;
        			}
        		}
        	}
 
        }
        // if there is only one empty edge, it is capturable
        if (emptyCount == 1)
        {	
        	return true;
        }
        // if this is reached, it must be false (empty count is 0)
        return false;
	}
	private boolean canCapture (int x, int y){
		if (board[x][y] == '+'&& canAccept(x,y, boundBoardSize)){
			
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

	//m is the move that the opponent made
	//check if opponent move is valid, capture cell if can be captured, update board
	public int opponentMove(Move m) {
		if (canCapture(m.Row, m.Col) || isShared(m.Row, m.Col)){
			captureCell(m.P, m.Row, m.Col);
			updateBoard(m.P, m.Row, m.Col);
			return 1;
		}
		else if (!(canCapture(m.Row, m.Col))){
			updateBoard(m.P, m.Row, m.Col);
			return 0;
		}
		else{
			return -1;
		}
	}
	// to copy board for future use when want to generate children for trees
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
/*
	//find out the coordinate of the adjacent centre
	private int otherCenter(int x, int y, int xCent, int yCent)
	{   
		int xPartner = 0, yPartner = 0;
		int adjXCent = 0, adjYCent = 0;
		xPartner = xCent - x;
		yPartner = yCent - y;
		adjXCent = x - xPartner;
		adjYCent = y - yPartner;
		return 0;
	}
	private boolean isChain(){
		for (int xCent = 1; xCent < 4*boundBoardSize -1; xCent += 2)
		{
			for (int yCent = 1; yCent < 4*boundBoardSize -1; yCent += 2)
			{
				if (canAccept(xCent,yCent) && board[xCent][yCent] == '-' && canChain(xCent,yCent))
				{ 
					
					
					
					
					
					
				}
			}		
		} 
		
		return true;
	}
	//is it part of a chain. argument centre
	public boolean canChain(int xCent, int yCent)
	{
		int emptyCount = 0;
		// iterate through the surrounding positions
        for (int i = -1; i <= 1; i++)
        {
        	for (int j = -1; j <= 1; j++)
        	{
        		// for each edge, if it is empty (signified by +), if so add to the count of empty edges
        		if (i+j!=0 && board[xCent+i][yCent+j] == ('+'))
        		{
        			emptyCount += 1;
        			chain.x
        			// return false if there is more than two empty edge
        			if (emptyCount > 2)
        			{    
        				return false;
        			}
        		}
        	}
 
        }
        // if there is only one empty edge, it is capturable
        if (emptyCount == 2)
        {
        	return true;
        }
        // if this is reached, it must be false (empty count is 0)
        return false;
	}*/

	//print a representation of your board to stdout
	public void printBoard(PrintStream output) {
		char [][] nBoard;
		nBoard = cloneBoard(board);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++){
				System.out.print(this.board[i][j]);
			}
			System.out.print("\n");
		}		
	}
}

package ai;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ai.*;

public class Cinanto implements Player{

	//n is the size of the board
	//p is the piece that the player uses, RED or BLUE
	//should initialize the needed parts of the ai
		
	public char board[][];
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
	private static boolean validPos(int x, int y, int n) {
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
		
		while (!boardValidPos(x,y)) {
			x = (int)(Math.random() * size);
			y = (int)(Math.random() * size);
		}
		
		Move temp = new Move();
		temp.Col = x;
		temp.Row = y;
		temp.P = p;
		
		moveResult(temp);
		
		return temp;
		
	}

	/**
	 * function called to reflect the opponent's move
	 * @param m the opponent's move
	 * @return int code reflecting the move state
	 */
	public int opponentMove(Move m) {
		
		// check if the position is valid
		if (!validPos(m.Col, m.Row, boundBoardSize))
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
	
	/**
	 * updates the player's internal state given a move
	 * @param move Move
	 * @return the number of positions captured
	 */
	public static int getCapturedPositions(char[][] board, int size) {
		int temp = 0;
		
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
		
		System.out.println(blue);
		
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
		board[move.Col][move.Row] = (move.P == Piece.RED ? 'R' : 'B');
			// return move
		return board;
	}
	
	public static char[][] cloneBoard(char[][] board) {
	    int length = board.length;
	    char[][] newBoard = new char[length][board[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(board[i], 0, newBoard[i], 0, board[i].length);
	    }
	    return newBoard;
	}

	private static int boardSize(char[][] boardContent) {
		return (boardContent.length + 1) / 4;
	}
	
	//print a representation of your board to stdout
	public void printBoard(PrintStream output) {
		
		for (char[] i : board) {
			for (char j : i) output.print(j);
			output.print("\n");
		}
	}
}

	/*
	public static int getWinner(char[][] boardContent) {
		int blueCount = 0, redCount = 0;
		// iterate through position on the board
		for (int x = 1; x < boardSize(boardContent); x += 2){
			for (int y = 1; y < boardSize(boardContent); y += 2){
				if (boardContent[x][y]=='R'){
					redCount++;
				}
				else if (boardContent[x][y]=='B'){
					blueCount++;
				}
				else{
					return 0;
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
*/


/*

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

	private char board[][];
	private int  size; 
	private int boundBoardSize;
	private int player;
	private static boolean canAccept(int x, int y, int n)
	{
		// return if the coordinate falls within the bounds of the board
		return (Math.abs(x-y) <= 2*n - 1);
	}

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

	
	public Move makeMove() {
		//if (getWinner ==0){			
		//}
		//board[][]
		int i,j;
		
		Random random = new Random();
		Move move = new Move();
		
		int col = random.nextInt(4*boundBoardSize - 1);
		int row = random.nextInt(4*boundBoardSize - 1);
		
		while (canAccept(row, col, boundBoardSize) == false || board[row][col]=='-') {
			col = random.nextInt(4*boundBoardSize - 1);
			row = random.nextInt(4*boundBoardSize - 1);
		}
		
		move.Col = col;
		move.Row = row;
		move.P = this.player;
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
	
	/*
	private static int isCapturedSquare(int x, int y, char[][] board, Integer p) {
		
		if (x%2 == 1 && y%2 == 1) {
			if (char[x-1][y-1] != '+' &&
				char[x  ][y-1] != '+' &&
				char[x-1][y  ] != '+' &&
				char[x+1][y+1] != '+' &&
				char[x  ][y+1] != '+' &&
				char[x+1][y  ] != '+') {
				return p;
			}
		}
	}
	
	private static int isCapturedMove(int x, int y, char[][] board, Integer p) {
		
		int boardSize = board.length - 1;
		
		if (x == 0 && y == 0) 	return isCapturedMove(1		,	 1,board,p);
		
		if (x == 0) 
			if (y % 2 == 0) 	return isCapturedMove(1		,y + 1,board,p);
			else				return isCapturedMove(1		,y	  ,board,p);
		
		if (y == 0)
			if (x % 2 == 0) 	return isCapturedMove(x + 1 ,1    ,board,p);
			else				return isCapturedMove(x		,1	  ,board,p);
		
		if (x == boardSize && y == boardSize)
		
	}
	
	
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
	private void updateBoard(int p, int x, int y){
		if (p == 1){
			board[x][y] = 'B';
		}
		else{
			board[x][y] = 'R';
		}
		
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
	}

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
 */
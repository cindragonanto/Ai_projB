package aiproj.hexifence.cinanto;


import java.util.Scanner;

/**
 * Representation of a Hexifence board
 * @author Cindy Inanto(cinanto), Campbell Wright(campbellw)
 * @version 0.0.1
 * @see HexifenceSolution
 */
public class Board 
{
	private int  hexLayer; 		  // the given N value 
	private char array [][]; 	  // array to keep input
	private int  size; 			  // the size of array
	private int  spaceCount; 	  // how many edges available
	private int  capturableCount; // how many cells are capturable with one move
	
	private static final int N_SIZE_OFFSET    = 3; //board size offset
	private static final int N_SIZE_MULTIPLER = 4; //board size multiplier - how many tiles for a given N
	
	/**
	 * construct a board instance for a given size
	 * @param hexLayer int size of the board
	 */
	public Board(int hexLayer) 
	{
		this.hexLayer = hexLayer; // store value of N
		size = N_SIZE_MULTIPLER * hexLayer - 1; 	  // size of array
		array = new char [size][size];
	}
	

	/**
	 * Fill the board based on an input scanner
	 * @param br Scanner to read in from
	 */
	public void	fillBoard(Scanner br)
	{
		spaceCount = 0;
		// iterate through each cell for the given board size
		for (int x = 0; x < size; x++)
		{
			for (int y = 0; y < size; y++)
			{
				// fill the board based on the input
				array[x][y] = br.next().charAt(0);
				// check the if the value is valid
				if (array[x][y] != '+' && 
					array[x][y] != '-' && 
					array[x][y] != 'R' && 
					array[x][y] != 'B')
				{   
					System.out.println("Invalid input");
					System.exit(0);
				}
				// check if the cell corresponds to an empty space
				if (array[x][y]=='+')
				{
					spaceCount ++;   //count avilable edges 
				}                    // separate method would still do same loop
			}                        // count here so loop efficiency is maximised 
		}
	}
	/**
	 * Get the amount of spaces
	 * @return int quantity of spaces
	 */
	public int getSpaceCount()
	{
		return spaceCount;
	}
	
	/**
	 * Get the maximum number of cells capturable in a single move
	 * @return int max number of cells capturable in a single move
	 */
	public int maxCellCapturable()
	{
		// if no cells are available return 0
	    if (getCapturableCount()==0){
	    	return 0;
	    } 
	    // otherwise at least one cell must be capturable
	    
		// check through each cell in the board, and check if it can be captured, and if it contains a shared edge
		for ( int x = 1; x < N_SIZE_MULTIPLER *hexLayer - N_SIZE_OFFSET; x++)
		{
			if (x%2!=0){
				for ( int y = 2; y < N_SIZE_MULTIPLER *hexLayer - N_SIZE_OFFSET; y+=2 )
				{
					// if the cell has a shared edge, it must mean two cells can be captured simultaneously
					if (canAccept(x,y) && isShared(x,y))
					{
						return 2;
					}
				}
			}
			else
			{
				for ( int y = 1; y < N_SIZE_MULTIPLER *hexLayer - N_SIZE_OFFSET; y++ )
				{   
					if (canAccept(x,y) && isShared(x,y))
					{
						return 2;
					}
				}
			}
		}
		// since neither two nor zero cells are capturable at the same time, only one cell may be captured
		return 1;
	}
	/**
	 * Get the number of cells capturable in this move
	 * @return int number of cells capturable in this move
	 */
	public int getCapturableCount()
	{
		capturableCount = 0;
		// iterate through position on the board
		for (int x = 1; x < size; x += 2)
		{
			for (int y = 1; y < size; y += 2)
			{
				// check if it can be captured
				if (canAccept(x,y) && canGet(x, y))
				{ 
					capturableCount++;
				}
			}		
		} 
		return capturableCount;
	}
	
	public static boolean canAccept(int x, int y, int size) {
		return (Math.abs(x-y) < 2*size - 1);
	}
	
	/**
	 * check the validity of coordinates
	 * @param x int x-coordinate
	 * @param y int y-coordinate
	 * @return if the coordinate is valid
	 */
	private boolean canAccept(int x, int y)
	{
		// return if the coordinate falls within the bounds of the board
		return (Math.abs(x-y) < 2*hexLayer - 1);
	}
	
	/**
	 * check if the coordinate is shared by another cell
	 * @param x int x-coordinate
	 * @param y int y-coordinate
	 * @return if the edge is shared by another cell
	 */
	private boolean isShared(int x, int y)
	{   
		// for each position case, check if the adjacent cells can be captured
		if (x%2 == 0 && y%2 == 0)
		{
			return (array[x][y] == '+' && canGet(x-1,y-1) && canGet(x+1,y+1));
		}
		if (x%2 == 0 && y%2 != 0)
		{
			return (array[x][y] == '+' && canGet(x-1,y) && canGet(x+1,y));
		}
		if (x%2 != 0 && y%2 == 0)
		{
			return (array[x][y] == '+' && canGet(x,y-1) && canGet(x,y+1));
		}
		return true;
	}
	
	public static boolean isShared(int x, int y, char[][] array) {
		// for each position case, check if the adjacent cells can be captured
		if (x%2 == 0 && y%2 == 0)
		{
			return (array[x][y] == '+' && canGet(x-1,y-1, array) && canGet(x+1,y+1, array));
		}
		if (x%2 == 0 && y%2 != 0)
		{
			return (array[x][y] == '+' && canGet(x-1,y, array) && canGet(x+1,y, array));
		}
		if (x%2 != 0 && y%2 == 0)
		{
			return (array[x][y] == '+' && canGet(x,y-1, array) && canGet(x,y+1, array));
		}
		return true;
	}
	
	public int getSize() {
		return size;
	}
	
	public char[][] getContent() {
		return array;
	}
	
	public static boolean canGet(int x, int y, char[][] array) {
		int emptyCount = 0;
		// iterate through the surrounding positions
        for (int i = -1; i <= 1; i++)
        {
        	for (int j = -1; j <= 1; j++)
        	{
        		// for each edge, if it is empty (signified by +), if so add to the count of empty edges
        		if (i+j!=0 && array[x+i][y+j] == ('+'))
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
		
	/**
	 * check if a cell can be captured with one move
	 * @param x int x-coordinate
	 * @param y int y-coordinate
	 * @return if the cell can be captured with one move
	 */
	private boolean canGet(int x, int y)
	{
		int emptyCount = 0;
		// iterate through the surrounding positions
        for (int i = -1; i <= 1; i++)
        {
        	for (int j = -1; j <= 1; j++)
        	{
        		// for each edge, if it is empty (signified by +), if so add to the count of empty edges
        		if (i+j!=0 && array[x+i][y+j] == ('+'))
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

}
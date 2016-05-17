package ai;
 
import java.util.List;
import java.util.Scanner;
 
/**
 * Solution for Hexifence
 * @author Cindy Inanto(cinanto), Campbell Wright(campbellw)
 * @version 0.0.1
 */
public class HexifenceSolution {
       
    public static void main(String args[] ) throws Exception {

        Cinanto player = new Cinanto();

    	player.init(2, 2);

    	player.printBoard(System.out);

    	Move temp;
		
    	for (int i = 0; i < 3; i++) {
    		
        	temp = player.makeMove();
        	
    	}
    	HexifenceMinimaxTree.playerP = Piece.RED;
    	Move tempMove = HexifenceMinimaxTree.HexifenceMinimaxMove(player.board, 2);

    	// THIS!!!!!!!!!!!!!
    	HexifenceMinimaxTree.PrintMove(tempMove);    	
    	player.board[tempMove.Row][tempMove.Col] = 'Q';
    	player.printBoard(System.out);
		
    }
}
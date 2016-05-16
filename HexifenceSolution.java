package ai;
 
import java.util.Scanner;
 
/**
 * Solution for Hexifence
 * @author Cindy Inanto(cinanto), Campbell Wright(campbellw)
 * @version 0.0.1
 */
public class HexifenceSolution {
       
    public static void main(String args[] ) throws Exception {

        Cinanto player = new Cinanto();

    	player.init(3, 2);

    	player.printBoard(System.out);

    	Move temp;
		
    	for (int i = 0; i < 72; i++) {
    		
        	temp = player.makeMove();
        	System.out.println(temp.Col + ", " + temp.Row);
        	
    	}
    	
    	System.out.println("Winner - " + player.getWinner());
    	
    	player.printBoard(System.out);
		
    }
}
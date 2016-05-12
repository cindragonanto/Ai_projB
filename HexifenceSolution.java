package ai;
 
import java.util.Scanner;
 
/**
 * Solution for Hexifence
 * @author Cindy Inanto(cinanto), Campbell Wright(campbellw)
 * @version 0.0.1
 */
public class HexifenceSolution {
       
    public static void main(String args[] ) throws Exception {
       
        Scanner br = new Scanner(System.in);
        int dim = 0;
        
        // read in the input
        try
        {
        dim = br.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Invalid input");
            System.exit(1);
        }
        
        // check if the input length is valid
        if (dim != 2 && dim != 3){
            System.out.println("Invalid input");
            System.exit(1);
        }
        
        // create a board and fill it
        Board board = new Board(dim);
        board.fillBoard(br);       
        
        br.close();

        // obtain the statistics
        int fillable = board.getSpaceCount();
        int capturable = board.getCapturableCount();
		int maxCap = board.maxCellCapturable();

		// output the statistics
        System.out.println("fillable = " + fillable);       
        System.out.println("max cap = " + maxCap);
        System.out.println("capturable = " +capturable);
    }
}
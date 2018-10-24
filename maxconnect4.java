import java.io.*;
import java.util.*;
/**
 * 
 * @author James Spargo
 * This class controls the game play for the Max Connect-Four game. 
 * To compile the program, use the following command from the maxConnectFour directory:
 * javac *.java
 *
 * the usage to run the program is as follows:
 * ( again, from the maxConnectFour directory )
 *
 *  -- for interactive mode:
 * java MaxConnectFour interactive [ input_file ] [ computer-next / human-next ] [ search depth]
 *
 * -- for one move mode
 * java maxConnectFour.MaxConnectFour one-move [ input_file ] [ output_file ] [ search depth]
 * 
 * description of arguments: 
 *  [ input_file ]
 *  -- the path and filename of the input file for the game
 *  
 *  [ computer-next / human-next ]
 *  -- the entity to make the next move. either computer or human. can be abbreviated to either C or H. This is only used in interactive mode
 *  
 *  [ output_file ]
 *  -- the path and filename of the output file for the game.  this is only used in one-move mode
 *  
 *  [ search depth ]
 *  -- the depth of the minimax search algorithm
 * 
 *   
 */
/*1001656413*/
public class maxconnect4
{
	//Method implementing the interactive mode 
	public static void interactive(GameBoard currentGame,String next_player,int depthLevel){
		
		int playColumn = 99;				//  the players choice of column to play
    	boolean playMade = false;
		int current_player = currentGame.getCurrentTurn();
		//Checking the current player(1 or 2)?
		System.out.println("Next Player:"+current_player);
		AiPlayer calculon = new AiPlayer();
	   	//Check the number of pieces in the board, continue if <42
	   	if( currentGame.getPieceCount() < 42 ){
	       //Execute if the next player is human
	       if(next_player.equals("human-next")){
				int piece_col;
				System.out.println("Enter the column to put the piece(0-6)");
				Scanner in =new Scanner(System.in);
				piece_col=in.nextInt();			
				playColumn= piece_col;
				//Checking the validity of the move
				while( !currentGame.isValidPlay( piece_col ) ){
					System.out.println("Invalid, Try again");
				    System.out.println("Enter the column to put the piece(0-6)");
					Scanner inp =new Scanner(System.in);
					piece_col=inp.nextInt();
				    playColumn = piece_col;
				}
				System.out.println("Human:"+current_player);
			}
			//Execute if the next player is computer
			if(next_player.equals("computer-next")){
				//Call the method of AiPlayer, which returns the move to be made by computer
				int play = calculon.findBestPlay( currentGame,depthLevel );
				playColumn=play;
				System.out.println("Computer:"+current_player);
			}		
			// play the piece
			currentGame.playPiece( playColumn );
	       	//Execute if the next player is computer, saving the board state after the move to computer.txt
	       	if(next_player.equals("computer-next")){	       
	        	currentGame.printGameBoardToFile( "computer.txt" );
	    	}
	    	//Execute if the next player is computer, saving the board state after the move to human.txt
	    	else{
	    		currentGame.printGameBoardToFile( "human.txt" );	
	    	}
	        // display the current game board
	        System.out.println("move " + currentGame.getPieceCount() 
	                           + ": Player " + current_player
	                           + ", column " + playColumn);
	        System.out.print("game state after move:\n");
	        currentGame.printGameBoard();
	    	// print the current scores
	        System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +
	                            ", Player2 = " + currentGame.getScore( 2 ) + "\n " );
	       	//If the board is full,exit
	        if(currentGame.BoardFull()){
	        	System.out.println("***********************GAME OVER*******************************");
	        	exit_function(0);
	        }
	        //If the current player was computer, then execute and make the next-player as human
	        if(next_player.equals("computer-next")){
				GameBoard currentG= new GameBoard("computer.txt");
		       	next_player="human-next";
		       	//Recursively calling the interactive method,to continue playing
		        interactive(currentG,next_player,depthLevel);
		    }
	    	//If the current player was human, then execute and make the next-player as computer
	    	else if(next_player.equals("human-next")){
		    	GameBoard currentG= new GameBoard("human.txt");
		       	next_player="computer-next";
		       	//Recursively calling the interactive method,to continue playing
		       	interactive(currentG,next_player,depthLevel);    		
	    	}  
	    }
		  
    }
  public static void main(String[] args) 
  {
    // check for the correct number of arguments
    if( args.length != 4 ) 
    {
      System.out.println("Four command-line arguments are needed:\n"
                         + "Usage: java [program name] interactive [input_file] [computer-next / human-next] [depth]\n"
                         + " or:  java [program name] one-move [input_file] [output_file] [depth]\n");

      exit_function( 0 );
     }
		
    // parse the input arguments
    String game_mode = args[0].toString();				// the game mode
    String input = args[1].toString();					// the input game file
    int depthLevel = Integer.parseInt( args[3] );  		// the depth level of the ai search
		
    // create and initialize the game board
    GameBoard currentGame = new GameBoard( input );
    
    // create the Ai Player
    AiPlayer calculon = new AiPlayer();
		
    //  variables to keep up with the game
    int playColumn = 99;				//  the players choice of column to play
    boolean playMade = false;			//  set to true once a play has been made
    System.out.print("\nMaxConnect-4 game\n");
    System.out.print("game state before move:\n");
    
    //print the current game board
    currentGame.printGameBoard();
    // print the current scores
    System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +
			", Player2 = " + currentGame.getScore( 2 ) + "\n " );
    //If interactive, then call the method interactive
    if(game_mode.equalsIgnoreCase( "interactive" )){
	  String next_player = args[2].toString();
      interactive(currentGame,next_player,depthLevel);
	   //return;
    } 
    //If one-move, then execute
    else if(game_mode.equalsIgnoreCase( "one-move" )){
	    String output = args[2].toString();				// the output game file
	     
	    // ****************** this chunk of code makes the computer play
	    if( currentGame.getPieceCount() < 42 ) 
	    {
	        int current_player = currentGame.getCurrentTurn();
		// AI play - Depth limited minimax including alpha beta pruning and get the move to make
		playColumn = calculon.findBestPlay( currentGame,depthLevel);
		
		// play the piece
		currentGame.playPiece( playColumn );
	        	
	        // display the current game board
	        System.out.println("move " + currentGame.getPieceCount() 
	                           + ": Player " + current_player
	                           + ", column " + playColumn);
	        System.out.print("game state after move:\n");
	        currentGame.printGameBoard();
	    
	        // print the current scores
	        System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +
	                            ", Player2 = " + currentGame.getScore( 2 ) + "\n " );
	        //Save the board state to the output
	        currentGame.printGameBoardToFile( output );
	    }

    	else{
			System.out.println("\nI can't play.\nThe Board is Full\n\nGame Over");
    	}
	    //************************** end computer play
	}
	else if( !game_mode.equalsIgnoreCase( "one-move" ) && !game_mode.equalsIgnoreCase( "interactive" ) ){
      System.out.println( "\n" + game_mode + " is an unrecognized game mode \n try again. \n" );
      return;
    } 		
    
    return;
    
} // end of main()
	
  /**
   * This method is used when to exit the program prematurly.
   * @param value an integer that is returned to the system when the program exits.
   */
  private static void exit_function( int value )
  {
      System.out.println("exiting from MaxConnectFour.java!\n\n");
      System.exit( value );
  }
} // end of class connectFour
import java.io.*;
import java.util.*;
/**
 * This is the Gameboard class.  It implements a two dimension array that
 * represents a connect four gameboard. It keeps track of the player making
 * the next play based on the number of pieces on the game board. It provides
 * all of the methods needed to implement the playing of a max connect four
 * game.
 * 
 * @author James Spargo
 *
 */

public class GameBoard implements Cloneable
{
    // class fields

    private int[][] playBoard;
    private int pieceCount;
    private int currentTurn;
    private int [][] action;
    private static int row=5,col=0,mxrow=0,mxcol=0;
    private  static int maxsucc=0,d=0,play,maxplayer,minplayer;
   	private int [][] succBoard;
    private int [][] tempBoard;
    private static int p,q,w; 
   
    /**
     * This constructor creates a GameBoard object based on the input file
     * given as an argument. It reads data from the input file and provides
     * lines that, when uncommented, will display exactly what has been read
     * in from the input file.  You can find these lines by looking for 
     * 
     * @param inputFile the path of the input file for the game
     */
    public GameBoard( String inputFile ){
		this.playBoard = new int[6][7];

		this.pieceCount = 0;
		int counter = 0;
		BufferedReader input = null;
		String gameData = null;

		// open the input file
		try 
		{
		    input = new BufferedReader( new FileReader( inputFile ) );
		} 
	    catch( IOException e ) 
		{
		    System.out.println("\nProblem opening the input file!\nTry again." +
				       "\n");
		    e.printStackTrace();
		}

		//read the game data from the input file
		for(int i = 0; i < 6; i++) 
		{
		    try 
		    {
			gameData = input.readLine();
			
			// testing
			// uncomment the next 2 lines to see the whole line read in
			//System.out.println("I just read ->" + gameData + "<- " +
			//		"outer for loop");
			
			// read each piece from the input file
				for( int j = 0; j < 7; j++ ){
				    //testing- uncomment the next 3 lines to see each piece
				    // that was read in
				    //System.out.println("I just read ->" +
				    //		( gameData.charAt( counter ) - 48 ) + 
				    //		"<- inner for loop");
				    
				    this.playBoard[ i ][ j ] = gameData.charAt( counter++ ) - 48;
				    
				    // sanity check
				    if( !( ( this.playBoard[ i ][ j ] == 0 ) ||
					   ( this.playBoard[ i ][ j ] == 1 ) ||
					   ( this.playBoard[ i ][ j ] == 2 ) ) ) 
		                    {
					System.out.println("\nProblems!\n--The piece read " +
							   "from the input file was not a 1, a 2 or a 0" );
					this.exit_function( 0 );
				    }
				    
				    if( this.playBoard[ i ][ j ] > 0 ){
						this.pieceCount++;
				    }
				}
		    } 
		    catch( Exception e ){
				System.out.println("\nProblem reading the input file!\n" +
						   "Try again.\n");
				e.printStackTrace();
				this.exit_function( 0 );
		    }

		    //reset the counter
		    counter = 0;
		    
		} // end for loop

		// read one more line to get the next players turn
		try{
		    gameData = input.readLine();
		} 
		catch( Exception e ) 
		{
		    System.out.println("\nProblem reading the next turn!\n" +
				       "--Try again.\n");
		    e.printStackTrace();
		}

		this.currentTurn = gameData.charAt( 0 ) - 48;
		
		//testing-uncomment the next 2 lines to see which current turn was read
		//System.out.println("the current turn i read was->" +
		//		this.currentTurn );

		// make sure the turn corresponds to the number of pcs played already
		if(!( ( this.currentTurn == 1) || ( this.currentTurn == 2 ) ) ) 
		{
		    System.out.println("Problems!\n the current turn read is not a " +
				       "1 or a 2!");
		    this.exit_function( 0 );
		} 
		else if ( this.getCurrentTurn() != this.currentTurn ) 
		{
		    System.out.println("Problems!\n the current turn read does not " +
				       "correspond to the number of pieces played!");
		    this.exit_function( 0 );			
		}
    } // end GameBoard( String )

	
    /**
     * This constructor creates a GameBoard object from another double
     * indexed array.
     * 
     * @param masterGame a dual indexed array
     */
    public GameBoard( int masterGame[][] ){
		
		this.playBoard = new int[6][7];
		this.pieceCount = 0;

		for( int i = 0; i < 6; i++ ) 
		{
		    for( int j = 0; j < 7; j++) 
		    {
			this.playBoard[ i ][ j ] = masterGame[ i ][ j ];
			
			if( this.playBoard[i][j] > 0 )
			{
			    this.pieceCount++;
			}
		   }
		}
    } // end GameBoard( int[][] )

    /**
     * this method returns the score for the player given as an argument.
     * it checks horizontally, vertically, and each direction diagonally.
     * currently, it uses for loops, but i'm sure that it can be made 
     * more efficient.
     * 
     * @param player the player whose score is being requested.  valid
     * values are 1 or 2
     * @return the integer of the players score
     */
    public int getScore( int player ){
		//reset the scores
		int playerScore = 0;

		//check horizontally
		for( int i = 0; i < 6; i++ ){
		    for( int j = 0; j < 4; j++ ){
				if( ( this.playBoard[ i ][j] == player ) &&
				    ( this.playBoard[ i ][ j+1 ] == player ) &&
				    ( this.playBoard[ i ][ j+2 ] == player ) &&
				    ( this.playBoard[ i ][ j+3 ] == player ) ) 
				{
				    playerScore++;
				}
		    }
		} // end horizontal

		//check vertically
		for( int i = 0; i < 3; i++ ) {
		    for( int j = 0; j < 7; j++ ) {
				if( ( this.playBoard[ i ][ j ] == player ) &&
				    ( this.playBoard[ i+1 ][ j ] == player ) &&
				    ( this.playBoard[ i+2 ][ j ] == player ) &&
				    ( this.playBoard[ i+3 ][ j ] == player ) ) {
				    playerScore++;
				}
		    }
		} // end verticle
		
		//check diagonally - backs lash ->	\
		for( int i = 0; i < 3; i++ ){
			for( int j = 0; j < 4; j++ ) {
			    if( ( this.playBoard[ i ][ j ] == player ) &&
				( this.playBoard[ i+1 ][ j+1 ] == player ) &&
				( this.playBoard[ i+2 ][ j+2 ] == player ) &&
				( this.playBoard[ i+3 ][ j+3 ] == player ) ) {
					playerScore++;
			    }
			}
		}
		    
		    //check diagonally - forward slash -> /
		for( int i = 0; i < 3; i++ ){
			for( int j = 0; j < 4; j++ ) {
			    if( ( this.playBoard[ i+3 ][ j ] == player ) &&
				( this.playBoard[ i+2 ][ j+1 ] == player ) &&
				( this.playBoard[ i+1 ][ j+2 ] == player ) &&
				( this.playBoard[ i ][ j+3 ] == player ) ) {
					playerScore++;
			    }
			}
		}// end player score check
		    
		    return playerScore;
    } // end getScore

    /**
     * the method gets the current turn
     * @return an int value representing whose turn it is.  either a 1 or a 2
     */
    public int getCurrentTurn(){
		return ( this.pieceCount % 2 ) + 1 ;
    } // end getCurrentTurn


    /**
     * this method returns the number of pieces that have been played on the
     * board 
     * 
     * @return an int representing the number of pieces that have been played
     * on board alread
     */
    public int getPieceCount(){
		return this.pieceCount;
    }

    /**
     * this method returns the whole gameboard as a dual indexed array
     * @return a dual indexed array representing the gameboard
     */
    public int[][] getGameBoard(){
		return this.playBoard;
    }
    /*---------------------1001656413 Additions for alpha beta decision with depth limited-----------------------------------------*/
	//Constructor without parameters
	public GameBoard(){
        
        this.playBoard = new int[6][7];
        this.pieceCount = 0;

        for( int i = 0; i < 6; i++ ) 
        {
            for( int j = 0; j < 7; j++) 
            {
            this.playBoard[ i ][ j ] = 0;
           }
        }
    }  
    //Computing the Eval, for the state passsed(if the depth limit is reached)
    public double getEvalFunct( int s[][], int plyr ){
		//reset the score
		double playerScore = 0;
		int ply=plyr;
		//Weights for 4 in a row,3 in a row with a blank, 2 in a row with 2 blanks, 1 in a row and 3 blanks respectively
		double w1=1,w2=0.90,w3=0.50,w4=0.40;
		//Counts of 4 in a row,3 in a row with a blank, 2 in a row with 2 blanks, 1 in a row and 3 blanks respectively 
		int f1=0,f2=0,f3=0,f4=0;
		//check horizontally
		for( int i = 0; i < 6; i++ ){
		    for( int j = 0; j < 4; j++ ){
				//4 in a row horizontally
				if( ( s[ i ][j] == ply ) &&( s[ i ][ j+1 ] == ply ) &&( s[ i ][ j+2 ] == ply ) &&( s[ i ][ j+3 ] == ply ) ){
					f1++;
				}
				//3 in a row horizontally and a blank 
				if( ( s[ i ][j] == ply ) &&( s[ i ][ j+1 ] == ply ) &&( s[ i ][ j+2 ] == ply ) &&( s[ i ][ j+3 ] == 0 ) ){
					f2++;
				}
				else if( ( s[ i ][j] == 0 ) &&( s[ i ][ j+1 ] == ply ) &&( s[ i ][ j+2 ] == ply ) &&( s[ i ][ j+3 ] == ply ) ){
					f2++;
				}
				//2 in a row horizontally and 2 blanks
				if( ( s[ i ][j] == ply ) &&( s[ i ][ j+1 ] == ply ) &&( s[ i ][ j+2 ] == 0 ) &&( s[ i ][ j+3 ] == 0 ) ){
					f3++;
				}
				else if( ( s[ i ][j] == 0 ) &&( s[ i ][ j+1 ] == 0 ) &&( s[ i ][ j+2 ] == ply ) &&( s[ i ][ j+3 ] == ply ) ){
					f3++;
				}
				//3 in a row horizontally and a blank
				if( ( s[ i ][j] == ply ) &&( s[ i ][ j+1 ] == 0 ) &&( s[ i ][ j+2 ] == 0 ) &&( s[ i ][ j+3 ] == 0 ) ){
					f4++;
				}
				else if( ( s[ i ][j] == 0 ) &&( s[ i ][ j+1 ] == 0 ) &&( s[ i ][ j+2 ] == 0 ) &&( s[ i ][ j+3 ] == ply ) ){
					f4++;
				}
			}
		} // end horizontal
		//check vertically
		for( int i = 0; i < 3; i++ ) {
		    for( int j = 0; j < 7; j++ ) {
		    	//4 in a row vertically
				if( ( s[ i ][ j ] == ply ) &&( s[ i+1 ][ j ] == ply ) &&(s[ i+2 ][ j ] == ply ) &&( s[ i+3 ][ j ] == ply ) ) {
				    f1++;
				}
				//3 in a row and a blank vertically
				if( ( s[ i ][ j ] == ply ) &&( s[ i+1 ][ j ] == ply ) &&(s[ i+2 ][ j ] == ply ) &&( s[ i+3 ][ j ] == 0 ) ) {
				    f2++;
				}
				else if( ( s[ i ][ j ] == 0 ) &&( s[ i+1 ][ j ] == ply ) &&(s[ i+2 ][ j ] == ply ) &&( s[ i+3 ][ j ] == ply ) ) {
				    f2++;
				}
				//2 in a row and 2 blanks vertically
				if( ( s[ i ][ j ] == ply ) &&( s[ i+1 ][ j ] == ply ) &&(s[ i+2 ][ j ] == 0 ) &&( s[ i+3 ][ j ] == 0 ) ) {
				    f3++;
				}
				else if( ( s[ i ][ j ] == 0 ) &&( s[ i+1 ][ j ] == 0 ) &&(s[ i+2 ][ j ] == ply ) &&( s[ i+3 ][ j ] == ply ) ) {
				    f3++;
				}
				//1 in a row and 3 blanks vertically
				if( ( s[ i ][ j ] == ply ) &&( s[ i+1 ][ j ] == 0 ) &&(s[ i+2 ][ j ] == 0 ) &&( s[ i+3 ][ j ] == 0 ) ) {
				    f4++;
				}
				else if( ( s[ i ][ j ] == 0 ) &&( s[ i+1 ][ j ] == 0 ) &&(s[ i+2 ][ j ] == 0 ) &&( s[ i+3 ][ j ] == ply ) ) {
				    f4++;
				}
		    }
		} // end verticle
		//check diagonally - backs lash ->	\
		for( int i = 0; i < 3; i++ ){
			for( int j = 0; j < 4; j++ ) {
			    //4 in a row diagonally(\)
			    if( ( s[ i ][ j ] == ply ) &&( s[ i+1 ][ j+1 ] == ply) &&( s[ i+2 ][ j+2 ] == ply ) &&( s[ i+3 ][ j+3 ] == ply ) ) {
					f1++;
			    }
			    //3 in a row diagonally(\) and a blank
			     if( ( s[ i ][ j ] == ply ) &&( s[ i+1 ][ j+1 ] == ply) &&( s[ i+2 ][ j+2 ] == ply ) &&( s[ i+3 ][ j+3 ] == 0 ) ) {
					f2++;
			    }
			     else if( ( s[ i ][ j ] == 0 ) &&( s[ i+1 ][ j+1 ] == ply) &&( s[ i+2 ][ j+2 ] == ply ) &&( s[ i+3 ][ j+3 ] == ply ) ) {
					f2++;
			    }
			    //2 in a row diagonally(\) and 2 blanks
			     if( ( s[ i ][ j ] == ply ) &&( s[ i+1 ][ j+1 ] == ply) &&( s[ i+2 ][ j+2 ] == 0 ) &&( s[ i+3 ][ j+3 ] == 0 ) ) {
					f3++;
			    }
			    else if( ( s[ i ][ j ] == 0 ) &&( s[ i+1 ][ j+1 ] == 0) &&( s[ i+2 ][ j+2 ] == ply ) &&( s[ i+3 ][ j+3 ] == ply ) ) {
					f3++;
			    }
			    //1 in a row diagonally (\) and a blank
			     if( ( s[ i ][ j ] == ply ) &&( s[ i+1 ][ j+1 ] == 0) &&( s[ i+2 ][ j+2 ] == 0 ) &&( s[ i+3 ][ j+3 ] == 0 ) ) {
					f4++;
			    }
			    else if( ( s[ i ][ j ] == 0 ) &&( s[ i+1 ][ j+1 ] == 0) &&( s[ i+2 ][ j+2 ] == 0 ) &&( s[ i+3 ][ j+3 ] == ply ) ) {
					f4++;
			    }
			}
		}
		//check diagonally - forward slash -> /
		for( int i = 0; i < 3; i++ ){
			for( int j = 0; j < 4; j++ ) {
			    //4 in a row diagonally(/)
			    if( ( s[ i+3 ][ j ] == ply ) &&( s[ i+2 ][ j+1 ] == ply ) &&( s[ i+1 ][ j+2 ] == ply ) &&( s[ i ][ j+3 ] == ply ) ) {
					f1++;
			    }
			    //3 in a row diagonally(/)
				if( ( s[ i+3 ][ j ] == ply ) &&( s[ i+2 ][ j+1 ] == ply ) &&( s[ i+1 ][ j+2 ] == ply ) &&( s[ i ][ j+3 ] == 0 ) ) {
					f2++;
			    }
			    else if( ( s[ i+3 ][ j ] == 0 ) &&( s[ i+2 ][ j+1 ] == ply ) &&( s[ i+1 ][ j+2 ] == ply ) &&( s[ i ][ j+3 ] == ply ) ) {
					f2++;
			    }
			    //2 in a row and 2 blanks
			    if( ( s[ i+3 ][ j ] == ply ) &&( s[ i+2 ][ j+1 ] == ply ) &&( s[ i+1 ][ j+2 ] == 0 ) &&( s[ i ][ j+3 ] == 0 ) ) {
					f3++;
			    }
			    else if( ( s[ i+3 ][ j ] == 0 ) &&( s[ i+2 ][ j+1 ] == 0 ) &&( s[ i+1 ][ j+2 ] == ply ) &&( s[ i ][ j+3 ] == ply ) ) {
					f3++;
			    }
			    //1 in a row and 3 blanks
			    if( ( s[ i+3 ][ j ] == ply ) &&( s[ i+2 ][ j+1 ] == 0 ) &&( s[ i+1 ][ j+2 ] == 0 ) &&( s[ i ][ j+3 ] == 0 ) ) {
					f4++;
			    }
			    else if( ( s[ i+3 ][ j ] == 0 ) &&( s[ i+2 ][ j+1 ] == 0 ) &&( s[ i+1 ][ j+2 ] == 0 ) &&( s[ i ][ j+3 ] == ply ) ) {
					f4++;
			    }
			}
		}// end player score check
		//Compute the Eval score total
		playerScore=((f1*w1)+(f2*w2)+(f3*w3)+(f4*w4));
		return playerScore;
    } 
    //Check if Terminal state 
    public  Boolean terminal(int[][] s){
        for(int q=0;q<6;q++){
         	for(int w=0;w<7;w++){
         		//If any empty ,then board not terminal
         		 if(s[q][w]==0)return false;  
         	}  	
        }
        return true;
    }
    //maxvalue function
    public  double maxvalue(double alpha,double beta,int d,int depth){
		play=maxplayer;
		//If terminal state or depth level reached, then return the score
        if((d==(depth))||terminal(this.playBoard)){
        	//Get the score from eval method(Score of max - Score of min)
        	double esum=getEvalFunct(this.playBoard,maxplayer)-getEvalFunct(this.playBoard,minplayer);
        	return esum;
        }
        double t=0;
        int z=0;
	    int g[][],sc[][];
	    //Current gameboard state is stored
	   	g=this.getGameBoard();
	    GameBoard []suc=new GameBoard[7];
	     //Get the number of successors for this board state
	    int gc=this.getNumberofSucc();
	    col=0;
	    row=5;
	    //Retrieving the successors for the current state
	    for(z=0;z<gc;z++){
			//Temporary object to storing the state
			GameBoard temp=new GameBoard();
		    suc[z] = new GameBoard();
		    temp.playBoard=g;
		     //Calling the successor function- one successor
		    temp.successorFn();
		    sc=temp.getGameBoard();
		    //Setting the successors
		    for(int i=0;i<6;i++){
		    
		    	for(int j=0;j<7;j++){
		    	   suc[z].playBoard[i][j]=sc[i][j];		
		    	}
			}		    
			temp.removePieces(row,col);
		    col++;
	    } 
	    double val=Double.NEGATIVE_INFINITY;
	    //Max calling the minvalue function for each successor
	    for(z=0;z<gc;z++){
	    	t=suc[z].minvalue(alpha,beta,d+1,depth);
	    	if(val<t){
    			val=t;
    		}
    		//Alpha-Beta Pruning,if satisfies
    		if(val>=beta){
    			return val;
    		}
    		play=maxplayer;
    		if(alpha<val){
    			alpha=val;
    		}
	    }
    	return val;
    }
    //Method to remove the pieces from board
    public void removePieces(int row, int col){
    	for(int i=0;i<6;i++){
    		for(int j=0;j<7;j++){
    			if(i==row&&j==col){
    				this.playBoard[i][j]=0;
    			}
    		}
    	}
    }
    //minvalue function
    public  double minvalue(double alpha,double beta,int d,int depth){
        play=minplayer;
        //If terminal state or depth level reached, then return the score
        if((d==(depth))|| terminal(this.playBoard)){
        	//Get the score from eval method(Score of max - Score of min)
        	double esum=getEvalFunct(this.playBoard,maxplayer)-getEvalFunct(this.playBoard,minplayer);
        	return esum;
        }
        double t=0;int z=0;
	    int g[][],sc[][];
	    //Current gameboard state is stored
	    g=this.getGameBoard();
	    GameBoard []suc=new GameBoard[7];
	    //Get the number of successors for this board state
	    int gc=this.getNumberofSucc();
	    col=0;
	    row=5;
	    //Retrieving the successors for the current state
	    for(z=0;z<gc;z++){
	    	//Temporary object to storing the state
			GameBoard temp=new GameBoard();
		    suc[z] = new GameBoard();
		    temp.playBoard=g;
		    //Calling the successor function- one successor
		    temp.successorFn();
		    sc=temp.getGameBoard();
		    //Setting the successors 
		    for(int i=0;i<6;i++){
		    	for(int j=0;j<7;j++){
		    	   suc[z].playBoard[i][j]=sc[i][j];		
		    	}
			}		    
			temp.removePieces(row,col);
		    col++;
	    }
	    double val=Double.POSITIVE_INFINITY;
	    //Min calling the maxvalue function for each successor
	    for(z=0;z<gc;z++){
	    	t=suc[z].maxvalue(alpha,beta,d+1,depth);
	    	//Checking if the value of t is lesser than val, then val is set to set
	    	if(val>t){
    			val=t;
    		}
    		//Alpha beta pruning,if condition satisfies
    		if(val<=alpha){
    			return val;
    		}
    		play=minplayer;
    		if(beta>val){
    			beta=val;
    		}
	    }
    	return val;
    }
    //Computes the successor
    public  void successorFn(){
      int i=5;
      int j=col;
      int c=0;
      while(i>=0&&j<7){
           if(this.playBoard[i][j]==0){
                //Makes the move in the successor board accordingly, for max and minplayer
                if(play==1){
                    this.playBoard[i][j]=1;
                    row=i;
                    col=j;
                }
                else{
                	this.playBoard[i][j]=2;
                    row=i;
                    col=j;                            
                }
               break;
            }
            else{
            	i--;
            	if(i<0){
            		i=5;
            		j++;
            	}
            }
        };
    }
    //Retrieves the number of successors for the current state
    public int getNumberofSucc(){
    	int i=0,j=0;
    	int c=0;
    	for(j=0;j<7;j++){
    		for(i=0;i<6;i++){
    			if(this.playBoard[i][j]==0){
    				c++;
    				i=6;
    			}
    		}
    	}
    	return c;
    }
    //Method making the Depth limited minimax including alpha beta pruning 
    public int alphabetaDecision(GameBoard currentGame,int depth){
    	int best=0;
    	double t=0;
    	int z=0;
    	int g[][],sc[][];
    	int pcol=0;
    	//Array to choose column index to be returned
    	int plycol[]={0,0,0,0,0,0,0};
    	//Setting alpha and beta
    	double alpha=Double.NEGATIVE_INFINITY;
    	double beta=Double.POSITIVE_INFINITY;
    	int d=0;
    	//Depth should be minimum 0
    	if(depth==0){
    		System.out.println("Minimum Depth 1");
    		exit_function(0);
    	}
    	row=5;
    	col=0;
    	//Object array to keep the successors
    	GameBoard []suc=new GameBoard[7];
    	//Retrieving the number of successors
    	int gc=currentGame.getNumberofSucc();
    	g=currentGame.getGameBoard();
    	//Get the player making the move(MAX)
    	play=currentGame.getCurrentTurn();
    	//Setting the maxplayer value
    	maxplayer=play;
    	//Setting the minplayer value
    	if(maxplayer==1){
    		minplayer=2;
    	}
    	else minplayer=1;
		//Retrieving the successors for the current state
		for(z=0;z<gc;z++){
			//A temporary board
			GameBoard temp=new GameBoard();
		    suc[z] = new GameBoard();
		    temp.playBoard=g;
		    //Calling the successor function- one successor
		    temp.successorFn();
		    //Setting the column index array
		    plycol[pcol]=col;
		    sc=temp.getGameBoard();
		    //Storing the successor each time 
		    for(int i=0;i<6;i++){
		    	for(int j=0;j<7;j++){
		    	   suc[z].playBoard[i][j]=sc[i][j];		
		    	}
		    }
		    //Taking back the move made		    
		    temp.removePieces(row,col);
		   	col++;
		    pcol++;
    	}
    	double val=Double.NEGATIVE_INFINITY;
    	best=0;
    	pcol=0;
    	//For each successor, calling the minvalue function
    	for(z=0;z<gc;z++){
    		t=suc[z].minvalue(alpha,beta,d+1,depth);
    		//If the value of t is greater than val,then setting new val as t
    		if(val<t){
    			val=t;
    			//index of the col
    			best=plycol[pcol];
    			if(alpha<val){
    				alpha=val;
    			}   			
    			//Alpha beta pruning,if condition satisfies
 				if(val>=beta){
    				return best;
    			}   		
    		}
    		play=maxplayer;
    		pcol++;
    	}
    	//Return the column move to be made	
      return best;
    }
    //Check if the board is full
    public boolean BoardFull(){
    	int i=0,j=0;
    	for(j=0;j<7;j++){
    		for(i=0;i<6;i++){
    			if(this.playBoard[i][j]==0){
    				return false;
    			}
    		}
    	}	
    	return true;
    }
    /*---------------------End of changes 1001656413 made-----------------------------------------*/
    /**
     * a method that determines if a play is valid or not. It checks to see if
     * the column is within bounds.  If the column is within bounds, and the
     * column is not full, then the play is valid.
     * @param column an int representing the column to be played in.
     * @return true if the play is valid<br>
     * false if it is either out of bounds or the column is full
     */
    public boolean isValidPlay( int column ) {
		if ( !( column >= 0 && column <= 6 ) ) {
		    // check the column bounds
		    return false;
		} else if( this.playBoard[0][ column ] > 0 ) {
		    // check if column is full
		    return false;
		} else {
		    // column is NOT full and the column is within bounds
		    return true;
		}
    }
    
    /**
     * This method plays a piece on the game board.
     * @param column the column where the piece is to be played.
     * @return true if the piece was successfully played<br>
     * false otherwise
     */
    public boolean playPiece( int column ) {
	
		// check if the column choice is a valid play
		if( !this.isValidPlay( column ) ) {
		    return false;
		} else {
		    
		    //starting at the bottom of the board,
		    //place the piece into the first empty spot
		    for( int i = 5; i >= 0; i-- ) {
			if( this.playBoard[i][column] == 0 ) {
			    if( this.pieceCount % 2 == 0 ){
					this.playBoard[i][column] = 1;
					this.pieceCount++;
					
				}
				else { 
					this.playBoard[i][column] = 2;
					this.pieceCount++;
			    }
			    
			    //testing
			    //warning: uncommenting the next 3 lines will
			    //potentially produce LOTS of output
			    //System.out.println("i just played piece in column ->" +
			    //		column + "<-");
			    //this.printGameBoard();
			    //end testing
			    
			    return true;
			}
		    }
		    //the pgm shouldn't get here
		    System.out.println("Something went wrong with playPiece()");
		    
		    return false;
		}
    } //end playPiece
    
    /***************************  solution methods **************************/
    
    /**
     * this method removes the top piece from the game board
     * @param column the column to remove a piece from 
     */
    public void removePiece( int column ){
		// starting looking at the top of the game board,
		// and remove the top piece
		for( int i = 0; i < 6; i++ ) {
		    if( this.playBoard[ i ][ column ] > 0 ) {
				this.playBoard[ i ][ column ] = 0;
				this.pieceCount--;
				
				break;
		    }
		}
	//testing
	//WARNING: uncommenting the next 3 lines will potentially
	//produce LOTS of output
	//System.out.println("gameBoard.removePiece(). I am removing the " +
	//		"piece in column ->" + column + "<-");
	//this.printGameBoard();
	//end testing
	
    } // end remove piece	
    
    /************************  end solution methods **************************/
    
    /**
     * this method prints the GameBoard to the screen in a nice, pretty,
     * readable format
     */
    public void printGameBoard(){
		System.out.println(" -----------------");
		
		for( int i = 0; i < 6; i++ ) 
	        {
		    System.out.print(" | ");
		    for( int j = 0; j < 7; j++ ) 
	            {
	                System.out.print( this.playBoard[i][j] + " " );
	            }

		    System.out.println("| ");
		}
		
		System.out.println(" -----------------");
    } // end printGameBoard
    
    /**
     * this method prints the GameBoard to an output file to be used for
     * inspection or by another running of the application
     * @param outputFile the path and file name of the file to be written
     */
    public void printGameBoardToFile( String outputFile ) {
		try {
		    BufferedWriter output = new BufferedWriter(
							       new FileWriter( outputFile ) );
		    
		    for( int i = 0; i < 6; i++ ) {
			for( int j = 0; j < 7; j++ ) {
			    output.write( this.playBoard[i][j] + 48 );
			}
			output.write("\r\n");
		    }
		    
		    //write the current turn
		    output.write( this.getCurrentTurn() + "\r\n");
		    output.close();
		    
		} catch( IOException e ) {
		    System.out.println("\nProblem writing to the output file!\n" +
				       "Try again.");
		    e.printStackTrace();
		}
    } // end printGameBoardToFile()
    
    private void exit_function( int value ){
		System.out.println("exiting from GameBoard.java!\n\n");
		System.exit( value );
    }
    
}  // end GameBoard class
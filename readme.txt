Name - Navaneeth Thekkumpat

UTA ID - 1001656413

Programming Language used: Java

How the code is structured.
			-Programmed in Java.
			-Sample code is used.
			-In GameBoard.java, added the following functions
			1)GameBoard()- Constructor without parameters
			2)getEvalFunct()- To calculate the eval value of the game state.
			3)terminal()- Checks if the game state is terminal or not
			4)maxvalue()-maxvalue function 
			5)minvalue()-minvalue function 
			6)removePieces()- Used to remove pieces of certain row and column and set it to 0 in the gameboard
			7)successorFn()- Used to retrieve the successor of a particular game state
			8)getNumberofSucc()-Used to retrieve the number of successors for particular game state
			9)alphabetaDecision()-The index of the column, for which the move is to be made.
			10)BoardFull()-to check if the gameboard is full or not

Files		   :	-Submited a ZIPPED directory called assignment4_nxt6413.zip in the blackboard. 
			-This zip directory includes maxconnect4.java,GameBoard.java,AiPlayer.java,RuntimeReport.pdf ,EvalFunction.pdf and readme.txt files.
			-Keep all input files in the same directory.
How to run the code: 	1)Compile the maxconnect4.javausing the following command
				javac maxconnect4.java
		    	2)Run using the following command (one-move)
				java maxconnect4 one-move sourcefile destinationfile depth-limit 
				Eg:
				- java maxconnect4 one-move input1.txt input4.txt 7 
				Or
				- java maxconnect4 one-move input2.txt input4.txt 10
 			3)Run using the following command (interactive)
				java maxconnect4 interactive sourcefile <computer-next/human-next> 7
				Eg:
				- java maxconnect4 interactive input1.txt computer-next 7 
				

References	   : 	1) To know the Double infinity values, referred
		     	- https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html 
			2) Referred https://docs.oracle.com/javase/tutorial/java/javaOO/objectcreation.html 

			3)Scaffolding code provided by author james spargo.
			
			





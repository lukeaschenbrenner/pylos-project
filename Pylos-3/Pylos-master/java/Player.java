/**
*	@author Joe Chance
*	@version 1.0
**/


// Try to implement "score [place a1 / raise a1 / etc.]" which will display the score of the board based on that move
// Implement a separate board with different weights and a minimax with a different search depth
// Figure out infinite loop

import java.util.*;

class Player
{
	public static final int HUMAN = 1;
	public static final int AI = 2;
	
	private int playerType;
	private int numSpheres;
	private int colour;
	
	private static String[] cells = {
		"a1", "a2", "a3", "a4",
		"b1", "b2", "b3", "b4",
		"c1", "c2", "c3", "c4",
		"d1", "d2", "d3", "d4",
		"e1", "e2", "e3",
		"f1", "f2", "f3",
		"g1", "g2", "g3",
		"h1", "h2",
		"i1", "i2",
		"j1"
	};
	
	/**
	*	Initializes a player with 15 spheres in hand.
	*	@param type should be one of HUMAN or AI
	*	@param colour should be one of Board.EMPTY_SPHERE, Board.BLACK_SPHERE or Board.WHITE_SPHERE
	**/
	public Player(int type, int colour)
	{
		playerType = type;
		numSpheres = 15;
		this.colour = colour;
	}
	
	/**
	*	@param board a reference to the board that the move will be applied to
	*	@param scanner a scanner that will provide any human input for moves
	*	@param otherPlayer a reference to the opposite player in the game
	**/
	public void move(Board board, Scanner scanner, Player otherPlayer)
	{
		String move = "";
		if (getPlayerType() == HUMAN)
		{
			System.out.print("Enter your move, or \"list\" to see possible moves: ");
			move = scanner.nextLine();
			while (!board.isValidMove(move, this) || move.equals("list"))
			{
				if (move.equals("list"))
				{
					printPossibleMoves(board, this, otherPlayer);
					System.out.print("Enter your move: ");
					move = scanner.nextLine();
					continue;
				}
				System.out.print("Invalid move, enter again: ");
				move = scanner.nextLine();
			}
			if(move.equals("score")){
				System.out.print("Score is: ");
				System.out.println(board.evaluateBoard(this, otherPlayer));
				System.out.println("(Negative score favors white, positive favors black.)");	
				move(board, scanner, otherPlayer);
			}
			else if(move.contains("score")){
				System.out.print("Score is: ");
				String moveToGuess = move.substring("score".length()+1);
				board.move(moveToGuess, this);
				System.out.println(board.evaluateBoard(this, otherPlayer));
				System.out.println("(Negative score favors white, positive favors black.)");
				board.undoMove(moveToGuess, this);
				move(board, scanner, otherPlayer);
			}else{
				board.move(move, this);
			}
		}
		else if (getPlayerType() == AI)
		{
			String bestMove = Minimax.findMove(board, this, otherPlayer);
			if (bestMove.equals(""))
				System.out.println("Could not find move");
			else
				board.move(bestMove, this);
		}
	}
	
	/**
	*	Prints a list of the possible moves
	**/
	private void printPossibleMoves(Board board, Player thisPlayer, Player otherPlayer)
	{
		System.out.println("Your possible moves:");
		moveEnumeration: for (String cell: cells)
		{
			// All placing combinations
			String move = "place " + cell;
			
			if (board.isValidMove(move, thisPlayer))
				System.out.println(move);
			
			// Only consider removals if the place move is valid and leads to the completion of a row, column or square
			if (board.canPlaceSphere(cell, thisPlayer) && board.placeMoveCompletes(cell, thisPlayer))
			{
				for (String firstRemove: cells)
				{
					move = "place " + cell + " r:" + firstRemove;
					
					if (board.isValidMove(move, thisPlayer))
						System.out.println(move);
					
					// Only try removing 2 if we can remove the 1st
					if (board.isValidMove(move, thisPlayer))
					{
						for (String secondRemove: cells)
						{
							move = "place " + cell + " r:" + firstRemove + ":" + secondRemove;
							if (board.isValidMove(move, thisPlayer))
								System.out.println(move);
						}
					}
				}
			}
			
			// Only consider raises if cell contains a sphere of the player's colour
			if (board.getCell(cell) == thisPlayer.getColour())
			{
				// All raising combinations
				for (String toCell: cells)
				{
					move = "raise " + cell + " " + toCell;
					
					if (board.isValidMove(move, thisPlayer))
						System.out.println(move);
					
					// Only consider removals if the raise move is valid and leads to the completion of a row, column or square
					if (board.canRaiseSphere(cell, toCell, thisPlayer) && board.raiseMoveCompletes(cell, toCell, thisPlayer))
					{
						for (String firstRemove: cells)
						{
							move = "raise " + cell + " " + toCell + " r:" + firstRemove;
							
							if (board.isValidMove(move, thisPlayer))
								System.out.println(move);
							
							// Only try removing 2 if we can remove the 1st
							if (board.isValidMove(move, thisPlayer))
							{
								for (String secondRemove: cells)
								{
									move = "raise " + cell + " " + toCell + " r:" + firstRemove + ":" + secondRemove;
							
									if (board.isValidMove(move, thisPlayer))
										System.out.println(move);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	*	@return an integer count of how many spheres the player has in hand
	**/
	public int getNumSpheres() { return numSpheres; }
	public void setNumSpheres(int num) { numSpheres = num; }
	/**
	*	@return an integer indicating whether the player is a human, or uses AI in it's move choices
	**/
	public int getPlayerType() { return playerType; }
	/**
	*	@return an integer indicating the colour of the player's spheres
	**/
	public int getColour() { return colour; }
}
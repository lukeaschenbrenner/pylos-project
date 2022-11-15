/**
*	@author Joe Chance
*	@version 1.0
**/

import java.util.*;

class Minimax
{
	/**
	*	Feel free to modify this value
	**/
	public static final int SEARCH_DEPTH = 5;
	
	private static String bestMove = "";
	
	private static int nodesTraversed = 0;
	private static int stringsTraversed = 0;
	private static int turns = 0;
	
	private static long averageTime = 0;
	
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
	*	Returns the best move.
	*	@param board the board for which to find a move
	*	@param p1 the player that a move is being searched for
	*	@param p2 the other player
	*	@return a string containing what minimax has deemed the best move, or an empty string if a move could not be found
	**/
	public static String findMove(Board board, Player p1, Player p2)
	{
		bestMove = "";
		nodesTraversed = 0;
		stringsTraversed = 0;
		
		turns++;
		
		long startTime = System.nanoTime();
		//minimax(board, p1, p2, 0, SEARCH_DEPTH);
		minimax(board, p1, p2, 0, SEARCH_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
		long finishTime = System.nanoTime();
		averageTime = (averageTime * (turns - 1) + (finishTime - startTime)) / turns;
		
		System.out.println("Best move found: "+bestMove);
		System.out.println("Took " + ((1.0*finishTime-startTime)/1000000000.0) + " seconds");
		System.out.println("Traversed " + nodesTraversed + " nodes");
		System.out.println("Enumerated through " + stringsTraversed + " strings");
		System.out.println("Turns: "+turns);
		System.out.println("Average turn time: " + (averageTime*1.0/1000000000.0) + " seconds");
		
		return bestMove;
	}
	
	private static int minimax(Board board, Player p1, Player p2, int depth, int maxDepth, int alpha, int beta)
	{
		// Base cases - leaf nodes (either terminal, or have reached max depth)
		if (board.gameOver(p1, p2))
			return board.getWinner(p1, p2) == Board.BLACK_SPHERE ? Integer.MAX_VALUE - 1 : Integer.MIN_VALUE + 1;
		else if (depth == maxDepth)
			return board.evaluateBoard(p1, p2);
			
		int bestMoveScore = p1.getColour() == Board.BLACK_SPHERE ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		
		moveEnumeration: for (String cell: cells)
		{
			// All placing combinations
			String move = "place " + cell;
			bestMoveScore = getBestScore(board, move, p1, p2, bestMoveScore, depth, maxDepth, alpha, beta);
			
			// alpha is MAX's lower bound, beta is MIN's upper bound
			if (p1.getColour() == Board.BLACK_SPHERE)
				alpha = bestMoveScore;
			else
				beta = bestMoveScore;
			// Cutoff
			if (alpha >= beta)
					break moveEnumeration;
			
			// Only consider removals if the place move is valid and leads to the completion of a row, column or square
			if (board.canPlaceSphere(cell, p1) && board.placeMoveCompletes(cell, p1))
			{
				for (String firstRemove: cells)
				{
					move = "place " + cell + " r:" + firstRemove;
					bestMoveScore = getBestScore(board, move, p1, p2, bestMoveScore, depth, maxDepth, alpha, beta);
					
					// alpha is MAX's lower bound, beta is MIN's upper bound
					if (p1.getColour() == Board.BLACK_SPHERE)
						alpha = bestMoveScore;
					else
						beta = bestMoveScore;
					// Cutoff
					if (alpha >= beta)
							break moveEnumeration;
					
					// Only try removing 2 if we can remove the 1st
					if (board.isValidMove(move, p1))
					{
						for (String secondRemove: cells)
						{
							bestMoveScore = getBestScore(board, "place " + cell + " r:" + firstRemove + ":" + secondRemove, p1, p2, bestMoveScore, depth, maxDepth, alpha, beta);
							
							// alpha is MAX's lower bound, beta is MIN's upper bound
							if (p1.getColour() == Board.BLACK_SPHERE)
								alpha = bestMoveScore;
							else
								beta = bestMoveScore;
							// Cutoff
							if (alpha >= beta)
									break moveEnumeration;
						}
					}
				}
			}
			
			// Only consider raises if cell contains a sphere of the player's colour
			if (board.getCell(cell) == p1.getColour())
			{
				// All raising combinations
				for (String toCell: cells)
				{
					move = "raise " + cell + " " + toCell;
					bestMoveScore = getBestScore(board, move, p1, p2, bestMoveScore, depth, maxDepth, alpha, beta);
					
					// alpha is MAX's lower bound, beta is MIN's upper bound
					if (p1.getColour() == Board.BLACK_SPHERE)
						alpha = bestMoveScore;
					else
						beta = bestMoveScore;
					// Cutoff
					if (alpha >= beta)
							break moveEnumeration;
					
					// Only consider removals if the raise move is valid and leads to the completion of a row, column or square
					if (board.canRaiseSphere(cell, toCell, p1) && board.raiseMoveCompletes(cell, toCell, p1))
					{
						for (String firstRemove: cells)
						{
							move = "raise " + cell + " " + toCell + " r:" + firstRemove;
							bestMoveScore = getBestScore(board, "raise " + cell + " " + toCell + " r:" + firstRemove, p1, p2, bestMoveScore, depth, maxDepth, alpha, beta);
							
							// alpha is MAX's lower bound, beta is MIN's upper bound
							if (p1.getColour() == Board.BLACK_SPHERE)
								alpha = bestMoveScore;
							else
								beta = bestMoveScore;
							// Cutoff
							if (alpha >= beta)
									break moveEnumeration;
							
							// Only try removing 2 if we can remove the 1st
							if (board.isValidMove(move, p1))
							{
								for (String secondRemove: cells)
								{
									bestMoveScore = getBestScore(board, "raise " + cell + " " + toCell + " r:" + firstRemove + ":" + secondRemove, p1, p2, bestMoveScore, depth, maxDepth, alpha, beta);
									
									// alpha is MAX's lower bound, beta is MIN's upper bound
									if (p1.getColour() == Board.BLACK_SPHERE)
										alpha = bestMoveScore;
									else
										beta = bestMoveScore;
									// Cutoff
									if (alpha >= beta)
											break moveEnumeration;
								}
							}
						}
					}
				}
			}
		}
		
		return bestMoveScore;
	}
	
	private static int getBestScore(Board board, String move, Player p1, Player p2, int bestScore, int depth, int maxDepth, int alpha, int beta)
	{
		stringsTraversed++;
		if (board.isValidMove(move, p1))
		{
			nodesTraversed++;
			board.move(move, p1);
			int moveScore = minimax(board, p2, p1, depth + 1, maxDepth, alpha, beta);
			board.undoMove(move, p1);
			
			// If the move is better for this player than previously seen, set it's score to be the new best,
			// and the static bestMove variable to be the move
			if (p1.getColour() == Board.BLACK_SPHERE && moveScore > bestScore ||
				p1.getColour() == Board.WHITE_SPHERE && moveScore < bestScore)
			{
				bestScore = moveScore;
				if (depth == 0)
					bestMove = move;
			}
		}
		return bestScore;
	}
}
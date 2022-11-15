/**
*	@author Joe Chance
*	@version 1.0
**/

import java.util.*;

class Pylos
{
	/**
	*	Entry point for the game, asks the user for the number of human players, and if applicable,
	*	which colour he/she would like to play.
	**/
	public static void main(String[] args)
	{
		Player white, black, turn;
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to Pylos!");
		System.out.println("Place move syntax: place <cell> [r:<cell>[:<cell>]]");
		System.out.println("Raise move syntax: raise <cell> <cell> [r:<cell>[:<cell>]]");
		// Ask for number of human players
		int numPlayers = -1;
		do
		{
			System.out.print("Enter the number of human players (0-2): ");
			numPlayers = scanner.nextInt();
			scanner.nextLine();
		}
		while (numPlayers < 0 || numPlayers > 2);
		
		// Initialize default human and AI player
		white = new Player(Player.HUMAN, Board.WHITE_SPHERE);
		black = new Player(Player.AI, Board.BLACK_SPHERE);
		
		// Configure the game for between 0 and 2 human players
		switch (numPlayers)
		{
			case 0:
				white = new Player(Player.AI, Board.WHITE_SPHERE);
				break;
			case 1:
				System.out.print("Enter your colour (0 for black, 1 for white): ");
				int colour = scanner.nextInt();
				scanner.nextLine();
				if (colour == 0)
				{
					white = new Player(Player.AI, Board.WHITE_SPHERE);
					black = new Player(Player.HUMAN, Board.BLACK_SPHERE);
				}
				break;
			case 2:
				black = new Player(Player.HUMAN, Board.BLACK_SPHERE);
				break;
		}
		Board gameBoard = new Board();
		turn = white;
		
		while (!gameBoard.gameOver(black, white))
		{
			gameBoard.printBoard();
			System.out.println((turn == white ? "White" : "Black") + ", it's your move, you have "+turn.getNumSpheres()+" sphere(s) left");
			turn.move(gameBoard, scanner, turn == white ? black : white);
			if (turn.getNumSpheres() <= 0 && !gameBoard.gameOver(black, white))
			{
				System.out.println("You ran out of spheres!");
				System.exit(0);
			}
			turn = (turn == white ? black : white);
		}
		
		System.out.println((gameBoard.getWinner(black, white) == Board.BLACK_SPHERE ? "Black" : "White") + " wins!");
		gameBoard.printBoard();
	}
}
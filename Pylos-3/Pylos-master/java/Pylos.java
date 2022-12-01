/**
*	@author Joe Chance
*	@version 1.0
**/

import java.lang.reflect.InvocationTargetException;
import java.util.*;

class Pylos
{
	public static ArrayList<Class<?>> aiModels = new ArrayList<>();
	/**
	*	Entry point for the game, asks the user for the number of human players, and if applicable,
	*	which colour he/she would like to play.
	**/
	public static void main(String[] args)
	{
		aiModels.add(Minimax.class);
		aiModels.add(AltMiniMax.class);
	
		Player white, black, turn;
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to Pylos!");
		System.out.println("Place move syntax: place <cell> [r:<cell>[:<cell>]]");
		System.out.println("Raise move syntax: raise <cell> <cell> [r:<cell>[:<cell>]]");
		System.out.println("View score of move syntax: score [place <cell>/raise <cell> <cell>] [r:<cell>[:<cell>]");
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
		black = new Player(Player.HUMAN, Board.BLACK_SPHERE);
		
		// Configure the game for between 0 and 2 human players
		switch (numPlayers)
		{
			case 0:
				System.out.println("AI models:");
				for(int i = 0; i < aiModels.size(); i++){
					try {
						System.out.println((i+1) + ". " + ((Object)(aiModels.get(i).getDeclaredConstructor().newInstance())).toString());
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
					
				}
				System.out.print("Select the AI model for the white ball player:");
				int aiOne = scanner.nextInt() - 1;
				scanner.nextLine();

				System.out.print("Select the AI model for the black ball player:");
				int aiTwo = scanner.nextInt() - 1;
				scanner.nextLine();

				white = new Player(aiOne, Board.WHITE_SPHERE);
				black = new Player(aiTwo, Board.BLACK_SPHERE);
				break;
			case 1:
				System.out.print("Enter your color (0 for black, 1 for white): ");
				int color = scanner.nextInt();
				scanner.nextLine();

				System.out.println("AI models:");
				for(int i = 0; i < aiModels.size(); i++){
					try {
						System.out.println((i+1) + ". " + ((Object)(aiModels.get(i).getDeclaredConstructor().newInstance())).toString());
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
					
				}
				System.out.print("Select the AI model for the " + (color == 0? "black" : "white") + " color ball: ");
				aiOne = scanner.nextInt() - 1;
				scanner.nextLine();

				//Replace block below with AI model selection
				if (color == 0)
				{
				
					white = new Player(aiOne, Board.WHITE_SPHERE);
					black = new Player(Player.HUMAN, Board.BLACK_SPHERE);
				}
				break;
			case 2:
			//Do nothing	
			//black = new Player(Player.HUMAN, Board.BLACK_SPHERE);
				break;
		}
		Board gameBoard = new Board();

		turn = white;
		
		while (!gameBoard.gameOver(black, white))
		{
			gameBoard.printBoard();
			System.out.println("Current score: " + gameBoard.evaluateBoard(black, white));
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
		if(gameBoard.getWinner(black, white) == Board.BLACK_SPHERE){
			if(black.getPlayerType() >= 0){
				try {
					System.out.println("AI model of winner: " + aiModels.get(black.getPlayerType()).getDeclaredConstructor().newInstance().toString());
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}else{
			if(black.getPlayerType() >= 0){
				try {
					System.out.println("AI model of winner: " + aiModels.get(white.getPlayerType()).getDeclaredConstructor().newInstance().toString());
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		gameBoard.printBoard();
	}
}
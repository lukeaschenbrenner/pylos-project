public class ScoringEval {
    	/**
	*	These are the weights for the evaluation function
	**/
	private int SPHERES_LEFT_WEIGHT = 2;
	private int CLIMBING_WEIGHT = 3;
		/**
	*	The evaluation function for the board. Used by the minimax class to determine the favourability of the board.
	*	A positive value indicates that the board favours black, with negative favouring white. The greater the magnitude,
	*	the greater the favourability.
	*	@param p1 a reference to player 1
	*	@param p2 a reference to player 2
	*	@return an integer "score" for the board
	**/
	private Board currentBoard;
	
	public ScoringEval(Board b, int SPHERES_LEFT_WEIGHT, int CLIMBING_WEIGHT){
		currentBoard = b;
		this.SPHERES_LEFT_WEIGHT = SPHERES_LEFT_WEIGHT; // 2
		this.CLIMBING_WEIGHT = CLIMBING_WEIGHT; // 3
	}

	public int evaluateBoard(Player p1, Player p2)
	{
		Player black, white;
		
		int score = 0;
		if (p1.getColour() == Board.BLACK_SPHERE)
		{
			black = p1;
			white = p2;
		}
		else
		{
			black = p2;
			white = p1;
		}
		
		score += SPHERES_LEFT_WEIGHT * (black.getNumSpheres() - white.getNumSpheres());
		for (int level = 0; level < 4; level++)
		{
			score += CLIMBING_WEIGHT * (level + 1) * currentBoard.countSpheresOnLevel(level, black);
			score -= CLIMBING_WEIGHT * (level + 1) * currentBoard.countSpheresOnLevel(level, white);
		}
		return score;
	}
	
}

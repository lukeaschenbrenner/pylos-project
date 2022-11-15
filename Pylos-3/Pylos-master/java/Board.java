/**
*	@author Joe Chance
*	@version 1.0
**/

import java.util.*;

class Board
{
	public static final int EMPTY_SPHERE = 0;
	public static final int BLACK_SPHERE = 1;
	public static final int WHITE_SPHERE = 2;
	
	/**
	*	These are the weights for the evaluation function
	**/
	private static final int SPHERES_LEFT_WEIGHT = 2;
	private static final int CLIMBING_WEIGHT = 3;
	
	private int[] board;
	private int[] rowOffsetTable = {0, 4, 8, 12, 16, 19, 22, 25, 27, 29};
	// This maps a board index to the board positions that must have a sphere in them for a sphere to be placed at the key index
	private HashMap<Integer, ArrayList<Integer>> cellsBelow;
	private HashMap<Integer, ArrayList<Integer>> cellsAbove;
	// This maps a board index to groups of 3 indices that must be checked to determine whether a square has been completed around it
	private HashMap<Integer, ArrayList<Integer>> checkSquares;
	
	/**
	*	Constructs a standard Pylos game board, containing 30 empty cells.
	*	Also initializes the data required for some of the checking.
	**/
	public Board()
	{
		board = new int[30];
		Arrays.fill(board, EMPTY_SPHERE);
		initCellsBelow();
		initCellsAbove();
		initCheckSquares();
	}
	
	/**
	*	Initializes the map that stores the board indices of the cells below a given cell.
	**/
	private void initCellsBelow()
	{
		cellsBelow = new HashMap<>();
		for (int i = 0; i < 16; i++)
			cellsBelow.put(i, new ArrayList<Integer>());
		cellsBelow.put(16, new ArrayList<Integer>(Arrays.asList(0, 1, 4, 5)));
		cellsBelow.put(17, new ArrayList<Integer>(Arrays.asList(1, 2, 5, 6)));
		cellsBelow.put(18, new ArrayList<Integer>(Arrays.asList(2, 3, 6, 7)));
		cellsBelow.put(19, new ArrayList<Integer>(Arrays.asList(4, 5, 8, 9)));
		cellsBelow.put(20, new ArrayList<Integer>(Arrays.asList(5, 6, 9, 10)));
		cellsBelow.put(21, new ArrayList<Integer>(Arrays.asList(6, 7, 10, 11)));
		cellsBelow.put(22, new ArrayList<Integer>(Arrays.asList(8, 9, 12, 13)));
		cellsBelow.put(23, new ArrayList<Integer>(Arrays.asList(9, 10, 13, 14)));
		cellsBelow.put(24, new ArrayList<Integer>(Arrays.asList(10, 11, 14, 15)));
		cellsBelow.put(25, new ArrayList<Integer>(Arrays.asList(16, 17, 19, 20)));
		cellsBelow.put(26, new ArrayList<Integer>(Arrays.asList(17, 18, 20, 21)));
		cellsBelow.put(27, new ArrayList<Integer>(Arrays.asList(19, 20, 22, 23)));
		cellsBelow.put(28, new ArrayList<Integer>(Arrays.asList(20, 21, 23, 24)));
		cellsBelow.put(29, new ArrayList<Integer>(Arrays.asList(25, 26, 27, 28)));
	}
	
	/**
	*	Initializes the map that stores the board indices of the cells above a given cell.
	**/
	private void initCellsAbove()
	{
		cellsAbove = new HashMap<>();
		// First level
		cellsAbove.put(0, new ArrayList<Integer>(Arrays.asList(16)));
		cellsAbove.put(1, new ArrayList<Integer>(Arrays.asList(16, 17)));
		cellsAbove.put(2, new ArrayList<Integer>(Arrays.asList(17, 18)));
		cellsAbove.put(3, new ArrayList<Integer>(Arrays.asList(18)));
		cellsAbove.put(4, new ArrayList<Integer>(Arrays.asList(16, 19)));
		cellsAbove.put(5, new ArrayList<Integer>(Arrays.asList(16, 17, 19, 20)));
		cellsAbove.put(6, new ArrayList<Integer>(Arrays.asList(17, 18, 20, 21)));
		cellsAbove.put(7, new ArrayList<Integer>(Arrays.asList(18, 21)));
		cellsAbove.put(8, new ArrayList<Integer>(Arrays.asList(19, 22)));
		cellsAbove.put(9, new ArrayList<Integer>(Arrays.asList(19, 20, 22, 23)));
		cellsAbove.put(10, new ArrayList<Integer>(Arrays.asList(20, 21, 23, 24)));
		cellsAbove.put(11, new ArrayList<Integer>(Arrays.asList(21, 24)));
		cellsAbove.put(12, new ArrayList<Integer>(Arrays.asList(22)));
		cellsAbove.put(13, new ArrayList<Integer>(Arrays.asList(22, 23)));
		cellsAbove.put(14, new ArrayList<Integer>(Arrays.asList(23, 24)));
		cellsAbove.put(15, new ArrayList<Integer>(Arrays.asList(24)));
		
		// Second level
		cellsAbove.put(16, new ArrayList<Integer>(Arrays.asList(25)));
		cellsAbove.put(17, new ArrayList<Integer>(Arrays.asList(25, 26)));
		cellsAbove.put(18, new ArrayList<Integer>(Arrays.asList(26)));
		cellsAbove.put(19, new ArrayList<Integer>(Arrays.asList(25, 27)));
		cellsAbove.put(20, new ArrayList<Integer>(Arrays.asList(25, 26, 27, 28)));
		cellsAbove.put(21, new ArrayList<Integer>(Arrays.asList(26, 28)));
		cellsAbove.put(22, new ArrayList<Integer>(Arrays.asList(27)));
		cellsAbove.put(23, new ArrayList<Integer>(Arrays.asList(27, 28)));
		cellsAbove.put(24, new ArrayList<Integer>(Arrays.asList(28)));
		
		// Third level
		cellsAbove.put(25, new ArrayList<Integer>(Arrays.asList(29)));
		cellsAbove.put(26, new ArrayList<Integer>(Arrays.asList(29)));
		cellsAbove.put(27, new ArrayList<Integer>(Arrays.asList(29)));
		cellsAbove.put(28, new ArrayList<Integer>(Arrays.asList(29)));
		
		// Fourth level
		cellsAbove.put(29, new ArrayList<Integer>());
	}
	
	/**
	*	Initializes the map that, for a given cell, stores, in groups of 3, the indices of the other cells
	*	that together with the given cell, form a 2x2 square. This method is used when checking whether
	*	a move will lead to a completion.
	**/
	private void initCheckSquares()
	{
		checkSquares = new HashMap<Integer, ArrayList<Integer>>();
		
		// First level
		checkSquares.put(0, new ArrayList<Integer>(Arrays.asList(1, 4, 5)));
		checkSquares.put(1, new ArrayList<Integer>(Arrays.asList(0, 4, 5, 2, 5, 6)));
		checkSquares.put(2, new ArrayList<Integer>(Arrays.asList(1, 5, 6, 3, 6, 7)));
		checkSquares.put(3, new ArrayList<Integer>(Arrays.asList(2, 6, 7)));
		checkSquares.put(4, new ArrayList<Integer>(Arrays.asList(0, 1, 5, 5, 8, 9)));
		checkSquares.put(5, new ArrayList<Integer>(Arrays.asList(0, 1, 4, 1, 2, 6, 4, 8, 9, 6, 9, 10)));
		checkSquares.put(6, new ArrayList<Integer>(Arrays.asList(1, 2, 5, 2, 3, 7, 5, 9, 10, 7, 10, 11)));
		checkSquares.put(7, new ArrayList<Integer>(Arrays.asList(2, 3, 6, 6, 10, 11)));
		checkSquares.put(8, new ArrayList<Integer>(Arrays.asList(4, 5, 9, 9, 12, 13)));
		checkSquares.put(9, new ArrayList<Integer>(Arrays.asList(4, 5, 8, 5, 6, 10, 8, 12, 13, 10, 13, 14)));
		checkSquares.put(10, new ArrayList<Integer>(Arrays.asList(5, 6, 9, 6, 7, 11, 9, 13, 14, 11, 14, 15)));
		checkSquares.put(11, new ArrayList<Integer>(Arrays.asList(6, 7, 10, 10, 14, 15)));
		checkSquares.put(12, new ArrayList<Integer>(Arrays.asList(8, 9, 13)));
		checkSquares.put(13, new ArrayList<Integer>(Arrays.asList(8, 9, 12, 9, 10, 14)));
		checkSquares.put(14, new ArrayList<Integer>(Arrays.asList(9, 10, 13, 10, 11, 15)));
		checkSquares.put(15, new ArrayList<Integer>(Arrays.asList(10, 11, 14)));
		
		// Second level
		checkSquares.put(16, new ArrayList<Integer>(Arrays.asList(17, 19, 20)));
		checkSquares.put(17, new ArrayList<Integer>(Arrays.asList(16, 19, 20, 18, 20, 21)));
		checkSquares.put(18, new ArrayList<Integer>(Arrays.asList(17, 20, 21)));
		checkSquares.put(19, new ArrayList<Integer>(Arrays.asList(16, 17, 20, 20, 22, 23)));
		checkSquares.put(20, new ArrayList<Integer>(Arrays.asList(16, 17, 19, 17, 18, 21, 19, 22, 23, 21, 23, 24)));
		checkSquares.put(21, new ArrayList<Integer>(Arrays.asList(17, 18, 20, 20, 23, 24)));
		checkSquares.put(22, new ArrayList<Integer>(Arrays.asList(19, 20, 23)));
		checkSquares.put(23, new ArrayList<Integer>(Arrays.asList(19, 20, 22, 20, 21, 24)));
		checkSquares.put(24, new ArrayList<Integer>(Arrays.asList(20, 21, 23)));
	}
	
	/**
	*	Should only be used internally. Converts a cell address into an integer index into the board array
	*	@param cell the given cell address
	*	@return an integer index into the board array
	**/
	private int getCellIndex(String cell)
	{
		cell = cell.toLowerCase();
		return rowOffsetTable[(int)(cell.charAt(0)-'a')] + Character.digit(cell.charAt(1), 10)-1;
	}
	
	/**
	*	Returns the level that a cell resides in. This can be one of (0, 1, 2, 3)
	*	with 0 being the bottom 4x4 level, and 3 being the top of the pyramid.
	**/
	private int getLevel(String cell)
	{
		if (!isValidCellAddress(cell))
			throw new InvalidCellAddressException();
			
		char c = cell.charAt(0);
		if (c >= 'a' && c <= 'd')
			return 0;
		else if (c >= 'e' && c <= 'g')
			return 1;
		else if (c >= 'h' && c <= 'i')
			return 2;
		else
			return 3;
	}
	
	/**
	*	Returns whether a cell address is valid or not. To be a valid cell address, the string must
	*	start with a letter between 'a' and 'j', and must be followed by a number indicating the column
	*	number, which should be between 1 and 4 for a row on the first level, 1 and 3 on the second,
	*	1 and 2 on the third, and 1 on the fourth.
	*	@param addr a string to be checked
	*	@return a boolean indicating whether the string parameter is a valid cell address
	**/
	public boolean isValidCellAddress(String addr)
	{
		addr = addr.toLowerCase();
		if (addr.length() != 2 || !Character.isLetter(addr.charAt(0)) || !Character.isDigit(addr.charAt(1)))
			return false;
		char c = addr.charAt(0);
		int d = Character.digit(addr.charAt(1), 10);
		if (c >= 'a' && c <= 'd' && d >= 1 && d <= 4)
			return true;
		else if (c >= 'e' && c <= 'g' && d >= 1 && d <= 3)
			return true;
		else if (c >= 'h' && c <= 'i' && d >= 1 && d <= 2)
			return true;
		else if (c == 'j' && d == 1)
			return true;
		else
			return false;
	}
	
	/**
	*	Returns whether the player can remove a sphere from the cell.
	*	To pass this test the cell must contain a sphere of the player's colour,
	*	and there must not be any spheres in the cells above the given cell
	*	@param cell a string containing the cell address
	*	@param player a reference to the player who will be removing the sphere
	*	@return a boolean indicating whether the player may remove a sphere from the cell
	**/
	public boolean canRemoveSphere(String cell, Player player)
	{
		if (getCell(cell) != player.getColour())
			return false;
		// Can't remove sphere if there are spheres resting above it
		ArrayList<Integer> checkCells = cellsAbove.get(getCellIndex(cell));
		for (Integer index: checkCells)
		{
			if (board[index] != EMPTY_SPHERE)
				return false;
		}
		return true;
	}
	
	/**
	*	Removes the player's sphere from the cell. Assumes that the removal is valid
	*	as determined by canRemoveSphere. This method also handles the sphere count of the player.
	*	So the player's sphere count is incremented once the removal has occurred.
	*	@param cell a string containing the cell address from which a sphere will be removed
	*	@param player a reference to the player removing a sphere
	**/
	public void removeSphere(String cell, Player player)
	{
		setCell(cell, EMPTY_SPHERE);
		player.setNumSpheres(player.getNumSpheres()+1);
	}
	
	/**
	*	Returns whether the player can place a sphere in a cell.
	*	For this to be true, the given cell must be empty, the player must have at least one sphere to place,
	*	and any cells below the given cell must be filled.
	*	@param cell a string containing the cell in which a sphere will be placed
	*	@param player a reference to the player who will place a sphere
	*	@return a boolean indicating whether the player may place a sphere in the cell
	**/
	public boolean canPlaceSphere(String cell, Player player)
	{
		if (getCell(cell) != EMPTY_SPHERE || player.getNumSpheres() <= 0 || (player.getColour() == EMPTY_SPHERE))
			return false;
		
		ArrayList<Integer> checkCells = cellsBelow.get(getCellIndex(cell));
		for (Integer index: checkCells)
		{
			if (board[index] == EMPTY_SPHERE)
				return false;
		}
		return true;
	}
	
	/**
	*	Returns true if from and to are both valid cell addresses,
	*	from contains a sphere with no other spheres above it,
	*	to is empty and is above the 1st level and has 4 spheres below it, none of which can be addressed by from,
	*	otherwise returns false
	*	@param from the cell address of the sphere to be raised
	*	@param to the address of the cell to be raised to
	*	@param the player to perform the raise move
	*/
	public boolean canRaiseSphere(String from, String to, Player player)
	{
		// TODO: Don't allow player to raise to lower level
		from = from.toLowerCase();
		to = to.toLowerCase();
		if (!isValidCellAddress(from) || !isValidCellAddress(to) || getCell(from) != player.getColour() || getCell(to) != EMPTY_SPHERE)
			return false;
			
		if (getLevel(to) <= getLevel(from))
			return false;
			
		// Check whether there are any spheres above the sphere to be raised
		ArrayList<Integer> cellsAboveFrom = cellsAbove.get(getCellIndex(from));
		for (Integer index: cellsAboveFrom)
		{
			if (board[index] != EMPTY_SPHERE)
				return false;
		}
		
		// Can't raise a sphere onto the first level (indices 0-15)
		if (getCellIndex(to) >= 0 && getCellIndex(to) <= 15)
			return false;
		
		// Ensure all cells below to are filled, and that none of them are the sphere which we are raising
		ArrayList<Integer> cellsBelowTo = cellsBelow.get(getCellIndex(to));
		for (Integer index: cellsBelowTo)
		{
			if (board[index] == EMPTY_SPHERE || index == getCellIndex(from))
				return false;
		}
		
		return true;
	}
	
	/**
	*	Raises a sphere.
	*	Note: this method performs no checking on whether the raise move provided is valid or not.
	*	This checking must be performed externally. This choice was made to avoid redundant checking.
	*	@param from the address of the sphere to be raised
	*	@param to the address of the cell to be raised to
	**/
	public void raiseSphere(String from, String to)
	{
		board[getCellIndex(to)] = board[getCellIndex(from)];
		board[getCellIndex(from)] = EMPTY_SPHERE;
	}
	
	/**
	*	Determines whether the provided "place" move will complete a row, column or square of spheres of the player's colour.
	*	This method can be used to determine whether a place move requires removals after.
	*	Note: the provided move must be a valid place move, or an exception will be thrown
	*
	*	@param move a string that should contain a valid place move for the player
	*	@param player a reference to the player making the move
	*
	*	@return a boolean indicating whether the move does complete a row, column or square for the player or not
	*
	*	@throws InvalidMoveException if the provided string does not contain a valid place move
	**/
	public boolean placeMoveCompletes(String move, Player player)
	{
		if (!canPlaceSphere(move, player))
			throw new InvalidMoveException();
			
		setCell(move, player.getColour());
		boolean completes = checkForCompleteRow(move) || checkForCompleteCol(move) || checkForCompleteSquare(move);
		setCell(move, EMPTY_SPHERE);
		
		return completes;
	}
	
	/**
	*	Determines whether the provided "raise" move will complete a row, column or square of spheres of the player's colour.
	*	This method can be used to determine whether a raise move requires removals after.
	*	Note: the provided move must be a valid place move, or an exception will be thrown
	*
	*	@param move a string that should contain a valid raise move for the player
	*	@param player a reference to the player making the move
	*
	*	@return a boolean indicating whether the move does complete a row, column or square for the player or not
	*
	*	@throws InvalidMoveException if the provided string does not contain a valid raise move
	**/
	public boolean raiseMoveCompletes(String from, String to, Player player)
	{
		if (!canRaiseSphere(from, to, player))
			throw new InvalidMoveException();
			
		raiseSphere(from, to);
		boolean completes = checkForCompleteRow(to) || checkForCompleteCol(to) || checkForCompleteSquare(to);
		raiseSphere(to, from);
		
		return completes;
	}
	
	/**
	*	Returns true if move is a legal move. A valid move must be of the form "place <address> [r:<removal_1>[:<removal_2>]]" or
	*	"raise <raise_from> <raise_to> [r:<removal_1>[:<removal_2>]]". A place or raise move that completes a square, column or
	*	row of player's spheres must also contain a removal that removes at least one of player's spheres to be valid. This simplifies
	*	moving as it reduces moves to 1 step, rather than 2.
	*	@param move a string containing the move to be checked
	*	@param player the player making the move
	*	@return a boolean indicating whether the provided move made by the player is valid or not
	*/
	public boolean isValidMove(String move, Player player)
	{
		if (move == null)
			return false;
			
		String[] moveComponents = move.split(" ");
		// A "place" move
		if (moveComponents[0].equals("place") && moveComponents.length >= 2 && moveComponents.length <= 3)
		{
			if (!isValidCellAddress(moveComponents[1]) || !canPlaceSphere(moveComponents[1], player))
				return false;
				
			if (placeMoveCompletes(moveComponents[1], player))
			{
				if (moveComponents.length == 2)
					return false;
					
				String[] removeComponents = moveComponents[2].split(":");
				if (removeComponents.length == 2 || removeComponents.length == 3)
				{
					if (!removeComponents[0].equals("r"))
						return false;
						
					boolean isValid = true;
					
					setCell(moveComponents[1], player.getColour());
					ArrayList<Integer> originals = new ArrayList<>();
					// Remove cells in order, it is an invalid move if we are unable to do this for any of the cells
					for (int i = 1; i < removeComponents.length; i++)
					{
						if (!canRemoveSphere(removeComponents[i], player))
						{
							isValid = false;
							break;
						}
						originals.add(getCell(removeComponents[i]));
						setCell(removeComponents[i], EMPTY_SPHERE);
					}
					for (int i = 0; i < originals.size(); i++)
					{
						setCell(removeComponents[i+1], originals.get(i));
					}
					
					setCell(moveComponents[1], EMPTY_SPHERE);
					
					return isValid;
				}
			}
			else if (moveComponents.length == 2)
				return true;
		}
		// A "raise" move
		else if (moveComponents[0].equals("raise") && moveComponents.length >= 3 && moveComponents.length <= 4)
		{
			if (!isValidCellAddress(moveComponents[1]) || !isValidCellAddress(moveComponents[2]) || !canRaiseSphere(moveComponents[1], moveComponents[2], player))
				return false;
				
			if (raiseMoveCompletes(moveComponents[1], moveComponents[2], player))
			{
				if (moveComponents.length == 3)
					return false;
				
				String[] removeComponents = moveComponents[3].split(":");
				
				if ((removeComponents.length != 2 && removeComponents.length != 3) || !removeComponents[0].equals("r"))
					return false;
					
				boolean isValid = true;
				
				raiseSphere(moveComponents[1], moveComponents[2]);
				ArrayList<Integer> originals = new ArrayList<>();
				for (int i = 1; i < removeComponents.length; i++)
				{
					if (!canRemoveSphere(removeComponents[i], player))
					{
						isValid = false;
						break;
					}
					originals.add(getCell(removeComponents[i]));
					setCell(removeComponents[i], EMPTY_SPHERE);
				}
				for (int i = 0; i < originals.size(); i++)
				{
					setCell(removeComponents[i+1], originals.get(i));
				}
				
				raiseSphere(moveComponents[2], moveComponents[1]);
				
				return isValid;
			}
			else if (moveComponents.length == 3)
				return true;
		}
		return false;
	}
	
	/**
	*	Assumes the provided move is valid and makes it for the provided player. This method also manages
	*	the player's sphere count based on the move. So for example a place move with no removals will deduct
	*	1 from the player's sphere count.
	*	@param move - a string that is assumed to contain a valid move
	*	@param player - the player who's making the move
	*/
	public void move(String move, Player player)
	{
		if (!isValidMove(move, player))
			throw new InvalidMoveException();
		
		String components[] = move.split(" ");
		// "Place" a sphere
		if (components[0].equals("place"))
		{
			setCell(components[1], player.getColour());
			player.setNumSpheres(player.getNumSpheres()-1);
			
			if (components.length == 3)
			{
				String[] removeCells = components[2].split(":");
				
				for (int r = 1; r < removeCells.length; r++)
				{
					setCell(removeCells[r], EMPTY_SPHERE);
					player.setNumSpheres(player.getNumSpheres()+1);
				}
			}
		}
		// "Raise" a sphere
		else if (components[0].equals("raise"))
		{
			raiseSphere(components[1], components[2]);
			
			if (components.length == 4)
			{
				String[] removeCells = components[3].split(":");
				
				for (int r = 1; r < removeCells.length; r++)
				{
					setCell(removeCells[r], EMPTY_SPHERE);
					player.setNumSpheres(player.getNumSpheres()+1);
				}
			}
		}
	}
	
	/**
	*	Assumes that the provided move was made and is valid, and undoes it. This method is used
	*	by the minimax class to return the board to it's original state after a move has been traversed.
	*	@param move the move to be undone
	*	@param player the player who made the move that is to be undone
	**/
	public void undoMove(String move, Player player)
	{
		String[] parts = move.split(" ");
		
		if (parts[0].equals("place"))
		{
			// Return any removed spheres
			if (parts.length == 3)
			{
				String[] removedCells = parts[2].split(":");
				
				for (int r = 1; r < removedCells.length; r++)
				{
					setCell(removedCells[r], player.getColour());
					player.setNumSpheres(player.getNumSpheres() - 1);
				}
			}
			
			setCell(parts[1], EMPTY_SPHERE);
			player.setNumSpheres(player.getNumSpheres() + 1);
		}
		else if (parts[0].equals("raise"))
		{
			
			// Return any removed spheres
			if (parts.length == 4)
			{
				String[] removedCells = parts[3].split(":");
				
				for (int r = 1; r < removedCells.length; r++)
				{
					setCell(removedCells[r], player.getColour());
					player.setNumSpheres(player.getNumSpheres() - 1);
				}
			}

			raiseSphere(parts[2], parts[1]);
		}
	}
	
	/**
	*	Returns whether the row containing the given cell is filled with either black or white spheres
	*	@param cell the address of the cell whose row is checked
	*	@return a boolean indicating whether the row is full or not
	**/
	public boolean checkForCompleteRow(String cell)
	{
		int boardIndex = getCellIndex(cell);
		if (board[boardIndex] == EMPTY_SPHERE)
			return false;
		// Only care about complete rows on the first and second level
		if (boardIndex > 24)
			return false;
		// First level
		else if (boardIndex < 16)
		{
			int row = boardIndex / 4;
			for (int i = 0; i < 4; i++)
			{
				if (board[row*4+i] != board[boardIndex])
					return false;
			}
		}
		// Second level
		else
		{
			int relativeRow = (boardIndex - 16) / 3;
			for (int i = 0; i < 3; i++)
			{
				if (board[16+relativeRow*3+i] != board[boardIndex])
					return false;
			}
		}
		return true;
	}
	
	/**
	*	Returns whether the column containing the given cell is filled with either black or white spheres
	*	@param cell the address of the cell whose column is checked
	*	@return a boolean indicating whether the column is full or not
	**/
	public boolean checkForCompleteCol(String cell)
	{
		int boardIndex = getCellIndex(cell);
		if (board[boardIndex] == EMPTY_SPHERE)
			return false;
		// Only care about complete columns on the first and second level
		if (boardIndex > 24)
			return false;
		// First level
		else if (boardIndex < 16)
		{
			int col = boardIndex % 4;
			for (int i = 0; i < 4; i++)
			{
				if (board[i*4+col] != board[boardIndex])
					return false;
			}
		}
		// Second level
		else
		{
			int relativeCol = (boardIndex - 16) % 3;
			for (int i = 0; i < 3; i++)
			{
				if (board[16+i*3+relativeCol] != board[boardIndex])
					return false;
			}
		}
		return true;
	}
	
	/**
	*	Returns whether any of the 2x2 cell squares containing the given cell are filled with either black or white spheres
	*	@param cell the address of the cell whose squares containing it are to be checked
	*	@return a boolean indicating whether any of the squares are full or not
	**/
	public boolean checkForCompleteSquare(String cell)
	{
		int boardIndex = getCellIndex(cell);
		if (board[boardIndex] == EMPTY_SPHERE)
			return false;
			
		ArrayList<Integer> squaresToCheck;
		
		if (boardIndex < 0 || boardIndex >= 29)
			return false;
		else if (boardIndex < 25)
		{
			squaresToCheck = checkSquares.get(boardIndex);
			for (int i = 0; i < squaresToCheck.size()/3; i++)
			{
				if (board[squaresToCheck.get(i*3)] == board[boardIndex] && board[squaresToCheck.get(i*3+1)] == board[boardIndex] && board[squaresToCheck.get(i*3+2)] == board[boardIndex])
					return true;
			}
			return false;
		}
		// Third level
		else
		{
			for (int i = 25; i < 29; i++)
			{
				if (board[i] != board[boardIndex])
					return false;
			}
		}
		return true;
	}
	
	/**
	*	Determines whether the game is over. This condition is satisfied if any of the following hold:
	*	- The top of the pyramid contains a black or white sphere
	*	- Player 1 or 2 has run out of spheres
	*	@param p1 a reference to player 1
	*	@param p2 a reference to player 2
	*	@return a boolean indicating whether the game is over
	**/
	public boolean gameOver(Player p1, Player p2) {
		return board[29] == BLACK_SPHERE || board[29] == WHITE_SPHERE || p1.getNumSpheres() == 0 || p2.getNumSpheres() == 0;
	}
	
	/**
	*	This method should only be called if the game is over, as determined by gameOver() returning true.
	*	If the game is over then one of (BLACK_SPHERE, WHITE_SPHERE) is returned, indicating that that coloured
	*	player is the winner.
	*	@param p1 a reference to player 1
	*	@param p2 a reference to player 2
	*	@return an integer that indicates the winner, assuming the game is over
	**/
	public int getWinner(Player p1, Player p2)
	{
		if (board[29] != EMPTY_SPHERE)
			return board[29];
		return p1.getNumSpheres() == 0 ? p2.getColour() : (p2.getNumSpheres() == 0 ? p1.getColour() : 0);
	}
	
	/**
	*	Returns an integer index into the board array, given a string containing a cell address.
	*	@param addr a string containing the address of a cell
	*	@return an integer index into this classes internal board array
	**/
	public int getCell(String addr) {
		addr = addr.toLowerCase();
		return board[getCellIndex(addr)];
	}
	
	/**
	*	Assumes the given address is valid and sets that cell to the type provided.
	*	Type should be one of the following constants: EMPTY_SPHERE, BLACK_SPHERE, WHITE_SPHERE
	*	indicating whether that cell is set to empty, containing a black sphere, or containing a white sphere
	*	respectively. To determine whether addr is a valid address, the method isValidCellAddress should be used
	*	befor calling this method.
	*	@param addr a string containing an address which is assumed to be valid
	*	@type type an integer indicating the state that the cell should be set to
	**/
	public void setCell(String addr, int type) {
		addr = addr.toLowerCase();
		board[getCellIndex(addr)] = type;
	}
	
	/**
	*	The evaluation function for the board. Used by the minimax class to determine the favourability of the board.
	*	A positive value indicates that the board favours black, with negative favouring white. The greater the magnitude,
	*	the greater the favourability.
	*	@param p1 a reference to player 1
	*	@param p2 a reference to player 2
	*	@return an integer "score" for the board
	**/
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
			score += CLIMBING_WEIGHT * (level + 1) * countSpheresOnLevel(level, black);
			score -= CLIMBING_WEIGHT * (level + 1) * countSpheresOnLevel(level, white);
		}
		return score;
	}
	
	private int countSpheresOnLevel(int level, Player player)
	{
		int offset = 0;
		for (int i = 0; i < level; i++)
			offset += Math.pow(4 - i, 2);
		int levelLength = (4 - level) * (4 - level);
		
		int count = 0;
		for (int i = offset; i < offset + levelLength; i++)
		{
			if (board[i] == player.getColour())
				count++;
		}
		
		return count;
	}
	
	/**
	*	Prints a visual representation of the board to standard out. Each level is printed as an N x N grid,
	*	with the state of a cell being represented by one of (B, W, _), which represents a black sphere, white sphere,
	*	or empty cell respectively.
	**/
	public void printBoard()
	{
		for (int level = 0; level < 4; level++)
			printLevel(level);
	}
	
	private void printLevel(int level)
	{
		int gridSize = 4 - level;
		int offset = 0;
		for (int i = 0; i < level; i++)
			offset += Math.pow(4 - i, 2);
			
		for (int i = 0; i < gridSize; i++)
		{
			if (i == 0)
			{
				for (int j = 0; j < gridSize; j++)
					System.out.print(" _");
				System.out.println();
			}
			for (int j = 0; j < gridSize; j++)
			{
				if (j == 0)
					System.out.print("|");
				printSquare(offset+i*gridSize+j);
				System.out.print("|");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private void printSquare(int index)
	{
		switch (board[index])
		{
			case BLACK_SPHERE:
				System.out.print("B");
				break;
			case WHITE_SPHERE:
				System.out.print("W");
				break;
			case EMPTY_SPHERE:
				System.out.print("_");
				break;
			default:
				System.out.print("X");
				break;
		}
	}
}

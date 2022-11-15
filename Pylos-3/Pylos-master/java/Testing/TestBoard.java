/**
*	@author Joe Chance
*	@version 1.0
**/

import java.util.*;

class TestBoard
{

	private static String[] validAddresses = {
		"a1", "a2", "a3", "a4", "b1", "b4", "c1", "c4", "d1", "d4",
		"e1", "e2", "e3", "f1", "g1", "g3", "h1", "h2", "i1", "i2",
		"j1", "A1", "F2", "J1"
	};
	private static String[] invalidAddresses = {
		"a5", "a-1", "b5", "d5", "e4", "e5", "e9", "ee", "ea",
		"f4", "f9", "g4", "h3", "h9", "i3", "i4", "i9", "j2", "j3",
		"j9", "k1", "l4", "m4", "z0", "z1", "11", "pp"
	};
	private static String[] validPlacementOrder = {
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
	private static String[] invalidPlacementOrder = {
		"j1",
		"i1", "i2",
		"h1", "h2",
		"g1", "g2", "g3",
		"f1", "f2", "f3",
		"e1", "e2", "e3",
	};
	
	public static void main(String[] args)
	{
		if (testInvalidCellAddr())
			System.out.println("Invalid address tests passed");
		else
			System.out.println("Invalid address tests failed");
			
		if (testValidCellAddr())
			System.out.println("Invalid address tests passed");
		else
			System.out.println("Invalid address tests failed");
			
		if (testGetAndSetCell())
			System.out.println("Setting and getting cell tests passed");
		else
			System.out.println("Setting and getting cell tests failed");
			
		if (testValidBoardPlacement(validPlacementOrder))
			System.out.println("Valid sphere placement tests passed");
		else
			System.out.println("Valid sphere placement tests failed");
			
		if (testInvalidBoardPlacement(invalidPlacementOrder))
			System.out.println("Invalid sphere placement tests passed");
		else
			System.out.println("Invalid sphere placement tests failed");
			
		if (testValidSphereRaise())
			System.out.println("Valid sphere raise tests passed");
		else
			System.out.println("Valid sphere raise tests failed");
			
		if (testCompleteRow())
			System.out.println("Complete row tests passed");
		else
			System.out.println("Complete row tests failed");
	}
	
	public static boolean testInvalidCellAddr()
	{
		boolean passed = true;
		int failCount = 0;
		String[] testCases = invalidAddresses;
		Board board = new Board();
		for (String testCase: testCases)
		{
			if (board.isValidCellAddress(testCase))
			{
				passed = false;
				failCount++;
				System.out.println("Invalid address "+testCase+" failed test");
			}
		}
		System.out.println("Tests failed: "+failCount);
		return passed;
	}
	
	public static boolean testValidCellAddr()
	{
		boolean passed = true;
		int failCount = 0;
		String[] testCases = validAddresses;
		Board board = new Board();
		for (String testCase: testCases)
		{
			if (!board.isValidCellAddress(testCase))
			{
				passed = false;
				failCount++;
				System.out.println("Invalid address "+testCase+" failed test");
			}
		}
		System.out.println("Tests failed: "+failCount);
		return passed;
	}
	
	public static boolean testGetAndSetCell()
	{
		boolean passed = true;
		int failCount = 0;
		String[] testCases = validAddresses;
		Board board = new Board();
		for (String testCase: testCases)
		{
			board.setCell(testCase, board.EMPTY_SPHERE);
			if (board.getCell(testCase) != board.EMPTY_SPHERE)
			{
				passed = false;
				failCount++;
				System.out.println("Test failed: "+testCase+" should be empty");
			}
			board.setCell(testCase, board.BLACK_SPHERE);
			if (board.getCell(testCase) != board.BLACK_SPHERE)
			{
				passed = false;
				failCount++;
				System.out.println("Test failed: "+testCase+" should be black sphere");
			}
			board.setCell(testCase, board.WHITE_SPHERE);
			if (board.getCell(testCase) != board.WHITE_SPHERE)
			{
				passed = false;
				failCount++;
				System.out.println("Test failed: "+testCase+" should be white sphere");
			}
		}
		System.out.println("Tests failed: "+failCount);
		return passed;
	}
	
	public static boolean testValidBoardPlacement(String[] order)
	{
		boolean passed = true;
		int failCount = 0;
		String[] testCases = order;
		Board board = new Board();
		for (String testCase: testCases)
		{
			if (!board.canPlaceSphere(testCase, board.BLACK_SPHERE) || !board.canPlaceSphere(testCase, board.WHITE_SPHERE) || board.canPlaceSphere(testCase, board.EMPTY_SPHERE))
			{
				passed = false;
				failCount++;
				System.out.println("Test failed: couldn't place sphere at "+testCase);
			}
			board.setCell(testCase, board.BLACK_SPHERE);
		}
		System.out.println("Tests failed: "+failCount);
		return passed;
	}
	
	public static boolean testInvalidBoardPlacement(String[] order)
	{
		boolean passed = true;
		int failCount = 0;
		String[] testCases = order;
		Board board = new Board();
		for (String testCase: testCases)
		{
			if (board.canPlaceSphere(testCase, board.BLACK_SPHERE) || board.canPlaceSphere(testCase, board.WHITE_SPHERE) || board.canPlaceSphere(testCase, board.EMPTY_SPHERE))
			{
				passed = false;
				failCount++;
				System.out.println("Test failed: could place sphere at "+testCase);
			}
		}
		System.out.println("Tests failed: "+failCount);
		return passed;
	}
	
	public static boolean testValidSphereRaise()
	{
		boolean passed = true;
		int failCount = 0;
		String[] firstLevel = Arrays.copyOf(validPlacementOrder, 16);
		Board board = new Board();
		// Fill first level with spheres
		for (String cell: firstLevel)
			board.setCell(cell, Board.WHITE_SPHERE);
			
		String[] testCases = {
			"a1 e2", "a1 e3", "a1 f1", "a1 f2", "a1 f3", "a1 g1", "a1 g2", "a1 g3",
			"b2 e3", "b2 f3", "b2 g3", "b2 g2", "b2 g1"
		};
		
		for (String testCase: testCases)
		{
			String[] cells = testCase.split(" ");
			if (!board.canRaiseSphere(cells[0], cells[1]))
			{
				passed = false;
				failCount++;
				System.out.println("Test failed: couldn't raise sphere from"+cells[0]+" to "+cells[1]);
			}
		}
		System.out.println("Tests failed: "+failCount);
		return passed;
	}
	
	public static boolean testCompleteRow()
	{
		boolean passed = true;
		int failCount = 0;
		Board board = new Board();
		// Fill first level with spheres
		for (String cell: validPlacementOrder)
			board.setCell(cell, Board.WHITE_SPHERE);
			
		for (int i = 0; i < 25; i++)
		{
			if (!board.checkForCompleteRow(validPlacementOrder[i]))
			{
				passed = false;
				failCount++;
				System.out.println("Test failed: row not returning as complete");
			}
		}
		
		board.setCell("a1", Board.EMPTY_SPHERE);
		for (int i = 0; i < 4; i++)
		{
			if (board.checkForCompleteRow(validPlacementOrder[i]))
			{
				passed = false;
				failCount++;
				System.out.println("Test failed: incomplete row returning as complete");
			}
		}
		return passed;
	}
}
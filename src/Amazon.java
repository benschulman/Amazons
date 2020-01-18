public class Amazon {

	// Amazons game board
	public char board[][];
	// Array of positions of amazons for all teams
	public int positions[][];

	public int turn;

	/**
	 * The zero-argument constructor for the Amazon class. This instantiates the board to a 6 x 6 board and has 4
	 * Amazons. positions is instantiated with the starting coordinates (0,1), (0,4), (5,1), (5,4) for Amazons 1, 2, 3,
	 * and 4 respectively.
	 */
	public Amazon() {
		this.board = Config.board1;
		this.positions = Config.positions1;

		turn = 0;
	}

	/**
	 * Checks to see if there is a "line of sight" between startR, startC and row, col. A "line of sight" is any
	 * straight line (vertical, horizontal, or diagonal) that is unobstructed by a player or a fire character, i.e.
	 * there must exists a straight line of only ' ' characters between the two points. If the coordinates are the same,
	 * then false is returned.
	 *
	 * @param startR the row coordinate of the first point
	 * @param startC the column coordinate of the first point
	 * @param row the row coordinate of the second point
	 * @param col the column coordinate of the second point
	 * @return true if move is valid and false if not
	 */
	private boolean isValidMove(int startR, int startC, int row, int col) {

		// Check if coordinates exist on the board
		if(startR < 0 || startR >= this.board.length)
			return false;
		if(startC < 0 || startC >= this.board[0].length)
			return false;
		if(row < 0 || row >= this.board.length)
			return false;
		if(col < 0 || col >= this.board[0].length)
			return false;

		if(startR == row && startC == col)
			return false;

		// Horizontal
		if(row == startR) {
			// If start is first
			if(startC < col) {
				// loop forwards
				for(int i = startC + 1; i <= col; i++) {
					if (board[row][i] != ' ') {
						//System.out.println("Horiz");
						return false;
					}
				}
				// If start is last
			} else {
				// loop backwards
				for(int i = startC - 1; i >= col; i--){
					if(board[row][i] != ' '){
						return false;
					}
				}
			}
			return true;
		}

		// Vertical
		if(col == startC) {
			if(startR < row) {
				for(int i = startR + 1; i <= row; i++) {
					if(board[i][col] != ' '){
						//System.out.println("Vert");
						return false;
					}
				}
			} else {
				for(int i = startR - 1; i >= row; i--){
					if(board[i][col] != ' '){
						return false;
					}
				}
			}

			return true;
		}

		// Diagonal (top left to bottom right)
		if(startR-startC == row-col) {
			// Find lower row and columns
			if(startR < row) {
				startR++;
				startC++;
				while(startR <= row) {
					if(board[startR][startC] != ' '){
						return false;
					}

					startR++;
					startC++;
				}
			} else {
				startR--;
				startC--;
				while(startR >= row){
					if(board[startR][startC] != ' '){
						return false;
					}
					startR--;
					startC--;
				}
			}

			return true;
		}

		// Diagonal (bottom left to top right)

		if(startR+startC == row+col) {
			if(startC < col) {
				startC++;
				startR--;
				while(startC <= col){
					if(board[startR][startC] != ' '){
						return false;
					}
					startC++;
					startR--;
				}
			} else {
				startC--;
				startR++;
				while(startC >= col){
					if(board[startR][startC] != ' '){
						return false;
					}
					startC--;
					startR++;
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * This method will fire a shot to row, col on the board if it is a valid move (via isValidMove) from the location
	 * of Amazon number playerNumber.
	 *
	 * @param playerNumber the Amazon number to fire the shot from
	 * @param row the row on the board to fire the shot
	 * @param col the column on the board to fire the shot
	 * @return -1 if the move is not valid and 0 otherwise
	 */
	public int fireShot(int playerNumber, int row, int col) {
		if(playerNumber <= 0 || playerNumber > positions.length)
			return -1;

		int playerR = positions[playerNumber-1][0];
		int playerC = positions[playerNumber-1][1];

		if(!isValidMove(playerR, playerC, row, col))
			return -1;

		board[row][col] = 'x';
		return 0;
	}

	/**
	 * Moves the Amazon number playerNumber to the location row, column on the game board. Will only perform this
	 * movement if the coordinates given is a valid (via isValidMove) given the current location of the Amazon.
	 *
	 * @param playerNumber the Amazon number to move
	 * @param row the row on the board to move the Amazon
	 * @param col the column on the board to move the Amazon
	 * @return -1 if the move was invalid and 0 if move was successful.
	 */
	public int moveAmazon(int playerNumber, int row, int col) {
		if(playerNumber <= 0 || playerNumber > positions.length)
			return -1;

		int oldR = positions[playerNumber-1][0];
		int oldC = positions[playerNumber-1][1];

		if(!isValidMove(oldR, oldC, row, col))
			return -1;

		positions[playerNumber-1] = new int[]{row, col};

		char player = (char) (playerNumber + 48);

		this.board[oldR][oldC] = ' ';
		this.board[row][col] = player;


		return 0;
	}


	/**
	 * A private helper method that checks every adjacent square to the square with coordinates (row, col).
	 *
	 * @param row the row coordinate (must be a valid row)
	 * @param col the col coordinate (must be a valid col)
	 * @return true if at least one square is a ' ' false otherwise
	 */
	private boolean checkAround(int row, int col) {
		// Top Row
		if(!(row - 1 < 0)) {
			if(!(col-1 < 0))
				if(board[row-1][col-1] == ' ')
					return true;
			if(board[row-1][col] == ' ')
				return true;
			if(!(col + 1 >= board[row].length))
				if(board[row-1][col+1] == ' ')
					return true;
		}

		// Left
		if(!(col-1 < 0))
			if(board[row][col-1] == ' ')
				return true;
		// Right
		if(!(col+1 >= board[row].length))
			if(board[row][col+1] == ' ')
				return true;

		// Bottom Row
		if(!(row + 1 >= board.length)) {
			if(!(col-1 < 0))
				if(board[row+1][col-1] == ' ')
					return true;
			if(board[row+1][col] == ' ')
				return true;
			if(!(col+ 1 >= board[row+1].length))
				if(board[row+1][col+1] == ' ')
					return true;
		}

		return false;
	}

	/**
	 * Method to check if current player (via turn) can make a move.
	 * @return true if game is over, false otherwise
	 */
	public boolean isGameOver() {
		if(turn == 0) {
			for(int i = 0; i < positions.length/2; i++) {
				if(!checkAround(positions[i][0], positions[i][1]))
					return true;
			}
		} else {
			for(int i = positions.length/2; i < positions.length; i++) {
				if(!checkAround(positions[i][0], positions[i][1]))
					return true;
			}
		}
		return false;
	}

	/**
	 * This method prints the board to the console
	 */
	public void printBoard() {
		// Print Header
		System.out.print("––");
		for(int i = 0; i <= 3 * this.board[0].length ; i++)
			if(i % 3 == 0 && i != this.board[0].length * 3)
				System.out.print(i/3);
			else
				System.out.print("—");

		System.out.println();

		// Print board
		for(int i = 0; i < this.board.length; i++) {
			System.out.print(i + " |");
			for(int j = 0; j < this.board[i].length; j++) {
				System.out.print(this.board[i][j] + " "+ "|");
			}
			System.out.println();
		}

		// Print Footer
		for(int i = 0; i <= 3 * this.board[0].length ; i++)
			System.out.print("—");

		System.out.println();

	}

	/**
	 * This method takes in a String and interprets it as a game command. The function returns true if the String can be
	 * interpreted as a command and false otherwise. A command is valid if it has length 5 and is of the following form
	 * [Amazon Number][Row to move Amazon][Column to Move Amazon][Row to Fire][Column to Fire].
	 *
	 * @param input a String used as a command for the game
	 * @return false if input was invalid, true otherwise
	 */
	public boolean performAction(String input) {
		int player, r, c, rF, cF;

		// If input is incorrect length return false
		if(input == null || input.length() != 5)
			return false;
		try {
			// Parse input
			player = Integer.parseInt("" + input.charAt(0));
			r = Integer.parseInt("" + input.charAt(1));
			c = Integer.parseInt("" + input.charAt(2));

			rF = Integer.parseInt("" + input.charAt(3));
			cF = Integer.parseInt("" + input.charAt(4));
		}
		catch (NumberFormatException e) {
			return false;
		}
		// Check if turn matches Amazon number
		if(turn != (player-1)/(positions.length/2)) {
			System.out.println("Not your turn!");
			return false;
		}

		int oldR = positions[player-1][0];
		int oldC = positions[player-1][1];

		// Check if the move was successful, if not, return false
		if(moveAmazon(player, r, c) == -1) {
			return false;
		}
		// Check if shot was successful, if not, move Amazon back and return false
		if(fireShot(player, rF, cF) == -1) {
			moveAmazon(player, oldR, oldC);
			return false;
		}

		turn = (turn + 1) % 2;
		return true;
	}

}

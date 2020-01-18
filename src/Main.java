import java.util.Scanner;

public class Main implements Runnable {
	private int width = 1280;
	private int height = 720;

	private Thread thread;
	private Scanner scnr;
	private boolean running;
	private Amazon game;

	public void start() {
		System.out.println("Starting Amazons!");
		running = true;
		thread = new Thread(this, "Game");
		run();
	}

	private void init() {
		game = new Amazon();
		scnr = new Scanner(System.in);
	}

	public void run() {
		init();

		// The turn loop, will iterate for each turn
		while(!game.isGameOver() && running) {
			System.out.println("Player " + (game.turn + 1) + " turn:");

			boolean invalid = true;
			do {
				game.printBoard();
				System.out.println("Enter move (h for help, q to quit): ");
				String input = scnr.nextLine();

				if(input == null){
					invalid = true;
					continue;
				}

				if(input.equalsIgnoreCase("h")){
					printHelpMessage();
					continue;
				}

				if(input.equalsIgnoreCase("q")) {
					running = false;
					invalid = false;
					break;
				}

				invalid = !game.performAction(input);
				if(invalid)
					System.out.println("Invalid Move.");
			} while(invalid);
		}

		if(running) {
			game.printBoard();
			game.turn = game.turn + 1 % 2;
			System.out.println("Player " + (game.turn + 1) + " wins!" );
			running = false;
			return;
		}
		System.out.println("Quit game!");
		return;
	}

	private void printHelpMessage() {
		System.out.println("Format for move input:");
		System.out.println("\t[amazon to move][row to move to][column to move to][row to fire to][column to fire to]");
		System.out.println("\te.g. The input \"12101\" will move amazon 1 to the coordinates 2,1 and fire on 0,1");
		System.out.println("Enter to continue...");

		scnr.nextLine();
	}

	public static void main(String[] args) {
		new Main().start();
	}
}

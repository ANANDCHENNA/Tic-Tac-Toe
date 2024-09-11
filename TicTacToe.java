
import java.util.*;

public class TicTacToe {

    static String[] board;
    static String turn;
    static Scanner in = new Scanner(System.in);

    static String checkWinner() {
        for (int a = 0; a < 8; a++) {
            String line = null;

            switch (a) {
                case 0:
                    line = board[0] + board[1] + board[2];
                    break;
                case 1:
                    line = board[3] + board[4] + board[5];
                    break;
                case 2:
                    line = board[6] + board[7] + board[8];
                    break;
                case 3:
                    line = board[0] + board[3] + board[6];
                    break;
                case 4:
                    line = board[1] + board[4] + board[7];
                    break;
                case 5:
                    line = board[2] + board[5] + board[8];
                    break;
                case 6:
                    line = board[0] + board[4] + board[8];
                    break;
                case 7:
                    line = board[2] + board[4] + board[6];
                    break;
            }
            if (line.equals("XXX")) {
                return "X";
            } else if (line.equals("OOO")) {
                return "O";
            }
        }

        for (int a = 0; a < 9; a++) {
            if (Arrays.asList(board).contains(String.valueOf(a + 1))) {
                break;
            } else if (a == 8) {
                return "draw";
            }
        }

        return null;
    }

    static void printBoard() {
        System.out.println("|---|---|---|");
        System.out.println("| " + board[0] + " | " + board[1] + " | " + board[2] + " |");
        System.out.println("|-----------|");
        System.out.println("| " + board[3] + " | " + board[4] + " | " + board[5] + " |");
        System.out.println("|-----------|");
        System.out.println("| " + board[6] + " | " + board[7] + " | " + board[8] + " |");
        System.out.println("|---|---|---|");
    }

    static void playerMove() {
        System.out.println(turn + "'s turn; enter a slot number to place " + turn + " in:");
        int numInput;
        try {
            numInput = in.nextInt();
            if (!(numInput > 0 && numInput <= 9)) {
                System.out.println("Invalid input; re-enter slot number:");
                playerMove();
            } else if (board[numInput - 1].equals(String.valueOf(numInput))) {
                board[numInput - 1] = turn;
            } else {
                System.out.println("Slot already taken; re-enter slot number:");
                playerMove();
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input; re-enter slot number:");
            in.next(); // clear the invalid input
            playerMove();
        }
    }

    static void computerMove(int difficulty) {
        if (difficulty == 1) {
            randomMove(); // Easy difficulty
        } else if (difficulty == 2) {
            minimaxMove(); // Hard difficulty
        }
    }

    static void randomMove() {
        Random rand = new Random();
        int move;
        do {
            move = rand.nextInt(9);
        } while (!board[move].equals(String.valueOf(move + 1)));
        board[move] = turn;
        System.out.println("Computer (O) placed at position " + (move + 1));
    }

    // Minimax Algorithm
    static void minimaxMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < 9; i++) {
            if (board[i].equals(String.valueOf(i + 1))) {
                board[i] = "O"; // AI move
                int score = minimax(board, 0, false);
                board[i] = String.valueOf(i + 1); // Undo move
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        board[bestMove] = "O";
        System.out.println("Computer (O) placed at position " + (bestMove + 1));
    }

    // Minimax recursive function
    static int minimax(String[] board, int depth, boolean isMaximizing) {
        String result = checkWinner();
        if (result != null) {
            if (result.equals("O")) return 1;
            else if (result.equals("X")) return -1;
            else return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i].equals(String.valueOf(i + 1))) {
                    board[i] = "O";
                    int score = minimax(board, depth + 1, false);
                    board[i] = String.valueOf(i + 1);
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i].equals(String.valueOf(i + 1))) {
                    board[i] = "X";
                    int score = minimax(board, depth + 1, true);
                    board[i] = String.valueOf(i + 1);
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    public static void main(String[] args) {
        board = new String[9];
        turn = "X";
        String winner = null;

        for (int a = 0; a < 9; a++) {
            board[a] = String.valueOf(a + 1);
        }

        System.out.println("Welcome to 3x3 Tic Tac Toe.");
        printBoard();

        System.out.println("Select Game Mode: 1) Two Player  2) You vs Computer");
        int gameMode = in.nextInt();
        int difficulty = 1; // Default to Easy
        if (gameMode == 2) {
            System.out.println("Select Difficulty: 1) Easy  2) Hard");
            difficulty = in.nextInt();
        }

        while (winner == null) {
            if (gameMode == 1 || (gameMode == 2 && turn.equals("X"))) {
                playerMove();
            } else if (gameMode == 2 && turn.equals("O")) {
                computerMove(difficulty);
            }

            printBoard();
            winner = checkWinner();

            // Toggle turn
            turn = turn.equals("X") ? "O" : "X";

            // Announce whose turn it is
            if (winner == null && gameMode == 2 && difficulty == 1) {
                System.out.println(turn + "'s turn; " + (turn.equals("X") ? "Player" : "Computer") + " goes next.");
            }
        }

        if (winner.equalsIgnoreCase("draw")) {
            System.out.println("It's a draw! Thanks for playing.");
        } else {
            System.out.println("Congratulations! " + winner + "'s have won! Thanks for playing.");
        }
        in.close();
    }
}

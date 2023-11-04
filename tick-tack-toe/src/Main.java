import controller.GameController;
import models.*;
import strategies.winningstrategies.OrderOneColumnWinningStrategy;
import strategies.winningstrategies.OrderOneDiagonalWinningStrategy;
import strategies.winningstrategies.OrderOneRowWinningStrategy;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Create a game
        GameController gameController = new GameController();
        Scanner scanner = new Scanner(System.in);

        List<Player> players = List.of(
                new Player(new Symbol('X'), "Ashish", PlayerType.HUMAN),
                new Bot(new Symbol('O'), "Bot", BotDifficultyLevel.EASY)
        );
        int dimension = 3;

        Game game = gameController.createGame(
                dimension,
                players,
                List.of(
                        new OrderOneColumnWinningStrategy(dimension, players),
                        new OrderOneRowWinningStrategy(dimension, players),
                        new OrderOneDiagonalWinningStrategy(players)
                )
        );

        System.out.println("-------- Game is starting -----------");

        // while game status in progress
        while (gameController.getGameStatus(game).equals(GameStatus.IN_PROGRESS)) {
            System.out.println("This is how board looks like: ");
            // print board
            gameController.displayBoard(game);

            // print if undo
            System.out.println("Do you want to undo? (y/n)");
            // if yes -> call undo
            String input = scanner.next();

            if (input.equals("y")) {
                gameController.undo(game);
            } else {
                // move next player
                gameController.makeMove(game);
            }
        }

        // check status of game
        gameController.printResult(game);
    }
}
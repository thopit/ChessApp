package de.thomas.chess_app;

import org.junit.Test;

import chesspresso.Chess;
import chesspresso.game.Game;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import de.thomas.chess_app.search.Algorithm;

import static org.junit.Assert.assertTrue;

public class FirstTest {
    @Test
    public void thisAlwaysPasses() {
        assertTrue(true);
    }

    @Test
    public void testAlgorithm() {
        Game game = new Game();

        for (int k = 0; k < 2; k++) {
            short move = Algorithm.bestMoveAlphaBeta(game.getPosition(), 1);
            System.out.println("Moving");
            System.out.println(Move.getString(move));
            System.out.println();

            try {
                game.getPosition().doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
            }
        }
    }
}

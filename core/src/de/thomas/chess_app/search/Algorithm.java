package de.thomas.chess_app.search;

import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;
import de.thomas.chess_app.util.ChessUtil;

public class Algorithm {
    private static final int MAX_DEPTH = 1;

    public static short bestMoveAlphaBeta(Position position) {
        return bestMoveAlphaBeta(position, MAX_DEPTH);
    }

    public static short bestMoveAlphaBeta(Position position, int depth) {
        short[] moves = position.getAllMoves();
        int player = position.getToPlay();

        //Adapt player numbers to algorithm
        if (player == 0) {
            player = 1;
        }
        else if (player == 1) {
            player = -1;
        }

        int bestResult = -100000000;

        short bestMove = 0;

        /* DEBUG */
        System.out.println();
        System.out.println("Get best move");
         /* DEBUG */

        for (short move : moves) {
            Position testPosition = new Position(position);

            try {
                testPosition.doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
                return 0;
            }

            /* DEBUG */
            System.out.println("Checking move: " + Move.getString(move));
            /* DEBUG */

            int result = -alphaBeta(testPosition, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, -player);

            /* DEBUG */
            System.out.println("Result: " + result);
            /* DEBUG */


            if(result > bestResult) {
                bestResult = result;
                bestMove = move;
            }
        }

        /* DEBUG */
        System.out.println("Best move: " + Move.getString(bestMove));
        System.out.println();
        /* DEBUG */

        return bestMove;
    }

    private static int alphaBeta(Position position, int depth, int alpha, int beta, int player) {
        short[] moves = position.getAllMoves();

        if (depth == 0 || moves.length == 0) {

            /* DEBUG */
            for (int k = depth; k < MAX_DEPTH; k++) {
                System.out.print("  ");
            }
            System.out.println("Material value for player " + player + ": " + ChessUtil.getMaterial(position, player));
            /* DEBUG */


            return ChessUtil.getMaterial(position, player);
        }

        //TODO order moves for faster algorithm

        int bestValue = Integer.MIN_VALUE;

        for (short move : moves) {
            Position testPosition = new Position(position);

            /* DEBUG */
            for (int k = depth; k < MAX_DEPTH; k++) {
                System.out.print("  ");
            }
            System.out.println("Following move: " + Move.getString(move));
            /* DEBUG */

            try {
                testPosition.doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
                return 0;
            }

            int value = - alphaBeta(testPosition, depth - 1, -beta, -alpha, -player);

            bestValue = Math.max(bestValue, value);
            alpha = Math.max(alpha, value);


            if (alpha >= beta) {
                break;
            }
        }

        /* DEBUG */
        for (int k = depth; k < MAX_DEPTH; k++) {
            System.out.print("  ");
        }
        System.out.println("Best value: " + bestValue);
            /* DEBUG */

        return bestValue;
    }
}

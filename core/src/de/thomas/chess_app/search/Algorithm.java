package de.thomas.chess_app.search;

import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;
import de.thomas.chess_app.util.ChessUtil;

public class Algorithm {
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

        int bestResult = -1000000 * player;
        short bestMove = 0;

        for (short move : moves) {
            Position testPosition = new Position(position);

            try {
                testPosition.doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
                return 0;
            }

            int result = alphaBeta(testPosition, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, player);

            if ((player == 1 && result > bestResult) || (player == -1 && result < bestResult)) {
                bestResult = result;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private static int alphaBeta(Position position, int depth, int alpha, int beta, int player) {
        short[] moves = position.getAllMoves();

        return ChessUtil.getMaterial(position);

        /*
        if (depth == 0 || moves.length == 0) {
            return position.getMaterial();
        }

        return 0;
        */
    }
}

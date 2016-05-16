package de.thomas.chess_app.search;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;
import de.thomas.chess_app.util.ChessUtil;
import de.thomas.chess_app.util.DebugHelper;
import de.thomas.chess_app.util.Tuple;

public class Algorithm {
    private static final int MAX_DEPTH = 2;
    private static int positionsChecked = 0;


    public static short bestMoveAlphaBeta(Position position) {
        return bestMoveAlphaBeta(position, MAX_DEPTH);
    }

    public static short bestMoveAlphaBeta(Position position, int depth) {
        long startTime = System.nanoTime();

        short[] moves = position.getAllMoves();
        int player = position.getToPlay();

        //Adapt player numbers to algorithm
        if (player == 0) {
            player = 1;
        } else if (player == 1) {
            player = -1;
        }

        int bestResult = -ChessUtil.MAXIMUM_VALUE;

        short bestMove = 0;

        for (short move : moves) {
            Position testPosition = new Position(position);

            try {
                testPosition.doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
                System.exit(1);
            }

            positionsChecked++;

            DebugHelper.debug("Checking move: " + Move.getString(move), 2);

            int result = -alphaBeta(testPosition, depth, -ChessUtil.MAXIMUM_VALUE, ChessUtil.MAXIMUM_VALUE, -player);

            DebugHelper.debug("Result: " + result, 2);

            if (result > bestResult) {
                bestResult = result;
                bestMove = move;
            }
        }

        //zugzwang (imminent mate)
        if (bestMove == 0 && moves.length > 0) {
            bestMove = moves[0];
        }

        DebugHelper.debug("Best move: " + Move.getString(bestMove) + " | " + bestResult, 2);

        long timeNeeded = System.nanoTime() - startTime;
        double secondsNeeded = timeNeeded / 1E9;
        double posPerSec = positionsChecked / secondsNeeded;
        DebugHelper.debug("Positions checked: " + new DecimalFormat("#,###").format(positionsChecked)
                + " in " + String.format("%.4g", secondsNeeded) + " s"
                + " (" + new DecimalFormat("#,###").format(posPerSec) + " p/s)", 1);


        return bestMove;
    }

    private static int alphaBeta(Position position, int depth, int alpha, int beta, int player) {
        short[] moves = position.getAllMoves();

        if (depth == 0 || moves.length == 0) {
            int result = qSearch(position, depth, alpha, beta, player);
            DebugHelper.debug("Material value for player " + player + ": " + result, 3, MAX_DEPTH - depth);
            return result;
        }

        for (short move : moves) {
            DebugHelper.debug("Following move: " + Move.getString(move), 3, MAX_DEPTH - depth);

            try {
                position.doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
                System.exit(1);
            }

            positionsChecked++;

            int score = -alphaBeta(position, depth - 1, -beta, -alpha, -player);
            position.undoMove();

            if (score >= beta) {
                DebugHelper.debug("Best value: " + beta, 3, MAX_DEPTH - depth);
                return beta;
            }
            if (score > alpha) {
                alpha = score;
            }
        }

        DebugHelper.debug("Best value: " + alpha, 3, MAX_DEPTH - depth);

        return alpha;
    }

    private static int qSearch(Position position, int depth, int alpha, int beta, int player) {
        int standPat = ChessUtil.evaluate(position, player);

        if (standPat >= beta) {
            return beta;
        }
        if (alpha < standPat) {
            alpha = standPat;
        }

        short[] moves = position.getAllCapturingMoves();

        for (short move : moves) {
            DebugHelper.debug("Quiescent move: " + Move.getString(move), 3, MAX_DEPTH - depth);

            try {
                position.doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
                System.exit(1);
            }

            positionsChecked++;

            int score = -qSearch(position, depth - 1, -beta, -alpha, -player);
            position.undoMove();

            if (score >= beta) {
                return beta;
            }
            if (score > alpha) {
                alpha = score;
            }
        }

        return alpha;
    }
}

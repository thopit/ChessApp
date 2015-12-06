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
    private static final int MAX_DEPTH = 5;
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
        }
        else if (player == 1) {
            player = -1;
        }

        int bestResult = -100000000;

        short bestMove = 0;

        for (short move : moves) {
            Position testPosition = new Position(position);

            try {
                testPosition.doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
                return 0;
            }

            positionsChecked++;

            DebugHelper.debug("Checking move: " + Move.getString(move), 2);

            Tuple<Integer, List<Short>> resultTuple = alphaBeta(testPosition, depth, -100000000, 100000000, -player);
            int result = -resultTuple.x;
            List<Short> followingMoves = resultTuple.y;


            DebugHelper.debug("Result: " + result, 2);
            Collections.reverse(followingMoves);

            DebugHelper.debug(Move.getString(move), 2);
            for (Short m : followingMoves) {
                DebugHelper.debug(Move.getString(m), 2);
            }

            if(result > bestResult) {
                bestResult = result;
                bestMove = move;
            }
        }

        DebugHelper.debug("Best move: " + Move.getString(bestMove) + " | " + bestResult, 2);

        long timeNeeded = System.nanoTime() - startTime;
        double secondsNeeded = timeNeeded / 1E9;
        double posPerSec = positionsChecked / secondsNeeded;
        DebugHelper.debug("Positions checked: " + new DecimalFormat("#,###").format(positionsChecked)
                + " in " + String.format("%.4g", secondsNeeded) + " s"
                + " (" + new DecimalFormat("#,###").format(posPerSec) + " p/s)" , 1);


        return bestMove;
    }

    private static Tuple<Integer, List<Short>> alphaBeta(Position position, int depth, int alpha, int beta, int player) {
        short[] moves = position.getAllMoves();

        if (depth == 0 || moves.length == 0) {
            DebugHelper.debug("Material value for player " + player + ": " + ChessUtil.getMaterial(position, player), 3, MAX_DEPTH - depth);

            return new Tuple<Integer, List<Short>>(ChessUtil.getMaterial(position, player), new ArrayList<Short>());
            //return ChessUtil.getMaterial(position, player);
        }

        //TODO order moves for faster algorithm

        int bestValue = Integer.MIN_VALUE;
        short bestMove = 0;
        List<Short> bestMoveList = null;

        for (short move : moves) {
            Position testPosition = new Position(position);

            DebugHelper.debug("Following move: " + Move.getString(move), 3, MAX_DEPTH - depth);

            try {
                testPosition.doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
                return null;
            }

            positionsChecked++;


            Tuple<Integer, List<Short>> result = alphaBeta(testPosition, depth - 1, -beta, -alpha, -player);
            int value = -result.x;

            if (value > bestValue) {
                bestMove = move;
                bestMoveList = result.y;
            }

            bestValue = Math.max(bestValue, value);
            alpha = Math.max(alpha, value);


            if (alpha >= beta) {
                break;
            }
        }

        DebugHelper.debug("Best value: " + bestValue, 3, MAX_DEPTH - depth);
        bestMoveList.add(bestMove);

        return new Tuple<Integer, List<Short>>(bestValue, bestMoveList);
    }
}

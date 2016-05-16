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
        int alpha = -ChessUtil.MAXIMUM_VALUE;
        int beta = ChessUtil.MAXIMUM_VALUE;

        short bestMove = 0;

        for (short move : moves) {
            Position testPosition = new Position(position);

            try {
                testPosition.doMove(move);
            } catch (IllegalMoveException e) {
                //This should never happen
                e.printStackTrace();
                System.exit(1);
            }

            positionsChecked++;

            DebugHelper.debug("Checking move: " + Move.getString(move), 2);

            if (Move.getString(move).equals("b1-c3")) {
                System.out.println("DEBUG");
            }

            Tuple<Integer, List<Short>> resultTuple = alphaBeta(testPosition, depth, alpha, beta, -player);


            int result = -resultTuple.x;
            List<Short> followingMoves = resultTuple.y;


            DebugHelper.debug("Result: " + result, 2);
            Collections.reverse(followingMoves);

            DebugHelper.debug(Move.getString(move), 2);
            for (Short m : followingMoves) {
                DebugHelper.debug(Move.getString(m), 2);
            }

            if (result > bestResult) {
                bestResult = result;
                bestMove = move;
            }

            //TODO check how to to cutoffs
            /*
            alpha = Math.max(alpha, result);

            if (alpha >= beta) {
                break;
            }
            */
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

    private static Tuple<Integer, List<Short>> alphaBeta(Position position, int depth, int alpha, int beta, int player) {
        short[] moves = position.getAllMoves();

        if (depth == 0 || moves.length == 0) {
            Tuple<Integer, List<Short>> result = qSearch(position, depth, alpha, beta, player);
            DebugHelper.debug("Material value for player " + player + ": " + result.x, 3, MAX_DEPTH - depth);

            return result;
        }

        //TODO order moves for faster algorithm

        int bestValue = Integer.MIN_VALUE;
        short bestMove = 0;
        List<Short> bestMoveList = new ArrayList<Short>();

        for (short move : moves) {
            DebugHelper.debug("Following move: " + Move.getString(move), 3, MAX_DEPTH - depth);

            try {
                position.doMove(move);
            } catch (IllegalMoveException e) {
                //This should never happen
                e.printStackTrace();
                System.exit(1);
            }

            positionsChecked++;


            Tuple<Integer, List<Short>> result = alphaBeta(position, depth - 1, -beta, -alpha, -player);
            position.undoMove();

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

    private static Tuple<Integer, List<Short>> qSearch(Position position, int depth, int alpha, int beta, int player) {
        int standPat = ChessUtil.evaluate(position, player);

        if (standPat >= beta) {
            return new Tuple<Integer, List<Short>>(standPat, new ArrayList<Short>());
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
                //This should never happen
                e.printStackTrace();
                System.exit(1);
            }

            positionsChecked++;

            Tuple<Integer, List<Short>> result = qSearch(position, depth - 1, -beta, -alpha, -player);
            position.undoMove();

            int value = -result.x;

            if (value >= beta) {
                return new Tuple<Integer, List<Short>>(beta, new ArrayList<Short>());
            }

            if (value > alpha) {
                alpha = value;
            }

        }

        return new Tuple<Integer, List<Short>>(alpha, new ArrayList<Short>());
    }
}

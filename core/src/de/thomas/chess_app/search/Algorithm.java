package de.thomas.chess_app.search;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;
import de.thomas.chess_app.util.ChessUtil;
import de.thomas.chess_app.util.DebugHelper;

public class Algorithm {
    private static final int MAX_DEPTH = 2;
    private static int positionsChecked;

    public static short bestMoveAlphaBeta(Position position, List<Long> lastPositions) {
        return bestMoveAlphaBeta(position, MAX_DEPTH, lastPositions);
    }

    public static short bestMoveAlphaBeta(Position position, int depth, List<Long> lastPositions) {
        long startTime = System.nanoTime();
        positionsChecked = 0;

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

            List<Long> lastPositionsClone = new LinkedList<Long>(lastPositions);
            lastPositionsClone.add(testPosition.getHashCode());

            positionsChecked++;

            DebugHelper.debug("Checking move: " + Move.getString(move), 2);

            int result = -alphaBeta(testPosition, depth, -ChessUtil.MAXIMUM_VALUE, ChessUtil.MAXIMUM_VALUE, -player, lastPositionsClone);

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

    private static int alphaBeta(Position position, int depth, int alpha, int beta, int player, List<Long> lastPositions) {
        short[] moves = position.getAllMoves();

        if (depth == 0 || moves.length == 0) {
            int result = qSearch(position, depth, alpha, beta, player, lastPositions);
            DebugHelper.debug("Material value for player " + player + ": " + result, 3, MAX_DEPTH - depth);
            return result;
        }
        else if (hasThreeFoldRepetition(lastPositions, position.getHalfMoveClock())) {
            return 0;
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

            List<Long> lastPositionsClone = new LinkedList<Long>(lastPositions);
            lastPositionsClone.add(position.getHashCode());
            int score = -alphaBeta(position, depth - 1, -beta, -alpha, -player, lastPositionsClone);
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

    private static int qSearch(Position position, int depth, int alpha, int beta, int player, List<Long> lastPositions) {
        if (hasThreeFoldRepetition(lastPositions, position.getHalfMoveClock())) {
            return 0;
        }

        int standPat = ChessUtil.evaluate(position, player);

        if (standPat >= beta) {
            return beta;
        }
        if (alpha < standPat) {
            alpha = standPat;
        }

        short[] moves = position.getAllCapturingMoves();

        for (short move : moves) {
            DebugHelper.debug("Quiescent move: " + Move.getString(move), 4, MAX_DEPTH - depth);

            try {
                position.doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
                System.exit(1);
            }

            positionsChecked++;

            List<Long> lastPositionsClone = new LinkedList<Long>(lastPositions);
            lastPositionsClone.add(position.getHashCode());
            int score = -qSearch(position, depth - 1, -beta, -alpha, -player, lastPositionsClone);
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

    private static boolean hasThreeFoldRepetition(List<Long> lastPositions, int lastIrreversibleMove) {
        if (lastIrreversibleMove > 4) {
            long positionToCheck = lastPositions.get(lastPositions.size() - 1);
            int repetitionCount = 0;

            for (int k = lastPositions.size() - 3; k >= lastPositions.size() - 1 - lastIrreversibleMove; k-=2) {
                long currentPosition = lastPositions.get(k);
                if (currentPosition == positionToCheck) {
                    repetitionCount++;
                }
            }

            if (repetitionCount == 2) {
                return true;
            }
            else if (repetitionCount == 1 && lastPositions.size() > 2) {
                return true;
            }
        }

        return false;
    }
}

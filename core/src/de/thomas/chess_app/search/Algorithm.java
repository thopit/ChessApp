package de.thomas.chess_app.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;
import de.thomas.chess_app.util.ChessUtil;
import de.thomas.chess_app.util.DebugHelper;

public class Algorithm {
    private static final int MAX_DEPTH = 2;
    private static Map<Integer, Short> bestMoves;

    static {
        bestMoves = new HashMap<Integer, Short>();
    }

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

        DebugHelper.debug("\n Get best move", 2);

        for (short move : moves) {
            Position testPosition = new Position(position);

            try {
                testPosition.doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
                return 0;
            }

            DebugHelper.debug("Checking move: " + Move.getString(move), 2);

            int result = -alphaBeta(testPosition, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, -player);

            DebugHelper.debug("Result: " + result, 2);

            if(result > bestResult) {
                bestResult = result;
                bestMove = move;
            }
        }

        /*
        DebugHelper.debug("Best move: " + Move.getString(bestMove) + "\n", 2);


        List<Map.Entry<Integer, Short>> list = new ArrayList(bestMoves.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Short>>() {
            @Override
            public int compare(Map.Entry<Integer, Short> o1, Map.Entry<Integer, Short> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        bestMoves.clear();


        for (Map.Entry<Integer, Short> entry : list) {
            DebugHelper.debug(Move.getString(entry.getValue()), 1);
        }
        DebugHelper.debug("", 1);
        */

        return bestMove;
    }

    private static int alphaBeta(Position position, int depth, int alpha, int beta, int player) {
        short[] moves = position.getAllMoves();

        if (depth == 0 || moves.length == 0) {
            DebugHelper.debug("Material value for player " + player + ": " + ChessUtil.getMaterial(position, player), 3, MAX_DEPTH - depth);

            return ChessUtil.getMaterial(position, player);
        }


        //TODO order moves for faster algorithm

        int bestValue = Integer.MIN_VALUE;
        short bestMove = 0;

        for (short move : moves) {
            Position testPosition = new Position(position);

            DebugHelper.debug("Following move: " + Move.getString(move), 3, MAX_DEPTH - depth);

            try {
                testPosition.doMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
                return 0;
            }

            int value = - alphaBeta(testPosition, depth - 1, -beta, -alpha, -player);

            if (value > bestValue) {
                bestMove = move;
            }

            bestValue = Math.max(bestValue, value);
            alpha = Math.max(alpha, value);


            if (alpha >= beta) {
                break;
            }
        }

        DebugHelper.debug("Best value: " + bestValue, 3, MAX_DEPTH - depth);

        //bestMoves.put(depth, bestMove);


        return bestValue;
    }
}

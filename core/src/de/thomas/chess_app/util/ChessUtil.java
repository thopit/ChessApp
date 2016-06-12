package de.thomas.chess_app.util;

import chesspresso.Chess;
import chesspresso.position.Position;

public class ChessUtil {
    public static final int MAXIMUM_VALUE = 100000000;

    private static final int[] pawnSquareBlack = {
			0,   0,   0,   0,   0,   0,   0,   0,
			5,  10,  15,  20,  20,  15,  10,   5,
			4,   8,  12,  16,  16,  12,   8,   4,
			3,   6,   9,  12,  12,   9,   6,   3,
			2,   4,   6,   8,   8,   6,   4,   2,
			1,   2,   3, -10, -10,   3,   2,   1,
			0,   0,   0, -40, -40,   0,   0,   0,
			0,   0,   0,   0,   0,   0,   0,   0
    };

    private static final int[] pawnSquareWhite = {
            0,   0,   0,   0,   0,   0,   0,   0,
			0,   0,   0, -40, -40,   0,   0,   0,
			1,   2,   3, -10, -10,   3,   2,   1,
			2,   4,   6,   8,   8,   6,   4,   2,
			3,   6,   9,  12,  12,   9,   6,   3,
			4,   8,  12,  16,  16,  12,   8,   4,
			5,  10,  15,  20,  20,  15,  10,   5,
			0,   0,   0,   0,   0,   0,   0,   0
    };

    private static final int[] knightSquareBlack = {
            -10, -10, -10, -10, -10, -10, -10, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10, -30, -10, -10, -10, -10, -30, -10
    };

    private static final int[] knightSquareWhite = {
            -10, -10, -10, -10, -10, -10, -10, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10, -10, -10, -10, -10, -10, -10, -10
    };

    private static final int[] bishopSquareBlack = {
            -10, -10, -10, -10, -10, -10, -10, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10, -10, -20, -10, -10, -20, -10, -10
    };

    private static final int[] bishopSquareWhite = {
            -10, -10, -20, -10, -10, -20, -10, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10, -10, -10, -10, -10, -10, -10, -10,
    };

    private static final int[] rookSquareBlack = {
			  0,   0,   0,   0,   0,   0,   0,   0,
			 15,  15,  15,  15,  15,  15,  15,  15,
			  0,   0,   0,   0,   0,   0,   0,   0,
			  0,   0,   0,   0,   0,   0,   0,   0,
			  0,   0,   0,   0,   0,   0,   0,   0,
			  0,   0,   0,   0,   0,   0,   0,   0,
			  0,   0,   0,   0,   0,   0,   0,   0,
			-10,   0,   0,  10,  10,   0,   0, -10
    };

    private static final int[] rookSquareWhite = {
            -10,   0,   0,  10,  10,   0,   0, -10,
			  0,   0,   0,   0,   0,   0,   0,   0,
			  0,   0,   0,   0,   0,   0,   0,   0,
			  0,   0,   0,   0,   0,   0,   0,   0,
			  0,   0,   0,   0,   0,   0,   0,   0,
			  0,   0,   0,   0,   0,   0,   0,   0,
			 15,  15,  15,  15,  15,  15,  15,  15,
			  0,   0,   0,   0,   0,   0,   0,   0
    };

    private static final int[] queenSquareBlack = {
            -10, -10, -10, -10, -10, -10, -10, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10, -10, -20, -10, -10, -20, -10, -10
    };

    private static final int[] queenSquareWhite = {
            -10, -10, -10, -10, -10, -10, -10, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,  10,  10,   5,   0, -10,
			-10,   0,   5,   5,   5,   5,   0, -10,
			-10,   0,   0,   0,   0,   0,   0, -10,
			-10, -10, -20, -10, -10, -20, -10, -10
    };

    private static final int[] kingSquareOpeningBlack = {
            -40, -40, -40, -40, -40, -40, -40, -40,
			-40, -40, -40, -40, -40, -40, -40, -40,
			-40, -40, -40, -40, -40, -40, -40, -40,
			-40, -40, -40, -40, -40, -40, -40, -40,
			-40, -40, -40, -40, -40, -40, -40, -40,
			-40, -40, -40, -40, -40, -40, -40, -40,
			-20, -20, -20, -20, -20, -20, -20, -20,
			  0,  20,  40, -20,   0, -20,  40,  20
    };

    private static final int[] kingSquareOpeningWhite = {
              0,  20,  40, -20,   0, -20,  40,  20,
			-20, -20, -20, -20, -20, -20, -20, -20,
			-40, -40, -40, -40, -40, -40, -40, -40,
			-40, -40, -40, -40, -40, -40, -40, -40,
			-40, -40, -40, -40, -40, -40, -40, -40,
			-40, -40, -40, -40, -40, -40, -40, -40,
			-40, -40, -40, -40, -40, -40, -40, -40,
			-40, -40, -40, -40, -40, -40, -40, -40
    };

    private static final int[] kingSquareEndGameBlack = {
              0,  10,  20,  30,  30,  20,  10,   0,
			 10,  20,  30,  40,  40,  30,  20,  10,
			 20,  30,  40,  50,  50,  40,  30,  20,
			 30,  40,  50,  60,  60,  50,  40,  30,
			 30,  40,  50,  60,  60,  50,  40,  30,
			 20,  30,  40,  50,  50,  40,  30,  20,
			 10,  20,  30,  40,  40,  30,  20,  10,
			  0,  10,  20,  30,  30,  20,  10,   0
    };

    private static final int[] kingSquareEndGameWhite = {
              0,  10,  20,  30,  30,  20,  10,   0,
			 10,  20,  30,  40,  40,  30,  20,  10,
			 20,  30,  40,  50,  50,  40,  30,  20,
			 30,  40,  50,  60,  60,  50,  40,  30,
			 30,  40,  50,  60,  60,  50,  40,  30,
			 20,  30,  40,  50,  50,  40,  30,  20,
			 10,  20,  30,  40,  40,  30,  20,  10,
			  0,  10,  20,  30,  30,  20,  10,   0
    };


    public static int evaluate(Position position, int player) {
        if (position.isMate()) {
            return mateCalculator(position, player);
        }

        //TODO add 3 moves repetition
        if (position.isStaleMate() || position.isTerminal()) {
            return 0;
        }

        int materialValue = position.getMaterial();

        //Normalize to adjust to Chesspresso
        if (position.getToPlay() == 1) {
            materialValue *= -1;
        }
        //Normalize so positive value: player 1 is at advantage, negative value: player -1 is at advantage
        if (player == -1) {
            materialValue *= -1;
        }

        int positionValue = 0;

        int pawnsWhite = 0;
        int knightsWhite = 0;
        int bishopsWhite = 0;
        int rooksWhite = 0;
        int queensWhite = 0;
        int pawnsBlack = 0;
        int knightsBlack = 0;
        int bishopsBlack = 0;
        int rooksBlack = 0;
        int queensBlack = 0;

        int whiteKingSqi = -1;
        int blackKingSqi = -1;

        for (int square = 0; square < 64; square++) {
            int stone = position.getStone(square);

            if (stone == Chess.WHITE_PAWN) {
                positionValue += pawnSquareWhite[square];
                pawnsWhite++;
            }
            else if (stone == Chess.BLACK_PAWN) {
                positionValue -= pawnSquareBlack[square];
                pawnsBlack++;
            }
            else if (stone == Chess.WHITE_KNIGHT) {
                positionValue += knightSquareWhite[square];
                knightsWhite++;
            }
            else if (stone == Chess.BLACK_KNIGHT) {
                positionValue -= knightSquareBlack[square];
                knightsBlack++;
            }
            else if (stone == Chess.WHITE_BISHOP) {
                positionValue += bishopSquareWhite[square];
                bishopsWhite++;
            }
            else if (stone == Chess.BLACK_BISHOP) {
                positionValue -= bishopSquareBlack[square];
                bishopsBlack++;
            }
            else if (stone == Chess.WHITE_ROOK) {
                positionValue += rookSquareWhite[square];
                rooksWhite++;
            }
            else if (stone == Chess.BLACK_ROOK) {
                positionValue -= rookSquareBlack[square];
                rooksBlack++;
            }
            else if (stone == Chess.WHITE_QUEEN) {
                positionValue += queenSquareWhite[square];
                queensWhite++;
            }
            else if (stone == Chess.BLACK_QUEEN) {
                positionValue -= queenSquareBlack[square];
                queensBlack++;
            }
            else if (stone == Chess.WHITE_KING) {
                whiteKingSqi = square;
            }
            else if (stone == Chess.BLACK_KING) {
                blackKingSqi = square;
            }
        }

        //Check endgame
        boolean whiteEndGame = (queensWhite == 0 && rooksWhite <= 1)
                || (queensWhite == 1 && knightsWhite <= 1 && bishopsWhite == 0 && rooksWhite == 0) ||
                (queensWhite == 1 && knightsWhite == 0 && bishopsWhite <= 1 && rooksWhite == 0);

        boolean blackEndGame = (queensBlack == 0 && rooksBlack <= 1)
                || (queensBlack == 1 && knightsBlack <= 1 && bishopsBlack == 0 && rooksBlack == 0) ||
                (queensBlack == 1 && knightsBlack == 0 && bishopsBlack <= 1 && rooksBlack == 0);

        boolean endGame = whiteEndGame && blackEndGame;

        if (endGame) {
            positionValue += kingSquareEndGameWhite[whiteKingSqi];
            positionValue -= kingSquareEndGameBlack[blackKingSqi];
        }
        else {
            positionValue += kingSquareOpeningWhite[whiteKingSqi];
            positionValue -= kingSquareOpeningBlack[blackKingSqi];
        }

        if (player == -1) {
            positionValue *= -1;
        }

        int value = materialValue + positionValue;

        return value;
    }



    private static int mateCalculator(Position position, int player) {
        if (position.getToPlay() == 0) {
            if (player == 1) {
                return -MAXIMUM_VALUE;
            }
            else {
                return MAXIMUM_VALUE;
            }
        }
        else {
            if (player == 1) {
                return MAXIMUM_VALUE;
            }
            else {
                return -MAXIMUM_VALUE;
            }
        }
    }
}

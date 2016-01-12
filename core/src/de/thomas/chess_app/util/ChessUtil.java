package de.thomas.chess_app.util;

import chesspresso.Chess;
import chesspresso.position.Position;

public class ChessUtil {
    private static final int[] pawnSquareBlack = {
            0,  0,  0,  0,  0,  0,  0,  0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5,  5, 10, 25, 25, 10,  5,  5,
            0,  0,  0, 20, 20,  0,  0,  0,
            5, -5,-10,  0,  0,-10, -5,  5,
            5, 10, 10,-20,-20, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    private static final int[] pawnSquareWhite = {
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10,-20,-20, 10, 10,  5,
            5, -5,-10,  0,  0,-10, -5,  5,
            0,  0,  0, 20, 20,  0,  0,  0,
            5,  5, 10, 25, 25, 10,  5,  5,
            10, 10, 20, 30, 30, 20, 10, 10,
            50, 50, 50, 50, 50, 50, 50, 50,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    private static final int[] knightSquareBlack = {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50,
    };

    private static final int[] knightSquareWhite = {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50,
    };

    private static final int[] bishopSquareBlack = {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -20,-10,-10,-10,-10,-10,-10,-20,
    };

    private static final int[] bishopSquareWhite = {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -20,-10,-10,-10,-10,-10,-10,-20,
    };

    private static final int[] rookSquareBlack = {
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10, 10, 10, 10, 10,  5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            0,  0,  0,  5,  5,  0,  0,  0
    };

    private static final int[] rookSquareWhite = {
            0,  0,  0,  5,  5,  0,  0,  0 ,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            5, 10, 10, 10, 10, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0,
    };

    private static final int[] queenSquareBlack = {
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -5,  0,  5,  5,  5,  5,  0, -5,
            0,  0,  5,  5,  5,  5,  0, -5,
            -10,  5,  5,  5,  5,  5,  0,-10,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20
    };

    private static final int[] queenSquareWhite = {
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -10,  5,  5,  5,  5,  5,  0,-10,
            0,  0,  5,  5,  5,  5,  0, -5,
            -5,  0,  5,  5,  5,  5,  0, -5,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20,
    };

    private static final int[] kingSquareOpeningBlack = {
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            20, 20,  0,  0,  0,  0, 20, 20,
            20, 30, 10,  0,  0, 10, 30, 20
    };

    private static final int[] kingSquareOpeningWhite = {
            20, 30, 10,  0,  0, 10, 30, 20,
            20, 20,  0,  0,  0,  0, 20, 20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
    };

    private static final int[] kingSquareEndGameBlack = {
            -50,-40,-30,-20,-20,-30,-40,-50,
            -30,-20,-10,  0,  0,-10,-20,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-30,  0,  0,  0,  0,-30,-30,
            -50,-30,-30,-30,-30,-30,-30,-50
    };

    private static final int[] kingSquareEndGameWhite = {
            -50,-30,-30,-30,-30,-30,-30,-50,
            -30,-30,  0,  0,  0,  0,-30,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-20,-10,  0,  0,-10,-20,-30,
            -50,-40,-30,-20,-20,-30,-40,-50,
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
                return -100000000;
            }
            else {
                return 100000000;
            }
        }
        else {
            if (player == 1) {
                return 100000000;
            }
            else {
                return -100000000;
            }
        }
    }
}

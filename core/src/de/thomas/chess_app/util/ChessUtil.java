package de.thomas.chess_app.util;

import chesspresso.position.Position;

public class ChessUtil {
    public static int evaluate(Position position, int player) {
        int value = position.getMaterial();

        //Normalize to adjust to Chesspresso
        if (position.getToPlay() == 1) {
            value *= -1;
        }

        //Normalize so positive value: player 1 is at advantage, negative value: player -1 is at advantage
        if (player == -1) {
            value *= -1;
        }

        if (position.isMate()) {
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

        //TODO Deal with stalemate

        return value;
    }
}

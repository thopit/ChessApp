package de.thomas.chess_app.util;

import chesspresso.position.Position;

public class ChessUtil {
    public static int getMaterial(Position position, int player) {
        int value = position.getMaterial();

        //Normalize to adjust to Chesspresso
        if (position.getToPlay() == 1) {
            value *= -1;
        }

        //Normalize so positive value: player 1 is at advantage, negative value: player -1 is at advantage
        if (player == -1) {
            value *= -1;
        }

        return value;
    }
}

package de.thomas.chess_app.util;

import chesspresso.position.Position;

public class ChessUtil {
    public static int getMaterial(Position position) {
        if (position.getToPlay() == 0) {
            return position.getMaterial();
        }
        else {
            return position.getMaterial() * -1;
        }
    }
}

package de.thomas.chess_app.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.thomas.chess_app.controller.ChessController;

public class RedoButtonListener extends ChangeListener {
    private ChessController chessController;

    public RedoButtonListener(ChessController chessController) {
        this.chessController = chessController;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        chessController.goForward();
    }
}

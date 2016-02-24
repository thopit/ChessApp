package de.thomas.chess_app.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.thomas.chess_app.controller.ChessController;

public class RevertButtonListener extends ChangeListener {
    private ChessController chessController;

    public RevertButtonListener(ChessController chessController) {
        this.chessController = chessController;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        chessController.goBack();
    }
}

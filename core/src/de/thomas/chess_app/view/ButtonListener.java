package de.thomas.chess_app.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.thomas.chess_app.controller.ChessController;

public class ButtonListener extends ChangeListener {
    private ChessController chessController;

    public ButtonListener(ChessController chessController) {
        this.chessController = chessController;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        TextButton button = (TextButton) actor;
        //button.setChecked(true);
        chessController.computerMove();
        button.setChecked(false);
    }
}

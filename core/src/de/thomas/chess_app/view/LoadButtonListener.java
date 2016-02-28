package de.thomas.chess_app.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.io.File;

import de.thomas.chess_app.controller.ChessController;

public class LoadButtonListener extends ChangeListener {
    private final ChessController chessController;
    private final TextField textField;

    public LoadButtonListener(ChessController chessController, TextField textField) {
        this.chessController = chessController;
        this.textField = textField;
    }
    
    @Override
    public void changed(ChangeEvent event, Actor actor) {
        chessController.loadGame(new File(textField.getText()));
    }
}

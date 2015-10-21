package de.thomas.chess_app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.thomas.chess_app.controller.ChessController;
import de.thomas.chess_app.view.GameScreen;
import de.thomas.chess_app.view.InputListener;

public class ChessMain extends Game {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
        chesspresso.game.Game chessGame = new chesspresso.game.Game();

        GameScreen gameScreen = new GameScreen(chessGame);

        ChessController controller = new ChessController(chessGame, gameScreen);
        InputListener listener = new InputListener(controller);
        Gdx.input.setInputProcessor(listener);

		setScreen(gameScreen);
	}
}

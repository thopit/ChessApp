package de.thomas.chess_app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.thomas.chess_app.controller.ChessController;
import de.thomas.chess_app.view.GameScreen;
import de.thomas.chess_app.view.InputListener;
import de.thomas.chess_app.view.TestScreen;

public class ChessMain extends Game {
    public static final boolean TEST_MODE = false;
	
	@Override
	public void create () {
        chesspresso.game.Game chessGame = new chesspresso.game.Game();

        InputMultiplexer multiplexer = new InputMultiplexer();

        ChessController controller = new ChessController(chessGame);
        GameScreen gameScreen = new GameScreen(chessGame, multiplexer, controller);
        controller.setGameScreen(gameScreen);

        InputListener listener = new InputListener(controller);
        multiplexer.addProcessor(listener);
        Gdx.input.setInputProcessor(multiplexer);

		setScreen(gameScreen);
        //setScreen(new TestScreen());
	}
}

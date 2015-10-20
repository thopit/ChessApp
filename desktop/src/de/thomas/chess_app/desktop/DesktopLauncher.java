package de.thomas.chess_app.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.thomas.chess_app.ChessMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "ChessApp";
        config.width = 720;
        config.height = 810;


		new LwjglApplication(new ChessMain(), config);
	}
}

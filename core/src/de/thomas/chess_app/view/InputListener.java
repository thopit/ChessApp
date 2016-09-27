package de.thomas.chess_app.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.thomas.chess_app.controller.ChessController;

public class InputListener implements InputProcessor {
    private ChessController controller;
    int lastPosX;
    int lastPosY;

    public InputListener(ChessController controller) {
        this.controller = controller;
        lastPosX = -1;
        lastPosY = -1;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ENTER) {
            controller.computerMove();
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (lastPosX == -1 || lastPosY == -1) {
            controller.trySelect(screenX, screenY);

            lastPosX = screenX;
            lastPosY = screenY;
        }
        else {
            if (controller.tryMove(lastPosX, lastPosY, screenX, screenY)) {
                lastPosX = -1;
                lastPosY = -1;
            }
            else {
                controller.trySelect(screenX, screenY);

                lastPosX = screenX;
                lastPosY = screenY;
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (lastPosX != -1 || lastPosY != -1) {
            if (controller.tryMove(lastPosX, lastPosY, screenX, screenY)) {
                lastPosX = -1;
                lastPosY = -1;
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

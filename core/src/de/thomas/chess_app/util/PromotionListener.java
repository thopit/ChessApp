package de.thomas.chess_app.util;

import com.badlogic.gdx.Input;

public class PromotionListener implements Input.TextInputListener {
    private String text = "X";

    @Override
    public void input(String text) {
        this.text = text;
    }

    @Override
    public void canceled() {
        this.text = "Q";
    }

    public String getText() {
        while (text.equals("X")) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return text;
    }
}

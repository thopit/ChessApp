package de.thomas.chess_app.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;

import chesspresso.Chess;
import chesspresso.game.Game;
import chesspresso.move.Move;
import chesspresso.position.Position;


public class GameScreen implements Screen {
    private SpriteBatch batch;

    private Texture fieldWhite;
    private Texture fieldBlack;

    private Texture kingWhite;
    private Texture kingBlack;
    private Texture queenWhite;
    private Texture queenBlack;
    private Texture rookWhite;
    private Texture rookBlack;
    private Texture bishopWhite;
    private Texture bishopBlack;
    private Texture knightWhite;
    private Texture knightBlack;
    private Texture pawnWhite;
    private Texture pawnBlack;

    private Map<Short, Texture> textureMap;

    private final int Y_SHIFT = 90;
    private final int SQUARE_SIZE = 90;

    private Game chessGame;

    @Override
    public void show() {
        batch = new SpriteBatch();
        fieldBlack = new Texture("field_black.png");
        fieldWhite = new Texture("field_white.png");

        kingWhite = new Texture("king_white.png");
        kingBlack = new Texture("king_black.png");
        queenWhite = new Texture("queen_white.png");
        queenBlack = new Texture("queen_black.png");
        rookWhite = new Texture("rook_white.png");
        rookBlack = new Texture("rook_black.png");
        bishopWhite = new Texture("bishop_white.png");
        bishopBlack = new Texture("bishop_black.png");
        knightWhite = new Texture("knight_white.png");
        knightBlack = new Texture("knight_black.png");
        pawnWhite = new Texture("pawn_white.png");
        pawnBlack = new Texture("pawn_black.png");

        textureMap = new HashMap<Short, Texture>();
        textureMap.put(Chess.WHITE_KING, kingWhite);
        textureMap.put(Chess.BLACK_KING, kingBlack);
        textureMap.put(Chess.WHITE_QUEEN, queenWhite);
        textureMap.put(Chess.BLACK_QUEEN, queenBlack);
        textureMap.put(Chess.WHITE_ROOK, rookWhite);
        textureMap.put(Chess.BLACK_ROOK, rookBlack);
        textureMap.put(Chess.WHITE_BISHOP, bishopWhite);
        textureMap.put(Chess.BLACK_BISHOP, bishopBlack);
        textureMap.put(Chess.WHITE_KNIGHT, knightWhite);
        textureMap.put(Chess.BLACK_KNIGHT, knightBlack);
        textureMap.put(Chess.WHITE_PAWN, pawnWhite);
        textureMap.put(Chess.BLACK_PAWN, pawnBlack);

        chessGame = new Game();
    }

    private void test() {
        Game chessGame = new Game();
        short[] moves = chessGame.getPosition().getAllMoves();

        for (short m : moves) {
            String string = Move.getString(m);
            //System.out.println(string);
        }

        Position position = chessGame.getPosition();

        for (int k = 0; k < 64; k++) {
            int piece = position.getPiece(k);
            if (piece == Chess.WHITE_KING) {
                System.out.println("White king at: " + k);
            }

            if (piece == Chess.BLACK_KING) {
                System.out.println("Black king at: " + k);
            }
        }
    }

    @Override
    public void render(float delta) {
        Position position = chessGame.getPosition();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if ((x % 2 == 1 && y % 2 == 1) || (x % 2 == 0 && y % 2 == 0)) {
                    batch.draw(fieldBlack, x * SQUARE_SIZE, y * SQUARE_SIZE + Y_SHIFT);
                }
                else if ((x % 2 == 1 && y % 2 == 0) || (x % 2 == 0 && y % 2 == 1)) {
                   batch.draw(fieldWhite, x * SQUARE_SIZE, y * SQUARE_SIZE + Y_SHIFT);
                }

                short stone = (short) position.getStone(Chess.coorToSqi(x, y));

                if (stone != 0) {
                    batch.draw(textureMap.get(stone), x * SQUARE_SIZE, y * SQUARE_SIZE + Y_SHIFT);
                }
            }
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

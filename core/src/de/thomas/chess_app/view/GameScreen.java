package de.thomas.chess_app.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

import chesspresso.Chess;
import chesspresso.game.Game;
import chesspresso.move.Move;
import chesspresso.position.Position;


public class GameScreen implements Screen {
    private SpriteBatch batch;

    private OrthographicCamera camera;

    private Texture fieldWhite;
    private Texture fieldBlack;
    private Texture fieldSelected;

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

    private GridPoint2 selectedPosition;

    private final int WIDTH = 720;
    private final int HEIGHT = 1280;
    private final int Y_SHIFT = 240;//560;
    private final int SQUARE_SIZE = 90;

    private Game chessGame;

    public GameScreen(Game chessGame) {
        this.chessGame = chessGame;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        batch = new SpriteBatch();

        fieldBlack = new Texture("field_black.png");
        fieldWhite = new Texture("field_white.png");
        fieldSelected = new Texture("field_selected.png");

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

        selectedPosition = null;
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

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

                //Draw selection display
                if (selectedPosition != null && x == selectedPosition.x && y == selectedPosition.y) {
                    batch.draw(fieldSelected, x * SQUARE_SIZE, y * SQUARE_SIZE + Y_SHIFT);
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

    public int getSqi(int posX, int posY) {
        GridPoint2 position = getPosition(posX, posY);

        return Chess.coorToSqi(position.x, position.y);
    }

    public GridPoint2 getPosition(int posX, int posY) {
        Vector3 touchPos = new Vector3();
        touchPos.set(posX, posY, 0);
        camera.unproject(touchPos);

        int col = (int) (touchPos.x / SQUARE_SIZE);
        int row = (int) ((touchPos.y - Y_SHIFT) / SQUARE_SIZE);

        return new GridPoint2(col, row);
    }

    public void setSelectedPosition(GridPoint2 selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

}

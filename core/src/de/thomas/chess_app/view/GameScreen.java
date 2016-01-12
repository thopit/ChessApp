package de.thomas.chess_app.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.HashMap;
import java.util.Map;

import chesspresso.Chess;
import chesspresso.game.Game;
import chesspresso.position.Position;
import de.thomas.chess_app.controller.ChessController;
import de.thomas.chess_app.util.ChessUtil;


public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;

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

    private BitmapFont font;

    private Map<Short, Texture> textureMap;

    private GridPoint2 selectedPosition;

    private final int WIDTH = 720;
    private final int HEIGHT = 1280;
    private final int Y_SHIFT = 240;//560;
    private final int SQUARE_SIZE = 90;

    private String whiteEvaluation;
    private String blackEvaluation;

    private Game chessGame;
    private InputMultiplexer multiplexer;
    private ChessController controller;

    public GameScreen(Game chessGame, InputMultiplexer multiplexer, ChessController controller) {
        this.chessGame = chessGame;
        this.multiplexer = multiplexer;
        this.controller = controller;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        batch = new SpriteBatch();
        stage = new Stage();
        multiplexer.addProcessor(stage);

        Table table = new Table();
        table.setDebug(true);
        table.setBounds(320, 50, 200, 100);
        stage.addActor(table);

        final TextButton moveButton = new TextButton("Move", createSkin());
        table.add(moveButton).width(100).height(50);
        final TextButton revertButton = new TextButton("Revert", createSkin());
        table.add(revertButton).width(100).height(50);

        moveButton.addListener(new MoveButtonListener(controller));
        revertButton.addListener(new RevertButtonListener(controller));

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

        font = new BitmapFont(Gdx.files.internal("arial32.fnt"), false);

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

        whiteEvaluation = String.valueOf(ChessUtil.evaluate(chessGame.getPosition(), 1));
        blackEvaluation = String.valueOf(ChessUtil.evaluate(chessGame.getPosition(),- 1));
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

        font.draw(batch, "White advantage: " + whiteEvaluation, 111, 111);
        //font.draw(batch, "Black advantage: " + blackEvaluation, 111, 81);

        batch.end();

        stage.act(delta);
        stage.draw();
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
        batch.dispose();
        stage.dispose();
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


    public void updateEvaluation() {
        whiteEvaluation = String.valueOf(ChessUtil.evaluate(chessGame.getPosition(), 1));
        blackEvaluation = String.valueOf(ChessUtil.evaluate(chessGame.getPosition(), -1));
    }

    private Skin createSkin() {
        Skin skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        //textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
        return skin;
    }
}

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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chesspresso.Chess;
import chesspresso.game.Game;
import chesspresso.position.Position;
import de.thomas.chess_app.ChessMain;
import de.thomas.chess_app.controller.ChessController;
import de.thomas.chess_app.util.ChessUtil;


public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;

    private OrthographicCamera camera;

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

    private BitmapFont font;

    private Map<Short, Texture> textureMap;

    private GridPoint2 selectedPosition;
    private GridPoint2 selectedStartMousePosition;
    private int selectedStone;

    private final int WIDTH = 720;
    private final int HEIGHT = 1280;
    private final int Y_SHIFT = 420; //560 is maximum
    private final int SQUARE_SIZE = 90;

    private String whiteEvaluation;
    private boolean gameEnded;
    private String gameResult;
    private boolean calculating;
    private String calcString;
    private float calcTimer;

    private Game chessGame;
    List<Long> lastPositions;
    private InputMultiplexer multiplexer;
    private ChessController controller;

    public GameScreen(Game chessGame, InputMultiplexer multiplexer, ChessController controller) {
        this.chessGame = chessGame;
        this.multiplexer = multiplexer;
        this.controller = controller;
        startNewGame();
    }

    public void startNewGame() {
        lastPositions = new ArrayList<Long>();
        lastPositions.add(chessGame.getPosition().getHashCode());
        gameEnded = false;
        gameResult = "";
        calcString = "";
        calcTimer = 0;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        batch = new SpriteBatch();
        stage = new Stage();
        multiplexer.addProcessor(stage);

        //Scale if necessary
        if (Gdx.graphics.getHeight() < 960 || Gdx.graphics.getWidth() < 540) {
            stage.getViewport().setCamera(camera);
        }

        if (ChessMain.TEST_MODE) {
            Table table = new Table();
            table.setBounds(300, 100, 200, 120);

            stage.addActor(table);

            table.row();

            final TextButton moveButton = new TextButton("Move", createTextButtonSkin());
            table.add(moveButton).width(100).height(50).spaceBottom(10).expandX();

            table.row();

            final TextButton undoButton = new TextButton("Undo", createTextButtonSkin());
            table.add(undoButton).width(100).height(50).spaceBottom(10);
            final TextButton redoButton = new TextButton("Redo", createTextButtonSkin());
            table.add(redoButton).width(100).height(50).spaceBottom(10);

            table.row();

            final TextField textField = new TextField("Adams.pgn", createTextFieldSkin());
            table.add(textField).width(100).height(50).spaceBottom(10);

            table.row();
            final TextButton loadButton = new TextButton("Load", createTextButtonSkin());
            table.add(loadButton).width(100).height(50).spaceBottom(10);
            final TextButton saveButton = new TextButton("Save", createTextButtonSkin());
            table.add(saveButton).width(100).height(50).spaceBottom(10);

            table.row();
            final TextButton newButton = new TextButton("New", createTextButtonSkin());
            table.add(newButton).width(100).height(50);

            moveButton.addListener(new MoveButtonListener(controller));
            undoButton.addListener(new UndoButtonListener(controller));
            redoButton.addListener(new RedoButtonListener(controller));
            loadButton.addListener(new LoadButtonListener(controller, textField));
            saveButton.addListener(new SaveButtonListener(controller, textField));
            newButton.addListener(new NewButtonListener(controller));
        }
        else {
            Table table = new Table();
            table.setBounds(320, 100, 200, 120);

            stage.addActor(table);

            table.row();
            final TextButton moveButton = new TextButton("Move", createTextButtonSkin());
            table.add(moveButton).width(200).height(100).spaceBottom(20).expandX();
            table.row();
            final TextButton newButton = new TextButton("New", createTextButtonSkin());
            table.add(newButton).width(200).height(100);

            moveButton.addListener(new MoveButtonListener(controller));
            newButton.addListener(new NewButtonListener(controller));
        }

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
        selectedStartMousePosition = null;
        selectedStone = 0;

        whiteEvaluation = String.valueOf(ChessUtil.evaluate(chessGame.getPosition(), 1));
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

                short stone = (short) position.getStone(Chess.coorToSqi(x, y));

                if (stone != 0) {
                    if (selectedPosition == null ||  (selectedPosition != null && (x != selectedPosition.x || y != selectedPosition.y))) {
                        batch.draw(textureMap.get(stone), x * SQUARE_SIZE, y * SQUARE_SIZE + Y_SHIFT);
                    }
                }
            }
        }

        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        if (selectedStone != 0) {
            int deltaX = (int) mousePos.x - selectedStartMousePosition.x;
            int deltaY = (int) mousePos.y - selectedStartMousePosition.y;

            batch.draw(textureMap.get((short) selectedStone), selectedPosition.x * SQUARE_SIZE + deltaX, selectedPosition.y * SQUARE_SIZE + Y_SHIFT + deltaY);
        }


        if (gameEnded) {
            font.draw(batch, gameResult, 20, 220);
        }
        else if (ChessMain.TEST_MODE) {
            font.draw(batch, "White advantage: " + whiteEvaluation, 20, 220);
        }

        if (calculating) {
            font.draw(batch, "Calculating" + calcString, 20, 260);

            calcTimer += delta;

            if (calcTimer >= 0.5) {
                calcString += ".";
                calcTimer = 0;
            }

            if (calcString.length() > 3) {
                calcString = "";
            }
        }

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

        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        this.selectedStartMousePosition = new GridPoint2((int) mousePos.x, (int) mousePos.y);
    }

    public void setSelectedStone(int selectedStone) {
        this.selectedStone = selectedStone;
    }

    public void updateEvaluation() {
        updateEvaluation(true);
    }

    public void updateEvaluation(boolean checkForEnd) {
        int eval = ChessUtil.evaluate(chessGame.getPosition(), 1);
        whiteEvaluation = String.valueOf(eval);

        if (checkForEnd) {
            boolean isDraw = ChessUtil.hasThreeFoldRepetition(lastPositions, chessGame.getPosition().getHalfMoveClock()) || chessGame.getPosition().getHalfMoveClock() >= 100;

            if (Math.abs(eval) == ChessUtil.MAXIMUM_VALUE || isDraw)  {
                gameEnded = true;

                if (isDraw) {
                    gameResult = "Draw";
                }
                else if (eval > 0 ) {
                    gameResult = "White victory";
                }
                else {
                    gameResult = "Black victory";
                }
            }
        }
    }

    public void setChessGame(Game chessGame) {
        this.chessGame = chessGame;
    }

    public List<Long> getLastPositions() {
        return lastPositions;
    }

    public void addPosition(long position) {
        lastPositions.add(position);
    }

    public void removeLastPosition() {
        if (lastPositions.size() > 0) {
            lastPositions.remove(lastPositions.size() - 1);
        }
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setCalculating(boolean calculating) {
        this.calculating = calculating;
    }

    public boolean isCalculating() {
        return calculating;
    }

    public void setCalcTimer(float calcTimer) {
        this.calcTimer = calcTimer;
    }

    private Skin createTextButtonSkin() {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("default", new BitmapFont());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
        return skin;
    }

    private Skin createTextFieldSkin() {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        skin.add("default", new BitmapFont());

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = Color.BLACK;

        skin.add("default", textFieldStyle);
        return skin;
    }
}

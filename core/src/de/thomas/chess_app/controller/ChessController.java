package de.thomas.chess_app.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import chesspresso.Chess;
import chesspresso.game.Game;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.pgn.PGNReader;
import chesspresso.pgn.PGNSyntaxError;
import chesspresso.pgn.PGNWriter;
import chesspresso.position.Position;
import de.thomas.chess_app.search.Algorithm;
import de.thomas.chess_app.util.PromotionListener;
import de.thomas.chess_app.view.GameScreen;

public class ChessController {
    private Game chessGame;
    private GameScreen gameScreen;

    public ChessController(Game chessGame) {
        this.chessGame = chessGame;
    }

    public void trySelect(int posX, int posY) {
        GridPoint2 point = gameScreen.getPosition(posX, posY);
        int sqi = gameScreen.getSqi(posX, posY);

        if (sqi < 0 || sqi > 63) {
            return;
        }

        Position position = chessGame.getPosition();
        int piece = position.getStone(sqi);

        if (piece != 0 && Chess.stoneToColor(piece) == position.getToPlay()) {
            gameScreen.setSelectedPosition(point);
            gameScreen.setSelectedStone(piece);
        }
    }

    public boolean tryMove(int startX, int startY, int endX, int endY) {
        gameScreen.setSelectedPosition(null);
        gameScreen.setSelectedStone(0);

        if (gameScreen.isGameEnded() || gameScreen.isCalculating()) {
            return false;
        }

        int startSqi = gameScreen.getSqi(startX, startY);
        int endSqi = gameScreen.getSqi(endX, endY);

        if (startSqi < 0 || endSqi < 0 || startSqi > 63 || endSqi > 63 || startSqi == endSqi) {
            return false;
        }

        Position position = chessGame.getPosition();

        int stone = position.getStone(startSqi);

        if (stone != 0) {
            short move = getMove(stone, startSqi, endSqi);

            short[] possibleMoves = position.getAllMoves();

            for (short possibleMove : possibleMoves) {
                if (move == possibleMove) {
                    try {
                        position.doMove(move);

                        gameScreen.addPosition(position.getHashCode());
                        gameScreen.updateEvaluation();
                        return true;
                    } catch (IllegalMoveException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }

        return false;
    }

    public void computerMove() {
        if (gameScreen.isGameEnded() || gameScreen.isCalculating()) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                gameScreen.setCalculating(true);
                short move = Algorithm.bestMoveAlphaBeta(chessGame.getPosition(), gameScreen.getLastPositions());

                try {
                    chessGame.getPosition().doMove(move);
                    gameScreen.addPosition(chessGame.getPosition().getHashCode());
                } catch (IllegalMoveException e) {
                    e.printStackTrace();
                }

                gameScreen.updateEvaluation();
                gameScreen.setCalculating(false);
                gameScreen.setCalcTimer(0);
            }
        }).start();
    }

    private short getMove(int stone, int startSqi, int endSqi) {
        Position position = chessGame.getPosition();

        short move;

        //Castling
        if (stone == Chess.WHITE_KING && startSqi == Chess.E1 && endSqi == Chess.G1) {
            move = Move.getShortCastle(Chess.WHITE);
        } else if (stone == Chess.WHITE_KING && startSqi == Chess.E1 && endSqi == Chess.C1) {
            move = Move.getLongCastle(Chess.WHITE);
        } else if (stone == Chess.BLACK_KING && startSqi == Chess.E8 && endSqi == Chess.G8) {
            move = Move.getShortCastle(Chess.BLACK);
        } else if (stone == Chess.BLACK_KING && startSqi == Chess.E8 && endSqi == Chess.C8) {
            move = Move.getLongCastle(Chess.BLACK);
        }
        //En passant
        else if (position.getPiece(startSqi) == Chess.PAWN && position.getPiece(endSqi) == 0 && startSqi % 2 != endSqi % 2) {
            move = Move.getEPMove(startSqi, endSqi);
        }
        //Promotion
        else if (stone == Chess.WHITE_PAWN && Chess.sqiToRow(startSqi) == 6 && Chess.sqiToRow(endSqi) == 7) {
            PromotionListener listener = new PromotionListener();

            Gdx.input.getTextInput(listener, "Choose promotion", "", "Q, R, N, B");
            String input = listener.getText();
            System.out.println("Input: " + input);

            boolean capturing = position.getPiece(endSqi) != 0;

            if (input.equalsIgnoreCase("R") || input.equalsIgnoreCase("T")) {
                move = Move.getPawnMove(startSqi, endSqi, capturing, Chess.ROOK);
            } else if (input.equalsIgnoreCase("N") || input.equalsIgnoreCase("S")) {
                move = Move.getPawnMove(startSqi, endSqi, capturing, Chess.KNIGHT);
            } else if (input.equalsIgnoreCase("B") || input.equalsIgnoreCase("L")) {
                move = Move.getPawnMove(startSqi, endSqi, capturing, Chess.BISHOP);
            } else {
                move = Move.getPawnMove(startSqi, endSqi, capturing, Chess.QUEEN);
            }
        } else {
            boolean capturing = position.getPiece(endSqi) != 0;
            move = Move.getRegularMove(startSqi, endSqi, capturing);
        }

        return move;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void loadGame(File file) {
        Game game = null;

        try {
            game = new PGNReader(new FileInputStream(file), "Game").parseGame();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (PGNSyntaxError pgnSyntaxError) {
            pgnSyntaxError.printStackTrace();
            return;
        }

        chessGame = game;
        gameScreen.setChessGame(chessGame);
        gameScreen.updateEvaluation();
    }

    public void saveGame(File file) {
        //Needed for PGNWriter
        chessGame.getModel().getHeaderModel().setTag("Result", "*");

        try {
            PrintWriter writer = new PrintWriter(new File(file.getAbsolutePath()));
            new PGNWriter(writer).write(chessGame.getModel());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void newGame() {
        if (gameScreen.isCalculating()) {
            return;
        }

        Game game = new Game();
        chessGame = game;
        gameScreen.setChessGame(chessGame);
        gameScreen.startNewGame();
        gameScreen.updateEvaluation();
    }

    public void goForward() {
        chessGame.goForward();
        gameScreen.addPosition(chessGame.getPosition().getHashCode());
        gameScreen.updateEvaluation();
    }

    public void goBack() {
        chessGame.goBack();
        gameScreen.removeLastPosition();
        gameScreen.updateEvaluation();
    }
}

package de.thomas.chess_app.controller;

import com.badlogic.gdx.math.GridPoint2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import chesspresso.Chess;
import chesspresso.game.Game;
import chesspresso.game.GameHeaderModel;
import chesspresso.game.GameModel;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.pgn.PGNReader;
import chesspresso.pgn.PGNSyntaxError;
import chesspresso.pgn.PGNWriter;
import chesspresso.position.Position;
import de.thomas.chess_app.search.Algorithm;
import de.thomas.chess_app.util.ChessUtil;
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
        }
    }

    public boolean tryMove(int startX, int startY, int endX, int endY) {
        gameScreen.setSelectedPosition(null);

        int startSqi = gameScreen.getSqi(startX, startY);
        int endSqi = gameScreen.getSqi(endX, endY);

        if (startSqi < 0 || endSqi < 0 || startSqi > 63 || endSqi > 63) {
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
                        //System.out.println(ChessUtil.evaluate(position, 1));
                        position.doMove(move);


                        gameScreen.updateEvaluation();
                        //System.out.println(ChessUtil.evaluate(position, 1));
                        //System.out.println();
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
        short move = Algorithm.bestMoveAlphaBeta(chessGame.getPosition());

        try {
            chessGame.getPosition().doMove(move);
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }

        gameScreen.updateEvaluation();
    }

    private short getMove(int stone, int startSqi, int endSqi) {
        Position position = chessGame.getPosition();

        short move;

        //Castling
        if (stone == Chess.WHITE_KING && startSqi == Chess.E1 && endSqi == Chess.G1) {
            move = Move.getShortCastle(Chess.WHITE);
        }
        else if (stone == Chess.WHITE_KING && startSqi == Chess.E1 && endSqi == Chess.C1) {
            move = Move.getLongCastle(Chess.WHITE);
        }
        else if (stone == Chess.BLACK_KING && startSqi == Chess.E8 && endSqi == Chess.G8) {
            move = Move.getShortCastle(Chess.BLACK);
        }
        else if (stone == Chess.BLACK_KING && startSqi == Chess.E8 && endSqi == Chess.C8) {
            move = Move.getLongCastle(Chess.BLACK);
        }
        //En passant
        else if (position.getPiece(startSqi) == Chess.PAWN && position.getPiece(endSqi) == 0 && startSqi % 2 != endSqi % 2) {
            move = Move.getEPMove(startSqi, endSqi);
        }
        else {
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

    public void goForward() {
        chessGame.goForward();
    }

    public void goBack() {
        chessGame.goBack();
    }
}

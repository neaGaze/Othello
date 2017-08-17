package entities;

import android.util.Log;

import java.io.Serializable;

import exceptions.InvalidMoveException;
import exceptions.InvalidRuleException;
import exceptions.WrongPersonException;

/**
 * Created by neaGaze on 8/15/17.
 */

public class Game implements Serializable{

    private PlayerInterface[] players;

    private Rules rules;
    private static int PLAYER_SIZE = 2;

    public static interface OnUIUpdateListener {
        public void changeUIPieceColor(int x, int y, COLORS color);
    }

    private OnUIUpdateListener onUIUpdateListener;

    private Board board;
    public COLORS currentTurn = COLORS.WHITE;

    public Game(){
        // inititalize players
        players = new PlayerInterface[PLAYER_SIZE];
        players[0] = new Player("Player 1", COLORS.WHITE);
        players[0] = new Player("Player 2", COLORS.BLACK);

        // initialize board. mid-tables are always filled with pieces
        board = Board.getInstance();
        int midValue = (board.getSize() / 2) - 1;
        board.addNewPiece(midValue, midValue, new WhitePiece());
        board.addNewPiece((midValue + 1), midValue, new BlackPiece());
        board.addNewPiece(midValue, (midValue + 1), new BlackPiece());
        board.addNewPiece((midValue + 1), (midValue + 1), new WhitePiece());

        // setup rules
        rules = new Rules(this);
    }

    /**
     * Begin the game
     **/
    public void beginGame(Player player) throws WrongPersonException{
        Log.v("Player Turn","It's WHITE player turn");
        if(player.getColor() == COLORS.BLACK) throw new WrongPersonException("Wrong Person turn");
    }

    /**
     * When a person makes a move
     * **/
    public void addPiece(int x, int y, OnUIUpdateListener callback) throws WrongPersonException, InvalidMoveException, InvalidRuleException {

        // check with the rules to see if the move is valid
        rules.checkLegalMoves(x, y, currentTurn);

        // add new piece in board
        if(currentTurn == COLORS.BLACK)
            board.addNewPiece(x, y, new BlackPiece());
        else if(currentTurn == COLORS.WHITE)
            board.addNewPiece(x, y, new WhitePiece());
        else throw new WrongPersonException("No turn is defined");

        // convert the entire lines until the piece with the same color is reached
        rules.convertColor(x, y, currentTurn, callback);

        // toggle the players turn
        togglePlayers();
    }

    private void togglePlayers() {
        if(currentTurn == COLORS.BLACK) currentTurn = COLORS.WHITE;
        else if(currentTurn == COLORS.WHITE) currentTurn = COLORS.BLACK;
    }


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public OnUIUpdateListener getOnUIUpdateListener() {
        return onUIUpdateListener;
    }

    public void setOnUIUpdateListener(OnUIUpdateListener onUIUpdateListener) {
        this.onUIUpdateListener = onUIUpdateListener;
    }
}

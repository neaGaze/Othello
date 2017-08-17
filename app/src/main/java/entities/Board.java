package entities;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by neaGaze on 8/15/17.
 */

public class Board  implements Serializable{

    private static Board board;


    private int size = 8;

    private PieceInterface pieces[][];
    private int WHITE_SCORE = 0;
    private int BLACK_SCORE = 0;

    private boolean boardState[][];

    private Board() {
        init();
    }

    public Board(int size){
        this.size = size;
        init();
    }

    public void init(){
        this.pieces = new Piece[size][size];
        this.boardState = new boolean[size][size];
    }

    /**
     * Singleton Design Pattern
     * **/
    public static Board getInstance(){
        if(board == null)
            board = new Board();
        return board;
    }

    public void addNewPiece(int x, int y, PieceInterface piece) {
        this.pieces[x][y] = piece;
        initBoardState();
    }

    public int getBLACK_SCORE() {
        return BLACK_SCORE;
    }

    public void setBLACK_SCORE(int BLACK_SCORE) {
        this.BLACK_SCORE = BLACK_SCORE;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getWHITE_SCORE() {
        return WHITE_SCORE;
    }

    public void setWHITE_SCORE(int WHITE_SCORE) {
        this.WHITE_SCORE = WHITE_SCORE;
    }


    public PieceInterface[][] getPieces() {
        return pieces;
    }

    public PieceInterface getPiece(int x, int y){return pieces[x][y];}

    public void setPieces(PieceInterface[][] pieces) {
        this.pieces = pieces;
    }


    public boolean[][] getBoardState() {
        return boardState;
    }

    public void setBoardState(int x, int y, boolean value) {
        this.boardState[x][y] = value;
    }

    /**
     * Check to see if the board has all spaces filled up
     **/
    public boolean checkIfBoardFilled(){
        for (int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                if(pieces[i][j] == null) return false;
        return true;
    }

    /**
     * Initialize board state
     * **/
    public void initBoardState(){
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++) {
                //if(pieces[i][j] == null)
                boardState[i][j] = false;
                //else boardState[i][j] = true;
            }
    }

}

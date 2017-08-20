package entities;

import java.io.Serializable;

/**
 * Created by neaGaze on 8/15/17.
 *
 * Implement Factory Design Pattern and restrict creating more than 2 objects
 */

public class Player implements PlayerInterface, Serializable {

    public String name;

    private COLORS color;

    private int opponentPieces = 2; // initially there is always 2 black and 2 white pieces

    public Player(String name, COLORS color){
        this.name = name;
        this.color = color;
    }

    @Override
    public void makeMove(Board board, int x, int y) {
        board.addNewPiece(x, y, color == COLORS.WHITE ? new WhitePiece() : new BlackPiece());
    }

    public int getOpponentPieces() {
        return opponentPieces;
    }

    public void capture(int opponentCapture) {
        this.opponentPieces += opponentCapture;
    }

    public void lost(int pieceCount) {
        this.opponentPieces -= pieceCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public COLORS getColor() {
        return color;
    }

    public void setColor(COLORS color) {
        this.color = color;
    }
}

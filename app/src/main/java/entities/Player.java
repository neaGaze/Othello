package entities;

import java.io.Serializable;

/**
 * Created by neaGaze on 8/15/17.
 */

public class Player implements PlayerInterface, Serializable {

    public String name;

    private COLORS color;

    public Player(String name, COLORS color){
        this.name = name;
        this.color = color;
    }

    @Override
    public void makeMove(Board board, int x, int y) {
        board.addNewPiece(x, y, color == COLORS.WHITE ? new WhitePiece() : new BlackPiece());
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

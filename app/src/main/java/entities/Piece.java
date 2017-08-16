package entities;

/**
 * Created by neaGaze on 8/15/17.
 */

public abstract class Piece implements PieceInterface{

    public COLORS color;

    @Override
    public PieceInterface flip() {
        return null;
    }

    @Override
    public boolean isCounterColor(COLORS counterColor) {
        if(color == COLORS.BLACK && counterColor == COLORS.WHITE) return true;
        if(color == COLORS.WHITE && counterColor == COLORS.BLACK) return true;

        return false;
    }

}

package entities;

/**
 * Created by neaGaze on 8/15/17.
 */

public class BlackPiece extends Piece {

    public BlackPiece(){
        this.color = COLORS.BLACK;
    }

    @Override
    public Piece flip() {
        color = COLORS.WHITE;
        return this;
    }

}

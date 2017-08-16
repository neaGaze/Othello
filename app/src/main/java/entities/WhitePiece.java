package entities;

/**
 * Created by neaGaze on 8/15/17.
 */

public class WhitePiece extends Piece {


    public WhitePiece(){
        this.color = COLORS.WHITE;
    }

    @Override
    public Piece flip() {
        color = COLORS.BLACK;
        return this;
    }

}

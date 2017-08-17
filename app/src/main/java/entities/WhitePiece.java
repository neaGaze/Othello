package entities;

import java.io.Serializable;

/**
 * Created by neaGaze on 8/15/17.
 */

public class WhitePiece extends Piece implements Serializable{


    public WhitePiece(){
        this.color = COLORS.WHITE;
    }

    @Override
    public Piece flip() {
        color = COLORS.BLACK;
        return this;
    }

}
